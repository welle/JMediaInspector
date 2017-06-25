package jmediainspector.helpers.search.types.audio.filters;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import jmediainspector.config.Criteria;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.ComboboxCriteria;

/**
 * Criteria for Audio Codec.
 *
 * @author charlottew
 */
public class AudioCodecCriteria extends ComboboxCriteria<AudioMatroskaCodecIdEnum> {

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioCodecCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }
}
