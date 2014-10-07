package com.biit.abcd.webpages.elements.formmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.FormManager;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.DroolsSubmittedFormResultWindow;
import com.biit.abcd.webpages.components.DroolsSubmittedFormWindow;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.SettingsWindow;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class FormManagerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 504419812975550794L;
	private IconButton newFormButton, exportToDrools, createTestScenario, launchTestScenario;
	private FormManager parent;
	private List<IFormSelectedListener> formSelectedListeners;

	public FormManagerUpperMenu(FormManager parent) {
		super();
		this.parent = parent;
		formSelectedListeners = new ArrayList<>();
		defineMenu();
		setEnabledButtons();
	}

	private void defineMenu() {
		// Add new Form
		newFormButton = new IconButton(LanguageCodes.FORM_MANAGER_EDIT_FORM, ThemeIcon.FORM_MANAGER_ADD_FORM,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 6053447189295644721L;

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().addWindow(
								new WindowNewForm(parent, LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
										LanguageCodes.WINDOW_NEWFORM_NAME_TEXTFIELD));
					}
				});
		// Create rules and launch drools engine
		exportToDrools = new IconButton(LanguageCodes.FORM_MANAGER_EXPORT_RULES, ThemeIcon.FORM_MANAGER_EXPORT_RULES,
				LanguageCodes.FORM_MANAGER_EXPORT_RULES, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 267803697670003444L;

					@Override
					public void buttonClick(ClickEvent event) {
						final DroolsSubmittedFormWindow droolsWindow = new DroolsSubmittedFormWindow();
						droolsWindow.addAcceptActionListener(new AcceptActionListener() {

							@Override
							public void acceptAction(AcceptCancelWindow window) {
								// Accept button update the form from the
								// simpleViewForm.
								launchListeners();
								// After this SaveAsButton standard behavior is
								// launched automatically.
								// Show results in window if defined.
								if (droolsWindow.getOrbeonAppName() != null
										&& droolsWindow.getOrbeonAppName().length() > 0
										&& droolsWindow.getOrbeonFormName() != null
										&& droolsWindow.getOrbeonFormName().length() > 0
										&& droolsWindow.getOrbeonDocumentId() != null
										&& droolsWindow.getOrbeonDocumentId().length() > 0) {
									FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
									ISubmittedForm submittedForm;
									try {
										submittedForm = droolsExporter.processForm(UserSessionHandler
												.getFormController().getForm(), UserSessionHandler
												.getGlobalVariablesController().getGlobalVariables(), droolsWindow
												.getOrbeonAppName(), droolsWindow.getOrbeonFormName(), droolsWindow
												.getOrbeonDocumentId());

										if (submittedForm instanceof DroolsForm) {
											final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
													((DroolsForm) submittedForm).getSubmittedForm());
											droolsResultWindow.addAcceptActionListener(new AcceptActionListener() {
												@Override
												public void acceptAction(AcceptCancelWindow window) {
													droolsResultWindow.close();
												}
											});
											droolsResultWindow.showCentered();
										}
										droolsWindow.close();
									} catch (ExpressionInvalidException | RuleInvalidException | IOException e) {
										MessageManager.showError(LanguageCodes.ERROR_DROOLS_INVALID_RULE,
												e.getMessage());
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
									} catch (DocumentException | CategoryNameWithoutTranslation e) {
										MessageManager.showError(LanguageCodes.ERROR_ORBEON_IMPORTER_INVALID_FORM,
												e.getMessage());
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
									} catch (RuleNotImplementedException e) {
										MessageManager.showError(LanguageCodes.ERROR_RULE_NOT_IMPLEMENTED, e
												.getExpressionChain().getRepresentation());
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
									} catch (ActionNotImplementedException e) {
										MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
												LanguageCodes.WARNING_RULE_INCOMPLETE);
										AbcdLogger.warning(SettingsWindow.class.getName(), e.toString());
									} catch (Exception e) {
										MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR,
												LanguageCodes.ERROR_DROOLS_ENGINE);
										AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
									}
								}
							}
						});
						droolsWindow.showCentered();
					}
				});

		// Create new test scenario
		createTestScenario = new IconButton(LanguageCodes.FORM_MANAGER_CREATE_TEST_SCENARIOS,
				ThemeIcon.FORM_MANAGER_ADD_FORM, LanguageCodes.FORM_MANAGER_CREATE_TEST_SCENARIOS, IconSize.MEDIUM,
				new ClickListener() {
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
				ThemeIcon.FORM_MANAGER_EXPORT_RULES, LanguageCodes.FORM_MANAGER_LAUNCH_TEST_SCENARIOS, IconSize.MEDIUM,
				new ClickListener() {
					private static final long serialVersionUID = 2538065448920025133L;

					@Override
					public void buttonClick(ClickEvent event) {
					}
				});
		
		addIconButton(newFormButton);
		addIconButton(exportToDrools);
		addIconButton(createTestScenario);
//		addIconButton(launchTestScenario);
	}

	public void setEnabledButtons() {
		newFormButton.setEnabled(AbcdAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), DActivity.FORM_CREATE));
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
	}
}
