package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadata.main.constants.format.FormatEnum;
import aka.jmetadataquery.main.types.search.audio.AudioFormatSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.comparators.EnumByNameComparator;
import jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.componenttype.converters.FormatEnumStringConverter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.interfaces.AbstractInterface;

/**
 * Criteria for Audio Format.
 *
 * @author charlottew
 */
public class AudioFormatCriteria extends AbstractComboboxCriteria<FormatEnum> {

    /**
     * Default Constructor.
     */
    public AudioFormatCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioFormatCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Format";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioFormatCriteria newCriteria = new AudioFormatCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioFormatSearch audioFormatSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof FormatEnum) {
            final FormatEnum formatEnum = (FormatEnum) value;
            audioFormatSearch = new AudioFormatSearch(operation, formatEnum);
        }
        return audioFormatSearch;
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
