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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

public class AcceptCancelClearWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 5798236038284261870L;
	private List<ClearElementsActionListener> clearListeners;
	protected IconButton clearButton;

	public interface ClearElementsActionListener {
		public void clearAction(AcceptCancelClearWindow window);
	}

	public AcceptCancelClearWindow() {
		super();
		setModal(true);
		clearListeners = new ArrayList<>();
	}

	public AcceptCancelClearWindow(Component content) {
		super(content);
		clearListeners = new ArrayList<>();
	}

	@Override
	protected void generateAcceptCancelButton() {
		super.generateAcceptCancelButton();
		clearButton = new IconButton(LanguageCodes.DELETE_BUTTON_CAPTION, ThemeIcon.DELETE,
				LanguageCodes.DELETE_BUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -6302237054661116415L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireClearActionListeners();
					}
				});
	}

	@Override
	protected void generateLayout(Component content) {
		super.generateLayout(content);
		buttonLayout.addComponent(clearButton);
	}

	public void addClearActionListener(ClearElementsActionListener listener) {
		clearListeners.add(listener);
	}

	public void removeClearActionListener(ClearElementsActionListener listener) {
		clearListeners.remove(listener);
	}

	protected void fireClearActionListeners() {
		for (ClearElementsActionListener listener : clearListeners) {
			listener.clearAction(this);
		}
	}

}
