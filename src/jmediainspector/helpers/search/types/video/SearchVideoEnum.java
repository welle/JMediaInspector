package jmediainspector.helpers.search.types.video;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.AbstractInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;
import jmediainspector.helpers.search.types.video.filters.VideoAspectRatioCriteria;
import jmediainspector.helpers.search.types.video.filters.VideoBitRateCriteria;
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
    RESOLUTION(VideoResolutionCriteria.class),

    /**
     * Aspect ratio.
     */
    ASPECT_RATIO(VideoAspectRatioCriteria.class),

    /**
     * Bit rate.
     */
    BIT_RATE(VideoBitRateCriteria.class);

    @NonNull
    private Class<? extends AbstractInterface> filtersInterface;
    private static @NonNull List<Class<? extends AbstractInterface>> ALL_VALUES = new ArrayList<>();

    static {
        for (final @NonNull SearchVideoEnum searchVideoEnum : SearchVideoEnum.values()) {
            ALL_VALUES.add(searchVideoEnum.getFiltersInterface());
        }
    }

    SearchVideoEnum(@NonNull final Class<? extends AbstractInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    @Override
    public Class<? extends AbstractInterface> getFiltersInterface() {
        return this.filtersInterface;
    }

    @Override
    public @NonNull List<Class<? extends AbstractInterface>> getAllValues() {
        return ALL_VALUES;
    }

    @Override
    @NonNull
    public SearchInterface @NonNull [] getALL() {
        return values();
    }
}
