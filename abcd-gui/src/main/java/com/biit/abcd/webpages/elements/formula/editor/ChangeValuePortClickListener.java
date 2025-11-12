package com.biit.abcd.webpages.elements.formula.editor;

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

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.UI;

public abstract class ChangeValuePortClickListener implements FormulaPortClickListener{

	private boolean fireOnlyAtDoubleClick;
	private PortContentEditor windowEditor;
	
	public ChangeValuePortClickListener(PortContentEditor windowEditor, boolean fireOnlyAtDoubleClick) {
		this.fireOnlyAtDoubleClick = fireOnlyAtDoubleClick;
		this.windowEditor = windowEditor;
	}
	
	@Override
	public void formulaPortClicked(final FormulaPortComponent formulaPort, LayoutClickEvent clickEvent) {
		if(fireOnlyAtDoubleClick && !clickEvent.isDoubleClick()){
			return;
		}
		windowEditor.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				formulaPort.setValue(windowEditor.getValue());
				window.close();
			}
		});
		UI.getCurrent().addWindow(windowEditor);		
	}

}
