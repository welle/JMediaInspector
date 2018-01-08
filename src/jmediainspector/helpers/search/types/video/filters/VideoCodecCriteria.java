package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.VideoMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.VideoMpeg4CodecIdEnum;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.main.types.search.audio.AudioCodecIdSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.comparators.CodecEnumComparator;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxMultipleCriteria;
import jmediainspector.helpers.search.types.componenttype.converters.VideoCodecStringConverter;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Video Codec.
 *
 * @author charlottew
 */
public class VideoCodecCriteria extends AbstractComboboxMultipleCriteria<CodecEnum> {

    /**
     * Default Constructor.
     */
    public VideoCodecCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public VideoCodecCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Codec";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final VideoCodecCriteria newCriteria = new VideoCodecCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioCodecIdSearch audioCodecIdSearch = null;
        final CodecEnum value = getSelectedComboboxValue();
        if (operation != null) {
            audioCodecIdSearch = new AudioCodecIdSearch(operation, value);
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
        return this;
    }

    @Override
    public ComboBox<CodecEnum> getCombobox() {
        final List<CodecEnum> enumList = getCodecEnumList();

        final ComboBox<CodecEnum> result = new ComboBox<>();
        final VideoCodecStringConverter converter = new VideoCodecStringConverter();
        result.setConverter(converter);
        enumList.sort(CodecEnumComparator.INSTANCE);
        result.getItems().setAll(enumList);
        return result;
    }

    @NonNull
    private List<@NonNull CodecEnum> getCodecEnumList() {
        final List<@NonNull CodecEnum> enumList = new ArrayList<>();

        // FIXME Add element in list

        enumList.add(VideoMatroskaCodecIdEnum.V_MPEG4_ISO_AVC);
        enumList.add(VideoMatroskaCodecIdEnum.V_MPEG2);
        enumList.add(VideoMpeg4CodecIdEnum.H264);

        return enumList;
    }
}
