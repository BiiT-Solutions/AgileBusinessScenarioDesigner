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

import java.util.Comparator;
import java.util.List;

/**
 * Order an expression chain by the tree objects that compose the expression. Used in Table Decision Rules. Example:
 * question1 = answer1, question1 = answer2, question2 = answer1, question2 = answer2 ....
 */
public class ExpressionChainHierarchyComparator implements Comparator<ExpressionChain> {

    @Override
    public int compare(ExpressionChain expressionChain1, ExpressionChain expressionChain2) {
        //This two condition can't happen. still we guard it.
        if (expressionChain1 == null) {
            return -1;
        }
        if (expressionChain2 == null) {
            return 1;
        }

        List<Expression> expressions1 = expressionChain1.getExpressions();
        List<Expression> expressions2 = expressionChain2.getExpressions();

        for (int i = 0; i < Math.min(expressions1.size(), expressions2.size()); i++) {
            int comparation = 0;

            if (expressions1.get(i) instanceof ExpressionValueTreeObjectReference && expressions2.get(i) instanceof ExpressionValueTreeObjectReference) {
                ExpressionValueTreeObjectReference reference1 = (ExpressionValueTreeObjectReference) expressions1.get(i);
                ExpressionValueTreeObjectReference reference2 = (ExpressionValueTreeObjectReference) expressions2.get(i);

                //If both are null do nothing (comparation = 0)
                if (reference1.getReference() == null && reference2.getReference() == null) {
                    comparation = 0;
                } else if (reference1.getReference() == null && reference2.getReference() != null) {
                    return -1;
                } else if (reference1.getReference() != null && reference2.getReference() == null) {
                    return 1;
                } else {
                    comparation = reference1.getReference().compareTo(reference2.getReference());
                }
            } else if (expressions1.get(i) instanceof ExpressionChain && expressions2.get(i) instanceof ExpressionChain) {
                ExpressionChain chain1 = (ExpressionChain) expressions1.get(i);
                ExpressionChain chain2 = (ExpressionChain) expressions2.get(i);

                if (chain1.getExpressions().isEmpty()) {
                    return -1;
                } else if (chain2.getExpressions().isEmpty()) {
                    return 1;
                }

                // No differences among TreeObjects. Use representation as comparation
                comparation = chain1.getRepresentation(true).compareTo(chain2.getRepresentation(true));
            }
            if (comparation != 0) {
                return comparation;
            }
        }

        return 0;
    }
}
