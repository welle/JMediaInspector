package jmediainspector.helpers.search.types.text.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.main.types.search.text.TextNumberOfStreamSearch;
import javafx.scene.control.Spinner;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.componenttype.AbstractInputSpinnerCriteria;
import jmediainspector.helpers.search.componenttype.customs.UIIntegerSpinner;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.interfaces.AbstractInterface;

/**
 * Criteria for Audio Number of Stream.
 *
 * @author charlottew
 */
public class TextNumberOfStreamCriteria extends AbstractInputSpinnerCriteria<Integer> {

    /**
     * Default Constructor.
     */
    public TextNumberOfStreamCriteria() {
        // Internal use, do not delete, used in reflection.
        super();
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public TextNumberOfStreamCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.TEXT;
    }

    @Override
    public @NonNull String getFullName() {
        return "Number of streams";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final TextNumberOfStreamCriteria newCriteria = new TextNumberOfStreamCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        TextNumberOfStreamSearch textNumberOfStreamSearch = null;
        final Integer value = getSelectedValue();
        if (operation != null && value != null) {
            final Long valueLong = Long.valueOf(value.intValue());
            textNumberOfStreamSearch = new TextNumberOfStreamSearch(operation, valueLong);
        }
        return textNumberOfStreamSearch;
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
    public Spinner<Integer> getSpinner() {
        final UIIntegerSpinner result = new UIIntegerSpinner(1, 20, 1);
        result.setEditable(true);

        return result;
    }
}
