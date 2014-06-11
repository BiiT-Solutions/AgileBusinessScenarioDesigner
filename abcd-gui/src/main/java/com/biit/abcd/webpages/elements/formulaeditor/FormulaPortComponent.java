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
	private List<Type> acceptedTypes;

	private List<FormulaPortClickListener> listeners;

	public FormulaPortComponent(Type acceptedType) {
		List<Type> acceptedTypes = new ArrayList<Type>();
		acceptedTypes.add(acceptedType);
		initializate(acceptedTypes);
	}

	public FormulaPortComponent(List<Type> acceptedTypes) {
		initializate(acceptedTypes);
	}

	private void initializate(List<Type> acceptedTypes) {
		listeners = new ArrayList<FormulaPortClickListener>();
		this.acceptedTypes = new ArrayList<Type>();
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
					if (event.getClickedComponent().getParent().getParent() instanceof FormulaExpressionComponent) {
						if (getValue().isChildComponent(event.getClickedComponent())) {
							//Only fires if the element is a child of the Formula Expression.
							firePortClickListeners(event);
						}
					}
				} else {
					firePortClickListeners(event);
				}
			}
		});

		setImmediate(true);
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

	private void firePortClickListeners(LayoutClickEvent event) {
		for (FormulaPortClickListener listener : listeners) {
			listener.formulaPortClicked(this, event);
		}
	}

	public void setSelected(boolean value) {
		if (value) {
			setStyleName(CLASSNAME_FORMULA_PORT + " " + CLASSNAME_FORMULA_PORT_SELECTED);
		} else {
			setStyleName(CLASSNAME_FORMULA_PORT);
		}
	}

	public List<Type> getAcceptedTypes() {
		return acceptedTypes;
	}

	public void setValue(FormulaExpressionComponent value) {
		this.value = value;
		if (value != null) {
			rootLayout.removeAllComponents();
			rootLayout.addComponent(value);
			rootLayout.setWidth(null);
			rootLayout.setHeight(null);
			// Add click listeners to descendants.
			for (FormulaPortClickListener listener : listeners) {
				if (listener instanceof ChangeValuePortClickListener) {
					continue;
				}
				value.addFormulaPortClickListener(listener);
			}
		} else {
			rootLayout.removeAllComponents();
			rootLayout.setWidth(emptyWidth);
			rootLayout.setHeight(emptyHeight);
		}
	}

	public Type getType() {
		return getValue().getType();
	}

	public FormulaExpressionComponent getValue() {
		return value;
	}
}
