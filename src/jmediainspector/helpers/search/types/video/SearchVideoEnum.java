package jmediainspector.helpers.search.types.video;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.FiltersInterface;
import jmediainspector.helpers.search.types.video.filters.VideoResolutionCriteria;

public enum SearchVideoEnum {

    /**
     * General.
     */
    RESOLUTION(VideoResolutionCriteria.class);

    private Class<? extends FiltersInterface> filtersInterface;

    SearchVideoEnum(@NonNull final Class<? extends FiltersInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    public Class<? extends FiltersInterface> getFiltersInterface() {
        return this.filtersInterface;
    }
}
