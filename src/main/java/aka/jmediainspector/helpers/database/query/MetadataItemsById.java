package aka.jmediainspector.helpers.database.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.plexdb.bean.MetadataItemsEntity;
import aka.plexdb.persistence.finders.FinderMethod;

/**
 * select m from MetadataItemsEntity m where m.file like :title
 *
 * @author Cha
 */
public class MetadataItemsById implements FinderMethod<@NonNull MetadataItemsEntity> {

    @NonNull
    private static final String QUERY = "select m from MetadataItemsEntity m where m.id = :id";

    private @Nullable Integer id;

    /**
     * @param id
     */
    public void setId(@NonNull final Integer id) {
        this.id = id;
    }

    @Override
    @NonNull
    public Query getBuildedQuery(@NonNull final EntityManager em) {
        final Query query = em.createQuery(QUERY);
        query.setParameter("id", this.id);

        return query;
    }
}
