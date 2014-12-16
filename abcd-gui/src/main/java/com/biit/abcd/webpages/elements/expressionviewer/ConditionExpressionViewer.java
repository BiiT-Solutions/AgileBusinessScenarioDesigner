package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.core.drools.rules.validators.ExpressionValidator;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;

/**
 * Specialization of the expression viewer class to distinguish between
 * conditions and actions
 * 
 */
public class ConditionExpressionViewer extends ExpressionViewer {
	private static final long serialVersionUID = 5840439546050277560L;

	@Override
	protected void updateEvaluator() {
		try {
			ExpressionValidator.validateConditions(getExpressions());
			getEvaluatorOutput().setStyleName("expression-valid");
			getEvaluatorOutput().setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_VALID));
		} catch (Exception e) {
			AbcdLogger.debug(ConditionExpressionViewer.class.getName(), e.getMessage());
			getEvaluatorOutput().setStyleName("expression-invalid");
			getEvaluatorOutput().setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_INVALID));
		}
	}

}
