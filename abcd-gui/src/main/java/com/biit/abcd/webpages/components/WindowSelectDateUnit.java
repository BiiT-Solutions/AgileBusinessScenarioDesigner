package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.QuestionUnit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Selects if a date question expression is defined as year, month, or days.
 */
public class WindowSelectDateUnit extends AcceptCancelWindow {
	private static final long serialVersionUID = 361486551550136464L;
	private static final String width = "300px";
	private static final String height = "180px";

	private ComboBox unitSelector;

	public WindowSelectDateUnit(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public QuestionUnit getValue() {
		return (QuestionUnit) unitSelector.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		unitSelector = new ComboBox(inputFieldCaption);
		unitSelector.setNullSelectionAllowed(false);

		for (QuestionUnit unit : QuestionUnit.values()) {
			unitSelector.addItem(unit);
			String caption = "";
			switch (unit) {
			case DAYS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_DAY);
				break;
			case MONTHS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_MONTH);
				break;
			case YEARS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_YEAR);
				break;
			case DATE:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_DATE);
				break;
			}
			unitSelector.setItemCaption(unit, caption);
			unitSelector.setValue(unit);
		}

		unitSelector.focus();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(unitSelector);
		rootLayout.setComponentAlignment(unitSelector, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

	public void setValue(QuestionUnit value) {
		unitSelector.setValue(value);
	}
}