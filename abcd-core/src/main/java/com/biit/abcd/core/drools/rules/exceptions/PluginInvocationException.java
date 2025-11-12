package com.biit.abcd.core.drools.rules.exceptions;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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
