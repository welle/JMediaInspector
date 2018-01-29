package aka.jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.CodecEnumComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxMultipleCriteria;
import aka.jmediainspector.helpers.search.componenttype.converters.VideoCodecStringConverter;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.VideoMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.VideoMpeg4CodecIdEnum;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioCodecIdSearch;
import javafx.scene.control.ComboBox;

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
        final Operator operation = getSelectedOperator();
        AudioCodecIdSearch audioCodecIdSearch = null;
        final CodecEnum value = getSelectedComboboxValue();
        if (operation != null && value != null) {
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
