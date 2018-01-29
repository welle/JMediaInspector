package aka.jmediainspector.helpers.search.comparators;

import java.util.Comparator;

/**
 * Comparator for enumeration names.<br/>
 * Use Arrays.sort(values, EnumByNameComparator.INSTANCE); to sort all the values.
 *
 * @author charlottew
 */
public class EnumByNameComparator implements Comparator<Enum<?>> {

    /**
     * Get instance of EnumByNameComparator.
     */
    public static final Comparator<Enum<?>> INSTANCE = new EnumByNameComparator();

    @Override
    public int compare(final Enum<?> enum1, final Enum<?> enum2) {
        return enum1.name().compareTo(enum2.name());
    }
}
