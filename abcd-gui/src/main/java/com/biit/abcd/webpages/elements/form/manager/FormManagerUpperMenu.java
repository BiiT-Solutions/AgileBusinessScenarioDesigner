package com.biit.abcd.webpages.elements.form.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.PluginInvocationException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.core.testscenarios.TestScenarioDroolsSubmittedForm;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
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
import com.biit.abcd.webpages.elements.drools.results.DroolsSubmittedFormResultWindow;
import com.biit.abcd.webpages.elements.form.designer.RootForm;
import com.biit.abcd.webpages.elements.testscenario.ValidationReportWindow;
import com.biit.abcd.webpages.elements.testscenario.WindowLaunchTestScenario;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManagerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 504419812975550794L;
	private IconButton newButton, newFormButton, newVersion, exportToDrools, createTestScenario, launchTestScenario,
			removeForm, createPdf, importJson;
	private FormManager parent;
	private List<IFormSelectedListener> formSelectedListeners;
	private Form form;
	private IFormDao formDao;
	private ISubmittedForm resultsFromDrools;
	private List<IFormRemove> removeFormListeners;

	public interface IFormRemove {
		void removeButtonPressed();
	}

	public FormManagerUpperMenu(FormManager parent) {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		this.parent = parent;
		formSelectedListeners = new ArrayList<>();
		removeFormListeners = new ArrayList<>();
		defineMenu();
		setEnabledButtons();
	}

	private void defineMenu() {
		List<IconButton> settingsButtonsList = createNewFormButtons();
		newButton = addSubMenu(ThemeIcon.FORM_MANAGER_NEW, LanguageCodes.FORM_MANAGER_FORM,
				LanguageCodes.FORM_MANAGER_FORM, settingsButtonsList);
		newButton.setHeight("100%");
		newButton.setWidth(BUTTON_WIDTH);
		addIconButton(newButton);

		removeForm = new IconButton(LanguageCodes.FORM_MANAGER_REMOVE_FORM, ThemeIcon.FORM_MANAGER_REMOVE_FORM,
				LanguageCodes.FORM_MANAGER_REMOVE_FORM, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = -3126160822538614928L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchFormRemoveListener();
					}
				});
		addIconButton(removeForm);

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
										TestScenarioDroolsSubmittedForm testAnswerImporter = new TestScenarioDroolsSubmittedForm();
										final ISubmittedForm generatedSumbittedForm = testAnswerImporter
												.createSubmittedForm(UserSessionHandler.getFormController().getForm(),
														testScenarioDB.getTestScenarioForm());
										try {
											AbcdLogger.debug(this.getClass().getName(),
													"Form Answers:\n" + generatedSumbittedForm.toJson());
										} catch (Exception e) {
											AbcdLogger.debug(this.getClass().getName(), "Form Answers: null");
										}

										if ((testAnswerImporter.getTestScenarioModifications() != null)
												&& !testAnswerImporter.getTestScenarioModifications().isEmpty()) {

											final ValidationReportWindow windowAccept = new ValidationReportWindow(
													LanguageCodes.WARNING_TEST_SCENARIOS_VALIDATOR_WINDOW_CAPTION,
													testAnswerImporter.getTestScenarioModifications());
											windowAccept.addAcceptActionListener(new AcceptActionListener() {
												@Override
												public void acceptAction(AcceptCancelWindow window) {
													acceptActionTestScenarioReportWindow(droolsExporter,
															generatedSumbittedForm);
													windowAccept.close();
													launchTestScenarioWindow.close();
												}
											});
											windowAccept.showCentered();
										} else {
											resultsFromDrools = droolsExporter.processForm(
													UserSessionHandler.getFormController().getForm(), UserSessionHandler
															.getGlobalVariablesController().getGlobalVariables(),
													generatedSumbittedForm);
											if (resultsFromDrools instanceof DroolsForm) {
												logTestForms(resultsFromDrools);
												final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
														((DroolsForm) resultsFromDrools).getDroolsSubmittedForm(),
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
									} catch (DroolsRuleGenerationException | RuleNotImplementedException
											| ExpressionInvalidException | NullTreeObjectException
											| TreeObjectInstanceNotRecognizedException
											| TreeObjectParentNotValidException | NullCustomVariableException
											| NullExpressionValueException | BetweenFunctionInvalidException
											| DateComparisonNotPossibleException | PluginInvocationException
											| DroolsRuleCreationException | PrattParserException
											| ActionNotImplementedException | InvalidExpressionException e) {
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION);
									} catch (InvalidRuleException e) {
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.DROOLS_RULE_INVALID,
												((InvalidRuleException) e).getRuleName());
									} catch (DroolsRuleExecutionException e) {
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
										MessageManager.showError(LanguageCodes.ERROR_TITLE,
												LanguageCodes.DROOLS_RULES_EXECUTION_EXCEPTION);
									} catch (NotCompatibleTypeException ncte) {
										MessageManager.showError(LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION,
												LanguageCodes.ERROR_DESCRIPTION, ncte.getDescription());
									}
								}
							}
						});
						launchTestScenarioWindow.showCentered();
					}
				});

		createPdf = new SaveAsButton(LanguageCodes.CAPTION_CREATE_PDF, ThemeIcon.CREATE_PDF,
				LanguageCodes.TOOLTIP_CREATE_PDF, IconSize.MEDIUM, new SaveAsPdfAction());
		((SaveAsButton) createPdf).addSaveActionListener(new SaveActionListener() {
			@Override
			public void saveAction() {
				launchListeners();
			}
		});
		if (AbcdConfigurationReader.getInstance().isPdfEnabled()) {
			addIconButton(createPdf);
		}

		addIconButton(exportToDrools);
		addIconButton(createTestScenario);
		addIconButton(launchTestScenario);
	}

	private void logTestForms(final ISubmittedForm generatedSumbittedForm) {
		try {
			AbcdLogger.debug(this.getClass().getName(),
					"Original Form:\n" + UserSessionHandler.getFormController().getForm().toJson());
		} catch (Exception e) {
			AbcdLogger.debug(this.getClass().getName(), "Original Form: null");
		}
		try {
			AbcdLogger.debug(this.getClass().getName(), "Testing Submitted Form:\n" + generatedSumbittedForm.toJson());
		} catch (Exception e) {
			AbcdLogger.debug(this.getClass().getName(), "Testing Submitted Form: null");
		}
		try {
			AbcdLogger.debug(this.getClass().getName(), "Submitted Form:\n" + generatedSumbittedForm.toJson());
		} catch (Exception e) {
			AbcdLogger.debug(this.getClass().getName(), "Submitted Form: null");
		}
		try {
			AbcdLogger.debug(this.getClass().getName(),
					"Drools Submitted Form:\n" + ((DroolsForm) resultsFromDrools).getDroolsSubmittedForm().toJson());
		} catch (Exception e) {
			AbcdLogger.debug(this.getClass().getName(), "Drools Submitted Form: null");
		}
	}

	private List<IconButton> createNewFormButtons() {
		List<IconButton> iconButtonList = new ArrayList<IconButton>();
		// Add new Form
		newFormButton = new IconButton(LanguageCodes.FORM_MANAGER_NEW_FORM, ThemeIcon.FORM_MANAGER_NEW_FORM,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 6053447189295644721L;

					@Override
					public void buttonClick(ClickEvent event) {
						final WindowNewForm newFormWindow = new WindowNewForm(LanguageCodes.WINDOW_NEWFORM_WINDOW_TITLE,
								LanguageCodes.WINDOW_NEWFORM_NAME_TEXTFIELD, LanguageCodes.WINDOW_NEWFORM_NAME_COMBOBOX,
								new AbcdActivity[] { AbcdActivity.FORM_EDITING });
						newFormWindow.showCentered();
						newFormWindow.addAcceptActionListener(new AcceptActionListener() {

							@Override
							public void acceptAction(AcceptCancelWindow window) {
								if (newFormWindow.getValue() == null || newFormWindow.getValue().isEmpty()
										|| !newFormWindow.isValid()) {
									return;
								}
								if (!formDao.exists(newFormWindow.getValue(),
										newFormWindow.getOrganization().getUniqueId())) {
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
									form.setOrganizationId(newFormWindow.getOrganization().getUniqueId());
									((FormManager) parent).addNewForm(form);
									AbcdLogger.info(this.getClass().getName(),
											"User '" + UserSessionHandler.getUser().getEmailAddress()
													+ "' has created a " + form.getClass() + " with 'Name: "
													+ form.getName() + "'.");
									newFormWindow.close();
								} else {
									MessageManager.showError(LanguageCodes.ERROR_REPEATED_FORM_NAME);
								}
							}
						});

					}
				});
		newFormButton.setHeight("100%");
		newFormButton.setWidth(BUTTON_WIDTH);
		iconButtonList.add(newFormButton);

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

		newVersion.setHeight("100%");
		newVersion.setWidth(BUTTON_WIDTH);
		iconButtonList.add(newVersion);

		importJson = new IconButton(LanguageCodes.CAPTION_IMPORT_JSON_FORM, ThemeIcon.FORM_MANAGER_IMPORT_JSON_FORM,
				LanguageCodes.TOOLTIP_IMPORT_JSON_FORM, IconSize.MEDIUM);
		importJson.setHeight("100%");
		importJson.setWidth(BUTTON_WIDTH);
		iconButtonList.add(importJson);

		return iconButtonList;
	}

	private void acceptActionTestScenarioReportWindow(FormToDroolsExporter droolsExporter,
			ISubmittedForm generatedSumbittedForm) {
		try {
			resultsFromDrools = droolsExporter.processForm(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getGlobalVariablesController().getGlobalVariables(), generatedSumbittedForm);

			if (resultsFromDrools instanceof DroolsForm) {
				logTestForms(resultsFromDrools);
				final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
						((DroolsForm) resultsFromDrools).getDroolsSubmittedForm(),
						UserSessionHandler.getFormController().getForm());
				droolsResultWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						droolsResultWindow.close();
					}
				});
				droolsResultWindow.showCentered();
			}
		} catch (DroolsRuleGenerationException | RuleNotImplementedException | ExpressionInvalidException
				| NullTreeObjectException | TreeObjectInstanceNotRecognizedException | TreeObjectParentNotValidException
				| NullCustomVariableException | NullExpressionValueException | BetweenFunctionInvalidException
				| DateComparisonNotPossibleException | PluginInvocationException | DroolsRuleCreationException
				| PrattParserException | ActionNotImplementedException | InvalidExpressionException e) {
			// This is a generic exception for everything related with the
			// rules generation
			// The exception that triggered the launch of this exception is
			// inside the received exception
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION);
		} catch (InvalidRuleException e) {
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULE_INVALID,
					((InvalidRuleException) e).getRuleName());
		} catch (DroolsRuleExecutionException e) {
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e.getGeneratedException());
			MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULES_EXECUTION_EXCEPTION);
		} catch (NotCompatibleTypeException ncte) {
			MessageManager.showError(LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION, LanguageCodes.ERROR_DESCRIPTION,
					ncte.getDescription());
		}
	}

	public void setEnabledButtons() {
		try {
			newFormButton.setEnabled(getSecurityService()
					.isUserAuthorizedInAnyOrganization(UserSessionHandler.getUser(), AbcdActivity.FORM_CREATE));
		} catch (UserManagementException e) {
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
		if (createPdf != null) {
			createPdf.setEnabled(enableFormButtons);
		}
	}

	public void updateNewVersionButton(SimpleFormView selected) {
		if (selected != null && !(selected instanceof RootForm)) {
			newVersion.setEnabled(selected.isLastVersion() && selected.getStatus().equals(FormWorkStatus.FINAL_DESIGN)
					&& getSecurityService().isAuthorizedToForm(selected.getOrganizationId(),
							UserSessionHandler.getUser())
					&& !getSecurityService().isFormAlreadyInUse(selected.getId(), UserSessionHandler.getUser()));
		} else {
			newVersion.setEnabled(false);
		}
	}

	public void updateRemoveFormButton(SimpleFormView selected) {
		// Only some users can remove forms.
		try {
			removeForm.setVisible(getSecurityService().isUserAuthorizedInAnyOrganization(UserSessionHandler.getUser(),
					AbcdActivity.FORM_REMOVE));
		} catch (UserManagementException e) {
			removeForm.setVisible(false);
		}
		// When visible, enabled when can delete an element.
		removeForm.setEnabled(
				selected != null && !(selected instanceof RootForm) && getSecurityService().isAuthorizedActivity(
						UserSessionHandler.getUser(), selected.getOrganizationId(), AbcdActivity.FORM_REMOVE));
	}

	@Override
	public Set<Button> getSecuredButtons() {
		return new HashSet<Button>();
	}

	public void addFormRemoveListener(IFormRemove listener) {
		removeFormListeners.add(listener);
	}

	private void launchFormRemoveListener() {
		for (IFormRemove listener : removeFormListeners) {
			listener.removeButtonPressed();
		}
	}

	public void addImportJsonListener(ClickListener listener) {
		importJson.addClickListener(listener);
	}

}
