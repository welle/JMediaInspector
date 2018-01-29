package aka.jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNumberComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.constants.video.VideoResolutionSearchEnum;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.video.VideoResolutionSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for video resolution.
 *
 * <filter selected="true">
 * <type>EQUALS</type>
 * <value>R_1080</value>
 * </filter>
 *
 * @author charlottew
 */
public class VideoResolutionCriteria extends AbstractComboboxCriteria<VideoResolutionSearchEnum> {

    /**
     * Default Constructor.
     */
    public VideoResolutionCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public VideoResolutionCriteria(@NonNull final Criteria criteria) {
        super(criteria);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Resolution";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        filter.setType(ConditionFilter.GREATER_THAN.name());
        final VideoResolutionCriteria newCriteria = new VideoResolutionCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        VideoResolutionSearch videoResolutionSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof VideoResolutionSearchEnum) {
            final @NonNull VideoResolutionSearchEnum codecEnum = (VideoResolutionSearchEnum) value;
            videoResolutionSearch = new VideoResolutionSearch(operation, codecEnum);
        }
        return videoResolutionSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.GREATER_THAN);
        this.availableTypes.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.LESS_THAN);
        this.availableTypes.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(VideoResolutionSearchEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<VideoResolutionSearchEnum> result = new ComboBox<>();
        final StringConverter<VideoResolutionSearchEnum> converter = new StringConverter<VideoResolutionSearchEnum>() {
            @Override
            public String toString(final VideoResolutionSearchEnum object) {
                String name = object.name();
                if (name != null && name.trim().length() > 0) {
                    if (name.startsWith("R_")) {
                        name = name.substring(2, name.length());
                    }
                    name = name.replace("_", " ");
                    name = WordUtils.capitalizeFully(name);
                    final int firstSpaceIndex = name.indexOf(" ");
                    if (firstSpaceIndex == -1) {
                        name = name.toUpperCase();
                    } else {
                        name = name.substring(0, firstSpaceIndex).toUpperCase() + name.substring(firstSpaceIndex);
                    }
                }
                return name;
            }

            @Override
            public VideoResolutionSearchEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final VideoResolutionSearchEnum[] values = VideoResolutionSearchEnum.values();
        Arrays.sort(values, EnumByNumberComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
