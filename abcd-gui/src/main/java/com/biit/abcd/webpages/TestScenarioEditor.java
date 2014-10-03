package com.biit.abcd.webpages;

import java.util.Iterator;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.testscenario.SelectTestScenarioTableEditable;
import com.biit.abcd.webpages.components.testscenario.TestScenarioEditorUpperMenu;
import com.biit.abcd.webpages.components.testscenario.WindowNewTestScenario;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class TestScenarioEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -6743796589244668454L;

	private Panel dummyPanel;
	private SelectTestScenarioTableEditable tableSelectExpression;
	private TestScenarioEditorUpperMenu testScenarioUpperMenu;

	public TestScenarioEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
		collapsibleLayout.setSizeFull();

		// Create menu
		tableSelectExpression = new SelectTestScenarioTableEditable();
		tableSelectExpression.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4251583661250518900L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				UserSessionHandler.getTestScenariosController().setLastAccessTestScenario(getSelectedTestScenario());
				refreshTestScenario();
			}

		});
		collapsibleLayout.createMenu(tableSelectExpression);

		// Create content
		dummyPanel = new Panel();
		dummyPanel.setSizeFull();
		collapsibleLayout.setContent(dummyPanel);

		getWorkingAreaLayout().addComponent(collapsibleLayout);

		initUpperMenu();

		if (UserSessionHandler.getTestScenariosController().getTestScenarios() != null) {
			// Add tables
			tableSelectExpression.updateTable(UserSessionHandler.getTestScenariosController().getTestScenarios());

			sortTableMenu();

			if (UserSessionHandler.getFormController().getLastAccessExpression() != null) {
				tableSelectExpression.setSelectedTestScenario(UserSessionHandler.getTestScenariosController()
						.getLastAccessTestScenario());
			} else {
				// Select the first one if available.
				if (UserSessionHandler.getTestScenariosController().getTestScenarios().size() > 0) {

					Iterator<TestScenario> iterator = (UserSessionHandler.getTestScenariosController()
							.getTestScenarios().iterator());
					tableSelectExpression.setSelectedTestScenario(iterator.next());
				}
			}
			refreshTestScenario();
		} else {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
		}
	}

	private void initUpperMenu() {
		final TestScenarioEditor thisPage = this;

		testScenarioUpperMenu = new TestScenarioEditorUpperMenu();

		testScenarioUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6169497561023032003L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		testScenarioUpperMenu.addNewTestScenarioButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2168564207293259993L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new WindowNewTestScenario(thisPage, LanguageCodes.TEST_SCENARIOS_EDITOR_NEW_WINDOW_CAPTION,
								LanguageCodes.TEST_SCENARIOS_EDITOR_NEW_WINDOW_FIELD_CAPTION));
			}

		});

		testScenarioUpperMenu.addRemoveTestScenarioButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 4381294084873849759L;

			@Override
			public void buttonClick(ClickEvent event) {
				final AlertMessageWindow windowAccept = new AlertMessageWindow(
						LanguageCodes.WARNING_TEST_SCENARIO_DELETION);
				windowAccept.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						TestScenario testScenario = getSelectedTestScenario();
						removeSelectedTestScenario();
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
										+ testScenario.getClass() + " with 'Name: " + testScenario.getName() + "'.");
						windowAccept.close();
					}
				});
				windowAccept.showCentered();
			}
		});

		setUpperMenu(testScenarioUpperMenu);
	}

	private TestScenario getSelectedTestScenario() {
		return tableSelectExpression.getSelectedTestScenario();
	}

	private void save() {
		try {
			UserSessionHandler.getTestScenariosController().update(tableSelectExpression.getTestScenarios());
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			System.out.println(e);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(TestScenarioEditor.class.getName(), e);
		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void removeSelectedTestScenario() {
		tableSelectExpression.removeSelectedRow();
		refreshTestScenario();
	}

	public void addTestScenarioToMenu(TestScenario testScenario) {
		tableSelectExpression.addRow(testScenario);
		tableSelectExpression.setSelectedTestScenario(testScenario);
	}

	public void sortTableMenu() {
		tableSelectExpression.sort();
	}

	public void selectComponent(TestScenario testScenario) {
		tableSelectExpression.setSelectedTestScenario(testScenario);
	}

	private void refreshTestScenario() {
	}

}
