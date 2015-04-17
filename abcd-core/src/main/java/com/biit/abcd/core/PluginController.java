package com.biit.abcd.core;

import java.io.File;
import java.util.Collection;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.plugins.exceptions.InvalidMethodParametersException;
import com.biit.plugins.exceptions.MethodInvocationException;
import com.biit.plugins.exceptions.NoMethodFoundException;
import com.biit.plugins.interfaces.IPlugin;

/**
 * Singleton in charge of managing the plugins of the application
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PluginController {

	private static PluginController INSTANCE = new PluginController();
	private PluginManager pluginManager;
	private PluginManagerUtil pluginManagerUtil;

	public static PluginController getInstance() {
		return INSTANCE;
	}

	// Override of the clone method to avoid creating more than one instance
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private PluginController() {
		pluginManager = PluginManagerFactory.createPluginManager();
		pluginManagerUtil = new PluginManagerUtil(pluginManager);
		scanForPlugins();
	}

	/**
	 * Scans for new plugins in the specified path of the configuration file.
	 */
	public void scanForPlugins() {
		String folderToScan = AbcdConfigurationReader.getInstance().getPluginsPath();
		// If too short, plugin library launch
		// Caused by: java.lang.StringIndexOutOfBoundsException: String index
		// out of range: 4
		// at java.lang.String.substring(String.java:1907)
		// at
		// net.xeoh.plugins.base.impl.classpath.loader.FileLoader.loadFrom(FileLoader.java:83)
		if (folderToScan != null && folderToScan.length() > 4) {
			pluginManager.addPluginsFrom(new File(AbcdConfigurationReader.getInstance().getPluginsPath()).toURI());
		}
	}

	public boolean existsPlugins() {
		Collection<IPlugin> plugins = getAllPlugins();
		if ((plugins != null) && (!plugins.isEmpty())) {
			return true;
		}
		return false;
	}

	private Class getInterfaceClass(String interfaceName) {
		try {
			return Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	/**
	 * Returns all the plugins that are loaded
	 * 
	 * @return
	 */
	public Collection<IPlugin> getAllPlugins() {
		return pluginManagerUtil.getPlugins(IPlugin.class);
	}

	/**
	 * Returns all the plugins that implement the passed interface
	 * 
	 * @param pluginInterface
	 * @return
	 */
	public Collection<IPlugin> getAllPlugins(Class pluginInterface) {
		return pluginManagerUtil.getPlugins(pluginInterface);
	}

	/**
	 * Returns all the plugins that implement the passed class name
	 * 
	 * @param pluginInterface
	 * @return
	 */
	public Collection<IPlugin> getAllPlugins(String interfaceName) {
		return getAllPlugins(getInterfaceClass(interfaceName));
	}

	/**
	 * Returns the plugin specified by the class passed (the class can be an
	 * interface).<br>
	 * If several plugins implement the same class, one of them is selected
	 * randomly.
	 * 
	 * @param pluginInterface
	 * @return
	 */
	public Plugin getPlugin(Class pluginInterface) {
		return pluginManagerUtil.getPlugin(pluginInterface);
	}

	/**
	 * Returns the plugin specified by the class name passed (the class can be
	 * an interface).<br>
	 * If several plugins implement the same class name, one of them is selected
	 * randomly.
	 * 
	 * @param interfaceName
	 * @return
	 */
	public Plugin getPlugin(String interfaceName) {
		return getPlugin(getInterfaceClass(interfaceName));
	}

	/**
	 * Returns the plugin that matches the interface and the plugin name passed
	 * 
	 * @param interfaceName
	 * @param pluginName
	 * @return
	 */
	public IPlugin getPlugin(Class interfaceName, String pluginName) {
		Collection<IPlugin> plugins = getAllPlugins(interfaceName);
		for (IPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	/**
	 * Returns the plugin that matches the interface name and the plugin name
	 * passed as strings
	 * 
	 * @param interfaceName
	 * @param pluginName
	 * @return
	 */
	public IPlugin getPlugin(String interfaceName, String pluginName) {
		return getPlugin(getInterfaceClass(interfaceName), pluginName);
	}

	/**
	 * Executes the method of the plugin specified.<br>
	 * It takes any number of parameters and passes them to the method
	 * invocation.
	 * 
	 * @param interfaceName
	 * @param pluginName
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public Object executePluginMethod(Class interfaceName, String pluginName, String methodName, Object... parameters) {
		try {
			IPlugin pluginInterface = getPlugin(interfaceName, pluginName);
			return pluginInterface.executeMethod(methodName, parameters);

		} catch (IllegalArgumentException | NoMethodFoundException | InvalidMethodParametersException
				| MethodInvocationException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	/**
	 * Executes the method of the plugin specified.<br>
	 * It takes any number of parameters and passes them to the method
	 * invocation.
	 * 
	 * @param interfaceName
	 * @param pluginName
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public Object executePluginMethod(String interfaceName, String pluginName, String methodName, Object... parameters) {
		return executePluginMethod(getInterfaceClass(interfaceName), pluginName, methodName, parameters);
	}

}
