package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.serialization.expressions.ExpressionOperatorMathDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionOperatorMathSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines any mathematical operator.
 */
@Entity
@JsonDeserialize(using = ExpressionOperatorMathDeserializer.class)
@JsonSerialize(using = ExpressionOperatorMathSerializer.class)
@Table(name = "expression_operator_math")
public class ExpressionOperatorMath extends ExpressionOperator {
    private static final long serialVersionUID = 1688182189700104309L;
    private static final List<AvailableOperator> ALLOWED_OPERATORS = new ArrayList<AvailableOperator>(Arrays.asList(
            AvailableOperator.NULL, AvailableOperator.ASSIGNATION, AvailableOperator.PLUS, AvailableOperator.MINUS,
            AvailableOperator.MULTIPLICATION, AvailableOperator.DIVISION));

    public ExpressionOperatorMath() {
        super();
        try {
            setValue(AvailableOperator.NULL);
        } catch (NotValidOperatorInExpression e) {
            // This should never happen
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    public ExpressionOperatorMath(AvailableOperator operator) {
        super();
        try {
            setValue(operator);
        } catch (NotValidOperatorInExpression e) {
            // This should never happen
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    @Override
    public List<AvailableOperator> getAcceptedValues() {
        return ALLOWED_OPERATORS;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionOperatorMath) {
            super.copyData(object);
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionOperatorMath.");
        }
    }

}
