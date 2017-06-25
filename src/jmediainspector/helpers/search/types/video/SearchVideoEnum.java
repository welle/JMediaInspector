package jmediainspector.helpers.search.types.video;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.CriteriaInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;
import jmediainspector.helpers.search.types.video.filters.VideoResolutionCriteria;

/**
 * Search Video enumeration.
 *
 * @author charlottew
 */
public enum SearchVideoEnum implements SearchInterface {

    /**
     * Resolution.
     */
    RESOLUTION(VideoResolutionCriteria.class);

    @NonNull
    private Class<? extends CriteriaInterface> filtersInterface;

    SearchVideoEnum(@NonNull final Class<? extends CriteriaInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    @Override
    public Class<? extends CriteriaInterface> getFiltersInterface() {
        return this.filtersInterface;
    }
}
