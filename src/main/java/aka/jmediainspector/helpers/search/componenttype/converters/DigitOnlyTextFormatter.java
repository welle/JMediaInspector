package aka.jmediainspector.helpers.search.componenttype.converters;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

/**
 * FormatEnum string converter.
 *
 * @author charlottew
 */
public class DigitOnlyTextFormatter implements UnaryOperator<TextFormatter.Change> {

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

}