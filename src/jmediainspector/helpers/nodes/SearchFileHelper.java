package jmediainspector.helpers.nodes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.plexdb.bean.LibrarySectionsEntity;
import aka.plexdb.bean.MediaItemsEntity;
import aka.plexdb.bean.MediaPartsEntity;
import aka.plexdb.bean.MetadataItemsEntity;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import jmediainspector.constants.PlexConstants;
import jmediainspector.context.ApplicationContext;

/**
 * Node helper for file search.
 *
 * @author charlottew
 */
public final class SearchFileHelper {

    /**
     * Get list of node to display related to given MediaPartsEntity list and file.
     *
     * @param mediaPartsList list of MediaPartsEntity
     * @param file related file
     * @return list of node to display
     * @throws SQLException
     */
    @NonNull
    public static LinkedList<@NonNull Node> processResultFileInformationSearch(@NonNull final List<@NonNull MediaPartsEntity> mediaPartsList, @NonNull final File file) throws SQLException {
        final LinkedList<@NonNull Node> resultList = new LinkedList<>();
        if (mediaPartsList.isEmpty()) {
            final Text text1 = new Text("No match found in plex database for file: ");
            text1.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF");
            final Text text2 = new Text(file.getAbsolutePath() + "\n");
            text2.setStyle("-fx-fill: #FFFFFF");
        } else {
            final int i = 1;
            Text text = new Text("Result(s) founded for:");
            text.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-underline: true;");
            resultList.add(text);
            text = new Text(file.getAbsolutePath() + "\n\n");
            text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; ");
            resultList.add(text);
            for (final @NonNull MediaPartsEntity mediaParts : mediaPartsList) {
                text = new Text(i + ") ");
                text.setStyle("-fx-fill: #FFFFFF; -fx-font-weight: bold; -fx-text-origin: top;");
                resultList.add(text);
                final MediaItemsEntity mediaItemsEntity = mediaParts.getMediaItem();
                if (mediaItemsEntity != null) {
                    final MetadataItemsEntity metadataItemsEntity = mediaItemsEntity.getMetadataItem();
                    if (metadataItemsEntity != null) {
                        final String guid = metadataItemsEntity.getGuid();
                        final String title = metadataItemsEntity.getTitle();
                        final Hyperlink hyperlink = addLinkInformation(resultList, guid, title);
                        addImage(resultList, metadataItemsEntity, hyperlink);
                    }
                }
            }

            text = new Text("\n---------------------------------------------------------\n");
            text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; ");
            resultList.add(text);
        }

        return resultList;
    }

    private static void addImage(@NonNull final LinkedList<@NonNull Node> resultList, @NonNull final MetadataItemsEntity metadataItemsEntity, @Nullable final Hyperlink hyperlink) throws SQLException {
        try {
            String userThumbUrl = metadataItemsEntity.getUserThumbUrl();
            if (userThumbUrl != null) {
                final LibrarySectionsEntity librarySection = metadataItemsEntity.getLibrarySection();
                if (librarySection != null) {
                    final Integer sectionTypeValue = librarySection.getSectionType();
                    final PlexConstants.SECTION_TYPE sectionType = PlexConstants.SECTION_TYPE.getSectionType(sectionTypeValue);
                    if (sectionType != null) {
                        final int beginIndex = userThumbUrl.lastIndexOf("/");
                        final int endIndex = userThumbUrl.length();
                        userThumbUrl = userThumbUrl.substring(beginIndex, endIndex);
                        final String guid = metadataItemsEntity.getGuid();
                        final String shaGuid = DigestUtils.sha1Hex(guid);
                        final String cacheDir = shaGuid.substring(0, 1);
                        final String shaDir = shaGuid.substring(1, shaGuid.length());

//                        final StringBuilder sb = new StringBuilder();
//                        sb.append(getCurrentPlexDBDirectory().getAbsolutePath());
//                        sb.append("/metadata/");
//                        sb.append(sectionType.getDirectoryName());
//                        sb.append("/");
//                        sb.append(cacheDir);
//                        sb.append("/");
//                        sb.append(shaDir);
//                        sb.append(".bundle/Contents/_stored/posters/");
//                        sb.append(userThumbUrl);
//                        if (DevConstants.DEBUG) {
//                            System.err.println("[PlexToolsTabControler] addImage - " + sb.toString());
//                        }
//
//                        try {
//                            final Image image = new Image(new FileInputStream(sb.toString()), 300, 150, true, false);
//                            final ImageView imageView = new ImageView(image);
//                            if (hyperlink == null) {
//                                resultList.add(imageView);
//                            } else {
//                                hyperlink.setGraphic(imageView);
//                                resultList.add(hyperlink);
//                            }
//
//                            final Text text = new Text(" ");
//                            text.setStyle("-fx-fill: #FFFFFF; -fx-font-weight: bold;");
//                            resultList.add(text);
//                        } catch (final FileNotFoundException e) {
//                            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
//                        }

                    }
                }
            }
        } catch (final StringIndexOutOfBoundsException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
//        } catch (final NoSuchFileException e) {
//            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
        }
    }

    @Nullable
    private static Hyperlink addLinkInformation(final LinkedList<@NonNull Node> resultList, @Nullable final String guid, @Nullable final String title) {
        Hyperlink result = null;
        if (guid != null && title != null) {
            final int index = guid.indexOf("://");
            if (index > 0) {
                final int size = guid.length();
                try {
                    // TODO: Move to an helper
                    final String urlValue = guid.substring(index + 3, size);
                    final StringBuilder sb = new StringBuilder();
                    if (guid.toLowerCase().contains("imdb")) {
                        sb.append("http://www.imdb.com/title/");
                        sb.append(urlValue);
                    } else if (guid.toLowerCase().contains("freebase")) {
                        // deprecated
                    } else if (guid.toLowerCase().contains("themoviedb")) {
                        sb.append("https://www.themoviedb.org/movie/");
                        sb.append(urlValue);
                    } else if (guid.toLowerCase().contains("thetvdb")) {

                    }

                    final Hyperlink link = new Hyperlink(title);
                    link.setStyle("-fx-text-origin:bottom");
                    link.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().browse(new URI(sb.toString()));
                        } catch (final IOException | URISyntaxException e11) {
                            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e11.getMessage(), e11);
                        }
                    });

                    result = new Hyperlink();
                    result.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().browse(new URI(sb.toString()));
                        } catch (final IOException | URISyntaxException e11) {
                            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e11.getMessage(), e11);
                        }
                    });
                    resultList.add(link);
                } catch (final Exception e) {
                    ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e.getMessage(), e);
                }
            }
        }
        return result;
    }

}
