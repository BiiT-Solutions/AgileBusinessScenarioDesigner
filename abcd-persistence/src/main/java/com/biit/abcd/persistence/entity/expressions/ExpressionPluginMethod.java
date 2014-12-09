package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "expression_plugin_method")
public class ExpressionPluginMethod extends Expression {

	private Class<?> pluginInterface = null;
	private String pluginMethodName = null;

	public ExpressionPluginMethod() {
		super();
	}

	public ExpressionPluginMethod(Class<?> pluginInterface, String pluginMethodName) {
		super();
		setPluginInterface(pluginInterface);
		setPluginMethodName(pluginMethodName);
	}

	public Class<?> getPluginInterface() {
		return pluginInterface;
	}

	public void setPluginInterface(Class<?> pluginInterface) {
		this.pluginInterface = pluginInterface;
	}

	public String getPluginMethodName() {
		return pluginMethodName;
	}

	public void setPluginMethodName(String pluginMethodName) {
		this.pluginMethodName = pluginMethodName;
	}

	@Override
	protected String getExpression() {
		if (getPluginMethodName() != null) {
			return getPluginMethodName().substring(6) + "(";
		} else {
			return "";
		}
	}

	@Override
	public String getRepresentation() {
		if (getPluginMethodName() != null) {
			return getPluginMethodName().substring(6) + "(";
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
			setPluginMethodName(expressionMethod.getPluginMethodName());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionPluginMethod.");
		}
	}

}
