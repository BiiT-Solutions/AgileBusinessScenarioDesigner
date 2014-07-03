package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
	private ExpressionEditorOperatorsPropertiesComponent expressionEditorOperatorProperties;
	private ExpressionEditorGlobalConstantsPropertiesComponent expressionEditorGlobalConstantsProperties;
	private ExpressionEditorFormVariablesPropertiesComponent expressionEditorFormVariablesProperties;

	private FormExpression formExpression;

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
		rootLayout.setExpandRatio(viewLayout, 0.75f);
		rootLayout.setExpandRatio(tabMenu, 0.25f);

		setCompositionRoot(rootLayout);
	}

	private TabSheet createTabMenu() {
		TabSheet tabMenu = new TabSheet();
		tabMenu.setHeight("100%");

		expressionEditorOperatorProperties = new ExpressionEditorOperatorsPropertiesComponent();
		expressionEditorOperatorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
			}
		});
		expressionEditorOperatorProperties.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		expressionEditorOperatorProperties.setSizeFull();
		Tab tab1 = tabMenu.addTab(expressionEditorOperatorProperties);
		// tab.setCaption(ServerTranslate.translate(LanguageCodes.ABOUT_US_BIIT));
		tab1.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_MATHS.getThemeResource());

		expressionEditorGlobalConstantsProperties = new ExpressionEditorGlobalConstantsPropertiesComponent();
		expressionEditorGlobalConstantsProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
			}
		});
		expressionEditorGlobalConstantsProperties.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		expressionEditorGlobalConstantsProperties.setSizeFull();
		Tab tab2 = tabMenu.addTab(expressionEditorGlobalConstantsProperties);
		// tab.setCaption(ServerTranslate.translate(LanguageCodes.ABOUT_US_BIIT));
		tab2.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS.getThemeResource());

		expressionEditorFormVariablesProperties = new ExpressionEditorFormVariablesPropertiesComponent();
		expressionEditorFormVariablesProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
			}
		});
		expressionEditorFormVariablesProperties.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		expressionEditorFormVariablesProperties.setSizeFull();
		Tab tab3 = tabMenu.addTab(expressionEditorFormVariablesProperties);
		// tab.setCaption(ServerTranslate.translate(LanguageCodes.ABOUT_US_BIIT));
		tab3.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_VARIABLES.getThemeResource());

		return tabMenu;
	}

	protected void updatePropertiesComponent(Expression value) {
		expressionEditorOperatorProperties.updatePropertiesComponent(value);
		addButtonListeners();
	}

	public void refreshExpressionEditor(FormExpression selectedExpression) {
		this.formExpression = selectedExpression;
		expressionViewer.removeAllComponents();
		if (selectedExpression != null) {
			// Add table rows.
			expressionViewer.updateExpression(selectedExpression);
			expressionEditorOperatorProperties.updatePropertiesComponent(selectedExpression);
			addButtonListeners();
		}
	}

	private void addButtonListeners() {
		
	}

}
