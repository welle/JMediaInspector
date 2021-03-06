package aka.jmediainspector.config.helpers.context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Application;
import aka.jmediainspector.config.ObjectFactory;
import aka.jmediainspector.context.ApplicationContext;

/**
 * Context for Application configuration.
 *
 * @author Cha
 */
public class ApplicationConfigurationContext {

    @NonNull
    private final static ApplicationConfigurationContext INSTANCE = new ApplicationConfigurationContext();

    private Application application;
    @NonNull
    private final ObjectFactory factoryConfig = new ObjectFactory();
    @NonNull
    private final File configFile = new File(System.getProperty("user.home") + "/jmediainspector.xml");

    /**
     * Constructor.
     */
    private ApplicationConfigurationContext() {
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
            ApplicationContext.getInstance().getLogger().logp(Level.INFO, "ConfigurationHelper", "ConfigurationHelper", e.getMessage(), e);
            this.application = this.factoryConfig.createApplication();
            this.application.setPlex(this.factoryConfig.createPlex());
            this.application.setMetadatas(this.factoryConfig.createMetadatas());
            saveConfig();
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

            final OutputStream outputStream = new FileOutputStream(this.configFile);
            marshaller.marshal(this.application, outputStream);

            outputStream.close();
        } catch (JAXBException | IOException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "ApplicationContext", "saveConfig", e.getMessage(), e);
        }
    }

    /**
     * Get current context.
     *
     * @return instance of context
     */
    @NonNull
    public static ApplicationConfigurationContext getInstance() {
        return INSTANCE;
    }

    /**
     * Get the Application main configuration object.
     *
     * @return the application
     */
    @NonNull
    public final Application getApplication() {
        final Application currentApplication = this.application;
        assert currentApplication != null;
        return currentApplication;
    }

    /**
     * Get ObjectFactory.
     *
     * @return the factoryConfig
     */
    @NonNull
    public final ObjectFactory getFactoryConfig() {
        return this.factoryConfig;
    }

    /**
     * Get the config file.
     *
     * @return the configFile
     */
    @NonNull
    public final File getConfigFile() {
        return this.configFile;
    }

    /**
     * Reload file instance.
     */
    public void reload() {
        try {
            final JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Unmarshaller unmarshaller = jc.createUnmarshaller();

            this.application = (Application) unmarshaller.unmarshal(this.configFile);
        } catch (final JAXBException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.INFO, "ApplicationContext", "reload", e.getMessage(), e);
        }
    }

}
