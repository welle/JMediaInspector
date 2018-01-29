package aka.jmediainspector.helpers.database;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.helpers.database.query.MediaItemsById;
import aka.jmediainspector.helpers.database.query.MediaPartsTitleEqual;
import aka.jmediainspector.helpers.database.query.MetadataItemsById;
import aka.plexdb.bean.MediaItemsEntity;
import aka.plexdb.bean.MediaPartsEntity;
import aka.plexdb.bean.MetadataItemsEntity;
import aka.plexdb.persistence.PersistenceServiceProvider;
import aka.plexdb.persistence.services.MediaItemsPersistence;
import aka.plexdb.persistence.services.MediaPartsPersistence;
import aka.plexdb.persistence.services.MetadataItemsPersistence;

/**
 * Plex DB Helper.
 *
 * @author charlottew
 */
public final class PlexDBHelper {

    // final String query = "Select T1.file, T4.section_type, T3.* from media_parts as T1 inner join media_items as T2 on (T1.media_item_id == T2.id) inner join metadata_items as T3 on
    // (T2.metadata_item_id == T3.id) inner join library_sections as T4 on (T3.library_section_id = T4.id) where lower(T1.file) =\"" + file.getAbsolutePath().toLowerCase() + "\"";

    /**
     * Get MediaPartsEntity where file field is the absolute path of the given file.
     *
     * @param file
     * @return list of MediaPartsEntity related
     */
    @NonNull
    public static List<@NonNull MediaPartsEntity> getMediaParts(@NonNull final File file) {
        final MediaPartsPersistence mediaPartsPersistence = PersistenceServiceProvider.getService(MediaPartsPersistence.class);

        final MediaPartsTitleEqual finder = new MediaPartsTitleEqual();
        finder.setTitle(file.getAbsolutePath().toLowerCase());
        final List<@NonNull MediaPartsEntity> results = mediaPartsPersistence.executeQuery(finder);

        return results;
    }

    /**
     * Get MediaItemsEntity with the given ID.
     *
     * @param id
     * @return list of MediaItemsEntity related
     */
    @NonNull
    public static List<@NonNull MediaItemsEntity> getMediaItems(@NonNull final Integer id) {
        final MediaItemsPersistence mediaItemsPersistence = PersistenceServiceProvider.getService(MediaItemsPersistence.class);

        final MediaItemsById finder = new MediaItemsById();
        finder.setId(id);
        final List<@NonNull MediaItemsEntity> results = mediaItemsPersistence.executeQuery(finder);

        return results;
    }

    /**
     * Get MetadataItemsEntity with the given ID.
     *
     * @param id
     * @return list of MetadataItemsEntity related
     */
    @NonNull
    public static List<@NonNull MetadataItemsEntity> getMetadataItems(@NonNull final Integer id) {
        final MetadataItemsPersistence mediaItemsPersistence = PersistenceServiceProvider.getService(MetadataItemsPersistence.class);

        final MetadataItemsById finder = new MetadataItemsById();
        finder.setId(id);
        final List<@NonNull MetadataItemsEntity> results = mediaItemsPersistence.executeQuery(finder);

        return results;
    }
}
