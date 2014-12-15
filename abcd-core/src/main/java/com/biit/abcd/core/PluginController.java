package com.biit.abcd.core;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValue;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;
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
		// Caused by: java.lang.StringIndexOutOfBoundsException: String index out of range: 4
		// at java.lang.String.substring(String.java:1907)
		// at net.xeoh.plugins.base.impl.classpath.loader.FileLoader.loadFrom(FileLoader.java:83)
		if (folderToScan != null && folderToScan.length() > 4) {
			pluginManager.addPluginsFrom(new File(AbcdConfigurationReader.getInstance().getPluginsPath()).toURI());
		}
	}

	public boolean existsPlugins() {
		if ((getAllPlugins() != null) && (!getAllPlugins().isEmpty())) {
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
	 * Returns the plugin specified by the class passed (the class can be an interface).<br>
	 * If several plugins implement the same class, one of them is selected randomly.
	 * 
	 * @param pluginInterface
	 * @return
	 */
	public Plugin getPlugin(Class pluginInterface) {
		return pluginManagerUtil.getPlugin(pluginInterface);
	}

	/**
	 * Returns the plugin specified by the class name passed (the class can be an interface).<br>
	 * If several plugins implement the same class name, one of them is selected randomly.
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
	 * Returns the plugin that matches the interface name and the plugin name passed as strings
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
	 * It takes any number of parameters and passes them to the method invocation.
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
	 * It takes any number of parameters and passes them to the method invocation.
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

	/**
	 * Returns true if the plugin method call is well formed and false otherwise
	 * 
	 * @param expressionChain
	 * @return
	 */
	public boolean validateExpressionChain(ExpressionChain expressionChain) {
		// The left value must be a variable
		if (!(expressionChain.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
				&& !(expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)) {
			return false;
		}
		// The second expression must be ALWAYS and assignation
		if (!(expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				|| !(((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))) {
			return false;
		}
		// The third expression defines the method call
		if (!(expressionChain.getExpressions().get(2) instanceof ExpressionPluginMethod)) {
			return false;
		}
		// The last expression must be a right parenthesis
		if (!(expressionChain.getExpressions().get(expressionChain.getExpressions().size() - 1) instanceof ExpressionSymbol)
				|| !(((ExpressionSymbol) expressionChain.getExpressions().get(
						expressionChain.getExpressions().size() - 1)).getValue().equals(AvailableSymbol.RIGHT_BRACKET))) {
			return false;
		}
		return checkMethodParameters(expressionChain);
	}

	/**
	 * Checks that the parameters used in the expression chain matches the ones neede by the plugin method
	 * 
	 * @param expressionChain
	 * @return
	 */
	private boolean checkMethodParameters(ExpressionChain expressionChain) {
		ExpressionPluginMethod pluginMethod = (ExpressionPluginMethod) expressionChain.getExpressions().get(2);
		List<Class<?>> parameters = new ArrayList<>();

		for (int expressionIndex = 3; expressionIndex < expressionChain.getExpressions().size(); expressionIndex++) {
			if (expressionChain.getExpressions().get(expressionIndex) instanceof ExpressionValue<?>) {
				Class<?> expressionValueClass = getExpressionValueClass((ExpressionValue) expressionChain
						.getExpressions().get(expressionIndex));
				if (expressionValueClass != null) {
					parameters.add(expressionValueClass);
				}
			}
		}
		IPlugin pluginInterface = getPlugin(pluginMethod.getPluginInterface(), pluginMethod.getPluginName());
		if (pluginInterface == null) {
			return false;
		}
		try {
			pluginInterface.getPluginMethod(pluginMethod.getPluginMethodName(), listToArray(parameters));
		} catch (NoSuchMethodException e) {
			// If the method is not found, the parameters don't match
			return false;
		}
		return true;
	}

	/**
	 * Returns the class that represents the value inside the expression passed (if there is any)
	 * 
	 * @param expression
	 * @return
	 */
	private Class<?> getExpressionValueClass(ExpressionValue expression) {
		if (expression instanceof ExpressionValueBoolean) {
			return Boolean.class;
		} else if (expression instanceof ExpressionValueNumber) {
			return Double.class;
		} else if ((expression instanceof ExpressionValuePostalCode) || (expression instanceof ExpressionValueString)) {
			return String.class;
		} else if ((expression instanceof ExpressionValueTimestamp)
				|| (expression instanceof ExpressionValueSystemDate)) {
			return Timestamp.class;
		} else if (expression instanceof ExpressionValueCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return String.class;
			case NUMBER:
				return Double.class;
			case DATE:
				return Date.class;
			}
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			TreeObject treeObject = ((ExpressionValueTreeObjectReference) expression).getReference();
			if (!(treeObject instanceof Question)
					|| !(((Question) treeObject).getAnswerType().equals(AnswerType.INPUT))) {
				return null;
			} else {
				switch (((Question) treeObject).getAnswerFormat()) {
				case TEXT:
				case POSTAL_CODE:
					return String.class;
				case NUMBER:
					return Double.class;
				case DATE:
					if (((ExpressionValueTreeObjectReference) expression).getUnit() != null) {
						switch (((ExpressionValueTreeObjectReference) expression).getUnit()) {
						case YEARS:
						case MONTHS:
						case DAYS:
							return Integer.class;
						case DATE:
							return Date.class;
						}
					} else {
						return Date.class;
					}
					break;
				}
			}
		}
		return null;
	}

	private Class<?>[] listToArray(List<Class<?>> parameterList) {
		Class<?>[] parameters = new Class<?>[parameterList.size()];
		for (int index = 0; index < parameterList.size(); index++) {
			parameters[index] = parameterList.get(index);
		}
		return parameters;
	}
}
