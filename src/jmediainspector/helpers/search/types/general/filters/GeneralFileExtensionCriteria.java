package jmediainspector.helpers.search.types.general.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadataquery.main.types.constants.file.FileExtensionSearchEnum;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;

/**
 * Criteria for File extension.
 *
 * @author charlottew
 */
public class GeneralFileExtensionCriteria extends AbstractComboboxCriteria<FileExtensionSearchEnum> {

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);
    }

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
        // TODO Auto-generated method stub

    }
}
