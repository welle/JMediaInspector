package jmediainspector.config.helpers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import jmediainspector.config.Configuration;
import jmediainspector.config.Configurations;
import jmediainspector.config.ObjectFactory;
import jmediainspector.config.Paths;
import jmediainspector.config.Plex;

/**
 * Plex Configurations helper.
 *
 * @author cha
 */
public final class PlexConfigurationHelper extends AbstractConfigurationHelper {

    private final Plex plex;
    private final Configurations plexConfigurations;
    @NonNull
    private final ObjectFactory factoryConfig = new ObjectFactory();
    private Configuration selectedConfiguration;

    /**
     * Constructor.
     */
    public PlexConfigurationHelper() {
        super();

        final Configurations currentPlexConfigurations = getApplication().getPlex().getConfigurations();
        if (currentPlexConfigurations == null) {
            getApplication().getPlex().setConfigurations(this.factoryConfig.createConfigurations());
        }
        this.plex = getApplication().getPlex();
        this.plexConfigurations = this.plex.getConfigurations();
    }

    @Override
    public void onChanges() {
        super.onChanges();
        getApplication().setPlex(this.plex);
    }

    /**
     * Get the selected configuration.
     *
     * @return the selected configuration
     */
    @Nullable
    public Configuration getSelectedConfiguration() {
        Configuration currentSelectedConfiguration = this.selectedConfiguration;
        if (currentSelectedConfiguration == null) {
            for (final Configuration configuration : this.plexConfigurations.getConfiguration()) {
                if (configuration.isSelected()) {
                    currentSelectedConfiguration = configuration;
                    this.selectedConfiguration = currentSelectedConfiguration;

                    // found, just break
                    break;
                }
            }
        }

        return currentSelectedConfiguration;
    }

    /**
     * Set the selected configuration.
     *
     * @param configuration
     */
    public void setSelectedConfiguration(final Configuration configuration) {
        if (this.selectedConfiguration != null) {
            this.selectedConfiguration.setSelected(false);
        }

        this.selectedConfiguration = configuration;
        this.selectedConfiguration.setSelected(true);
    }

    /**
     * Get the configurations of Plex.
     *
     * @return configuration
     */
    public Configurations getConfigurations() {
        return this.plexConfigurations;
    }

    /**
     * Get a new Paths object.
     *
     * @return new Paths
     */
    public Paths getNewPaths() {
        return this.factoryConfig.createPaths();
    }

    /**
     * Delete the current configuration.
     *
     * @param configuration configuration to be deleted
     */
    public void deleteCurrentConfiguration(final Configuration configuration) {
        this.plexConfigurations.getConfiguration().remove(configuration);
    }

    /**
     * Add a new configuration.
     *
     * @return new configuration created
     */
    public Configuration addNewConfiguration() {
        final Configuration newConfiguration = this.factoryConfig.createConfiguration();
        final Paths newPaths = this.factoryConfig.createPaths();
        newConfiguration.setPaths(newPaths);
        if (this.plexConfigurations == null) {
            getApplication().setPlex(this.factoryConfig.createPlex());
//            this.application.setSearchs(this.factoryConfig.createApplicationSearchs());
        }
        this.plexConfigurations.getConfiguration().add(newConfiguration);

        assert newConfiguration != null;
        return newConfiguration;
    }

}
