package aka.jmediainspector.helpers.search.types.general.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNameComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.constants.file.FileExtensionSearchEnum;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.file.FileExtensionSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for File extension.
 *
 * @author charlottew
 */
public class GeneralFileExtensionCriteria extends AbstractComboboxCriteria<FileExtensionSearchEnum> {

    /**
     * Default Constructor.
     */
    public GeneralFileExtensionCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public GeneralFileExtensionCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.GENERAL;
    }

    @Override
    public @NonNull String getFullName() {
        return "File extension";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final GeneralFileExtensionCriteria newCriteria = new GeneralFileExtensionCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        FileExtensionSearch fileExtensionSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof FileExtensionSearchEnum) {
            final FileExtensionSearchEnum codecEnum = (FileExtensionSearchEnum) value;
            fileExtensionSearch = new FileExtensionSearch(operation, codecEnum);
        }
        return fileExtensionSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(FileExtensionSearchEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<FileExtensionSearchEnum> result = new ComboBox<>();
        final StringConverter<FileExtensionSearchEnum> converter = new StringConverter<FileExtensionSearchEnum>() {
            @Override
            public String toString(final FileExtensionSearchEnum object) {
                final String name = object.name();
                return name;
            }

            @Override
            public FileExtensionSearchEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final FileExtensionSearchEnum[] values = FileExtensionSearchEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);

        return result;
    }

}
