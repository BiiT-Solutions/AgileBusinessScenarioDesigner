package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.PluginController;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.ElementUpdatedListener;
import com.biit.abcd.webpages.components.ExpressionEditorTabComponent;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for editing an expression. Is composed by a viewer and a properties
 * menu in tabs.
 */
public abstract class ExpressionEditorComponent extends ExpressionEditorTabComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private TabOperatorLayout operatorLayout;

	public abstract VerticalLayout createViewersLayout();

	/**
	 * Visual CSS for enabled/disabled viewer.
	 */
	public abstract void updateSelectionStyles();

	public ExpressionEditorComponent() {
		super();
		initTabs();

		VerticalLayout viewLayout = createViewersLayout();

		getRootLayout().addComponent(viewLayout);
		getRootLayout().addComponent(getTabSheet());
		getRootLayout().setExpandRatio(viewLayout, 0.80f);
		getRootLayout().setExpandRatio(getTabSheet(), 0.20f);

		setCompositionRoot(getRootLayout());
		addKeyController();
	}

	public void initTabs() {
		// First Tab
		TabFormVariablesLayout formVariablesLayout = new ExpressionTabFormVariablesLayout();
		formVariablesLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				addElementToView(newElement);
			}
		});
		// Adding units to dates need to refresh the GUI.
		formVariablesLayout.addUpdateElementListener(new ElementUpdatedListener() {
			@Override
			public void elementUpdated(Object newElement) {
				if (getSelectedViewer() != null) {
					// Update all GUI to reflect changes.
					getSelectedViewer().updateExpression();
				}
			}
		});
		setTab(formVariablesLayout, "", ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_VARIABLES.getThemeResource());

		// Second Tab
		operatorLayout = new TabOperatorLayout();
		operatorLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				addElementToView(newElement);
			}
		});
		setTab(operatorLayout, "", ThemeIcon.EXPRESSION_EDITOR_TAB_MATHS.getThemeResource());

		// Third Tab
		TabFormGenericTreeObjectLayout formVariablesScopeLayout = new TabFormGenericTreeObjectLayout();
		formVariablesScopeLayout.addNewElementListener(new ElementAddedListener() {
			@Override
			public void elementAdded(Object newElement) {
				addElementToView(newElement);
			}
		});
		setTab(formVariablesScopeLayout, "", ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_GENERIC_VARIABLES.getThemeResource());

		// Fourth tab
		TabGlobalConstantsLayout globalConstantLayout = new TabGlobalConstantsLayout();
		globalConstantLayout.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				addElementToView(newElement);
			}

		});
		setTab(globalConstantLayout, "", ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS.getThemeResource());

		// Fifth tab (Plugins tab)
		if (PluginController.getInstance().existsPlugins()) {
			TabPluginsLayout pluginsLayout = new TabPluginsLayout();
			pluginsLayout.addNewElementListener(new ElementAddedListener() {
				@Override
				public void elementAdded(Object newElement) {
					addElementToView(newElement);
				}
			});
			setTab(pluginsLayout, "", ThemeIcon.EXPRESSION_EDITOR_TAB_PLUGIN.getThemeResource());
		}
	}

	protected void enableAssignOperator(boolean enabled) {
		operatorLayout.enableAssignOperator(enabled);
	}

	protected void addElementToView(Object newElement) {
		if (getSelectedViewer() != null) {
			getSelectedViewer().addElementToSelected((Expression) newElement);

			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has added a " + newElement.getClass() + " with 'Value: " + newElement + "'.");
		}
	}

	/**
	 * A Expression editor can have more than one viewer. When user click into a
	 * viewer, this one gains the focus and is selected.
	 */
	public abstract ExpressionViewer getSelectedViewer();

	/**
	 * Add all keyboard defined actions.
	 */
	protected void addKeyController() {
		addShortcutListener(new ShortcutListener("DELETE_SHORTCUT", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -71562151456777493L;

			@Override
			public void handleAction(Object sender, Object target) {
				Expression expression = getSelectedViewer().getSelectedExpression();
				if (getSelectedViewer().removeSelectedExpression()) {
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has deleted the Expression "
							+ expression.getRepresentation() + "'.");
				}
			}
		});

		addShortcutListener(new ShortcutListener("SELECT_NEXT", KeyCode.ARROW_RIGHT, null) {
			private static final long serialVersionUID = 7663105045629599269L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().selectNextExpression();
			}
		});

		addShortcutListener(new ShortcutListener("SELECT_PREVIOUS", KeyCode.ARROW_LEFT, null) {
			private static final long serialVersionUID = 8453120978479798559L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().selectPreviousExpression();
			}
		});

		addShortcutListener(new ShortcutListener("NEW_LINE", KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1836548908896874267L;

			@Override
			public void handleAction(Object sender, Object target) {
				getSelectedViewer().addElementToSelected(new ExpressionSymbol(AvailableSymbol.PILCROW));
			}
		});
	}
}
