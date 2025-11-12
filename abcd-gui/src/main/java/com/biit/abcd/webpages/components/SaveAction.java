package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

/**
 * Callback class to do the action
 */
public interface SaveAction {

	/**
	 * Returns if is valid to save or not. This serves to implement a validation
	 * condition previous to a download.
	 *
	 * @return true if is valid.
	 */
	boolean isValid();

	byte[] getInformationData();

	/**
	 * Extension of the file to generate. Also must be the type of file in
	 * graphviz.
	 *
	 * @return the extension of the file.S
	 */
	String getExtension();

	/**
	 * Mimetype of the generated file ("application/pdf", "image/png", ...)
	 *
	 * @return the mimetype.
	 */
	String getMimeType();

	/**
	 * Gets the file name.
	 *
	 * @return the file name.
	 */
	String getFileName();

}
