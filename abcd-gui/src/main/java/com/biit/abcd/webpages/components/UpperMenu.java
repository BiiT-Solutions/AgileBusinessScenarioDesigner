package com.biit.abcd.webpages.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
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
import com.biit.abcd.persistence.utils.Exceptions.TestScenarioNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public abstract class UpperMenu extends SecuredMenu {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "100px";
	public static final String MENU_HEIGHT = "70px";
	public static final String SEPARATOR_WIDTH = "10px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";
	private static final String SEPARATOR_STYLE = "v-menu-separator";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private IconButton formManagerButton, settingsButton;
	private IFormDao formDao;
	// Settings buttons
	private IconButton globalConstantsButton, clearCacheButton, logoutButton;
	private Map<IconButton, List<IconButton>> subMenuButtons;

	public UpperMenu() {
		super();
		defineUpperMenu();
		this.setContractIcons(true, BUTTON_WIDTH);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	protected void initHorizontalButtonGroup() {
		super.initHorizontalButtonGroup();
		subMenuButtons = new HashMap<>();

		upperRootLayout = new HorizontalLayout();
		upperRootLayout.setSizeFull();

		oldRootLayoutContainer = new HorizontalLayout();
		oldRootLayoutContainer.setSizeFull();
		oldRootLayoutContainer.setStyleName(CLASSNAME_HORIZONTAL_BUTTON_WRAPPER);

		// Add FormManager button.
		formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER, ThemeIcon.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 4002268252434768032L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							UserSessionHandler.getFormController().checkUnsavedChanges();
							UserSessionHandler.getTestScenariosController().checkUnsavedChanges();
							UiAccesser.releaseForm(UserSessionHandler.getUser());
							ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
						} catch (TreeObjectNotEqualsException | StorableObjectNotEqualsException
								| FormNotEqualsException | GroupNotEqualsException | QuestionNotEqualsException
								| CustomVariableNotEqualsException | ExpressionNotEqualsException
								| TableRuleNotEqualsException | RuleNotEqualsException | DiagramNotEqualsException
								| DiagramObjectNotEqualsException | NodeNotEqualsException | SizeNotEqualsException
								| PointNotEqualsException | BiitTextNotEqualsException
								| GlobalVariableNotEqualsException | VariableDataNotEqualsException
								| TestScenarioNotEqualsException e) {
							final AlertMessageWindow windowAccept = new AlertMessageWindow(
									LanguageCodes.WARNING_LOST_UNSAVED_DATA);
							windowAccept.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									UiAccesser.releaseForm(UserSessionHandler.getUser());
									ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
									windowAccept.close();
								}
							});
							windowAccept.showCentered();
						}
					}
				});
		formManagerButton.setEnabled(true);
		formManagerButton.setHeight("100%");
		formManagerButton.setWidth(BUTTON_WIDTH);

		CssLayout separator = new CssLayout();
		separator.setHeight("100%");
		separator.setWidth(SEPARATOR_WIDTH);
		separator.setStyleName(SEPARATOR_STYLE);

		// First create the buttons of the submenu
		List<IconButton> settingsButtonsList = createSettingButtons();
		settingsButton = addSubMenu(ThemeIcon.SETTINGS, LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP,
				LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, settingsButtonsList);
		settingsButton.setHeight("100%");
		settingsButton.setWidth(BUTTON_WIDTH);
		settingsButton.setId("settingsButton");

		Component currentRootLayout = getCompositionRoot();

		// First we change the composition root (vaadin component must be not in
		// the composition tree before adding it to another component)
		setCompositionRoot(upperRootLayout);
		oldRootLayoutContainer.addComponent(currentRootLayout);

		upperRootLayout.addComponent(oldRootLayoutContainer);
		upperRootLayout.addComponent(separator);
		upperRootLayout.addComponent(formManagerButton);
		upperRootLayout.addComponent(settingsButton);
		upperRootLayout.setExpandRatio(oldRootLayoutContainer, 1.0f);
		upperRootLayout.setExpandRatio(separator, 0.0f);
		upperRootLayout.setExpandRatio(settingsButton, 0.0f);
		upperRootLayout.setExpandRatio(formManagerButton, 0.0f);

		setSizeFull();
	}

	private void defineUpperMenu() {
		this.setWidth("100%");
		this.setHeight(MENU_HEIGHT);
		setStyleName("upper-menu v-horizontal-button-group");
	}

	/**
	 * 
	 * @param icon
	 * @param caption
	 * @param tooltip
	 * @param buttons
	 * @return
	 */
	public IconButton addSubMenu(ThemeIcon icon, LanguageCodes caption, LanguageCodes tooltip, List<IconButton> buttons) {
		IconButton subMenu = generateSubMenu(icon, caption, tooltip, buttons);
		return subMenu;
	}

	public IconButton generateSubMenu(ThemeIcon icon, LanguageCodes caption, LanguageCodes tooltip,
			final List<IconButton> buttons) {
		for (IconButton button : buttons) {
			button.addStyleName("v-popover-upper-submenu");
		}

		final IconButton subMenu = new IconButton(caption, icon, tooltip, IconSize.BIG);
		subMenu.addStyleName("opens-popover-menu");
		subMenu.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 9175409158532169878L;

			@Override
			public void buttonClick(ClickEvent event) {
				VerticalLayout rootLayout = new VerticalLayout();
				rootLayout.setSizeUndefined();

				final Popover popover = new Popover(rootLayout);
				popover.setClosable(true);

				for (final IconButton button : buttons) {
					button.setWidth(BUTTON_WIDTH);
					button.setHeight(MENU_HEIGHT);
					rootLayout.addComponent(button);
					button.addClickListener(new ClickListener() {
						private static final long serialVersionUID = -2214568128797434177L;

						@Override
						public void buttonClick(ClickEvent event) {
							popover.close();
						}
					});
				}
				popover.showRelativeTo(subMenu);
			}
		});
		subMenuButtons.put(subMenu, buttons);
		return subMenu;
	}

	private List<IconButton> createSettingButtons() {
		List<IconButton> iconButtonList = new ArrayList<IconButton>();
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		// Settings menu.
		IconButton aboutUsButton = new IconButton(LanguageCodes.CAPTION_ABOUT_US, ThemeIcon.ABOUT_US,
				LanguageCodes.TOOLTIP_ABOUT_US, IconSize.MEDIUM);
		aboutUsButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4996751752953783384L;

			@Override
			public void buttonClick(ClickEvent event) {
				(new WindowAboutUs()).showCentered();
			}
		});
		iconButtonList.add(aboutUsButton);

		// Global Constant Button can be only used by users with an specific
		// role.
		try {
			if (AbcdFormAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					AbcdActivity.GLOBAL_VARIABLE_EDITOR)) {
				globalConstantsButton = new IconButton(LanguageCodes.SETTINGS_GLOBAL_CONSTANTS,
						ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS, LanguageCodes.SETTINGS_GLOBAL_CONSTANTS,
						IconSize.MEDIUM);
				globalConstantsButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 5662848461729745562L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							UserSessionHandler.getFormController().checkUnsavedChanges();
							ApplicationFrame.navigateTo(WebMap.GLOBAL_VARIABLES);
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
									ApplicationFrame.navigateTo(WebMap.GLOBAL_VARIABLES);
									windowAccept.close();
								}
							});
							windowAccept.showCentered();
						}
					}
				});
				globalConstantsButton.setWidth("100%");
				iconButtonList.add(globalConstantsButton);
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		// Clear cache for admin users.
		try {
			if (AbcdFormAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					AbcdActivity.EVICT_CACHE)) {
				clearCacheButton = new IconButton(LanguageCodes.SETTINGS_CLEAR_CACHE, ThemeIcon.CLEAR_CACHE,
						LanguageCodes.SETTINGS_CLEAR_CACHE, IconSize.MEDIUM);
				clearCacheButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = -1121572145945309858L;

					@Override
					public void buttonClick(ClickEvent event) {
						final AlertMessageWindow windowAccept = new AlertMessageWindow(
								LanguageCodes.WARNING_CLEAR_CACHE);
						windowAccept.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								// Reset ehCache.
								formDao.evictAllCache();
								// Reset Liferay Users pool.
								AbcdFormAuthorizationService.getInstance().reset();
								ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
								AbcdLogger.info(this.getClass().getName(), "User '"
										+ UserSessionHandler.getUser().getEmailAddress()
										+ "' has cleared all the 2nd level cache.");
								MessageManager.showInfo(LanguageCodes.INFO_CACHE_CLEARED);
								windowAccept.close();
							}
						});
						windowAccept.showCentered();
					}
				});
				clearCacheButton.setWidth("100%");
				iconButtonList.add(clearCacheButton);
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		// Only if you are not connected using Liferay.
		logoutButton = new IconButton(LanguageCodes.SETTINGS_LOG_OUT, ThemeIcon.LOG_OUT,
				LanguageCodes.SETTINGS_LOG_OUT, IconSize.MEDIUM);
		logoutButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					UserSessionHandler.getFormController().checkUnsavedChanges();
					logout();
				} catch (TreeObjectNotEqualsException | StorableObjectNotEqualsException | FormNotEqualsException
						| GroupNotEqualsException | QuestionNotEqualsException | CustomVariableNotEqualsException
						| ExpressionNotEqualsException | TableRuleNotEqualsException | RuleNotEqualsException
						| DiagramNotEqualsException | DiagramObjectNotEqualsException | NodeNotEqualsException
						| SizeNotEqualsException | PointNotEqualsException | BiitTextNotEqualsException
						| GlobalVariableNotEqualsException | VariableDataNotEqualsException e) {
					final AlertMessageWindow windowAccept = new AlertMessageWindow(
							LanguageCodes.WARNING_LOST_UNSAVED_DATA);
					windowAccept.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							logout();
							windowAccept.close();
						}
					});
					windowAccept.showCentered();
				}
			}
		});
		logoutButton.setId("logoutButton");
		logoutButton.setWidth("100%");
		iconButtonList.add(logoutButton);

		return iconButtonList;
	}

	private void logout() {
		UiAccesser.releaseForm(UserSessionHandler.getUser());
		UI.getCurrent().getPage().setLocation("./VAADIN/logout.html");
		try {
			UI.getCurrent().close();
		} catch (Exception e) {
			AbcdLogger.errorMessage(UpperMenu.class.getName(), e);
		}
		UserSessionHandler.logout();
	}

	public void hideLogoutButton(boolean hide) {
		hideButton(settingsButton, logoutButton, !hide);
	}

	public void hideButton(IconButton submenu, IconButton button, boolean visible) {
		List<IconButton> buttons = subMenuButtons.get(submenu);
		if (buttons != null) {
			for (IconButton buttonInSubmenu : buttons) {
				if (buttonInSubmenu.equals(button)) {
					buttonInSubmenu.setVisible(visible);
				}
			}
		}
	}
}
