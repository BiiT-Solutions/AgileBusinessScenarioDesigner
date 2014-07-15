package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectRuleWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesRule extends PropertiesForClassComponent<DiagramRule> {
	private static final long serialVersionUID = -5724748150445403970L;
	private DiagramRule instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesRule() {
		super(DiagramRule.class);
	}

	@Override
	protected void setElementAbstract(DiagramRule element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORK_QUESTION_CAPTION));
		fieldWithSearchButton.setNullCaption("Rule");
		fieldWithSearchButton.setValue(null);
		if (instance.getRule() != null) {
			fieldWithSearchButton.setValue(instance.getRule(), instance.getRule().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2409172230358224943L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectRuleWindow ruleWindow = new SelectRuleWindow();
				ruleWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (ruleWindow.getValue() != null) {
							fieldWithSearchButton.setValue(ruleWindow.getValue(), ruleWindow.getValue().getName());
							instance.setRule(ruleWindow.getValue());
							firePropertyUpdateListener(instance);
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_TABLE);
						}
					}
				});
				ruleWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -3314196233359245226L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setRule(null);
				firePropertyUpdateListener(instance);
			}
		});

		FormLayout forkForm = new FormLayout();
		forkForm.setWidth(null);
		forkForm.addComponent(fieldWithSearchButton);

		addTab(forkForm, "TODO - JsonDiagramProperties Rule", true, 0);
	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
