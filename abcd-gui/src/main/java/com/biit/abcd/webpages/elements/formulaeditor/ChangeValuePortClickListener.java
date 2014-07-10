package com.biit.abcd.webpages.elements.formulaeditor;

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
