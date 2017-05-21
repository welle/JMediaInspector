package jmediainspector.context;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.PlexConfigurationHelper;

/**
 * Context.
 *
 * @author charlottew
 */
public class Context {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(Context.class.getName());
    @NonNull
    private final static Context INSTANCE = new Context();

    private PlexConfigurationHelper configurationHelper = null;

    /**
     * Constructor.
     */
    public Context() {
        try {
            this.configurationHelper = new PlexConfigurationHelper();
        } catch (final FileNotFoundException e) {
            LOGGER.logp(Level.SEVERE, "Context", "Context", e.getMessage(), e);
        }
    }

    /**
     * Get current context.
     *
     * @return instance of context
     */
    @NonNull
    public static Context getInstance() {
        return INSTANCE;
    }

    /**
     * Get current configurationHelper.
     *
     * @return current configurationHelper
     */
    @NonNull
    public PlexConfigurationHelper getCurrentConfigurationHelper() {
        final PlexConfigurationHelper currentConfigurationHelper = this.configurationHelper;
        assert currentConfigurationHelper != null;
        return currentConfigurationHelper;
    }
}
