package jmediainspector.helpers.search.types.interfaces;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Interface for search type.
 *
 * @author Cha
 */
public interface SearchInterface {

    /**
     * Get all FiltersInterface.
     *
     * @return FiltersInterface
     */
    @NonNull
    public Class<? extends CriteriaInterface> getFiltersInterface();
}
