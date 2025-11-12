package com.biit.abcd.webpages;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectExpressionTableEditable;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.SecuredSimpleExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.WindowNewExpression;
import com.biit.abcd.webpages.elements.formula.editor.ExpressionEditorUpperMenu;
import com.biit.form.exceptions.DependencyExistException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ExpressionEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
			Arrays.asList(AbcdActivity.READ));
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectExpressionTableEditable tableSelectExpression;

	public ExpressionEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
		collapsibleLayout.setSizeFull();

		// Create menu
		tableSelectExpression = new SelectExpressionTableEditable();
		tableSelectExpression.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				UserSessionHandler.getFormController().setLastAccessExpression(getSelectedExpression());
				refreshExpressionEditor();
			}

		});
		collapsibleLayout.createMenu(tableSelectExpression);

		// Create content
		expressionEditorComponent = new SecuredSimpleExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();
		collapsibleLayout.setContent(expressionEditorComponent);

		getWorkingAreaLayout().addComponent(collapsibleLayout);

		initUpperMenu();

		if (UserSessionHandler.getFormController().getForm() != null) {
			// Add tables
			tableSelectExpression.update(UserSessionHandler.getFormController().getForm());

			sortTableMenu();

			if (UserSessionHandler.getFormController().getLastAccessExpression() != null) {
				tableSelectExpression.setSelectedExpression(UserSessionHandler.getFormController()
						.getLastAccessExpression());
			} else {
				// Select the first one if available.
				if (UserSessionHandler.getFormController().getForm().getExpressionChains().size() > 0) {

					Iterator<ExpressionChain> iterator = (UserSessionHandler.getFormController().getForm()
							.getExpressionChains().iterator());
					tableSelectExpression.setSelectedExpression(iterator.next());
				}
			}
			refreshExpressionEditor();
		} else {
			AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
		}
	}
	
	private void refreshLeftTable(){
		ExpressionChain selectedChain = getSelectedExpression();
		tableSelectExpression.setSelectedExpression(null);
		tableSelectExpression.update(UserSessionHandler.getFormController().getForm());
		//We need to get the new instance that has been merged to the current jpa context.
		for(ExpressionChain chain: UserSessionHandler.getFormController().getForm().getExpressionChains()){
			if(selectedChain!=null && chain.getComparationId().equals(selectedChain.getComparationId())){
				tableSelectExpression.setSelectedExpression(chain);
			}
		}
	}

	private void initUpperMenu() {
		final ExpressionEditor thisPage = this;

		decisionTableEditorUpperMenu = new ExpressionEditorUpperMenu();

		decisionTableEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6036676119057486519L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		decisionTableEditorUpperMenu.addNewExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 377976184801401863L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new WindowNewExpression(thisPage, LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
								LanguageCodes.WINDOW_NEW_EXPRESSION_TEXTFIELD));
			}

		});

		decisionTableEditorUpperMenu.addRemoveExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3561685413299735048L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (getSelectedExpression() != null) {
					final AlertMessageWindow windowAccept = new AlertMessageWindow(
							LanguageCodes.WARNING_EXPRESSION_DELETION);
					windowAccept.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							ExpressionChain expChain = getSelectedExpression();
							try {
								CheckDependencies.checkExpressionDependencies(UserSessionHandler.getFormController()
										.getForm(), expChain);
								removeSelectedExpression();
								AbcdLogger.info(this.getClass().getName(), "User '"
										+ UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
										+ expChain.getClass() + " with 'Name: " + expChain.getName() + "'.");
								windowAccept.close();
							} catch (DependencyExistException e) {
								// Forbid the remove action if exist dependency.
								MessageManager.showError(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
										LanguageCodes.EXPRESSION_DESIGNER_WARNING_CANNOT_REMOVE_EXPRESSION);
								windowAccept.close();
							}
						}
					});
					windowAccept.showCentered();
				}
			}
		});

		decisionTableEditorUpperMenu.addCopyExpressionButtonClickListener((ClickListener) event -> {
            ExpressionChain expression = tableSelectExpression.getSelectedExpression();
            if (expression != null) {
                expression = (ExpressionChain) expression.generateCopy();
                expression.setName(expression.getName() + " - Copy");
                expression.resetIds();
                expression.setCreatedBy(UserSessionHandler.getUser().getUniqueId());
                UserSessionHandler.getFormController().getForm().getExpressionChains().add(expression);
                addExpressionToMenu(expression);
                sortTableMenu();
            }
        });

		setUpperMenu(decisionTableEditorUpperMenu);
	}

	private ExpressionChain getSelectedExpression() {
		return tableSelectExpression.getSelectedExpression();
	}

	private void refreshExpressionEditor() {
		((SimpleExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(getSelectedExpression());
		expressionEditorComponent.updateSelectionStyles();
	}

	private void save() {
		try {
			UserSessionHandler.getFormController().save();
			refreshLeftTable();
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(TableRuleEditor.class.getName(), e);
		}
	}

	@Override
	public List<AbcdActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private void removeSelectedExpression() {
		UserSessionHandler.getFormController().getForm().getExpressionChains()
				.remove(tableSelectExpression.getSelectedExpression());
		tableSelectExpression.removeSelectedRow();
		refreshExpressionEditor();
	}

	public void addExpressionToMenu(ExpressionChain expression) {
		tableSelectExpression.addRow(expression);
		tableSelectExpression.setSelectedExpression(expression);
	}

	public void sortTableMenu() {
		tableSelectExpression.sort();
	}

	public void selectComponent(ExpressionChain element) {
		tableSelectExpression.setSelectedExpression(element);
	}

	
}
