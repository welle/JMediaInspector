package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.main.types.search.video.VideoBitRateSearch;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractInputCriteria;
import jmediainspector.helpers.search.types.componenttype.converters.DigitOnlyTextFormatter;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Video BitRate.
 *
 * @author charlottew
 */
public class VideoBitRateCriteria extends AbstractInputCriteria<Long> {

    /**
     * Default Constructor.
     */
    public VideoBitRateCriteria() {
        // Internal use, do not delete, used in reflection.
        super(Long.class);
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public VideoBitRateCriteria(@NonNull final Criteria filter) {
        super(filter, Long.class);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Bit Rate (in kbit/s)";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final VideoBitRateCriteria newCriteria = new VideoBitRateCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        VideoBitRateSearch videoBitRateSearch = null;
        Long value = getSelectedValue();
        if (operation != null && value != null) {
            value = Long.valueOf(value.longValue() * 1000);
            videoBitRateSearch = new VideoBitRateSearch(operation, value);
        }
        return videoBitRateSearch;
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
    public TextField getTextField() {
        final TextField result = new TextField();
        final DigitOnlyTextFormatter filter = new DigitOnlyTextFormatter();
        result.setTextFormatter(new TextFormatter<String>(filter));
        return result;
    }
}
