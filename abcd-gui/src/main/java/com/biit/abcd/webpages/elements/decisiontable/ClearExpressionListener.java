package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public interface ClearExpressionListener {

	void clearExpression(TableRuleRow row, Object propertyId);

}
