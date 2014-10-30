package com.biit.abcd.webpages.elements.expressionviewer;

import java.sql.Timestamp;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.language.UserLocaleStringToDoubleConverter;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class ExpressionElement extends CssLayout {
	private static final long serialVersionUID = -1658928610594293678L;
	private static final String HEIGHT = "25px";
	private static final String STYLE = "v-expression-element";
	private Label elementName;

	public ExpressionElement(Expression expression, LayoutClickListener clickListener) {
		// Use language definition for dates.
		if (expression instanceof ExpressionValueTimestamp) {
			elementName = new Label(DateManager.convertDateToString((Timestamp) expression.getValue(),
					ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_DATE)));
			// Decimals are showed depending on the liferay's user configuration
		} else if (expression instanceof ExpressionValueNumber) {
			elementName = new Label();
			elementName.setConverter(new UserLocaleStringToDoubleConverter());
			ObjectProperty<Double> property = new ObjectProperty<Double>((Double) expression.getValue());
			elementName.setPropertyDataSource(property);
		} else {
			elementName = new Label(expression.getRepresentation());
		}

		this.setWidth(null);
		this.setHeight(HEIGHT);
		this.setStyleName(STYLE);

		addComponent(elementName);
		setImmediate(true);

		this.addLayoutClickListener(clickListener);
	}

	public String toString() {
		return elementName.getCaption();
	}

}
