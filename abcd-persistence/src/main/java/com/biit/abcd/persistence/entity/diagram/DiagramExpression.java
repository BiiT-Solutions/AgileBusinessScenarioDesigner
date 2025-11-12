package com.biit.abcd.persistence.entity.diagram;

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

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.serialization.diagram.DiagramExpressionDeserializer;
import com.biit.abcd.serialization.diagram.DiagramExpressionSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramExpressionDeserializer.class)
@JsonSerialize(using = DiagramExpressionSerializer.class)
@Table(name = "diagram_expression")
public class DiagramExpression extends DiagramElement {
    private static final long serialVersionUID = 406552071357685928L;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expression")
    private ExpressionChain expression;


    //For json purposes.
    @Transient
    private transient String expressionId;

    public DiagramExpression() {
        super();
    }

    @Override
    public DiagramText getText() {
        if (getExpression() != null) {
            return new DiagramText(getExpression().getName());
        } else {
            return new DiagramText("Calculation");
        }
    }

    public ExpressionChain getExpression() {
        return expression;
    }

    public void setExpression(ExpressionChain expression) {
        this.expression = expression;
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (expression != null) {
            expression.resetIds();
        }
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (expression != null) {
            innerStorableObjects.add(expression);
            innerStorableObjects.addAll(expression.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramExpression) {
            super.copyData(object);
            DiagramExpression diagramCalculation = (DiagramExpression) object;

            if (diagramCalculation.getExpression() != null) {
                ExpressionChain formExpression = new ExpressionChain();
                formExpression.copyData(diagramCalculation.getExpression());
                setExpression(formExpression);
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of DiagramCalculation.");
        }
    }


    public String getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(String expressionId) {
        this.expressionId = expressionId;
    }
}
