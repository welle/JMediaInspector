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
import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioCodecIdSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for Audio Codec.
 *
 * @author charlottew
 */
public class AudioCodecCriteria extends AbstractComboboxCriteria<AudioMatroskaCodecIdEnum> {

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

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        AudioCodecIdSearch audioCodecIdSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
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
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<AudioMatroskaCodecIdEnum> result = new ComboBox<>();
        final StringConverter<AudioMatroskaCodecIdEnum> converter = new StringConverter<AudioMatroskaCodecIdEnum>() {
            @Override
            public String toString(final AudioMatroskaCodecIdEnum object) {
                String name = object.name();
                if (name != null && name.trim().length() > 0) {
                    if (name.startsWith("A_")) {
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
            public AudioMatroskaCodecIdEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final AudioMatroskaCodecIdEnum[] values = AudioMatroskaCodecIdEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
