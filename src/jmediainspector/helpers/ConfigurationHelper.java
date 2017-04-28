package jmediainspector.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import jmediainspector.config.Configurations;
import jmediainspector.config.Configurations.Configuration;
import jmediainspector.config.Configurations.Configuration.Paths;
import jmediainspector.config.ObjectFactory;

/**
 * Configuration helper.
 *
 * @author cha
 */
/**
 * @author charlottew
 *
 */
public final class ConfigurationHelper {

    private @NonNull final static Logger LOGGER = Logger.getLogger(ConfigurationHelper.class.getName());

    private Configurations configurations;

    @NonNull
    private final ObjectFactory factoryConfig = new ObjectFactory();
    @NonNull
    private static File CONFIG_FILE;
    @Nullable
    private Configuration selectedConfiguration;

    static {
        final String homeDirectory = System.getProperty("user.home");
        CONFIG_FILE = new File(homeDirectory + "/jmediainspector.xml");
    }

    /**
     * Constructor.
     */
    public ConfigurationHelper() {
        if (!CONFIG_FILE.exists()) {
            this.configurations = this.factoryConfig.createConfigurations();
            saveConfig();
        }

        try {
            final JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Unmarshaller unmarshaller = jc.createUnmarshaller();

            this.configurations = (Configurations) unmarshaller.unmarshal(CONFIG_FILE);
        } catch (final JAXBException e) {
            LOGGER.logp(Level.SEVERE, "ConfigurationHelper", "ConfigurationHelper", e.getMessage(), e);
        }
    }

    /**
     * Save current configuration.
     */
    public void saveConfig() {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

            final OutputStream outputStream = new FileOutputStream(CONFIG_FILE);
            marshaller.marshal(this.configurations, outputStream);

            outputStream.close();
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
            for (final Configuration configuration : this.configurations.getConfiguration()) {
                if (configuration.isSelected() != null && configuration.isSelected().booleanValue()) {
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
    public void setSelectedConfiguration(@NonNull final Configuration configuration) {
        if (this.selectedConfiguration != null) {
            this.selectedConfiguration.setSelected(Boolean.FALSE);
        }

        this.selectedConfiguration = configuration;
        this.selectedConfiguration.setSelected(Boolean.TRUE);
    }

    /**
     * Get the configuration.
     *
     * @return configuration
     */
    public Configurations getConfigurations() {
        return this.configurations;
    }

    /**
     * Get a new Paths object.
     *
     * @return new Paths
     */
    public Paths getNewPaths() {
        return this.factoryConfig.createConfigurationsConfigurationPaths();
    }

    /**
     * Delete the current configuration.
     *
     * @param configuration configuration to be deleted
     */
    public void deleteCurrentConfiguration(@NonNull final Configuration configuration) {
        this.configurations.getConfiguration().remove(configuration);
    }

    /**
     * Add a new configuration.
     *
     * @return new configuration created
     */
    @NonNull
    public Configuration addNewConfiguration() {
        final Configuration newConfiguration = this.factoryConfig.createConfigurationsConfiguration();
        final Paths newPaths = this.factoryConfig.createConfigurationsConfigurationPaths();
        newConfiguration.setPaths(newPaths);
        this.configurations.getConfiguration().add(newConfiguration);

        assert newConfiguration != null;
        return newConfiguration;
    }
}
