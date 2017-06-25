package jmediainspector.helpers.search.types.interfaces;

import java.util.List;

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
    public Class<? extends AbstractInterface> getFiltersInterface();

    /**
     * Get all values.
     *
     * @return All values
     */
    @NonNull
    public List<Class<? extends AbstractInterface>> getAllValues();

}
