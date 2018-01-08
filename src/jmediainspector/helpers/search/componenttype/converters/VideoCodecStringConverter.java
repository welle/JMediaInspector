package jmediainspector.helpers.search.componenttype.converters;

import org.apache.commons.lang3.text.WordUtils;

import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import javafx.util.StringConverter;

/**
 * Video codec string converter.
 *
 * @author charlottew
 */
public class VideoCodecStringConverter extends StringConverter<CodecEnum> {

    @Override
    public String toString(final CodecEnum object) {
        String name = object.toString();
        if (name != null && name.trim().length() > 0) {
            if (name.startsWith("V_")) {
                name = name.substring(2, name.length());
            }
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
    public CodecEnum fromString(final String string) {
        return null;
    }

}