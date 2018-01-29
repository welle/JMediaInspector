package aka.jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNameComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.componenttype.converters.FormatEnumStringConverter;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadata.main.constants.format.FormatEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.video.VideoFormatSearch;
import javafx.scene.control.ComboBox;

/**
 * Criteria for Audio Format.
 *
 * @author charlottew
 */
public class VideoFormatCriteria extends AbstractComboboxCriteria<FormatEnum> {

    /**
     * Default Constructor.
     */
    public VideoFormatCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public VideoFormatCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Format";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final VideoFormatCriteria newCriteria = new VideoFormatCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        VideoFormatSearch videoFormatSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof FormatEnum) {
            final FormatEnum formatEnum = (FormatEnum) value;
            videoFormatSearch = new VideoFormatSearch(operation, formatEnum);
        }
        return videoFormatSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(FormatEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<FormatEnum> result = new ComboBox<>();
        final FormatEnumStringConverter converter = new FormatEnumStringConverter();
        result.setConverter(converter);
        final FormatEnum[] values = FormatEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
