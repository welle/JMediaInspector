package jmediainspector.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import jmediainspector.config.Application;
import jmediainspector.config.Configuration;
import jmediainspector.config.Configurations;
import jmediainspector.config.ObjectFactory;
import jmediainspector.config.Paths;
import jmediainspector.listeners.ConfigurationsListener;

/**
 * Plex Configurations helper.
 *
 * @author cha
 */
public final class PlexConfigurationHelper {

    private Application application;
    @NonNull
    private final static Logger LOGGER = Logger.getLogger(PlexConfigurationHelper.class.getName());
    private final Configurations plexConfigurations;
    @NonNull
    private final ObjectFactory factoryConfig = new ObjectFactory();
    @NonNull
    private final File configFile = new File(System.getProperty("user.home") + "/jmediainspector.xml");
    private Configuration selectedConfiguration;
    @NonNull
    private final List<@NonNull ConfigurationsListener> configurationsListenerList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @throws FileNotFoundException
     */
    public PlexConfigurationHelper() throws FileNotFoundException {
        this.application = null;
        if (!this.configFile.exists()) {
            this.application = this.factoryConfig.createApplication();
            saveConfig();
        }

        try {
            final JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Unmarshaller unmarshaller = jc.createUnmarshaller();

            this.application = (Application) unmarshaller.unmarshal(this.configFile);
        } catch (final JAXBException e) {
            LOGGER.logp(Level.INFO, "ConfigurationHelper", "ConfigurationHelper", e.getMessage(), e);
            this.application = this.factoryConfig.createApplication();
            this.application.setPlex(this.factoryConfig.createPlex());
            this.application.setMetadatas(this.factoryConfig.createMetadatas());
            saveConfig();
        }
        if (this.application == null) {
            throw new FileNotFoundException("Could not open configurations!");
        }

        final Configurations currentPlexConfigurations = this.application.getPlex().getConfigurations();
        if (currentPlexConfigurations == null) {
            this.application.getPlex().setConfigurations(this.factoryConfig.createConfigurations());
        }
        this.plexConfigurations = this.application.getPlex().getConfigurations();
    }

    /**
     * Save current configuration.
     */
    public void saveConfig() {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

            final OutputStream outputStream = new FileOutputStream(this.configFile);
            marshaller.marshal(this.application, outputStream);

            outputStream.close();

            // warn listener
            for (final ConfigurationsListener configurationsListener : this.configurationsListenerList) {
                configurationsListener.onChanges();
            }
        } catch (JAXBException | IOException e) {
            LOGGER.logp(Level.SEVERE, "ConfigurationHelper", "saveConfig", e.getMessage(), e);
        }
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
            this.selectedConfiguration.setSelected(Boolean.FALSE);
        }

        this.selectedConfiguration = configuration;
        this.selectedConfiguration.setSelected(Boolean.TRUE);
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
            this.application.setPlex(this.factoryConfig.createPlex());
//            this.application.setSearchs(this.factoryConfig.createApplicationSearchs());
        }
        this.plexConfigurations.getConfiguration().add(newConfiguration);

        assert newConfiguration != null;
        return newConfiguration;
    }

    /**
     * Add configurations listener.
     *
     * @param configurationsListener
     */
    public void addListener(@NonNull final ConfigurationsListener configurationsListener) {
        this.configurationsListenerList.add(configurationsListener);
    }

    /**
     * Remove configurations listener.
     *
     * @param configurationsListener
     */
    public void removeListener(@NonNull final ConfigurationsListener configurationsListener) {
        this.configurationsListenerList.remove(configurationsListener);
    }
}
