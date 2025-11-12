package com.biit.abcd.persistence.entity.rules;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.serialization.rules.TableRuleRowDeserializer;
import com.biit.abcd.serialization.rules.TableRuleRowSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@JsonDeserialize(using = TableRuleRowDeserializer.class)
@JsonSerialize(using = TableRuleRowSerializer.class)
@Table(name = "rule_decision_table_row")
@Cacheable(true)
public class TableRuleRow extends StorableObject implements Comparable<TableRuleRow> {
    private static final long serialVersionUID = 1887517541278941394L;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "conditions")
    private ExpressionChain conditions;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "actions")
    private ExpressionChain actions;

    public TableRuleRow() {
        conditions = new ExpressionChain();
        conditions.setName("TableRuleRowCondition");
        actions = new ExpressionChain();
        actions.setName("TableRuleRowAction");
    }

    // Simple (Question : Answer => Action) builder
    public TableRuleRow(Expression question, Expression answer, ExpressionChain action) {
        conditions = new ExpressionChain();
        conditions.setName("TableRuleRowCondition");
        actions = new ExpressionChain();
        actions.setName("TableRuleRowAction");
        getConditions().addExpression(question);
        getConditions().addExpression(answer);
        getActions().setExpressions(action.getExpressions());
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (conditions != null) {
            conditions.resetIds();
        }
        if (actions != null) {
            actions.resetIds();
        }
    }

    public void addCondition(Expression expression) {
        conditions.addExpression(expression);
    }

    public void removeCondition(Expression expression) {
        conditions.removeExpression(expression);
    }

    public ExpressionChain getConditions() {
        return conditions;
    }

    public void setConditions(ExpressionChain conditions) {
        this.conditions = conditions;
    }

    public void removeConditions() {
        conditions.removeAllExpressions();
    }

    public void setActions(ExpressionChain action) {
        this.actions = action;
    }

    public ExpressionChain getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return conditions.toString() + " -> " + actions.toString();
    }

    public int getConditionNumber() {
        return conditions.getExpressions().size();
    }

    public TableRuleRow generateCopy() {
        TableRuleRow copy = null;
        try {
            copy = this.getClass().newInstance();
            copy.copyData(this);
        } catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        return copy;
    }

    public void addEmptyExpressionPair() {
        addCondition(new ExpressionValueTreeObjectReference());
        addCondition(new ExpressionChain());
    }

    public void setExpression(int position, Expression expression) {
        getConditions().getExpressions().set(position, expression);
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        innerStorableObjects.add(conditions);
        innerStorableObjects.addAll(conditions.getAllInnerStorableObjects());
        innerStorableObjects.add(actions);
        innerStorableObjects.addAll(actions.getAllInnerStorableObjects());
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof TableRuleRow) {
            super.copyBasicInfo(object);
            TableRuleRow tableRuleRow = (TableRuleRow) object;

            ExpressionChain condition = new ExpressionChain();
            condition.copyData(tableRuleRow.getConditions());
            setConditions(condition);

            ExpressionChain action = new ExpressionChain();
            action.copyData(tableRuleRow.getActions());
            setActions(action);
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TableRuleRow.");
        }
    }

    @Override
    public int compareTo(TableRuleRow otherRow) {
        if (!getConditions().getExpressions().isEmpty() && !otherRow.getConditions().getExpressions().isEmpty()) {
            Expression expression1 = this.getConditions().getExpressions().get(0);
            Expression expression2 = otherRow.getConditions().getExpressions().get(0);
            if (expression1 instanceof ExpressionValueTreeObjectReference) {
                if (expression2 instanceof ExpressionValueTreeObjectReference) {
                    return ((ExpressionValueTreeObjectReference) expression1).getReference().compareTo(
                            ((ExpressionValueTreeObjectReference) expression2).getReference());
                }
                // First null values.
                return 1;
            } else {
                if (expression2 instanceof ExpressionValueTreeObjectReference) {
                    // First null values.
                    return -1;
                }
                return 0;
            }
        } else {
            if (!getActions().getExpressions().isEmpty() && !otherRow.getActions().getExpressions().isEmpty()) {
                Expression expression1 = this.getActions().getExpressions().get(0);
                Expression expression2 = otherRow.getActions().getExpressions().get(0);
                if (expression1 instanceof ExpressionValueTreeObjectReference) {
                    if (expression2 instanceof ExpressionValueTreeObjectReference) {
                        return ((ExpressionValueTreeObjectReference) expression1).getReference().compareTo(
                                ((ExpressionValueTreeObjectReference) expression2).getReference());
                    }
                    // First null values.
                    return 1;
                } else {
                    if (expression2 instanceof ExpressionValueTreeObjectReference) {
                        // First null values.
                        return -1;
                    }
                    return 0;
                }
            }
            // First empty expressions.
            return this.getConditions().getExpressions().size() - otherRow.getConditions().getExpressions().size();
        }
    }

    public ExpressionChain getConditionsForDrools() {
        ExpressionChain preParsedConditions = new ExpressionChain();

        // For each pair of conditions adds an AND, and between each pair adds
        // an EQUALS
        for (int index = 0; index < conditions.getExpressions().size() - 1; index += 2) {
            Expression questionExpression = conditions.getExpressions().get(index);
            Expression answerExpression = conditions.getExpressions().get(index + 1);

            // Question not empty
            if ((questionExpression instanceof ExpressionValueTreeObjectReference)
                    && (((ExpressionValueTreeObjectReference) questionExpression).getReference() != null)
                    &&
                    // Answer not empty
                    (answerExpression instanceof ExpressionChain) && (((ExpressionChain) answerExpression).getExpressions() != null)
                    && (!((ExpressionChain) answerExpression).getExpressions().isEmpty())) {

                if (index > 0) {
                    preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                }
                preParsedConditions.addExpression(questionExpression);

                if (((ExpressionChain) answerExpression).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
                    preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.EQUALS));
                }
                preParsedConditions.addExpression(answerExpression);
            }
        }
        return preParsedConditions;
    }
}
