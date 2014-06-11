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
	private static final String CLASSNAME_ROOT_LAYOUT = "v-formulaExpressionComponentRoot-layout";
	private static final String CLASSNAME_LAYOUT = "v-formulaExpressionComponent-layout";
	private static final String CLASSNAME_SECOND_LINE="v-second-line";

	private CssLayout rootLayout;
	private CssLayout fileLayout;

	public FormulaExpressionComponent() {
		super();

		rootLayout = new CssLayout();
		rootLayout.setStyleName(CLASSNAME_ROOT_LAYOUT);
		rootLayout.setSizeUndefined();

		addLine();

		setImmediate(true);
		setCompositionRoot(rootLayout);
		setStyleName(CLASSNAME);
	}

	public void addLine() {
		fileLayout = new CssLayout();
		fileLayout.addStyleName(CLASSNAME_LAYOUT);
		if(rootLayout.getComponentCount()!=0){
			fileLayout.addStyleName(CLASSNAME_LAYOUT+" "+CLASSNAME_SECOND_LINE);
		}		
		fileLayout.setWidth(null);
		fileLayout.setHeight(null);

		rootLayout.addComponent(fileLayout);
	}

	public void setValueToPort(FormulaPortComponent port, FormulaExpressionComponent expresion) {
		port.setValue(expresion);
	}

	protected Label addText(String text) {
		Label textLabel = new Label(text);
		textLabel.setImmediate(true);
		textLabel.setWidth(null);
		fileLayout.addComponent(textLabel);
		return textLabel;
	}

	protected void addPort(FormulaPortComponent port) {
		fileLayout.addComponent(port);
	}
	
	protected List<CssLayout> getLines(){
		List<CssLayout> lines = new ArrayList<CssLayout>();
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			lines.add((CssLayout) itr.next());
		}
		return lines;
	}

	protected List<FormulaPortComponent> getPorts() {
		List<FormulaPortComponent> ports = new ArrayList<FormulaPortComponent>();
		List<CssLayout> lines = getLines();
		for(CssLayout line: lines){
			Iterator<Component> itr = line.iterator();
			while (itr.hasNext()) {
				Component component = itr.next();
				if (component instanceof FormulaPortComponent) {
					ports.add((FormulaPortComponent) component);
				}
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

	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isChildComponent(Component clickedComponent) {
		System.out.println("kiwi?");
		Iterator<Component> layoutItr = rootLayout.iterator();
		while(layoutItr.hasNext()){
			CssLayout layout = (CssLayout)layoutItr.next();
			if(layout.getComponentIndex(clickedComponent)!=-1){
				return true;
			}
		}
		return false;
	}

}
