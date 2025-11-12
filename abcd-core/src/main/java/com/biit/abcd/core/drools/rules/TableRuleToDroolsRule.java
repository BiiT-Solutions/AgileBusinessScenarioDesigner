package com.biit.abcd.core.drools.rules;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.core.drools.rules.validators.RuleChecker;
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Transforms a Table rule to a list of drools rules.
 */
public class TableRuleToDroolsRule {

    /**
     * Convert a table rule in a list of rules. One rule for each row.
     *
     * @param tableRule
     * @param extraConditions
     * @return the list of rules
     * @throws ExpressionInvalidException
     * @throws RuleNotImplementedException
     * @throws ActionNotImplementedException
     * @throws NotCompatibleTypeException
     * @throws InvalidExpressionException
     * @throws PrattParserException
     */
    public static List<DroolsRule> parse(DiagramElement node, TableRule tableRule, ExpressionChain extraConditions, AtomicLong salience)
            throws ExpressionInvalidException, RuleNotImplementedException, ActionNotImplementedException,
            PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
        List<DroolsRule> newRules = new ArrayList<>();
        if (tableRule != null) {
            String tableRuleName = tableRule.getName();
            int i = 0;
            for (TableRuleRow row : tableRule.getRules()) {
                if (row.getActions() != null && row.getActions().getExpressions() != null
                        && !row.getActions().getExpressions().isEmpty()) {
                    DroolsRule newRule = new DroolsRule();

                    final ExpressionChain rowConditionExpression;
                    if (!row.getConditions().getExpressions().isEmpty() && !(row.getConditions().getExpressions().get(0) instanceof ExpressionValueCustomVariable)) {
                        rowConditionExpression = convertTableRowQuestionsToExpressionChain(row.getConditions());
                    } else {
                        rowConditionExpression = convertTableRowVariablesToExpressionChain(row.getConditions());
                    }

                    newRule.setName(RuleGenerationUtils.getRuleName(tableRuleName + "_row_" + i, extraConditions));
                    newRule.setSalience(salience.decrementAndGet());
                    newRule.setConditions(RuleGenerationUtils.flattenExpressionChain(rowConditionExpression));
                    newRule.addExtraConditions(extraConditions);
                    newRule.setActions(row.getActions());
                    newRules.add(newRule);
                }
                i++;
            }
        }
        for (DroolsRule droolsRule : newRules) {
            RuleChecker.checkRule(node, droolsRule, tableRule.getName());
        }
        return newRules;
    }

    /**
     * Adds the logic expressions that the table don't add
     *
     * @param conditions
     * @return
     */
    private static ExpressionChain convertTableRowQuestionsToExpressionChain(ExpressionChain conditions) {
        ExpressionChain preParsedConditions = new ExpressionChain();

        // For each pair of conditions adds an AND, and between each pair adds
        // an EQUALS
        for (int index = 0; index < conditions.getExpressions().size() - 1; index += 2) {
            Expression questionExpression = conditions.getExpressions().get(index);

            final Expression answerExpression;
            final ExpressionOperatorLogic operatorLogic;
            //Operator is the first token from the answer area.
            Expression expressionOperator = conditions.getExpressions().get(index + 1);
            if (expressionOperator instanceof ExpressionOperatorLogic) {
                operatorLogic = (ExpressionOperatorLogic) expressionOperator;
                answerExpression = conditions.getExpressions().get(index + 2);
                index++;
            } else {
                //Backwards compatibility. If no operator exists.
                answerExpression = conditions.getExpressions().get(index + 1);
                operatorLogic = new ExpressionOperatorLogic(AvailableOperator.EQUALS);
            }

            // The Question is not empty
            if ((questionExpression instanceof ExpressionValueTreeObjectReference)
                    && (((ExpressionValueTreeObjectReference) questionExpression).getReference() != null)
                    // The Answer is not empty
                    && (answerExpression instanceof ExpressionChain)
                    && (((ExpressionChain) answerExpression).getExpressions() != null)
                    && (!((ExpressionChain) answerExpression).getExpressions().isEmpty())) {

                if (!preParsedConditions.getExpressions().isEmpty()) {
                    preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                }
                preParsedConditions.addExpression(questionExpression);

                if (((ExpressionChain) answerExpression).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
                    preParsedConditions.addExpression(operatorLogic);
                }
                preParsedConditions.addExpression(answerExpression);
            }
        }
        return preParsedConditions;
    }

    private static ExpressionChain convertTableRowVariablesToExpressionChain(ExpressionChain conditions) {
        ExpressionChain preParsedConditions = new ExpressionChain();

        // For each pair of conditions adds an AND, and between each pair adds
        // an EQUALS
        for (int index = 0; index < conditions.getExpressions().size() - 1; index += 2) {
            if (!preParsedConditions.getExpressions().isEmpty()) {
                preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
            }
            preParsedConditions.addExpression(conditions.getExpressions().get(index));
            if (conditions.getExpressions().get(index + 1) instanceof ExpressionChain) {
                preParsedConditions.addExpressions(((ExpressionChain) conditions.getExpressions().get(index + 1)).getExpressions());
            } else {
                preParsedConditions.addExpression(conditions.getExpressions().get(index + 1));
            }
        }
        return preParsedConditions;
    }
}
