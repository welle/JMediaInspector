package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.constants.CompressionModeEnum;
import aka.jmetadataquery.main.types.search.audio.AudioCompressionModeSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.comparators.EnumByNameComparator;
import jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.interfaces.AbstractInterface;

/**
 * Criteria for Audio Compression mode.
 *
 * @author charlottew
 */
public class AudioCompressionModeCriteria extends AbstractComboboxCriteria<CompressionModeEnum> {

    /**
     * Default Constructor.
     */
    public AudioCompressionModeCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioCompressionModeCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Compression mode";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioCompressionModeCriteria newCriteria = new AudioCompressionModeCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioCompressionModeSearch audioCompressionModeSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof CompressionModeEnum) {
            final CompressionModeEnum codecEnum = (CompressionModeEnum) value;
            audioCompressionModeSearch = new AudioCompressionModeSearch(operation, codecEnum);
        }
        return audioCompressionModeSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(CompressionModeEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<CompressionModeEnum> result = new ComboBox<>();
        final StringConverter<CompressionModeEnum> converter = new StringConverter<CompressionModeEnum>() {
            @Override
            public String toString(final CompressionModeEnum object) {
                String name = object.name();
                if (name != null && name.trim().length() > 0) {
                    name = WordUtils.capitalizeFully(name);
                }
                return name;
            }

            @Override
            public CompressionModeEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final CompressionModeEnum[] values = CompressionModeEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
