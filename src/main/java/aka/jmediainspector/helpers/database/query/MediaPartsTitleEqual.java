package aka.jmediainspector.helpers.database.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.plexdb.bean.MediaPartsEntity;
import aka.plexdb.persistence.finders.FinderMethod;

/**
 * select m from MediaPartsEntity m where m.file = :title
 *
 * @author Cha
 */
public class MediaPartsTitleEqual implements FinderMethod<@NonNull MediaPartsEntity> {

    @NonNull
    private static final String QUERY = "select m from MediaPartsEntity m where m.file = :title";

    private @Nullable String title;

    /**
     * @param title
     */
    public void setTitle(@NonNull final String title) {
        this.title = title;
    }

    @Override
    @NonNull
    public Query getBuildedQuery(@NonNull final EntityManager em) {
        final Query query = em.createQuery(QUERY);
        query.setParameter("title", this.title);

        return query;
    }
}
