package jmediainspector.config.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.config.Application;
import jmediainspector.config.ObjectFactory;
import jmediainspector.config.helpers.context.ApplicationConfigurationContext;
import jmediainspector.listeners.ApplicationConfigurationsListener;

/**
 * All configurations helper must implements this common abstract class.
 *
 * @author Cha
 */
public abstract class AbstractConfigurationHelper implements ApplicationConfigurationsListener {

    @NonNull
    private final List<@NonNull ApplicationConfigurationsListener> configurationsListenerList = new ArrayList<>();
    @NonNull
    private final ApplicationConfigurationContext applicationContext;

    /**
     * Constructor.
     */
    public AbstractConfigurationHelper() {
        this.applicationContext = ApplicationConfigurationContext.getInstance();
    }

    /**
     * Get the Application main configuration object.
     *
     * @return the application
     */
    @NonNull
    public final Application getApplication() {
        return this.applicationContext.getApplication();
    }

    /**
     * Get ObjectFactory.
     *
     * @return the factoryConfig
     */
    @NonNull
    public final ObjectFactory getFactoryConfig() {
        return this.applicationContext.getFactoryConfig();
    }

    /**
     * Add configurations listener.
     *
     * @param configurationsListener
     */
    public void addListener(@NonNull final ApplicationConfigurationsListener configurationsListener) {
        this.configurationsListenerList.add(configurationsListener);
    }

    /**
     * Remove configurations listener.
     *
     * @param configurationsListener
     */
    public void removeListener(@NonNull final ApplicationConfigurationsListener configurationsListener) {
        this.configurationsListenerList.remove(configurationsListener);
    }

    /**
     * Get the config file.
     *
     * @return the configFile
     */
    @NonNull
    public final File getConfigFile() {
        return this.applicationContext.getConfigFile();
    }

    @Override
    public void onChanges() {
        // reload file
        this.applicationContext.reload();
    }

    /**
     * Save current configuration.
     */
    public void saveConfig() {
        this.applicationContext.saveConfig();

        for (final ApplicationConfigurationsListener applicationConfigurationsListener : this.configurationsListenerList) {
            applicationConfigurationsListener.onChanges();
        }
    }
}
