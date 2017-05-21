package jmediainspector.context;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.PlexConfigurationHelper;

/**
 * Application Context.
 *
 * @author charlottew
 */
public class ApplicationContext {

    @NonNull
    private final static ApplicationContext INSTANCE = new ApplicationContext();
    @NonNull
    private final PlexConfigurationHelper plexConfigurationHelper;
    @NonNull
    private final MetadataSearchConfigurationHelper metadataSearchConfigurationHelper;

    /**
     * Constructor.
     */
    private ApplicationContext() {
        this.plexConfigurationHelper = new PlexConfigurationHelper();
        this.metadataSearchConfigurationHelper = new MetadataSearchConfigurationHelper();

        this.plexConfigurationHelper.addListener(this.metadataSearchConfigurationHelper);
        this.metadataSearchConfigurationHelper.addListener(this.plexConfigurationHelper);
    }

    /**
     * Get current context.
     *
     * @return instance of context
     */
    @NonNull
    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    /**
     * Get current PlexConfigurationHelper.
     *
     * @return current PlexConfigurationHelper
     */
    @NonNull
    public PlexConfigurationHelper getCurrentPlexConfigurationHelper() {
        return this.plexConfigurationHelper;
    }

    /**
     * Get current MetadataSearchConfigurationHelper.
     *
     * @return current MetadataSearchConfigurationHelper
     */
    @NonNull
    public MetadataSearchConfigurationHelper getCurrentMetadataSearchConfigurationHelper() {
        return this.metadataSearchConfigurationHelper;
    }
}
