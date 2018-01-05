package jmediainspector.helpers.search.types.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.AbstractInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;
import jmediainspector.helpers.search.types.text.filters.TextLanguageCriteria;
import jmediainspector.helpers.search.types.text.filters.TextNumberOfStreamCriteria;

/**
 * Search Text(subtitles) enumeration.
 *
 * @author charlottew
 */
public enum SearchTextEnum implements SearchInterface {

    /**
     * Language.
     */
    LANGUAGE(TextLanguageCriteria.class),

    /**
     * Number of streams.
     */
    NUMBER_OF_STREAMS(TextNumberOfStreamCriteria.class);

    @NonNull
    private Class<? extends AbstractInterface> filtersInterface;
    private static @NonNull List<Class<? extends AbstractInterface>> ALL_VALUES = new ArrayList<>();

    static {
        for (final @NonNull SearchTextEnum searchTextEnum : SearchTextEnum.values()) {
            ALL_VALUES.add(searchTextEnum.getFiltersInterface());
        }
    }

    SearchTextEnum(@NonNull final Class<? extends AbstractInterface> filtersInterface) {
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
    public @NonNull SearchInterface @NonNull [] getALL() {
        return values();
    }
}
