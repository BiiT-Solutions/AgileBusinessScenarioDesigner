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

import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.form.entity.TreeObject;

/**
 * This class is used to generate the same generic code needed by almost all the
 * drools rules.<br>
 * To access drools variables or create specific binds the drools code is always
 * the same, so this class gathers all this methods and simplifies the
 * complexity of the drools parser.
 */
public class SimpleConditionsGenerator {

    private static void putTreeObjectInTreeObjectDroolsIdMap(TreeObject treeObject) {
        if (TreeObjectDroolsIdMap.get(treeObject) == null) {
            TreeObjectDroolsIdMap.put(treeObject, treeObject.getUniqueNameReadable());
        }
    }

    /**
     * Returns the simple conditions that look for a treeObject in the drools memory
     * and assign it to a variable
     *
     * @param treeObject
     * @return a trign with the condition.
     * @throws NullTreeObjectException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws TreeObjectParentNotValidException
     */
    public static String getTreeObjectConditions(TreeObject treeObject) throws NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        if (treeObject != null) {
            if (treeObject instanceof Form) {
                return simpleFormCondition(treeObject);
            } else if (treeObject instanceof Category) {
                return simpleCategoryConditions(treeObject);
            } else if ((treeObject instanceof Group) || (treeObject instanceof Question)) {
                return simpleGroupQuestionConditions(treeObject);
            } else if (treeObject instanceof Answer) {
                // Answers not parsed, checked here to avoid null pointers
                return "";
            } else {
                throw new TreeObjectInstanceNotRecognizedException(treeObject);
            }
        } else {
            throw new NullTreeObjectException();
        }
    }

    private static String getGroupQuestionCondition(TreeObject parent, TreeObject treeObject) {
        String treeObjectClass = treeObject.getClass().getSimpleName();
        return "\t$" + treeObject.getUniqueNameReadable() + " : DroolsSubmitted" + treeObjectClass + "( "
                + RuleGenerationUtils.returnSimpleTreeObjectNameFunction(treeObject) + "') from $"
                + parent.getUniqueNameReadable() + ".getChildren(ISubmitted" + treeObjectClass + ".class)"
                + RuleGenerationUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
    }

    private static String getCategoryCondition(TreeObject parent, TreeObject treeObject) {
        String treeObjectClass = treeObject.getClass().getSimpleName();
        return "\t$" + treeObject.getUniqueNameReadable() + " : DroolsSubmitted" + treeObjectClass + "( "
                + RuleGenerationUtils.returnSimpleTreeObjectNameFunction(treeObject) + "') from $"
                + parent.getUniqueNameReadable() + ".getChildren(ISubmittedCategory.class) "
                + RuleGenerationUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
    }

    private static String simpleFormCondition(TreeObject treeObject) {
        String conditions = "";
        putTreeObjectInTreeObjectDroolsIdMap(treeObject);
        conditions += "	$" + treeObject.getUniqueNameReadable()
                + " : DroolsSubmittedForm() from $droolsForm.getDroolsSubmittedForm() \n";
        return conditions;
    }

    private static String simpleCategoryConditions(TreeObject treeObject) throws NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        TreeObject parent = treeObject.getParent();
        if (parent != null) {
            if (parent instanceof Form) {
                putTreeObjectInTreeObjectDroolsIdMap(treeObject);
                String conditions = getTreeObjectConditions(parent);
                conditions += getCategoryCondition(parent, treeObject);
                return conditions;
            } else {
                throw new TreeObjectParentNotValidException(parent);
            }
        } else {
            throw new NullTreeObjectException();
        }
    }

    private static String simpleGroupQuestionConditions(TreeObject treeObject) throws NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        TreeObject parent = treeObject.getParent();
        if (parent != null) {
            if ((parent instanceof Category) || (parent instanceof Group)) {
                putTreeObjectInTreeObjectDroolsIdMap(treeObject);
                String conditions = getTreeObjectConditions(parent);
                conditions += getGroupQuestionCondition(parent, treeObject);
                return conditions;
            } else {
                throw new TreeObjectParentNotValidException(parent);
            }
        } else {
            throw new NullTreeObjectException();
        }
    }

    /**
     * Returns the conditions that look for a treeObject in the drools memory and
     * assign it to a variable.<br>
     * Also checks if the treeObject has a custom variable with value set.
     *
     * @param expressionValueCustomVariable
     * @return a string with the conditions.
     * @throws NullCustomVariableException
     * @throws NullTreeObjectException
     * @throws NullExpressionValueException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws TreeObjectParentNotValidException
     */
    public static String getTreeObjectCustomVariableConditions(
            ExpressionValueCustomVariable expressionValueCustomVariable)
            throws NullCustomVariableException, NullTreeObjectException, NullExpressionValueException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        if (expressionValueCustomVariable != null) {
            TreeObject treeObject = expressionValueCustomVariable.getReference();
            if (treeObject != null) {
                CustomVariable treeObjectCustomVariable = expressionValueCustomVariable.getVariable();
                if (treeObjectCustomVariable != null) {
                    if (treeObject instanceof Form) {
                        return simpleFormCustomVariableConditions(treeObject, treeObjectCustomVariable);
                    } else if (treeObject instanceof Category) {
                        return simpleCategoryCustomVariableConditions(treeObject, treeObjectCustomVariable);
                    } else if ((treeObject instanceof Group) || (treeObject instanceof Question)) {
                        return simpleGroupQuestionCustomVariableConditions(treeObject, treeObjectCustomVariable);
                    } else {
                        throw new TreeObjectInstanceNotRecognizedException(treeObject);
                    }
                } else {
                    throw new NullCustomVariableException();
                }
            } else {
                throw new NullTreeObjectException();
            }
        } else {
            throw new NullExpressionValueException();
        }
    }

    /**
     * Returns the Drools condition in String form.<br>
     * It also check for the custom variable value.
     *
     * @param customVariable
     * @param parent
     * @param treeObject
     * @return a string with the conditions.
     */
    private static String getCategoryCustomVariableCondition(CustomVariable customVariable, TreeObject parent,
                                                             TreeObject treeObject) {
        return DroolsParser.generateDroolsVariableCondition(treeObject, customVariable.getName(), null, null, null);
    }

    private static String getGroupQuestionCustomVariableCondition(CustomVariable customVariable, TreeObject parent,
                                                                  TreeObject treeObject) {
        return DroolsParser.generateDroolsVariableCondition(treeObject, customVariable.getName(), null, null, null);
    }

    private static String simpleFormCustomVariableConditions(TreeObject treeObject, CustomVariable customVariable) {
        putTreeObjectInTreeObjectDroolsIdMap(treeObject);
        return DroolsParser.generateDroolsVariableCondition(treeObject, customVariable.getName(), null, null, null);
    }

    private static String simpleCategoryCustomVariableConditions(TreeObject treeObject, CustomVariable customVariable)
            throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
            TreeObjectParentNotValidException {
        TreeObject parent = treeObject.getParent();
        if (parent != null) {
            if (parent instanceof Form) {
                putTreeObjectInTreeObjectDroolsIdMap(treeObject);
                String conditions = getTreeObjectConditions(parent);
                conditions += getCategoryCustomVariableCondition(customVariable, parent, treeObject);
                return conditions;
            } else {
                throw new TreeObjectParentNotValidException(parent);
            }
        } else {
            throw new NullTreeObjectException();
        }
    }

    private static String simpleGroupQuestionCustomVariableConditions(TreeObject treeObject,
                                                                      CustomVariable customVariable) throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
            TreeObjectParentNotValidException {
        TreeObject parent = treeObject.getParent();
        if (parent != null) {
            if ((parent instanceof Category) || (parent instanceof Group)) {
                putTreeObjectInTreeObjectDroolsIdMap(treeObject);
                String conditions = getTreeObjectConditions(parent);
                conditions += getGroupQuestionCustomVariableCondition(customVariable, parent, treeObject);
                return conditions;
            } else {
                throw new TreeObjectParentNotValidException(parent);
            }
        } else {
            throw new NullTreeObjectException();
        }
    }

    /**
     * Returns the conditions that look for a treeObject in the drools memory and
     * assign it to a variable.<br>
     * This method receives a custom variable but does not make the search for the
     * custom variable value set in the tree object.
     *
     * @param expressionValueCustomVariable
     * @return a string with the conditions.
     * @throws NullCustomVariableException
     * @throws NullTreeObjectException
     * @throws NullExpressionValueException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws TreeObjectParentNotValidException
     */
    public static String getTreeObjectCustomVariableConditionsWithoutScoreCheck(
            ExpressionValueCustomVariable expressionValueCustomVariable)
            throws NullCustomVariableException, NullTreeObjectException, NullExpressionValueException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        if (expressionValueCustomVariable != null) {
            TreeObject treeObject = expressionValueCustomVariable.getReference();
            if (treeObject != null) {
                CustomVariable treeObjectCustomvariable = expressionValueCustomVariable.getVariable();
                if (treeObjectCustomvariable != null) {
                    if (treeObject instanceof Form) {
                        return simpleFormCondition(treeObject);
                    } else if (treeObject instanceof Category) {
                        return simpleCategoryConditions(treeObject);
                    } else if ((treeObject instanceof Group) || (treeObject instanceof Question)) {
                        return simpleGroupQuestionConditions(treeObject);
                    } else {
                        throw new TreeObjectInstanceNotRecognizedException(treeObject);
                    }
                } else {
                    throw new NullCustomVariableException();
                }
            } else {
                throw new NullTreeObjectException();
            }
        } else {
            throw new NullExpressionValueException();
        }
    }
}
