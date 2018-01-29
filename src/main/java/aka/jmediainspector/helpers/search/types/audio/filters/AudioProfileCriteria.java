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
import aka.jmetadata.main.constants.profile.AudioProfileEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioProfileSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for Audio Profile.
 *
 * @author charlottew
 */
public class AudioProfileCriteria extends AbstractComboboxCriteria<AudioProfileEnum> {

    /**
     * Default Constructor.
     */
    public AudioProfileCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioProfileCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Profile";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioProfileCriteria newCriteria = new AudioProfileCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        AudioProfileSearch audioProfileSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof AudioProfileEnum) {
            final AudioProfileEnum codecEnum = (AudioProfileEnum) value;
            audioProfileSearch = new AudioProfileSearch(operation, codecEnum);
        }
        return audioProfileSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(AudioProfileEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<AudioProfileEnum> result = new ComboBox<>();
        final StringConverter<AudioProfileEnum> converter = new StringConverter<AudioProfileEnum>() {
            @Override
            public String toString(final AudioProfileEnum object) {
                String name = object.name();
                if (name != null && name.trim().length() > 0) {
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
            public AudioProfileEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final AudioProfileEnum[] values = AudioProfileEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
