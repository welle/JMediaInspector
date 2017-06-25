package jmediainspector.helpers.search.types.text;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.CriteriaInterface;
import jmediainspector.helpers.search.types.interfaces.SearchInterface;
import jmediainspector.helpers.search.types.text.filters.TextLanguageCriteria;

/**
 * Search Text(subtitles) enumeration.
 *
 * @author charlottew
 */
public enum SearchTextEnum implements SearchInterface {
    /**
     * Language.
     */
    LANGUAGE(TextLanguageCriteria.class);

    @NonNull
    private Class<? extends CriteriaInterface> filtersInterface;

    SearchTextEnum(@NonNull final Class<? extends CriteriaInterface> filtersInterface) {
        this.filtersInterface = filtersInterface;
    }

    @Override
    public Class<? extends CriteriaInterface> getFiltersInterface() {
        return this.filtersInterface;
    }
}
