package aka.jmediainspector.helpers.search.componenttype.customs;

import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

/**
 * Spinner auto commit.
 *
 * @author charlottew
 *
 * @param <T>
 */
public class SpinnerAutoCommit<T> extends Spinner<T> {

    /**
     * Default constructor.
     */
    public SpinnerAutoCommit() {
        super();
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param min minimum value
     * @param max maximum value
     * @param initialValue initial value
     */
    public SpinnerAutoCommit(final int min, final int max, final int initialValue) {
        super(min, max, initialValue);
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param min minimum value
     * @param max maximum value
     * @param initialValue initial value
     * @param amountToStepBy step increment/decrement
     */
    public SpinnerAutoCommit(final int min, final int max, final int initialValue, final int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param min minimum value
     * @param max maximum value
     * @param initialValue initial value
     */
    public SpinnerAutoCommit(final double min, final double max, final double initialValue) {
        super(min, max, initialValue);
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param min minimum value
     * @param max maximum value
     * @param initialValue initial value
     * @param amountToStepBy step increment/decrement
     */
    public SpinnerAutoCommit(final double min, final double max, final double initialValue, final double amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param items list of items in spinner
     */
    public SpinnerAutoCommit(final ObservableList<T> items) {
        super(items);
        addListenerKeyChange();
    }

    /**
     * Add key change listener.
     */
    public void addListenerKeyChange() {
        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            commitEditorText();
        });
    }

    /**
     * Commit the text editor.
     */
    public void commitEditorText() {
        if (!isEditable()) {
            return;
        }
        final String text = getEditor().getText();
        final SpinnerValueFactory<T> valueFactory = getValueFactory();
        if (valueFactory != null) {
            final StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                final T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }
}
