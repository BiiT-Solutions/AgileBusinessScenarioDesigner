package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.DecisionTableEditor;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindoNewTable extends Window {
	private static final long serialVersionUID = -466962195753116776L;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 170;
	private DecisionTableEditor parent;
	private TableRule tableRule;

	public WindoNewTable(DecisionTableEditor parent) {
		super();
		this.parent = parent;
		tableRule = new TableRule();
		setWidth(Math.min(WINDOW_WIDTH, UI.getCurrent().getPage().getBrowserWindowWidth()), Unit.PIXELS);
		setHeight(Math.min(WINDOW_HEIGHT, UI.getCurrent().getPage().getBrowserWindowHeight()), Unit.PIXELS);
		this.setCaption(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER));

		setContent(generateContent());
		setResizable(false);
		setModal(true);
		center();

	}

	public AbstractOrderedLayout generateContent() {
		VerticalLayout mainLayout = new VerticalLayout();

		final TextField tableNameTextField = new TextField(
				ServerTranslate.tr(LanguageCodes.WINDOW_NEWTABLE_NAME_TEXTFIELD));
		tableNameTextField.setWidth("100%");
		// formDescription.setMaxLength(Form.MAX_DESCRIPTION_LENGTH);

		mainLayout.addComponent(tableNameTextField);
		mainLayout.setExpandRatio(tableNameTextField, 1.0f);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		Button acceptButton = new IconButton(LanguageCodes.ACCEPT_BUTTON_CAPTION, ThemeIcons.ACCEPT,
				LanguageCodes.ACCEPT_BUTTON_CAPTION, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -3292565406584483547L;

					@Override
					public void buttonClick(ClickEvent event) {
						for (TableRule existingTableRule : UserSessionHandler.getFormController().getForm()
								.getTableRules()) {
							if (existingTableRule.getName().equals(tableNameTextField.getValue())) {
								MessageManager.showError(LanguageCodes.ERROR_REPEATED_TABLE_RULE_NAME);
								return;
							}
						}
						tableRule.setName(tableNameTextField.getValue());
						tableRule.setCreatedBy(UserSessionHandler.getUser());
						tableRule.setUpdatedBy(UserSessionHandler.getUser());
						tableRule.setUpdateTime();
						UserSessionHandler.getFormController().getForm().getTableRules().add(tableRule);
						parent.addTableRuleToMenu(tableRule);
						parent.sortTableMenu();
						close();
					}
				});

		buttonLayout.addComponent(acceptButton);
		buttonLayout.setComponentAlignment(acceptButton, Alignment.MIDDLE_CENTER);

		Button cancel = new IconButton(LanguageCodes.CANCEL_BUTTON_CAPTION, ThemeIcons.CANCEL, null, IconSize.SMALL,
				new ClickListener() {
					private static final long serialVersionUID = 521904682248680077L;

					@Override
					public void buttonClick(ClickEvent event) {
						close();
					}
				});

		buttonLayout.addComponent(cancel);
		buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		return mainLayout;
	}

}
