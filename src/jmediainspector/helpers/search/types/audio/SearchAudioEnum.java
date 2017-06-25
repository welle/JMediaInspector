package jmediainspector.helpers.search.types.audio;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
import jmediainspector.helpers.search.types.interfaces.CriteriaInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;

/**
 * Search audio criterias.
 *
 * @author Cha
 */
public enum SearchAudioEnum implements SearchInterface {
    /**
     * Codec.
     */
    CODEC(AudioCodecCriteria.class);

    @NonNull
    private Class<? extends CriteriaInterface> filtersInterface;

    SearchAudioEnum(@NonNull final Class<? extends CriteriaInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    @Override
    public Class<? extends CriteriaInterface> getFiltersInterface() {
        return this.filtersInterface;
    }
}
