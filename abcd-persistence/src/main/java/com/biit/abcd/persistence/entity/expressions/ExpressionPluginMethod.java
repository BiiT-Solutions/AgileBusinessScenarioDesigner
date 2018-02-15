package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "expression_plugin_method")
public class ExpressionPluginMethod extends Expression {
	private static final long serialVersionUID = -1357787104083039897L;
	
	@Column(name="plugin_interface")
	private Class<?> pluginInterface = null;
	
	@Column(name="plugin_name")
	private String pluginName = null;
	
	@Column(name="plugin_method_name")
	private String pluginMethodName = null;

	public ExpressionPluginMethod() {
		super();
	}

	public ExpressionPluginMethod(Class<?> pluginInterface, String pluginName, String pluginMethodName) {
		super();
		setPluginInterface(pluginInterface);
		setPluginName(pluginName);
		setPluginMethodName(pluginMethodName);
	}

	public Class<?> getPluginInterface() {
		return pluginInterface;
	}

	public void setPluginInterface(Class<?> pluginInterface) {
		this.pluginInterface = pluginInterface;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginMethodName() {
		return pluginMethodName;
	}

	public void setPluginMethodName(String pluginMethodName) {
		this.pluginMethodName = pluginMethodName;
	}

	@Override
	protected String getExpression() {
		if (getPluginName() != null && getPluginMethodName() != null) {
			return getPluginName() + "." + getPluginMethodName().substring(6) + "(";
		} else {
			return "";
		}
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		if (getPluginName() != null && getPluginMethodName() != null) {
			return getPluginName() + "." + getPluginMethodName().substring(6) + "(";
		} else {
			return "";
		}
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionPluginMethod) {
			ExpressionPluginMethod expressionMethod = (ExpressionPluginMethod) object;
			super.copyData(expressionMethod);
			setPluginInterface(expressionMethod.getPluginInterface());
			setPluginName(expressionMethod.getPluginName());
			setPluginMethodName(expressionMethod.getPluginMethodName());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionPluginMethod.");
		}
	}

}
