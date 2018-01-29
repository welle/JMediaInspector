package aka.jmediainspector.helpers.search.comparators;

import java.util.Comparator;

import aka.jmetadata.main.constants.video.AspectRatio;

/**
 * Comparator for Aspect ratio enum.<br/>
 * Use Arrays.sort(values, AspectRatioComparator.INSTANCE); to sort all the values.
 *
 * @author charlottew
 */
public class AspectRatioComparator implements Comparator<AspectRatio> {

    /**
     * Get instance of AspectRatioComparator.
     */
    public static final Comparator<AspectRatio> INSTANCE = new AspectRatioComparator();

    @Override
    public int compare(final AspectRatio enum1, final AspectRatio enum2) {
        final double ratio1 = enum1.getAspectRatio();
        final double ratio2 = enum2.getAspectRatio();

        final Double ratioDouble1 = new Double(ratio1);
        final Double ratioDouble2 = new Double(ratio2);

        return ratioDouble1.compareTo(ratioDouble2);
    }
}
