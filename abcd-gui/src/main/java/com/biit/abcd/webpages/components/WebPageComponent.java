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

import com.vaadin.navigator.View;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * Parent class for all webPages. it extends a CustomComponent. Any child class needs to call
 * {@link CustomComponent#setCompositionRoot(Component)} in its constructor.
 *
 */
public abstract class WebPageComponent extends CustomComponent implements View {
	private static final long serialVersionUID = -386946981801328161L;
	private AbstractOrderedLayout rootLayout;

	public AbstractOrderedLayout getRootLayout() {
		return rootLayout;
	}

	/**
	 * Each page can decide if want a VerticalLayout, HorizontalLayout, ...
	 *
	 * @param rootLayout
	 */
	public void setRootLayout(AbstractOrderedLayout rootLayout) {
		this.rootLayout = rootLayout;
	}

}
