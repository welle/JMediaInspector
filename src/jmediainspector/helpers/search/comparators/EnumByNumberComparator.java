package jmediainspector.helpers.search.comparators;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Comparator for enumeration names.<br/>
 * Use Arrays.sort(values, EnumByNameComparator.INSTANCE); to sort all the values.
 *
 * @author charlottew
 */
public class EnumByNumberComparator implements Comparator<Enum<?>> {

    /**
     * Get instance of EnumByNameAndNumberComparator.
     */
    public static final Comparator<Enum<?>> INSTANCE = new EnumByNumberComparator();

    @Override
    public int compare(final Enum<?> enum1, final Enum<?> enum2) {
        String name1 = enum1.name();
        String name2 = enum2.name();
        // Remove all non digits
        name1 = name1.replaceAll("[^\\d]", "");
        name2 = name2.replaceAll("[^\\d]", "");

        final BigDecimal big1 = new BigDecimal(name1);
        final BigDecimal big2 = new BigDecimal(name2);

        return big1.compareTo(big2);
    }
}
