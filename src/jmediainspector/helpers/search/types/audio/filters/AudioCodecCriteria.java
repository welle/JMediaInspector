package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.main.types.search.audio.AudioCodecIdSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Audio Codec.
 *
 * @author charlottew
 */
public class AudioCodecCriteria extends AbstractComboboxCriteria<AudioMatroskaCodecIdEnum> {

    private AudioCodecCriteria audioCodecCriteria;

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
        final AudioCodecCriteria newCriteria = new AudioCodecCriteria(filter);
        this.audioCodecCriteria = newCriteria;

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = this.audioCodecCriteria.getSelectedOperator();
        AudioCodecIdSearch audioCodecIdSearch = null;
        final Enum<?> value = this.audioCodecCriteria.getSelectedEnumValue();
        if (operation != null && value instanceof CodecEnum) {
            final CodecEnum codecEnum = (CodecEnum) value;
            audioCodecIdSearch = new AudioCodecIdSearch(operation, codecEnum);
        }
        return audioCodecIdSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(AudioMatroskaCodecIdEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this.audioCodecCriteria;
    }

}
