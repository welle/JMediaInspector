package aka.jmediainspector.config.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.config.Criterias;
import aka.jmediainspector.config.Metadatas;
import aka.jmediainspector.config.Search;
import aka.jmediainspector.config.Searchs;

/**
 * Helper to manage search criteria for metatata.
 *
 * @author Cha
 */
public class MetadataSearchConfigurationHelper extends AbstractConfigurationHelper {

    private final Metadatas metadatas;
    private final Searchs searchs;
    private final Search currentSelectedSearch;

    /**
     * Search type.
     *
     * @author Cha
     */
    public enum Type {
        /**
         * Search in Plex.
         */
        Plex("Plex"),

        /**
         * Search in files.
         */
        File("File");

        @NonNull
        private String name;

        Type(@NonNull final String name) {
            this.name = name;
        }

        /**
         * Get name.
         *
         * @return name
         */
        @NonNull
        public String getName() {
            return this.name;
        }
    }

    /**
     * Constructor.
     */
    public MetadataSearchConfigurationHelper() {
        super();

        final Searchs currentSearchs = getApplication().getMetadatas().getSearchs();
        if (currentSearchs == null) {
            getApplication().getMetadatas().setSearchs(getFactoryConfig().createSearchs());
        }

        this.metadatas = getApplication().getMetadatas();
        this.searchs = getApplication().getMetadatas().getSearchs();
        if (this.searchs.getSearch().isEmpty()) {
            addNewSearch(Type.Plex);
        }
        this.currentSelectedSearch = this.searchs.getSearch().get(0);
    }

    @Override
    public void onChanges() {
        super.onChanges();

        // Override plex configuration with the current one.
        getApplication().setMetadatas(this.metadatas);
    }

    /**
     * Get the searchs of metadatas.
     *
     * @return configuration
     */
    public Searchs getSearchs() {
        return this.searchs;
    }

    /**
     * Delete the current search.
     *
     * @param search search to be deleted
     */
    public void deleteCurrentConfiguration(final Search search) {
        this.searchs.getSearch().remove(search);
    }

    /**
     * Add a new search.
     *
     * @param type search type
     * @return new search created
     */
    @NonNull
    public Search addNewSearch(@NonNull final Type type) {
        final Search newSearch = getFactoryConfig().createSearch();
        final Criterias newCriterias = getFactoryConfig().createCriterias();
        newSearch.setCriterias(newCriterias);
        newSearch.setType(type.name());
        this.searchs.getSearch().add(newSearch);

        assert newSearch != null;
        return newSearch;
    }

    /**
     * Get all searches of given type.
     *
     * @param type
     * @return all search of given type
     */
    @NonNull
    public List<Search> getSearchByType(@NonNull final Type type) {
        List<Search> result = null;

        if (this.searchs != null && this.searchs.getSearch() != null && !this.searchs.getSearch().isEmpty()) {
            result = this.searchs.getSearch().stream()
                    .filter(search -> type.getName().equals(search.getType()))
                    .collect(Collectors.toList());
        }
        if (result == null) {
            result = new ArrayList<>();
        }

        return result;
    }

    /**
     * Add a new filter.
     *
     * @return new filter created
     */
    @NonNull
    public Criteria getNewCriteria() {
        final Criteria newCriteria = getFactoryConfig().createCriteria();
        this.currentSelectedSearch.getCriterias().getCriteria().add(newCriteria);

        assert newCriteria != null;
        return newCriteria;
    }
}
