package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.Expressions;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for editing an expression. Is composed by a viewer and a properties menu in tabs.
 */
public class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private ExpressionViewer expressionViewer;
	private TabSheet tabMenu;

	public ExpressionEditorComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setMargin(false);
		viewLayout.setSpacing(false);

		tabMenu = createTabMenu();

		expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();

		viewLayout.addComponent(expressionViewer);

		rootLayout.addComponent(viewLayout);
		// rootLayout.addComponent(expressionEditorProperties);
		rootLayout.addComponent(tabMenu);
		rootLayout.setExpandRatio(viewLayout, 0.80f);
		rootLayout.setExpandRatio(tabMenu, 0.20f);

		setCompositionRoot(rootLayout);
	}

	private TabSheet createTabMenu() {
		TabSheet tabMenu = new TabSheet();
		tabMenu.setHeight("100%");

		// First tab.
		TabOperatorLayout operatorLayout = new TabOperatorLayout();
		operatorLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		Tab tab1 = tabMenu.addTab(operatorLayout);
		tab1.setDescription("");
		tab1.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_MATHS.getThemeResource());

		// Second Tab
		TabFormVariablesLayout formVariablesLayout = new TabFormVariablesLayout();
		formVariablesLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		Tab tab2 = tabMenu.addTab(formVariablesLayout);
		tab2.setDescription("");
		tab2.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_VARIABLES.getThemeResource());

		// Third tab
		TabGlobalConstantsLayout globalConstantLayout = new TabGlobalConstantsLayout();
		globalConstantLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		Tab tab3 = tabMenu.addTab(globalConstantLayout);
		tab3.setDescription("");
		tab3.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS.getThemeResource());

		return tabMenu;
	}

	public void refreshExpressionEditor(Expressions selectedExpression) {
		expressionViewer.removeAllComponents();
		if (selectedExpression != null) {
			// Add table rows.
			expressionViewer.updateExpression(selectedExpression);
		}
	}

}
