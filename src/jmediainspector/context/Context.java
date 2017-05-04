package jmediainspector.context;

import jmediainspector.helpers.ConfigurationHelper;

/**
 * Context.
 *
 * @author charlottew
 */
public class Context {

    private final static Context INSTANCE = new Context();

    /**
     * Get current context.
     *
     * @return instance of context
     */
    public static Context getInstance() {
        return INSTANCE;
    }

    private final ConfigurationHelper configurationHelper = new ConfigurationHelper();

    /**
     * Get current configurationHelper.
     *
     * @return current configurationHelper
     */
    public ConfigurationHelper getCurrentConfigurationHelper() {
        return this.configurationHelper;
    }

}
