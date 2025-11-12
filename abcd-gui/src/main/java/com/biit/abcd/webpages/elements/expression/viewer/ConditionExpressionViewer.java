package com.biit.abcd.webpages.elements.expression.viewer;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
