package jmediainspector.helpers.search.types.audio;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
import jmediainspector.helpers.search.types.general.SearchGeneralEnum;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;

/**
 * Search audio enumeration.
 *
 * @author Cha
 */
public enum SearchAudioEnum implements SearchInterface {
    /**
     * Codec.
     */
    CODEC(AudioCodecCriteria.class);

    @NonNull
    private Class<? extends AbstractInterface> filtersInterface;
    private static @NonNull List<Class<? extends AbstractInterface>> ALL_VALUES = new ArrayList<>();

    static {
        for (final @NonNull SearchGeneralEnum searchGeneralEnum : SearchGeneralEnum.values()) {
            ALL_VALUES.add(searchGeneralEnum.getFiltersInterface());
        }
    }

    SearchAudioEnum(@NonNull final Class<? extends AbstractInterface> filtersInterface) {
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
}
