package aka.jmediainspector.helpers.html;

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

import aka.jmediainspector.constants.PlexConstants;
import aka.jmediainspector.context.ApplicationContext;
import aka.plexdb.bean.LibrarySectionsEntity;
import aka.plexdb.bean.MetadataItemsEntity;
import j2html.TagCreator;
import j2html.tags.ContainerTag;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;

/**
 * Node helper for file search.
 *
 * @author charlottew
 */
public final class SearchFileResultHelper {

    /**
     * Get HTML to display related to given files.
     *
     * @param fileList related file
     * @return HTML string
     */
    @NonNull
    public static String processResultFileInformationSearch(@NonNull final List<File> fileList) {
        final ContainerTag result = TagCreator.html();

        result.with(TagCreator.head("Result"));
        final ContainerTag body = TagCreator.body();

        final ContainerTag ulList = TagCreator.ul();
        for (final File file : fileList) {
            final ContainerTag link = TagCreator.a(file.getName());
            link.withHref(file.getAbsolutePath());
            final ContainerTag item = TagCreator.li(link);
            ulList.with(item);
        }

        body.with(ulList);
        result.with(body);

        return result.render();
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
