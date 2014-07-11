package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.ElementUpdatedListener;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for editing an expression. Is composed by a viewer and a properties menu in tabs.
 */
public abstract class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;

	private TabSheet tabMenu;

	public abstract VerticalLayout createViewersLayout();

	/**
	 * Visual CSS for enabled/disabled viewer.
	 */
	public abstract void updateSelectionStyles();

	public ExpressionEditorComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		tabMenu = createTabMenu();

		VerticalLayout viewLayout = createViewersLayout();

		rootLayout.addComponent(viewLayout);
		// rootLayout.addComponent(expressionEditorProperties);
		rootLayout.addComponent(tabMenu);
		rootLayout.setExpandRatio(viewLayout, 0.80f);
		rootLayout.setExpandRatio(tabMenu, 0.20f);

		setCompositionRoot(rootLayout);
		addKeyController();
	}

	private TabSheet createTabMenu() {
		TabSheet tabMenu = new TabSheet();
		tabMenu.setHeight("100%");

		// First tab.
		TabOperatorLayout operatorLayout = new TabOperatorLayout();
		operatorLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				if (getSelectedViewer() != null) {
					getSelectedViewer().addElementToSelected((Expression) newElement);
				}
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
				if (getSelectedViewer() != null) {
					getSelectedViewer().addElementToSelected((Expression) newElement);
				}
			}

		});
		//Adding units to dates need to refresh the GUI.
		formVariablesLayout.addUpdateElementListener(new ElementUpdatedListener() {
			@Override
			public void elementUpdated(Object newElement) {
				if (getSelectedViewer() != null) {
					// Update all gui to reflech changes.
					getSelectedViewer().updateExpression();
				}
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
				if (getSelectedViewer() != null) {
					getSelectedViewer().addElementToSelected((Expression) newElement);
				}
			}

		});
		Tab tab3 = tabMenu.addTab(globalConstantLayout);
		tab3.setDescription("");
		tab3.setIcon(ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS.getThemeResource());

		return tabMenu;
	}

	/**
	 * A Expression editor can have more than one viewer. When user click into a viewer, this one gains the focus and is
	 * selected.
	 */
	public abstract ExpressionViewer getSelectedViewer();

	/**
	 * Add all keyboard defined actions.
	 */
	private void addKeyController() {
		this.addShortcutListener(new ShortcutListener("DELETE_SHORTCUT", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -71562151456777493L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().removeSelectedExpression();
			}
		});

		this.addShortcutListener(new ShortcutListener("SELECT_NEXT", KeyCode.ARROW_RIGHT, null) {
			private static final long serialVersionUID = 7663105045629599269L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().selectNextExpression();
			}
		});

		this.addShortcutListener(new ShortcutListener("SELECT_PREVIOUS", KeyCode.ARROW_LEFT, null) {
			private static final long serialVersionUID = 8453120978479798559L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().selectPreviousExpression();
			}
		});
	}
}
