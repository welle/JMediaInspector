package jmediainspector.helpers.search.types.componenttype.converters;

import javafx.util.StringConverter;

/**
 * Integer StringConverter.
 *
 * @author charlottew
 */
public class IntegerStringConverter extends StringConverter<Integer> {

    @Override
    public String toString(final Integer object) {
        return object + "";
    }

    @Override
    public Integer fromString(final String text) {
        Integer result = null;
        if (text == null || text.trim().length() == 0) {
            result = Integer.valueOf(1);
        } else {
            boolean isAllDigits = true;
            for (int i = 0; i < text.length(); i++) {
                if (!Character.isDigit(text.charAt(i))) {
                    result = Integer.valueOf(1);
                    isAllDigits = false;
                }
            }

            if (isAllDigits) {
                result = Integer.valueOf(text);
            }
        }
        return result;
    }

}
