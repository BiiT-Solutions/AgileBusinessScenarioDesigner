package com.biit.abcd.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.utils.exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.core.utils.exceptions.ExpressionNotEqualsException;
import com.biit.abcd.core.utils.exceptions.FormNotEqualsException;
import com.biit.abcd.core.utils.exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.core.utils.exceptions.GroupNotEqualsException;
import com.biit.abcd.core.utils.exceptions.QuestionNotEqualsException;
import com.biit.abcd.core.utils.exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.core.utils.exceptions.TableRuleNotEqualsException;
import com.biit.abcd.core.utils.exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.core.utils.exceptions.VariableDataNotEqualsException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;

/**
 * Compares two forms. Must be equals (but with different IDs and ComparationIds).
 */
public class FormVersionComparator {
	private static Set<StorableObject> alreadyComparedForm1Element = new HashSet<>();

	private static void compare(StorableObject object1, StorableObject object2) throws StorableObjectNotEqualsException {
		// if ((object1.getId() != null && object2.getId() == null)
		// || (object1.getId() == null && object2.getId() != null)) {
		// throw new StorableObjectNotEqualsException("Storable objects has different ids state: " + object1.getId()
		// + " <-> " + object2.getId());
		// }

		if (object1.getId() != null && object2.getId() != null && object1.getId().equals(object2.getId())) {
			throw new StorableObjectNotEqualsException("Storable objects has same id!: '" + object1.getId() + "'");
		}
		if (object1.getComparationId().equals(object2.getComparationId())) {
			throw new StorableObjectNotEqualsException("Storable objects are the same objects: '" + object1
					+ "' and '" + object2 + "'.");
		}
	}

	private static void compare(Group object1, Group object2) throws GroupNotEqualsException {
		if (object1.isRepeatable() != object2.isRepeatable()) {
			throw new GroupNotEqualsException("Check repeatable options of groups '" + object1 + "' and '" + object2
					+ "'.");
		}
	}

	private static void compare(Question object1, Question object2) throws QuestionNotEqualsException {
		if (object1.getAnswerFormat() != object2.getAnswerFormat()) {
			throw new QuestionNotEqualsException("Answer formats are different between questions '" + object1
					+ "' and '" + object2 + "'.");
		}
		if (object1.getAnswerType() != object2.getAnswerType()) {
			throw new QuestionNotEqualsException("Answer types are different between questions '" + object1 + "' and '"
					+ object2 + "'.");
		}
	}

	private static void compare(CustomVariable object1, CustomVariable object2)
			throws CustomVariableNotEqualsException, StorableObjectNotEqualsException {

		if (object1 == null || object2 == null) {
			throw new CustomVariableNotEqualsException("One value is null '" + object1 + "' and '" + object2 + "'.");
		}

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getForm() != null && object2.getForm() == null)
				|| (object1.getForm() == null && object2.getForm() != null)
				|| ((object1.getForm() != null && object2.getForm() != null) && !object1.getForm().getName()
						.equals(object2.getForm().getName()))) {
			throw new CustomVariableNotEqualsException("Parents are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");
		}
		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| (object1.getName() != null && object2.getName() != null && !object1.getName().equals(
						object2.getName()))) {
			throw new CustomVariableNotEqualsException("Names are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");

		}
		if (object1.getScope() != object2.getScope()) {
			throw new CustomVariableNotEqualsException("Scopes are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");

		}
		if (object1.getType() != object2.getType()) {
			throw new CustomVariableNotEqualsException("Types are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");
		}
	}

	private static void compare(GlobalVariable object1, GlobalVariable object2)
			throws GlobalVariableNotEqualsException, VariableDataNotEqualsException, StorableObjectNotEqualsException {
		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| (object1.getName() != null && object2.getName() != null && !object1.getName().equals(
						object2.getName()))) {
			throw new GlobalVariableNotEqualsException("Names are different between global variables '" + object1
					+ "' and '" + object2 + "'.");
		}

		if ((object1.getFormat() != null && object2.getFormat() == null)
				|| (object1.getFormat() == null && object2.getFormat() != null)
				|| (object1.getFormat() != null && object2.getFormat() != null && !object1.getFormat().equals(
						object2.getFormat()))) {
			throw new GlobalVariableNotEqualsException("Formats are different between global variables '" + object1
					+ "' and '" + object2 + "'.");
		}

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		// Compare Expressions
		Iterator<VariableData> variableDataIterator1 = object1.getVariableData().iterator();
		Iterator<VariableData> variableDataIterator2 = object2.getVariableData().iterator();
		do {
			if (variableDataIterator1.hasNext() != variableDataIterator2.hasNext()) {
				throw new GlobalVariableNotEqualsException("VariableData list length differs!");
			}
			compare(variableDataIterator1.next(), variableDataIterator2.next());
		} while (variableDataIterator1.hasNext() || variableDataIterator2.hasNext());
	}

	private static void compare(VariableData object1, VariableData object2) throws VariableDataNotEqualsException,
			StorableObjectNotEqualsException {
		if ((object1.getValidFrom() != null && object2.getValidFrom() == null)
				|| (object1.getValidFrom() == null && object2.getValidFrom() != null)
				|| (object1.getValidFrom() != null && object2.getValidFrom() != null && !object1.getValidFrom().equals(
						object2.getValidFrom()))) {
			throw new VariableDataNotEqualsException("ValidFrom information is not equals between '" + object1
					+ "' and '" + object2 + "'.");
		}

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getValidTo() != null && object2.getValidTo() == null)
				|| (object1.getValidTo() == null && object2.getValidTo() != null)
				|| (object1.getValidTo() != null && object2.getValidTo() != null && !object1.getValidTo().equals(
						object2.getValidTo()))) {
			throw new VariableDataNotEqualsException("getValidTo information is not equals between '" + object1
					+ "' and '" + object2 + "'.");
		}

		// Variable Data value is number, string or Date.
		if ((object1.getValue() != null && object2.getValue() == null)
				|| (object1.getValue() == null && object2.getValue() != null)
				|| (object1.getValue() != null && object2.getValue() != null && !object1.getValue().equals(
						object2.getValue()))) {
			throw new VariableDataNotEqualsException("Value is not equals between '" + object1 + "' and '" + object2
					+ "'.");
		}
	}

	private static void compare(Expression object1, Expression object2) throws StorableObjectNotEqualsException,
			ExpressionNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			CustomVariableNotEqualsException {
		if (object1.getClass() != object2.getClass()) {
			throw new ExpressionNotEqualsException("Classes are different between expressions '" + object1 + "' and '"
					+ object2 + "'");
		}

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if (object1.isEditable() != object2.isEditable()) {
			throw new ExpressionNotEqualsException("Editable fields are different between expressions '" + object1
					+ "' and '" + object2 + "'.");
		}
		if (object1.getSortSeq() != object2.getSortSeq()) {
			throw new ExpressionNotEqualsException("SortSeq fields are different between expressions '" + object1
					+ "' and '" + object2 + "'.");
		}

		try {
			compareExpressionValue(object1.getValue(), object2.getValue());
		} catch (QuestionNotEqualsException | TreeObjectNotEqualsException | GroupNotEqualsException ene) {
			throw new ExpressionNotEqualsException("Value fields are different between expressions '" + object1
					+ "' and '" + object2 + "'. Nested exception is: " + ene.getMessage());
		}
		// Some expressions has an extra field.
		if (object1 instanceof ExpressionValueGenericCustomVariable) {
			compare(((ExpressionValueGenericCustomVariable) object1).getVariable(),
					((ExpressionValueGenericCustomVariable) object2).getVariable());
		}

		if (object1 instanceof ExpressionValueCustomVariable) {
			compare(((ExpressionValueCustomVariable) object1).getVariable(),
					((ExpressionValueCustomVariable) object2).getVariable());
		}
	}

	@SuppressWarnings("unchecked")
	private static void compareExpressionValue(Object value1, Object value2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException,
			ExpressionNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			CustomVariableNotEqualsException {

		if (value1 instanceof StorableObject) {
			compare((StorableObject) value1, (StorableObject) value2);
		}

		if ((value1 != null && value2 == null) || (value1 == null && value2 != null)) {
			throw new ExpressionNotEqualsException("Value fields are different: '" + value1 + "' and '" + value2 + "'");
		}
		if (value1.getClass() != value2.getClass()) {
			throw new ExpressionNotEqualsException("Classes are different: '" + value1 + "' and '" + value2 + "'");
		}
		// Special cases.
		if (value1 instanceof TreeObject) {
			compare((TreeObject) value1, (TreeObject) value2);
		} else if (value1 instanceof ExpressionChain) {
			compare((ExpressionChain) value1, (ExpressionChain) value2);
		} else if (value1 instanceof GlobalVariable) {
			compare((GlobalVariable) value1, (GlobalVariable) value2);
		} else if (value1 instanceof List) {
			if (!((List<?>) value1).isEmpty() && ((List<?>) value1).get(0) instanceof Expression) {
				compare((List<Expression>) value1, (List<Expression>) value2);
			}
		} else if (!value1.equals(value2)) {
			throw new ExpressionNotEqualsException("Value fields are different: '" + value1 + "' and '" + value2
					+ "' -> " + value1.getClass().getName());
		}
	}

	private static void compare(ExpressionChain object1, ExpressionChain object2)
			throws StorableObjectNotEqualsException, ExpressionNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, CustomVariableNotEqualsException {

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| (object1.getName() != null && object1.getName() != null && !object1.getName().equals(
						object2.getName()))) {
			throw new ExpressionNotEqualsException("Names are different between expressions '" + object1 + "' and '"
					+ object2 + "'.");
		}

		// Compare Expressions
		Iterator<Expression> expressionsIterator1 = object1.getExpressions().iterator();
		Iterator<Expression> expressionsIterator2 = object2.getExpressions().iterator();
		do {
			if (expressionsIterator1.hasNext() != expressionsIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("ExpressionChain list length differs!");
			}
			Expression expression1 = expressionsIterator1.next();
			Expression expression2 = expressionsIterator2.next();
			// ExpressionChain inside an ExpressionChain.
			if (expression1 instanceof ExpressionChain) {
				compare((ExpressionChain) expression1, (ExpressionChain) expression2);
			}
			compare(expression1, expression2);
		} while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext());

	}

	private static void compare(List<Expression> object1, List<Expression> object2)
			throws ExpressionNotEqualsException, StorableObjectNotEqualsException, CustomVariableNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		// Compare Expressions
		Iterator<Expression> expressionsIterator1 = object1.iterator();
		Iterator<Expression> expressionsIterator2 = object2.iterator();
		do {
			if (expressionsIterator1.hasNext() != expressionsIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("ExpressionChain list length differs!");
			}
			Expression expression1 = expressionsIterator1.next();
			Expression expression2 = expressionsIterator2.next();
			// ExpressionChain inside an ExpressionChain.
			if (expression1 instanceof ExpressionChain) {
				compare((ExpressionChain) expression1, (ExpressionChain) expression2);
			}
			compare(expression1, expression2);
		} while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext());
	}

	private static void compare(TreeObject object1, TreeObject object2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException {
		// Already compared because it is a child of a compared element.
		if (alreadyComparedForm1Element.contains(object1)) {
			return;
		}

		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| !object1.getName().equals(object2.getName())) {
			throw new TreeObjectNotEqualsException("Names are different between tree objects '" + object1 + "' and '"
					+ object2 + "'.");
		}
		if ((object1.getLabel() != null && object2.getLabel() == null)
				|| (object1.getLabel() == null && object2.getLabel() != null)
				|| !object1.getLabel().equals(object2.getLabel())) {
			throw new TreeObjectNotEqualsException("Labels are different between tree objects '" + object1 + "' and '"
					+ object2 + "'.");
		}

		// Compare parent.
		if (((object1.getParent() != null && object2.getParent() == null)
				|| (object1.getParent() == null && object2.getParent() != null) || ((object1.getParent() != null && object2
				.getParent() != null) && (!object1.getParent().getName().equals(object2.getParent().getName()))))) {
			throw new TreeObjectNotEqualsException("TreeObject '" + object1 + "' compared with '" + object2
					+ "' has different parents '" + object1.getParent() + "' and '" + object2.getParent() + "'.");
		}

		// Compare Children.
		for (int i = 0; i < object1.getChildren().size(); i++) {
			compare(object1.getChildren().get(i), object2.getChildren().get(i));
		}

		// Compare specific data
		if (object1 instanceof Group) {
			compare((Group) object1, (Group) object2);
		} else if (object1 instanceof Question) {
			compare((Question) object1, (Question) object2);
		}
		alreadyComparedForm1Element.add(object1);
	}

	private static void compare(TableRule object1, TableRule object2) throws StorableObjectNotEqualsException,
			TableRuleNotEqualsException, ExpressionNotEqualsException, CustomVariableNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| !object1.getName().equals(object2.getName())) {
			throw new TableRuleNotEqualsException("Names are different between table rules '" + object1 + "' and '"
					+ object2 + "'.");
		}

		// Compare table rules rows.
		Iterator<TableRuleRow> tableRuleRowIterator1 = object1.getRules().iterator();
		Iterator<TableRuleRow> tableRuleRowIterator2 = object2.getRules().iterator();
		do {
			if (tableRuleRowIterator1.hasNext() != tableRuleRowIterator2.hasNext()) {
				throw new TableRuleNotEqualsException("Row list length differs!");
			}
			compare(tableRuleRowIterator1.next(), tableRuleRowIterator2.next());
		} while (tableRuleRowIterator1.hasNext() || tableRuleRowIterator2.hasNext());
	}

	private static void compare(TableRuleRow object1, TableRuleRow object2) throws StorableObjectNotEqualsException,
			TableRuleNotEqualsException, ExpressionNotEqualsException, CustomVariableNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		compare(object1.getConditionChain(), object2.getConditionChain());
		compare(object1.getActionChain(), object2.getActionChain());
	}

	/**
	 * Form1 is the previous version of Form2.
	 * 
	 * @param form1
	 * @param form2
	 * @throws TreeObjectNotEqualsException
	 * @throws StorableObjectNotEqualsException
	 * @throws FormNotEqualsException
	 * @throws QuestionNotEqualsException
	 * @throws GroupNotEqualsException
	 * @throws CustomVariableNotEqualsException
	 * @throws ExpressionNotEqualsException
	 * @throws GlobalVariableNotEqualsException
	 * @throws VariableDataNotEqualsException
	 * @throws TableRuleNotEqualsException
	 */
	public static void compare(Form form1, Form form2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, FormNotEqualsException, GroupNotEqualsException,
			QuestionNotEqualsException, CustomVariableNotEqualsException, ExpressionNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException, TableRuleNotEqualsException {
		if ((form1 == null || form2 == null) && (form1 != null || form2 != null)) {
			throw new FormNotEqualsException("Obtained form is null");
		}
		if (form1.getChildren().size() != form2.getChildren().size()) {
			throw new FormNotEqualsException("Form has different children size!");
		}
		if (!form1.getName().equals(form2.getName())) {
			throw new FormNotEqualsException("Form has different name!");
		}
		if (form1.getVersion() + 1 != (int) form2.getVersion()) {
			throw new FormNotEqualsException("Form's versions are incorrect!");
		}
		if (form1.getOrganizationId() != form2.getOrganizationId()) {
			throw new FormNotEqualsException("Form's organizations are different!");
		}
		// Previous version ends when the other starts.
		if ((form1.getAvailableTo() == null && form2.getAvailableTo() != null)
				|| (form1.getAvailableTo() != null && form2.getAvailableTo() == null)
				|| ((form1.getAvailableTo() != null && form2.getAvailableTo() != null) && !form1.getAvailableTo()
						.equals(form2.getAvailableFrom()))) {
			throw new FormNotEqualsException("Form's validTo and ValidFrom are different!");
		}
		// Compare treeObject hierarchy.
		for (int i = 0; i < form2.getChildren().size(); i++) {
			compare(form1.getChildren().get(i), form2.getChildren().get(i));
		}

		// Compare custom variables. First sort the set to be sure the comparation is correct.
		List<CustomVariable> customVariables1 = new ArrayList<>(form1.getCustomVariables());
		Collections.sort(customVariables1, new CustomVariableSorter());
		List<CustomVariable> customVariables2 = new ArrayList<>(form2.getCustomVariables());
		Collections.sort(customVariables2, new CustomVariableSorter());
		Iterator<CustomVariable> variableIterator1 = customVariables1.iterator();
		Iterator<CustomVariable> variableIterator2 = customVariables2.iterator();
		do {
			if (variableIterator1.hasNext() != variableIterator2.hasNext()) {
				throw new CustomVariableNotEqualsException("CustomVariables list length differs!");
			}
			compare(variableIterator1.next(), variableIterator2.next());
		} while (variableIterator1.hasNext() || variableIterator2.hasNext());

		// Compare Expressions. First sort the set to be sure the comparation is correct.
		List<ExpressionChain> expressionChain1 = new ArrayList<>(form1.getExpressionChains());
		Collections.sort(expressionChain1, new ExpressionSorter());
		List<ExpressionChain> expressionChain2 = new ArrayList<>(form2.getExpressionChains());
		Collections.sort(expressionChain2, new ExpressionSorter());
		Iterator<ExpressionChain> expressionsIterator1 = expressionChain1.iterator();
		Iterator<ExpressionChain> expressionsIterator2 = expressionChain2.iterator();
		do {
			if (expressionsIterator1.hasNext() != expressionsIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("ExpressionChain list length differs!");
			}
			compare(expressionsIterator1.next(), expressionsIterator2.next());
		} while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext());

		// Compare TableRules
		Iterator<TableRule> tableRulesIterator1 = form1.getTableRules().iterator();
		Iterator<TableRule> tableRulesIterator2 = form2.getTableRules().iterator();
		do {
			if (tableRulesIterator1.hasNext() != tableRulesIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("TableRules list length differs!");
			}
			compare(tableRulesIterator1.next(), tableRulesIterator2.next());
		} while (tableRulesIterator1.hasNext() || tableRulesIterator2.hasNext());
	}
}

class ExpressionSorter implements Comparator<ExpressionChain> {
	@Override
	public int compare(ExpressionChain arg0, ExpressionChain arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class CustomVariableSorter implements Comparator<CustomVariable> {
	@Override
	public int compare(CustomVariable arg0, CustomVariable arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}