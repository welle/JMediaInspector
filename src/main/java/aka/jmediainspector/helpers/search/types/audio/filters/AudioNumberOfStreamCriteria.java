package aka.jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.componenttype.AbstractInputSpinnerCriteria;
import aka.jmediainspector.helpers.search.componenttype.customs.UIIntegerSpinner;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioNumberOfStreamSearch;
import javafx.scene.control.Spinner;

/**
 * Criteria for Audio Number of Stream.
 *
 * @author charlottew
 */
public class AudioNumberOfStreamCriteria extends AbstractInputSpinnerCriteria<Integer> {

    /**
     * Default Constructor.
     */
    public AudioNumberOfStreamCriteria() {
        // Internal use, do not delete, used in reflection.
        super();
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioNumberOfStreamCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Number of streams";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioNumberOfStreamCriteria newCriteria = new AudioNumberOfStreamCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        AudioNumberOfStreamSearch audioNumberOfStreamSearch = null;
        final Integer value = getSelectedValue();
        if (operation != null && value != null) {
            audioNumberOfStreamSearch = new AudioNumberOfStreamSearch(operation, Long.valueOf(value.intValue()));
        }
        return audioNumberOfStreamSearch;
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
