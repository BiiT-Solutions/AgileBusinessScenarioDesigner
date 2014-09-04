package com.biit.abcd.webpages.components;

import java.io.IOException;

import org.dom4j.DocumentException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SettingsWindow extends PopupWindow {

	private static final long serialVersionUID = 4258182015635300330L;
	private static final String width = "300px";
	private ISubmittedForm submittedForm;

	public SettingsWindow() {
		setClosable(true);
		setResizable(false);
		setDraggable(false);
		setModal(true);
		center();
		setWidth(width);
		setHeight(null);
		setContent(generateContent());
		setCaption(ServerTranslate.translate(LanguageCodes.SETTINGS_TITLE));
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		Button globalConstantsButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_GLOBAL_CONSTANTS),
				new ClickListener() {
					private static final long serialVersionUID = 5662848461729745562L;

					@Override
					public void buttonClick(ClickEvent event) {
						final AlertMessageWindow windowAccept = new AlertMessageWindow(
								LanguageCodes.WARNING_LOST_UNSAVED_DATA);
						windowAccept.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								ApplicationFrame.navigateTo(WebMap.GLOBAL_VARIABLES);
								windowAccept.close();
							}
						});
						windowAccept.showCentered();
						close();
					}
				});
		Button droolsEngineButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_DROOLS_ENGINE),
				new ClickListener() {
					private static final long serialVersionUID = 5662848461729745562L;

					@Override
					public void buttonClick(ClickEvent event) {
						final DroolsSubmittedFormWindow droolsWindow = new DroolsSubmittedFormWindow();
						droolsWindow.addAcceptActionListener(new AcceptActionListener() {

							@Override
							public void acceptAction(AcceptCancelWindow window) {
								String formInfo = droolsWindow.getFormInfo();

								try {

									FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
									submittedForm = droolsExporter.submittedForm(UserSessionHandler
											.getFormController().getForm(), UserSessionHandler
											.getGlobalVariablesController().getGlobalVariables(), formInfo);

//									AbcdLogger.debug(this.getClass().getName(), droolsExporter.getGeneratedRules());

									final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
											submittedForm);
									droolsResultWindow.showCentered();
									droolsWindow.close();
								} catch (ExpressionInvalidException | RuleInvalidException | IOException
										| NotValidOperatorInExpression e) {
									MessageManager.showError(LanguageCodes.ERROR_DROOLS_INVALID_RULE, e.getMessage());
									AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
								} catch (CategoryDoesNotExistException | DocumentException
										| CategoryNameWithoutTranslation e) {
									MessageManager.showError(LanguageCodes.ERROR_ORBEON_IMPORTER_INVALID_FORM,
											e.getMessage());
									AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
								} catch (RuleNotImplementedException e) {
									MessageManager.showError(LanguageCodes.ERROR_RULE_NOT_IMPLEMENTED, e
											.getExpressionChain().getRepresentation());
									AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
								}
							}
						});
						droolsWindow.showCentered();
						close();
					}
				});
		Button logoutButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_LOG_OUT),
				new ClickListener() {
					private static final long serialVersionUID = -1121572145945309858L;

					@Override
					public void buttonClick(ClickEvent event) {
						final AlertMessageWindow windowAccept = new AlertMessageWindow(
								LanguageCodes.WARNING_LOST_UNSAVED_DATA);
						windowAccept.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								ApplicationFrame.navigateTo(WebMap.LOGIN_PAGE);
								windowAccept.close();
							}
						});
						windowAccept.showCentered();
						close();
					}
				});
		Button closeButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_CLOSE), new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		globalConstantsButton.setWidth("100%");
		droolsEngineButton.setWidth("100%");
		logoutButton.setWidth("100%");
		closeButton.setWidth("100%");

		rootLayout.addComponent(globalConstantsButton);
		rootLayout.addComponent(droolsEngineButton);
		rootLayout.addComponent(logoutButton);
		rootLayout.addComponent(closeButton);

		return rootLayout;
	}
}
