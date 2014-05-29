package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class FormulaExpressionComponent extends CustomComponent {
	private static final long serialVersionUID = 1350097467868000964L;
	private static final String CLASSNAME = "v-formulaExpressionComponent";
	private static final String CLASSNAME_LAYOUT = "v-formulaExpressionComponent-layout";

	private CssLayout rootLayout;

	public FormulaExpressionComponent() {
		super();
		rootLayout = new CssLayout();
		rootLayout.setStyleName(CLASSNAME_LAYOUT);
		rootLayout.setWidth(null);
		rootLayout.setHeight(null);
		
		setCompositionRoot(rootLayout);
		setStyleName(CLASSNAME);
	}

	protected void addText(String text) {
		Label textLabel = new Label(text);
		textLabel.setWidth(null);
		rootLayout.addComponent(textLabel);
	}

	protected void addPort(FormulaPortComponent port) {
		rootLayout.addComponent(port);
	}

	protected List<FormulaPortComponent> getPorts() {
		List<FormulaPortComponent> ports = new ArrayList<FormulaPortComponent>();
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			Component component = itr.next();
			if (component instanceof FormulaPortComponent) {
				ports.add((FormulaPortComponent) component);
			}
		}
		return ports;
	}

	public void addFormulaPortClickListener(FormulaPortClickListener listener) {
		List<FormulaPortComponent> ports = getPorts();
		for (FormulaPortComponent formulaPortComponent : ports) {
			formulaPortComponent.addFormulaPortClickListener(listener);
		}
	}

	public FormulaElementType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
