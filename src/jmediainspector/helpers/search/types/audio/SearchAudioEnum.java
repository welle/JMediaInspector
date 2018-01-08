package jmediainspector.helpers.search.types.audio;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.interfaces.AbstractInterface;
import jmediainspector.helpers.search.interfaces.SearchInterface;
import jmediainspector.helpers.search.types.audio.filters.AudioBitRateCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioChannelCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioCompressionModeCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioFormatCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioLanguageCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioLanguageTagCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioMaxBitRateCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioNumberOfStreamCriteria;
import jmediainspector.helpers.search.types.audio.filters.AudioProfileCriteria;

/**
 * Search audio enumeration.
 *
 * @author Cha
 */
public enum SearchAudioEnum implements SearchInterface {

    /**
     * Bitrate.
     */
    BITRATE(AudioBitRateCriteria.class),

    /**
     * Bitrate Max.
     */
    BITRATE_MAX(AudioMaxBitRateCriteria.class),

    /**
     * Number of streams.
     */
    NUMBER_OF_STREAMS(AudioNumberOfStreamCriteria.class),

    /**
     * Profile.
     */
    PROFILE(AudioProfileCriteria.class),

    /**
     * Channel.
     */
    CHANNEL(AudioChannelCriteria.class),

    /**
     * Codec.
     */
    CODEC(AudioCodecCriteria.class),

    /**
     * Compression mode.
     */
    COMPRESSION_MODE(AudioCompressionModeCriteria.class),

    /**
     * Format.
     */
    FORMAT(AudioFormatCriteria.class),

    /**
     * Language tag.
     */
    LANGUAGE_TAG(AudioLanguageTagCriteria.class),

    /**
     * Language.
     */
    LANGUAGE(AudioLanguageCriteria.class);

    @NonNull
    private Class<? extends AbstractInterface> filtersInterface;
    private static @NonNull List<Class<? extends AbstractInterface>> ALL_VALUES = new ArrayList<>();

    static {
        for (final @NonNull SearchAudioEnum searchAudioEnum : SearchAudioEnum.values()) {
            ALL_VALUES.add(searchAudioEnum.getFiltersInterface());
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

    @Override
    public @NonNull SearchInterface @NonNull [] getALL() {
        return values();
    }
}
