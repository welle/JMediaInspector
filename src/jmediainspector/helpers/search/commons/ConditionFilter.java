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
     * Contains only.
     */
    CONTAINS(BinaryCondition.Op.LIKE, "Contains"),

    /**
     * Contains only.
     */
    CONTAINS_ONLY(BinaryCondition.Op.EQUAL_TO, "Contains only"),

    /**
     * Less than.
     */
    LESS_THAN(BinaryCondition.Op.LESS_THAN, "Less than"),

    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO(BinaryCondition.Op.LESS_THAN_OR_EQUAL_TO, "Less than or equal to"),

    /**
     * Greater than.
     */
    GREATER_THAN(BinaryCondition.Op.GREATER_THAN, "Greater than"),

    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO(BinaryCondition.Op.GREATER_THAN_OR_EQUAL_TO, "Greater than or equal to"),

    /**
     * Like.
     */
    LIKE(BinaryCondition.Op.LIKE, "Like"),

    /**
     * Not like.
     */
    NOT_LIKE(BinaryCondition.Op.NOT_LIKE, "Not like"),

    /**
     * Equal to.
     */
    EQUALS(BinaryCondition.Op.EQUAL_TO, "Equal to"),

    /**
     * Not equal to.
     */
    NOT_EQUALS(BinaryCondition.Op.NOT_EQUAL_TO, "Not equal to");

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

        if (condition != null && condition.trim().length() > 0) {
            for (final ConditionFilter conditionFilter : values()) {
                if (conditionFilter.getReadableName().equals(condition)) {
                    result = conditionFilter;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @return the operation
     */
    public final Op getOperation() {
        return this.operation;
    }
}
