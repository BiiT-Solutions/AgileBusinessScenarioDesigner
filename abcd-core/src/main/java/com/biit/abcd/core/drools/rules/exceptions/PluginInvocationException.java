package com.biit.abcd.core.drools.rules.exceptions;


public class PluginInvocationException extends Exception {
	private static final long serialVersionUID = -8070327939271645446L;
	String pluginInterfaceName = null;
	String pluginMethod = null;

	public PluginInvocationException(String message, String pluginInterfaceName, String pluginMethod) {
		super(message);
		this.pluginInterfaceName = pluginInterfaceName;
		this.pluginMethod = pluginMethod;
	}

	public String getPluginInterfaceName() {
		return pluginInterfaceName;
	}

	public String getPluginMethod() {
		return pluginMethod;
	}
}
