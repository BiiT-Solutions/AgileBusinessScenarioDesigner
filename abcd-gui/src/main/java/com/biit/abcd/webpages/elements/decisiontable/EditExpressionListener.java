package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.rules.TableRuleRow;


public interface EditExpressionListener {

	void editExpression(TableRuleRow row, Object propertyId);

}
