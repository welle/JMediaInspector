package jmediainspector.helpers.search.componenttype.converters;

import org.apache.commons.lang3.text.WordUtils;

import aka.jmetadata.main.constants.format.FormatEnum;
import javafx.util.StringConverter;

/**
 * FormatEnum string converter.
 *
 * @author charlottew
 */
public class FormatEnumStringConverter extends StringConverter<FormatEnum> {

    @Override
    public String toString(final FormatEnum object) {
        String name = object.name();
        if (name != null && name.trim().length() > 0) {
            name = name.replace("_", " ");
            name = WordUtils.capitalizeFully(name);
            final int firstSpaceIndex = name.indexOf(" ");
            if (firstSpaceIndex == -1) {
                name = name.toUpperCase();
            } else {
                name = name.substring(0, firstSpaceIndex).toUpperCase() + name.substring(firstSpaceIndex);
            }
        }
        return name;
    }

    @Override
    public FormatEnum fromString(final String string) {
        return null;
    }

}