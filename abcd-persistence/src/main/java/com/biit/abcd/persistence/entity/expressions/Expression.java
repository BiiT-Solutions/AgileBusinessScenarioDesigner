package com.biit.abcd.persistence.entity.expressions;

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
import com.biit.abcd.serialization.expressions.ExpressionDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * Basic class for defining an expression. Any other expression must inherit
 * from this class.
 */
@Entity
@JsonDeserialize(using = ExpressionDeserializer.class)
@JsonSerialize(using = ExpressionSerializer.class)
@Table(name = "expression_basic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class Expression extends StorableObject {
    private static final long serialVersionUID = 4816405922566823101L;

    @Transient
    private boolean editable;

    // For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
    // we cannot use the list of children with
    // @Orderby or @OrderColumn we use our own order manager.
    @Column(name = "sort_sequence", nullable = false)
    private long sortSeq = 0;

    public Expression() {
        super();
        editable = true;
    }

    public void copyBasicExpressionInfo(Expression expression) {
        setCreatedBy(expression.getCreatedBy());
        setCreationTime(expression.getCreationTime());
        setEditable(expression.isEditable());
        setSortSeq(expression.getSortSeq());
    }

    public final Expression generateCopy() {
        Expression copy = null;
        try {
            copy = this.getClass().newInstance();
            copy.copyData(this);
        } catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        return copy;
    }

    /**
     * Returns the expression in string format that can be evaluated by a Expression
     * Evaluator. Not allowed characters are ',', '.', ':', operators, ... that must
     * filtered of the expression if necessary.
     *
     * @return the expression as a string.
     */
    protected abstract String getExpression();

    /**
     * Returns a text representation of the Expression
     *
     * @param showWhiteCharacter if white characters must be keeped or not.
     * @return a string as a representation.
     */
    public abstract String getRepresentation(boolean showWhiteCharacter);

    public long getSortSeq() {
        return sortSeq;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    public void setSortSeq(long sortSeq) {
        this.sortSeq = sortSeq;
    }

    @Override
    public String toString() {
        return getExpression();
    }

    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof Expression) {
            super.copyBasicInfo(object);
            Expression expression = (Expression) object;
            setEditable(expression.isEditable());
            setSortSeq(expression.getSortSeq());
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Expression.");
        }
    }

    public abstract Object getValue();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Expression that = (Expression) o;
        return Objects.equals(getComparationId(), that.getComparationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getComparationId());
    }
}
