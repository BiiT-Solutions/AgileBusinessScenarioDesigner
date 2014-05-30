package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.List;

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class PortContentEditor extends AcceptCancelWindow {
	private static final long serialVersionUID = 4460942893259168920L;
	private ComboBox comboBox;

	public PortContentEditor(List<FormulaElementType> contentTypesToGenerate) {
		super();
		setModal(true);
		setResizable(false);
		setWidth("50%");
		setHeight("50%");
		center();

		Component content = generateContent(contentTypesToGenerate);
		setContent(content);
	}

	private Component generateContent(List<FormulaElementType> contentTypesToGenerate) {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeUndefined();
		for (FormulaElementType formulaElementType : FormulaElementType.getAnyType()) {
			if (contentTypesToGenerate.contains(formulaElementType)) {
				switch (formulaElementType) {
				case EXPRESION:
					rootLayout.addComponent(generateExpressionContent());
					break;

				default:
					break;
				}
			}
		}
		return rootLayout;
	}

	private Component generateExpressionContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		comboBox = new ComboBox();
		for (FormulaExpresion formulaExpresion : FormulaExpresion.values()) {
			comboBox.addItem(formulaExpresion);
			comboBox.setItemCaption(formulaExpresion, formulaExpresion.getExpresionClass().getName());
		}
		rootLayout.addComponent(comboBox);
		return rootLayout;
	}

	public FormulaExpressionComponent getValue() {
		return ((FormulaExpresion) comboBox.getValue()).getNewFormulaExpressionComponent();
	}
}
