package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

public class FormulaPortComponent extends CustomComponent {
	private static final long serialVersionUID = -7211660100593264284L;
	private static final String CLASSNAME_FORMULA_PORT = "v-formula-port";
	private static final String CLASSNAME_FORMULA_PORT_LAYOUT = "v-formula-port-layout";
	private static final String CLASSNAME_FORMULA_PORT_SELECTED = "v-selected";
	private static final String emptyWidth = "60px";
	private static final String emptyHeight = "20px";

	private FormulaExpressionComponent value;

	private CssLayout rootLayout;
	private List<FormulaElementType> acceptedTypes;

	private List<FormulaPortClickListener> listeners;

	public FormulaPortComponent(FormulaElementType acceptedType) {
		List<FormulaElementType> acceptedTypes = new ArrayList<FormulaElementType>();
		acceptedTypes.add(acceptedType);
		initializate(acceptedTypes);
	}

	public FormulaPortComponent(List<FormulaElementType> acceptedTypes) {
		initializate(acceptedTypes);
	}

	private void initializate(List<FormulaElementType> acceptedTypes) {
		listeners = new ArrayList<FormulaPortClickListener>();
		this.acceptedTypes = new ArrayList<FormulaElementType>();
		this.acceptedTypes.addAll(acceptedTypes);

		rootLayout = new CssLayout();
		rootLayout.setWidth(emptyWidth);
		rootLayout.setHeight(emptyHeight);
		rootLayout.setStyleName(CLASSNAME_FORMULA_PORT_LAYOUT);
		rootLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 5673848977203821194L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.getClickedComponent() != null) {
					if(event.getClickedComponent().getParent().getParent() instanceof FormulaExpressionComponent){
						firePortClickListeners();
					}
				} else {
					firePortClickListeners();
				}
			}
		});

		setCompositionRoot(rootLayout);
		setSizeUndefined();
		setStyleName(CLASSNAME_FORMULA_PORT);
	}

	public void addFormulaPortClickListener(FormulaPortClickListener listener) {
		listeners.add(listener);
	}

	public void removeFormulaPortClickListener(FormulaPortClickListener listener) {
		listeners.remove(listener);
	}

	private void firePortClickListeners() {
		for (FormulaPortClickListener listener : listeners) {
			listener.formulaPortClicked(this);
		}
	}

	public void setSelected(boolean value) {
		if (value) {
			setStyleName(CLASSNAME_FORMULA_PORT + " " + CLASSNAME_FORMULA_PORT_SELECTED);
		} else {
			setStyleName(CLASSNAME_FORMULA_PORT);
		}
	}

	public List<FormulaElementType> getAcceptedTypes() {
		return acceptedTypes;
	}

	public void setValue(FormulaExpressionComponent value) {
		this.value = value;
		if (value != null) {
			rootLayout.removeAllComponents();
			rootLayout.addComponent(value);
			rootLayout.setWidth(null);
			rootLayout.setHeight(null);
		} else {
			rootLayout.removeAllComponents();
			rootLayout.setWidth(emptyWidth);
			rootLayout.setHeight(emptyHeight);
		}
	}

	public FormulaElementType getType() {
		return getValue().getType();
	}

	public FormulaExpressionComponent getValue() {
		return value;
	}
}
