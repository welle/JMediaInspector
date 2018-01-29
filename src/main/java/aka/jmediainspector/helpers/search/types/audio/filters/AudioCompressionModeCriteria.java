package aka.jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNameComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.audio.CompressionModeEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioCompressionModeSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

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
        final Operator operation = getSelectedOperator();
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
