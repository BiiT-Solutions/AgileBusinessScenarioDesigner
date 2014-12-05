package com.biit.abcd.core;

import java.io.File;
import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.plugins.interfaces.IDroolsRulePlugin;
import com.biit.plugins.interfaces.IPlugin;

/**
 * Singleton in charge of managing the plugins of the application
 * 
 */
public class PluginController {

	private static PluginController INSTANCE = new PluginController();
	private PluginManager pluginManager;
	private PluginManagerUtil pluginManagerUtil;

	private PluginController() {
		pluginManager = PluginManagerFactory.createPluginManager();
		pluginManagerUtil = new PluginManagerUtil(pluginManager);
		scanForPlugins();
	}

	public static PluginController getInstance() {
		return INSTANCE;
	}

	// Override of the clone method to avoid creating more than one instance
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void runPlugins() {
		Collection<IPlugin> plugins = pluginManagerUtil.getPlugins(IPlugin.class);
		if (plugins != null) {
			for (IPlugin plugin : plugins) {
				System.out.println(plugin.sayHello());
			}
		}
	}
	
	public IDroolsRulePlugin getDroolsPlugin() {
		return pluginManagerUtil.getPlugin(IDroolsRulePlugin.class);
	}

	public void scanForPlugins() {
		pluginManager.addPluginsFrom(new File(AbcdConfigurationReader.getInstance().getPluginsPath()).toURI());
	}
}
