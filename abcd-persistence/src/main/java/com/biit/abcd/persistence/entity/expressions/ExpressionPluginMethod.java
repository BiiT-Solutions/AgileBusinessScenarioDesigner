package com.biit.abcd.persistence.entity.expressions;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.serialization.expressions.ExpressionPluginMethodDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionPluginMethodSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = ExpressionPluginMethodDeserializer.class)
@JsonSerialize(using = ExpressionPluginMethodSerializer.class)
@Table(name = "expression_plugin_method")
public class ExpressionPluginMethod extends Expression {
    private static final long serialVersionUID = -1357787104083039897L;

    @Column(name = "plugin_interface")
    private Class<?> pluginInterface = null;

    @Column(name = "plugin_name")
    private String pluginName = null;

    @Column(name = "plugin_method_name")
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
