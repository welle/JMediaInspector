package jmediainspector.helpers.search.commons;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

/**
 * Search filters enumeration.
 *
 * @author charlottew
 */
public enum ConditionFilter {

    /**
     * Less than.
     */
    LESS_THAN(BinaryCondition.Op.LESS_THAN),

    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO(BinaryCondition.Op.LESS_THAN_OR_EQUAL_TO),

    /**
     * Greater than.
     */
    GREATER_THAN(BinaryCondition.Op.GREATER_THAN),

    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO(BinaryCondition.Op.GREATER_THAN_OR_EQUAL_TO),

    /**
     * Like.
     */
    LIKE(BinaryCondition.Op.LIKE),

    /**
     * Not like.
     */
    NOT_LIKE(BinaryCondition.Op.NOT_LIKE),

    /**
     * Equal to.
     */
    EQUALS(BinaryCondition.Op.LESS_THAN),

    /**
     * Not equal to.
     */
    NOT_EQUALS(BinaryCondition.Op.NOT_EQUAL_TO);

    private ConditionFilter(final BinaryCondition.Op operation) {

    }
}
