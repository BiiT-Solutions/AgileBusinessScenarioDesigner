package com.biit.abcd.webpages.elements.formmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.importer.TestScenarioAnswerImporter;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleExecutionException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.FormManager;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.SaveActionListener;
import com.biit.abcd.webpages.components.SaveAsButton;
import com.biit.abcd.webpages.components.SettingsWindow;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.abcd.webpages.elements.droolsresults.DroolsSubmittedFormResultWindow;
import com.biit.abcd.webpages.elements.formdesigner.RootForm;
import com.biit.abcd.webpages.elements.testscenario.ValidationReportWindow;
import com.biit.abcd.webpages.elements.testscenario.WindowLaunchTestScenario;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManagerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 504419812975550794L;
	private IconButton newFormButton, newVersion, exportToDrools, createTestScenario, launchTestScenario;
	private FormManager parent;
	private List<IFormSelectedListener> formSelectedListeners;
	private Form form;
	private IFormDao formDao;
	private ISubmittedForm submittedForm;

	public FormManagerUpperMenu(FormManager parent) {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		this.parent = parent;
		formSelectedListeners = new ArrayList<>();
		defineMenu();
		setEnabledButtons();
	}

	private void defineMenu() {
		// Add new Form
		newFormButton = new IconButton(LanguageCodes.FORM_MANAGER_NEW_FORM, ThemeIcon.FORM_MANAGER_ADD_FORM,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 6053447189295644721L;

					@Override
					public void buttonClick(ClickEvent event) {
						final WindowNewForm newFormWindow = new WindowNewForm(
								LanguageCodes.WINDOW_NEWFORM_WINDOW_TITLE, LanguageCodes.WINDOW_NEWFORM_NAME_TEXTFIELD,
								LanguageCodes.WINDOW_NEWFORM_NAME_COMBOBOX,
								new AbcdActivity[] { AbcdActivity.FORM_EDITING });
						newFormWindow.showCentered();
						newFormWindow.addAcceptActionListener(new AcceptActionListener() {

							@Override
							public void acceptAction(AcceptCancelWindow window) {
								if (newFormWindow.getValue() == null || newFormWindow.getValue().isEmpty()) {
									return;
								}
								try {
									if (!formDao.exists(newFormWindow.getValue(), newFormWindow.getOrganization()
											.getOrganizationId())) {
										form = new Form();
										try {
											form.setLabel(newFormWindow.getValue());
										} catch (FieldTooLongException e) {
											MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
													LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
											try {
												form.setLabel(newFormWindow.getValue().substring(0,
														StorableObject.MAX_UNIQUE_COLUMN_LENGTH));
											} catch (FieldTooLongException e1) {
												// Impossible.
											}
										}
										form.setLastVersion(true);
										form.setCreatedBy(UserSessionHandler.getUser());
										form.setUpdatedBy(UserSessionHandler.getUser());
										form.setOrganizationId(newFormWindow.getOrganization().getOrganizationId());
										((FormManager) parent).addNewForm(form);
										AbcdLogger.info(this.getClass().getName(), "User '"
												+ UserSessionHandler.getUser().getEmailAddress() + "' has created a "
												+ form.getClass() + " with 'Name: " + form.getName() + "'.");
										newFormWindow.close();
									} else {
										MessageManager.showError(LanguageCodes.ERROR_REPEATED_FORM_NAME);
									}
								} catch (UnexpectedDatabaseException e) {
									AbcdLogger.errorMessage(FormManager.class.getName(), e);
									MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
											LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
								}
							}
						});

					}
				});
		newVersion = new IconButton(LanguageCodes.FORM_MANAGER_NEW_FORM_VERSION,
				ThemeIcon.FORM_MANAGER_FORM_NEW_VERSION, LanguageCodes.FORM_MANAGER_NEW_FORM_VERSION, IconSize.MEDIUM,
				new ClickListener() {
					private static final long serialVersionUID = 8916936867106777144L;

					@Override
					public void buttonClick(ClickEvent event) {
						final AlertMessageWindow windowAccept = new AlertMessageWindow(
								LanguageCodes.WARNING_NEW_VERSION);
						windowAccept.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								((FormManager) parent).newFormVersion();
								windowAccept.close();
							}
						});
						windowAccept.showCentered();
					}

				});

		// Create rules and launch drools engine
		exportToDrools = new SaveAsButton(LanguageCodes.FORM_MANAGER_EXPORT_RULES, ThemeIcon.FORM_MANAGER_EXPORT_RULES,
				LanguageCodes.FORM_MANAGER_EXPORT_RULES, IconSize.MEDIUM, new SaveDroolsRulesAction());
		((SaveAsButton) exportToDrools).addSaveActionListener(new SaveActionListener() {
			@Override
			public void saveAction() {
				launchListeners();
			}
		});

		// Create new test scenario
		createTestScenario = new IconButton(LanguageCodes.FORM_MANAGER_CREATE_TEST_SCENARIOS, ThemeIcon.FORM_TEST_PAGE,
				LanguageCodes.FORM_MANAGER_CREATE_TEST_SCENARIOS, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = -1628560253598118060L;

					@Override
					public void buttonClick(ClickEvent event) {
						// Accept button update the form from the
						// simpleViewForm.
						launchListeners();
						ApplicationFrame.navigateTo(WebMap.TEST_SCENARIOS);
					}
				});

		// Launch test scenario
		launchTestScenario = new IconButton(LanguageCodes.FORM_MANAGER_LAUNCH_TEST_SCENARIOS,
				ThemeIcon.FORM_TEST_LAUNCH, LanguageCodes.FORM_MANAGER_LAUNCH_TEST_SCENARIOS, IconSize.MEDIUM,
				new ClickListener() {
					private static final long serialVersionUID = 2538065448920025133L;

					@Override
					public void buttonClick(ClickEvent event) {
						final WindowLaunchTestScenario launchTestScenarioWindow = new WindowLaunchTestScenario(
								parent.getForm());
						launchTestScenarioWindow.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								Long formId = launchTestScenarioWindow.getSelectedFormId();
								Long testScenarioId = launchTestScenarioWindow.getSelectedTestScenarioId();
								if ((formId != null) && (testScenarioId != null)) {
									parent.setFormById(formId);
									TestScenario testScenarioDB = UserSessionHandler.getTestScenariosController()
											.getTestScenarioById(testScenarioId);
									final FormToDroolsExporter droolsExporter = new FormToDroolsExporter();

									try {
										// Generate the submitted form based on
										// the test scenario
										TestScenarioAnswerImporter testAnswerImporter = new TestScenarioAnswerImporter();
										final ISubmittedForm generatedSumbittedForm = testAnswerImporter
												.createSubmittedForm(UserSessionHandler.getFormController().getForm(),
														testScenarioDB);

										if ((testAnswerImporter.getScenarioModifications() != null)
												&& !testAnswerImporter.getScenarioModifications().isEmpty()) {

											final ValidationReportWindow windowAccept = new ValidationReportWindow(
													LanguageCodes.WARNING_TEST_SCENARIOS_VALIDATOR_WINDOW_CAPTION,
													testAnswerImporter.getScenarioModifications());
											windowAccept.addAcceptActionListener(new AcceptActionListener() {
												@Override
												public void acceptAction(AcceptCancelWindow window) {
													try {
														submittedForm = droolsExporter.processForm(UserSessionHandler
																.getFormController().getForm(), UserSessionHandler
																.getGlobalVariablesController().getGlobalVariables(),
																generatedSumbittedForm);

														if (submittedForm instanceof DroolsForm) {
															final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
																	((DroolsForm) submittedForm).getSubmittedForm(),
																	UserSessionHandler.getFormController().getForm());
															droolsResultWindow
																	.addAcceptActionListener(new AcceptActionListener() {
																		@Override
																		public void acceptAction(
																				AcceptCancelWindow window) {
																			droolsResultWindow.close();
																		}
																	});
															droolsResultWindow.showCentered();
														}
													} catch (Exception e) {
													}
													windowAccept.close();
													launchTestScenarioWindow.close();
												}
											});
											windowAccept.showCentered();
										} else {
											submittedForm = droolsExporter.processForm(UserSessionHandler
													.getFormController().getForm(), UserSessionHandler
													.getGlobalVariablesController().getGlobalVariables(),
													generatedSumbittedForm);

											if (submittedForm instanceof DroolsForm) {
												final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
														((DroolsForm) submittedForm).getSubmittedForm(),
														UserSessionHandler.getFormController().getForm());
												droolsResultWindow.addAcceptActionListener(new AcceptActionListener() {
													@Override
													public void acceptAction(AcceptCancelWindow window) {
														droolsResultWindow.close();
													}
												});
												droolsResultWindow.showCentered();
											}
											launchTestScenarioWindow.close();
										}
									} catch (UnexpectedDatabaseException e) {
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.ERROR_DATABASE_UNEXPECTED_EXCEPTION);

									} catch (DroolsRuleGenerationException e) {
										// This is a generic exception for
										// everything related with the rules
										// generation
										// The exception that triggered the
										// launch of this exception is inside
										// the received exception
										AbcdLogger.errorMessage(SettingsWindow.class.getName(),
												e.getGeneratedException());
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION);

									} catch (DroolsRuleExecutionException e) {
										AbcdLogger.errorMessage(SettingsWindow.class.getName(),
												e.getGeneratedException());
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.DROOLS_RULES_EXECUTION_EXCEPTION);
									}
								}
							}
						});
						launchTestScenarioWindow.showCentered();
					}
				});

		addIconButton(newFormButton);
		addIconButton(newVersion);
		addIconButton(exportToDrools);
		addIconButton(createTestScenario);
		addIconButton(launchTestScenario);
	}

	public void setEnabledButtons() {
		try {
			newFormButton.setEnabled(AbcdFormAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
					UserSessionHandler.getUser(), AbcdActivity.FORM_CREATE));
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void launchListeners() {
		for (IFormSelectedListener listener : formSelectedListeners) {
			listener.formSelected();
		}
	}

	public void addFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.add(listener);
	}

	public void removeFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.remove(listener);
	}

	public void updateButtons(boolean enableFormButtons) {
		if (exportToDrools != null) {
			exportToDrools.setEnabled(enableFormButtons);
		}
		if (createTestScenario != null) {
			createTestScenario.setEnabled(enableFormButtons);
		}
	}

	public void updateNewVersionButton(SimpleFormView selected) {
		if (selected != null && !(selected instanceof RootForm)) {
			newVersion.setEnabled(selected.isLastVersion()
					&& !AbcdFormAuthorizationService.getInstance().isFormReadOnly(selected,
							UserSessionHandler.getUser()));
		} else {
			newVersion.setEnabled(false);
		}
	}
}
