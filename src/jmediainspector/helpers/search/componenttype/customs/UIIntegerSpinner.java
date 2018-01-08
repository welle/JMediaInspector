package jmediainspector.helpers.search.componenttype.customs;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import jmediainspector.helpers.search.componenttype.converters.IntegerStringConverter;

/**
 * Integer Spinner.
 *
 * @author charlottew
 */
public class UIIntegerSpinner extends SpinnerAutoCommit<Integer> {

    /**
     * Default constructor.
     */
    public UIIntegerSpinner() {
        super();
        // The converter to convert between text and item object.
        final IntegerStringConverter integerStringConverter = new IntegerStringConverter();
        getValueFactory().setConverter(integerStringConverter);
        addListenerKeyChange();
    }

    /**
     * Constructor.
     *
     * @param min minimum value
     * @param max maximum value
     * @param initialValue initial value
     */
    public UIIntegerSpinner(final int min, final int max, final int initialValue) {
        super(min, max, initialValue);
        // The converter to convert between text and item object.
        final IntegerStringConverter integerStringConverter = new IntegerStringConverter();
        getValueFactory().setConverter(integerStringConverter);
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
    public UIIntegerSpinner(final int min, final int max, final int initialValue, final int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        // The converter to convert between text and item object.
        final IntegerStringConverter integerStringConverter = new IntegerStringConverter();
        getValueFactory().setConverter(integerStringConverter);
        addListenerKeyChange();
    }

    /**
     * Commit the text editor.
     */
    @Override
    public void commitEditorText() {
        if (isEditable()) {
            final String text = getEditor().getText();
            final SpinnerValueFactory<Integer> valueFactory = getValueFactory();
            if (valueFactory != null) {
                final StringConverter<Integer> converter = valueFactory.getConverter();
                if (converter != null) {
                    final Integer value = converter.fromString(text);
                    valueFactory.setValue(value);
                    getEditor().setText(converter.toString(value));
                }
            }
        }
    }
}
