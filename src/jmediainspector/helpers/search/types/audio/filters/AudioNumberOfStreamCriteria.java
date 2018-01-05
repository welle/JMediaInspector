package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.audio.AudioNumberOfStreamSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractEditableComboboxCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Audio Number of Stream.
 *
 * @author charlottew
 */
public class AudioNumberOfStreamCriteria extends AbstractEditableComboboxCriteria<Long> {

    @NonNull
    private static ObservableList<@NonNull Long> VALUES_LIST;
    static {
        VALUES_LIST = FXCollections.observableArrayList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2));
    }

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
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioNumberOfStreamSearch audioNumberOfStreamSearch = null;
        System.err.println("[AudioNumberOfStreamCriteria] getSearch - ddddd ");
        final Long value = getSelectedComboboxValue();
        System.err.println("[AudioNumberOfStreamCriteria] getSearch - " + value);
        if (operation != null && value != null) {
            audioNumberOfStreamSearch = new AudioNumberOfStreamSearch(operation, value);
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
    public ComboBox<Long> getCombobox() {
        final ComboBox<Long> result = new ComboBox<>();
        final UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(final TextFormatter.Change change) {
                final String text = change.getText();
                for (int i = 0; i < text.length(); i++) {
                    if (!Character.isDigit(text.charAt(i))) {
                        return null;
                    }
                }
                return change;
            }
        };
        result.setT.setConverter(new TextFormatter<Long>(filter));
        result.setEditable(true);
        result.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                final Long valueLong = Long.valueOf(result.getValue());
                if (!result.getItems().contains(valueLong)) {
                    result.getItems().add(valueLong);
                }
            }
        });
        result.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            final Long valueLong = Long.valueOf(result.getValue());
            if (!result.getItems().contains(valueLong)) {
                result.getItems().add(valueLong);
            }
        });
        result.getItems().setAll(AudioNumberOfStreamCriteria.VALUES_LIST);
        return result;
    }
}
