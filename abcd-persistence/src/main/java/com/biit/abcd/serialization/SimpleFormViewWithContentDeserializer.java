package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.jackson.serialization.BaseStorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SimpleFormViewWithContentDeserializer extends BaseStorableObjectDeserializer<SimpleFormViewWithContent> {

    @Override
    public void deserialize(SimpleFormViewWithContent element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setLabel(parseString("label", jsonObject));
        element.setVersion(parseInteger("version", jsonObject));
        element.setOrganizationId(parseLong("organizationId", jsonObject));

        element.setAvailableFrom(parseTimestamp("availableFrom", jsonObject));
        element.setAvailableTo(parseTimestamp("availableTo", jsonObject));
        if (jsonObject.get("status") != null) {
            element.setStatus(FormWorkStatus.getFromString(jsonObject.get("status").textValue()));
        }
        element.setJson(parseString("json", jsonObject));
    }

    private void updateExpressionValueTreeObjectReference(Form form, Expression expression) {
        if (expression instanceof ExpressionValueTreeObjectReference) {
            final ExpressionValueTreeObjectReference expressionValueTreeObjectReference = (ExpressionValueTreeObjectReference) expression;
            expressionValueTreeObjectReference.setReference(form.getChildByComparationId(expressionValueTreeObjectReference.getReferenceId()));
        }
    }

    private void update(Form form, Rule rule) {
        update(form, rule.getConditions());
        update(form, rule.getActions());
    }

    private void update(Form form, TableRule table) {
        for (TableRuleRow tableRuleRow : table.getRules()) {
            update(form, tableRuleRow.getConditions());
            update(form, tableRuleRow.getActions());
        }
    }


    private void update(Form form, ExpressionChain expressionChain) {
        for (Expression expression : expressionChain.getExpressions()) {
            updateExpressionValueTreeObjectReference(form, expression);
        }
    }
}
