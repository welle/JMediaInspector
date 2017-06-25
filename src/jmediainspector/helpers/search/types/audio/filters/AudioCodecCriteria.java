package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;

/**
 * Criteria for Audio Codec.
 *
 * @author charlottew
 */
public class AudioCodecCriteria extends AbstractComboboxCriteria<AudioMatroskaCodecIdEnum> {

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);
    }

    /**
     * Default Constructor.
     */
    public AudioCodecCriteria() {
        // Internal use, do not delete, used in reflection.
    }

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

    @Override
    public @NonNull String getFullName() {
        return "Codec";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioCodecCriteria audioCodecCriteria = new AudioCodecCriteria(filter);

        searchHelper.addCriteria(audioCodecCriteria);
    }
}
