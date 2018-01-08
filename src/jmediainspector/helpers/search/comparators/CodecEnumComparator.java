package jmediainspector.helpers.search.comparators;

import java.util.Comparator;

import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;

/**
 * Comparator for CodecEnum enum.<br/>
 * Use Arrays.sort(values, CodecEnumComparator.INSTANCE); to sort all the values.
 *
 * @author charlottew
 */
public class CodecEnumComparator implements Comparator<CodecEnum> {

    /**
     * Get instance of CodecEnumComparator.
     */
    public static final Comparator<CodecEnum> INSTANCE = new CodecEnumComparator();

    @Override
    public int compare(final CodecEnum enum1, final CodecEnum enum2) {
        final String name1 = enum1.toString();
        final String name2 = enum2.toString();

        return name1.compareTo(name2);
    }
}
