package jmediainspector.helpers.search.types.general.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.constants.file.FileExtensionSearchEnum;
import aka.jmetadataquery.main.types.search.file.FileExtensionSearch;
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
        final BinaryCondition.Op operation = getSelectedOperator();
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
