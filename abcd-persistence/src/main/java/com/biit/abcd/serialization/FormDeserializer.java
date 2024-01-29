package com.biit.abcd.serialization;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.jackson.serialization.BaseFormDeserializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FormDeserializer extends BaseFormDeserializer<Form> {

    @Override
    public void deserialize(Form element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setAvailableFrom(parseTimestamp("availableFrom", jsonObject));
        element.setAvailableTo(parseTimestamp("availableTo", jsonObject));
        if (jsonObject.get("status") != null) {
            element.setStatus(FormWorkStatus.getFromString(jsonObject.get("status").textValue()));
        }
        if (jsonObject.get("diagrams") != null) {
            element.setDiagrams(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("diagrams").toString(), Diagram[].class))));
        }
        if (jsonObject.get("tableRules") != null) {
            element.setTableRules(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("tableRules").toString(), TableRule[].class)));
        }
        if (jsonObject.get("customVariables") != null) {
            element.setCustomVariables(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("customVariables").toString(), CustomVariable[].class))));
        }
        if (jsonObject.get("expressionChains") != null) {
            element.setExpressionChains(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("expressionChains").toString(), ExpressionChain[].class))));
        }

        // Diagram objects deserialization
        final JsonNode diagramObjects = jsonObject.get("rules");
        if (diagramObjects != null) {
            //Handle children one by one.
            if (diagramObjects.isArray()) {
                final Set<Rule> rules = new HashSet<>();
                for (JsonNode childNode : diagramObjects) {
                    try {
                        final Class<? extends Rule> classType = (Class<? extends Rule>) Class.forName(childNode.get("class").asText());
                        rules.add(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid rule object:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
                element.setRules(rules);
            }
        }

        //Set ExpressionValueTreeObjectReference tree objects from referenceId.
        for (Diagram diagram : element.getDiagrams()) {
            for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
                if (diagramObject instanceof DiagramTable) {
                    final TableRule tableRule = element.getTableByComparationId(((DiagramTable) diagramObject).getTableId());
                    ((DiagramTable) diagramObject).setTable(tableRule);
                    update(element, tableRule);
                }
                if (diagramObject instanceof DiagramRule) {
                    final Rule rule = element.getRulesByComparationId(((DiagramRule) diagramObject).getRuleId());
                    ((DiagramRule) diagramObject).setRule(rule);
                    update(element, rule);
                }
                if (diagramObject instanceof DiagramExpression) {
                    final ExpressionChain expression = element.getExpressionsByComparationId(((DiagramExpression) diagramObject).getExpressionId());
                    ((DiagramExpression) diagramObject).setExpression(expression);
                    update(element, expression);
                }
            }
        }

        for (TableRule tableRule : element.getTableRules()) {
            update(element, tableRule);
        }

        for (ExpressionChain expressionChain : element.getExpressionChains()) {
            update(element, expressionChain);
        }

        for (Rule rules : element.getRules()) {
            update(element, rules);
        }

        for (CustomVariable customVariable : element.getCustomVariables()) {
            update(element, customVariable);
        }
    }

    private void update(Form form, CustomVariable customVariable) {
        customVariable.setForm(form);
    }

    private void updateExpression(Form form, Expression expression) {
        updateExpressionValueTreeObjectReference(form, expression);
        updateExpressionValueCustomVariable(form, expression);
        updateExpressionValueGenericCustomVariable(form, expression);
        if (expression instanceof ExpressionChain) {
            for (Expression child : ((ExpressionChain) expression).getExpressions()) {
                updateExpression(form, child);
            }
        }
    }

    private void updateExpressionValueTreeObjectReference(Form form, Expression expression) {
        //For ExpressionValueTreeObjectReference and ExpressionValueCustomVariable
        if (expression instanceof ExpressionValueTreeObjectReference) {
            final ExpressionValueTreeObjectReference expressionValueTreeObjectReference = (ExpressionValueTreeObjectReference) expression;
            expressionValueTreeObjectReference.setReference(form.getChildByComparationId(expressionValueTreeObjectReference.getReferenceId()));
        }
    }

    private void updateExpressionValueCustomVariable(Form form, Expression expression) {
        if (expression instanceof ExpressionValueCustomVariable) {
            final ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) expression;
            expressionValueCustomVariable.setVariable(form.getCustomVariableByComparationId(expressionValueCustomVariable.getVariableId()));
        }
    }

    private void updateExpressionValueGenericCustomVariable(Form form, Expression expression) {
        if (expression instanceof ExpressionValueGenericCustomVariable) {
            final ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) expression;
            expressionValueGenericCustomVariable.setVariable(form.getCustomVariableByComparationId(expressionValueGenericCustomVariable.getVariableId()));
        }
    }

    private void update(Form form, Rule rule) {
        if (rule != null) {
            update(form, rule.getConditions());
            update(form, rule.getActions());
        }
    }

    private void update(Form form, TableRule table) {
        if (table != null && table.getRules() != null) {
            for (TableRuleRow tableRuleRow : table.getRules()) {
                update(form, tableRuleRow.getConditions());
                update(form, tableRuleRow.getActions());
            }
        }
    }


    private void update(Form form, ExpressionChain expressionChain) {
        if (expressionChain != null && expressionChain.getExpressions() != null) {
            for (Expression expression : expressionChain.getExpressions()) {
                updateExpression(form, expression);
            }
        }
    }
}

