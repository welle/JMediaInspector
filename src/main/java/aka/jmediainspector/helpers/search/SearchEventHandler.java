package aka.jmediainspector.helpers.search;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Cha
 */
public class SearchEventHandler implements EventHandler<ActionEvent> {

    @NonNull
    private final AbstractInterface<?> abstractInterface;
    private final SearchHelper searchHelper;
    private @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController;

    /**
     * Constructor.
     *
     * @param searchHelper
     * @param abstractInterface
     * @param abstractSearchCriteriaController
     */
    public SearchEventHandler(final SearchHelper searchHelper, @NonNull final AbstractInterface<?> abstractInterface, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        this.abstractInterface = abstractInterface;
        this.searchHelper = searchHelper;
        this.abstractSearchCriteriaController = abstractSearchCriteriaController;
    }

    @Override
    public void handle(final ActionEvent event) {
        this.abstractInterface.handleEvent(this.searchHelper, this.abstractSearchCriteriaController);
    }
}
