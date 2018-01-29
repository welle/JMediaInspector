package aka.jmediainspector.helpers.search.commons;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.jmetadataquery.search.constants.conditions.Operator;

/**
 * Search filters enumeration.
 *
 * @author charlottew
 */
public enum ConditionFilter {

    /**
     * Contains only.
     */
    CONTAINS(Operator.LIKE, "Contains"),

    /**
     * Contains only.
     */
    CONTAINS_ONLY(Operator.EQUAL_TO, "Contains only"),

    /**
     * Less than.
     */
    LESS_THAN(Operator.LESS_THAN, "Less than"),

    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO(Operator.LESS_THAN_OR_EQUAL_TO, "Less than or equal to"),

    /**
     * Greater than.
     */
    GREATER_THAN(Operator.GREATER_THAN, "Greater than"),

    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO(Operator.GREATER_THAN_OR_EQUAL_TO, "Greater than or equal to"),

    /**
     * Like.
     */
    LIKE(Operator.LIKE, "Like"),

    /**
     * Not like.
     */
    NOT_LIKE(Operator.NOT_LIKE, "Not like"),

    /**
     * Equal to.
     */
    EQUALS(Operator.EQUAL_TO, "Equal to"),

    /**
     * Not equal to.
     */
    NOT_EQUALS(Operator.NOT_EQUAL_TO, "Not equal to");

    @NonNull
    private Operator operation;
    private @NonNull String readableName;

    private ConditionFilter(@NonNull final Operator operation, @NonNull final String readableName) {
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
    @NonNull
    public final Operator getOperation() {
        return this.operation;
    }
}
