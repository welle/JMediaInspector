package aka.jmediainspector.helpers.search.types.general.filters;

import java.time.LocalTime;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.componenttype.AbstractInputTimeCriteria;
import aka.jmediainspector.helpers.search.componenttype.customs.UITimeSpinner;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.general.GeneralDurationSearch;

/**
 * Criteria for File extension.
 *
 * @author charlottew
 */
public class GeneralDurationCriteria extends AbstractInputTimeCriteria {

    /**
     * Default Constructor.
     */
    public GeneralDurationCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public GeneralDurationCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.GENERAL;
    }

    @Override
    public @NonNull String getFullName() {
        return "Duration";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final GeneralDurationCriteria newCriteria = new GeneralDurationCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        GeneralDurationSearch generalDurationSearch = null;
        final Long value = getSelectedValue();
        if (operation != null && value != null) {
            generalDurationSearch = new GeneralDurationSearch(operation, value);
        }
        return generalDurationSearch;
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
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public UITimeSpinner getTimeSpinner() {
        final UITimeSpinner timeSpinner = new UITimeSpinner(LocalTime.of(1, 30));
        return timeSpinner;
    }

}
