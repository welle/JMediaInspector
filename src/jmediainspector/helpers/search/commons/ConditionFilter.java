package jmediainspector.helpers.search.commons;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.BinaryCondition.Op;

/**
 * Search filters enumeration.
 *
 * @author charlottew
 */
public enum ConditionFilter {

    /**
     * Less than.
     */
    LESS_THAN(BinaryCondition.Op.LESS_THAN, "less than"),

    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO(BinaryCondition.Op.LESS_THAN_OR_EQUAL_TO, "less than or equal to"),

    /**
     * Greater than.
     */
    GREATER_THAN(BinaryCondition.Op.GREATER_THAN, "greater than"),

    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO(BinaryCondition.Op.GREATER_THAN_OR_EQUAL_TO, "greater than or equal to"),

    /**
     * Like.
     */
    LIKE(BinaryCondition.Op.LIKE, "like"),

    /**
     * Not like.
     */
    NOT_LIKE(BinaryCondition.Op.NOT_LIKE, "not like"),

    /**
     * Equal to.
     */
    EQUALS(BinaryCondition.Op.EQUAL_TO, "equal to"),

    /**
     * Not equal to.
     */
    NOT_EQUALS(BinaryCondition.Op.NOT_EQUAL_TO, "not equal to");

    private Op operation;
    private @NonNull String readableName;

    private ConditionFilter(final BinaryCondition.Op operation, @NonNull final String readableName) {
        this.operation = operation;
        this.readableName = readableName;
    }

    /**
     * Get readable name.
     *
     * @return readable name
     */
    @NonNull
    public String getReadableName() {
        return this.readableName;
    }

    /**
     * Get related condition filter.
     *
     * @param condition
     * @return related section type
     */
    @Nullable
    public static ConditionFilter getConditionFilter(@Nullable final String condition) {
        ConditionFilter result = null;
        for (final ConditionFilter conditionFilter : values()) {
            if (conditionFilter.getReadableName().equals(condition)) {
                result = conditionFilter;
                break;
            }
        }
        return result;
    }
}
