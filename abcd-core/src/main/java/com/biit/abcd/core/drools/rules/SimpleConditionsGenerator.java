package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.form.TreeObject;

public class SimpleConditionsGenerator {

	private static void putTreeObjectInTreeObjectDroolsIdMap(TreeObject treeObject) {
		if (TreeObjectDroolsIdMap.get(treeObject) == null) {
			TreeObjectDroolsIdMap.put(treeObject, treeObject.getUniqueNameReadable());
		}
	}

	/**
	 * Returns the simple conditions that look for a treeObject in the drools memory and assign it to a variable
	 * 
	 * @param treeObject
	 * @return
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
			} else {
				throw new TreeObjectInstanceNotRecognizedException(treeObject);
			}
		} else {
			throw new NullTreeObjectException();
		}
	}

	/**
	 * Returns the Drools condition in String form.
	 * 
	 * @param parent
	 * @param treeObject
	 * @return
	 * @throws TreeObjectInstanceNotRecognizedException
	 */
	private static String getGroupQuestionCondition(TreeObject parent, TreeObject treeObject) {
		String treeObjectClass = treeObject.getClass().getSimpleName();
		return "\t$" + treeObject.getUniqueNameReadable() + " : Submitted" + treeObjectClass + "( "
				+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObject) + "') from $"
				+ parent.getUniqueNameReadable() + ".getChildren(I" + treeObjectClass + ".class)"
				+ RulesUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
	}

	private static String getCategoryCondition(TreeObject parent, TreeObject treeObject) {
		String treeObjectClass = treeObject.getClass().getSimpleName();
		return "\t$" + treeObject.getUniqueNameReadable() + " : Submitted" + treeObjectClass + "( "
				+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObject) + "') from $"
				+ parent.getUniqueNameReadable() + ".getChildren(ICategory.class) "
				+ RulesUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
	}

	private static String simpleFormCondition(TreeObject treeObject) throws NullTreeObjectException {
		String conditions = "";
		putTreeObjectInTreeObjectDroolsIdMap(treeObject);
		conditions += "	$" + treeObject.getUniqueNameReadable()
				+ " : SubmittedForm() from $droolsForm.getSubmittedForm() \n";
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
	 * Returns the conditions that look for a treeObject in the drools memory and assign it to a variable.<br>
	 * Also checks if the treeObject has a custom variable with value set.
	 * 
	 * @param expressionValueCustomVariable
	 * @return
	 * @throws NullCustomVariableException
	 * @throws NullTreeObjectException
	 * @throws NullExpressionValueException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws TreeObjectParentNotValidException
	 */
	public static String getTreeObjectCustomVariableConditions(
			ExpressionValueCustomVariable expressionValueCustomVariable) throws NullCustomVariableException,
			NullTreeObjectException, NullExpressionValueException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException {
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
	 * @return
	 * @throws TreeObjectInstanceNotRecognizedException
	 */
	private static String getCategoryCustomVariableCondition(CustomVariable customVariable, TreeObject parent,
			TreeObject treeObject) throws TreeObjectInstanceNotRecognizedException {
		String treeObjectClass = treeObject.getClass().getSimpleName();
		return "\t$" + treeObject.getUniqueNameReadable() + " : Submitted" + treeObjectClass + "( "
				+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObject) + "', isScoreSet('"
				+ customVariable.getName() + "')) from $" + parent.getUniqueNameReadable()
				+ ".getChildren(ICategory.class) " + RulesUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
	}

	private static String getGroupQuestionCustomVariableCondition(CustomVariable customVariable, TreeObject parent,
			TreeObject treeObject) throws TreeObjectInstanceNotRecognizedException {
		String treeObjectClass = treeObject.getClass().getSimpleName();
		return "\t$" + treeObject.getUniqueNameReadable() + " : Submitted" + treeObjectClass + "( "
				+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObject) + "', isScoreSet('"
				+ customVariable.getName() + "')) from $" + parent.getUniqueNameReadable() + ".getChildren(I"
				+ treeObjectClass + ".class)" + RulesUtils.addFinalCommentsIfNeeded(treeObject) + "\n";
	}

	private static String simpleFormCustomVariableConditions(TreeObject treeObject, CustomVariable customVariable)
			throws NullExpressionValueException, NullTreeObjectException, NullCustomVariableException {
		putTreeObjectInTreeObjectDroolsIdMap(treeObject);
		String conditions = "\t$" + treeObject.getUniqueNameReadable() + " : SubmittedForm( isScoreSet('"
				+ customVariable.getName() + "')) from $droolsForm.getSubmittedForm() \n";
		return conditions;
	}

	private static String simpleCategoryCustomVariableConditions(TreeObject treeObject, CustomVariable customVariable)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
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
	 * Returns the conditions that look for a treeObject in the drools memory and assign it to a variable.<br>
	 * This method receives a custom variable but does not make the search for the custom variable value set in the tree
	 * object.
	 * 
	 * @param expressionValueCustomVariable
	 * @return
	 * @throws NullCustomVariableException
	 * @throws NullTreeObjectException
	 * @throws NullExpressionValueException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws TreeObjectParentNotValidException
	 */
	public static String getTreeObjectCustomVariableConditionsWithoutScoreCheck(
			ExpressionValueCustomVariable expressionValueCustomVariable) throws NullCustomVariableException,
			NullTreeObjectException, NullExpressionValueException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException {
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
