package jmediainspector.helpers.search.types.general;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.general.filters.GeneralFileExtensionCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;

/**
 * General search criteria.
 *
 * @author Cha
 */
public enum SearchGeneralEnum implements SearchInterface {

    /**
     * File Extension.
     */
    EXTENSION(GeneralFileExtensionCriteria.class);

    @NonNull
    private Class<? extends AbstractInterface> filtersInterface;
    private static @NonNull List<Class<? extends AbstractInterface>> ALL_VALUES = new ArrayList<>();

    static {
        for (final @NonNull SearchGeneralEnum searchGeneralEnum : SearchGeneralEnum.values()) {
            ALL_VALUES.add(searchGeneralEnum.getFiltersInterface());
        }
    }

    SearchGeneralEnum(@NonNull final Class<? extends AbstractInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    @Override
    public Class<? extends AbstractInterface> getFiltersInterface() {
        return this.filtersInterface;
    }

    @Override
    public @NonNull List<Class<? extends AbstractInterface>> getAllValues() {
        return ALL_VALUES;
    }

    @Override
    @NonNull
    public SearchInterface @NonNull [] getALL() {
        return values();
    }
}
