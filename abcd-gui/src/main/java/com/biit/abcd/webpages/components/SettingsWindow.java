package com.biit.abcd.webpages.components;

import java.io.IOException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SettingsWindow extends PopupWindow {

	private static final long serialVersionUID = 4258182015635300330L;
	private static final String width = "300px";

	private IFormDao formDao;

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

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		// Global Constant Button can be only used by users with an specific role.
		try {
			if (AbcdAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					DActivity.GLOBAL_VARIABLE_EDITOR)) {
				Button globalConstantsButton = new Button(
						ServerTranslate.translate(LanguageCodes.SETTINGS_GLOBAL_CONSTANTS), new ClickListener() {
							private static final long serialVersionUID = 5662848461729745562L;

							@Override
							public void buttonClick(ClickEvent event) {
								try {
									UserSessionHandler.getFormController().checkUnsavedChanges();
									ApplicationFrame.navigateTo(WebMap.GLOBAL_VARIABLES);
								} catch (TreeObjectNotEqualsException | StorableObjectNotEqualsException
										| FormNotEqualsException | GroupNotEqualsException | QuestionNotEqualsException
										| CustomVariableNotEqualsException | ExpressionNotEqualsException
										| TableRuleNotEqualsException | RuleNotEqualsException
										| DiagramNotEqualsException | DiagramObjectNotEqualsException
										| NodeNotEqualsException | SizeNotEqualsException | PointNotEqualsException
										| BiitTextNotEqualsException | GlobalVariableNotEqualsException
										| VariableDataNotEqualsException e) {
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
							}
						});
				globalConstantsButton.setWidth("100%");
				rootLayout.addComponent(globalConstantsButton);
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		// Clear cache for admin users.
		try {
			if (AbcdAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					DActivity.EVICT_CACHE)) {
				Button clearCacheButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_CLEAR_CACHE),
						new ClickListener() {
							private static final long serialVersionUID = -1121572145945309858L;

							@Override
							public void buttonClick(ClickEvent event) {
								final AlertMessageWindow windowAccept = new AlertMessageWindow(
										LanguageCodes.WARNING_CLEAR_CACHE);
								windowAccept.addAcceptActionListener(new AcceptActionListener() {
									@Override
									public void acceptAction(AcceptCancelWindow window) {
										formDao.evictAllCache();
										AbcdLogger.info(this.getClass().getName(), "User '"
												+ UserSessionHandler.getUser().getEmailAddress()
												+ "' has cleared all the 2nd level cache.");
										MessageManager.showInfo(LanguageCodes.INFO_CACHE_CLEARED);
										windowAccept.close();
									}
								});
								windowAccept.showCentered();
								close();
							}
						});
				clearCacheButton.setWidth("100%");
				rootLayout.addComponent(clearCacheButton);
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		Button logoutButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_LOG_OUT),
				new ClickListener() {
					private static final long serialVersionUID = -1121572145945309858L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							UserSessionHandler.getFormController().checkUnsavedChanges();
							ApplicationFrame.navigateTo(WebMap.LOGIN_PAGE);
							UserSessionHandler.logout();
						} catch (TreeObjectNotEqualsException | StorableObjectNotEqualsException
								| FormNotEqualsException | GroupNotEqualsException | QuestionNotEqualsException
								| CustomVariableNotEqualsException | ExpressionNotEqualsException
								| TableRuleNotEqualsException | RuleNotEqualsException | DiagramNotEqualsException
								| DiagramObjectNotEqualsException | NodeNotEqualsException | SizeNotEqualsException
								| PointNotEqualsException | BiitTextNotEqualsException
								| GlobalVariableNotEqualsException | VariableDataNotEqualsException e) {
							final AlertMessageWindow windowAccept = new AlertMessageWindow(
									LanguageCodes.WARNING_LOST_UNSAVED_DATA);
							windowAccept.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									ApplicationFrame.navigateTo(WebMap.LOGIN_PAGE);
									UserSessionHandler.logout();
									windowAccept.close();
								}
							});
							windowAccept.showCentered();
							close();
						}
					}
				});
		logoutButton.setWidth("100%");
		rootLayout.addComponent(logoutButton);

		Button closeButton = new Button(ServerTranslate.translate(LanguageCodes.SETTINGS_CLOSE), new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		closeButton.setWidth("100%");
		rootLayout.addComponent(closeButton);

		return rootLayout;
	}
}
