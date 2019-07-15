package com.biit.abcd.persistence.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;

/**
 * Compares two forms. Must be equals (but with different IDs and
 * ComparationIds).
 */
public class FormComparator {
	private Set<StorableObject> alreadyComparedForm1Element = new HashSet<>();
	private boolean checkIds = true;

	public FormComparator(boolean checkIds) {
		this.checkIds = checkIds;
	}

	private void compare(StorableObject object1, StorableObject object2) throws StorableObjectNotEqualsException {
		if ((object1 != null && object2 == null) || (object1 == null && object2 != null)) {
			throw new StorableObjectNotEqualsException(
					"One of the Storable objects is null: '" + object1 + "' and '" + object2 + "'.");
		}

		if (checkIds && object1.getId() != null && object2.getId() != null && object1.getId().equals(object2.getId())) {
			throw new StorableObjectNotEqualsException("Storable objects has same id!: '" + object1.getId() + "'");
		}

		if (checkIds && object1.getComparationId().equals(object2.getComparationId())) {
			throw new StorableObjectNotEqualsException(
					"Storable objects are the same objects: '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(Group object1, Group object2) throws GroupNotEqualsException {
		if (object1.isRepeatable() != object2.isRepeatable()) {
			throw new GroupNotEqualsException(
					"Check repeatable options of groups '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(Question object1, Question object2) throws QuestionNotEqualsException {
		if (object1.getAnswerFormat() != object2.getAnswerFormat()) {
			throw new QuestionNotEqualsException(
					"Answer formats are different between questions '" + object1 + "' and '" + object2 + "'.");
		}
		if (object1.getAnswerType() != object2.getAnswerType()) {
			throw new QuestionNotEqualsException(
					"Answer types are different between questions '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(CustomVariable object1, CustomVariable object2)
			throws CustomVariableNotEqualsException, StorableObjectNotEqualsException {

		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getForm() != null && object2.getForm() == null)
				|| (object1.getForm() == null && object2.getForm() != null)
				|| ((object1.getForm() != null && object2.getForm() != null)
						&& !object1.getForm().getName().equals(object2.getForm().getName()))) {
			throw new CustomVariableNotEqualsException(
					"Parents are different between custom variables '" + object1 + "' and '" + object2 + "'.");
		}
		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null) || (object1.getName() != null
						&& object2.getName() != null && !object1.getName().equals(object2.getName()))) {
			throw new CustomVariableNotEqualsException(
					"Names are different between custom variables '" + object1 + "' and '" + object2 + "'.");

		}
		if (object1.getScope() != object2.getScope()) {
			throw new CustomVariableNotEqualsException(
					"Scopes are different between custom variables '" + object1 + "' and '" + object2 + "'.");

		}
		if (object1.getType() != object2.getType()) {
			throw new CustomVariableNotEqualsException(
					"Types are different between custom variables '" + object1 + "' and '" + object2 + "'.");
		}
		if (!Objects.equals(object1.getDefaultValue(), object2.getDefaultValue())) {
			throw new CustomVariableNotEqualsException(
					"Default value are different between custom variables '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(GlobalVariable object1, GlobalVariable object2)
			throws GlobalVariableNotEqualsException, VariableDataNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}

		if (object1 == null || object2 == null) {
			throw new GlobalVariableNotEqualsException("Cannot compare '" + object1 + "' with '" + object2 + "'.");
		}

		// No compare Ids. Global Variables are not duplicated.

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null) || (object1.getName() != null
						&& object2.getName() != null && !object1.getName().equals(object2.getName()))) {
			throw new GlobalVariableNotEqualsException(
					"Names are different between global variables '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getFormat() != null && object2.getFormat() == null)
				|| (object1.getFormat() == null && object2.getFormat() != null) || (object1.getFormat() != null
						&& object2.getFormat() != null && !object1.getFormat().equals(object2.getFormat()))) {
			throw new GlobalVariableNotEqualsException(
					"Formats are different between global variables '" + object1 + "' and '" + object2 + "'.");
		}

		// Compare Expressions
		Iterator<VariableData> variableDataIterator1 = object1.getVariableData().iterator();
		Iterator<VariableData> variableDataIterator2 = object2.getVariableData().iterator();
		while (variableDataIterator1.hasNext() || variableDataIterator2.hasNext()) {
			if (variableDataIterator1.hasNext() != variableDataIterator2.hasNext()) {
				throw new GlobalVariableNotEqualsException("VariableData list length differs!");
			}
			compare(variableDataIterator1.next(), variableDataIterator2.next());
		}
	}

	private void compare(VariableData object1, VariableData object2) throws VariableDataNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}

		if (object1 == null || object2 == null) {
			throw new VariableDataNotEqualsException("Cannot compare '" + object1 + "' with '" + object2 + "'.");
		}

		// No compare Ids. Variables are not duplicated.
		if ((object1.getValidFrom() != null && object2.getValidFrom() == null)
				|| (object1.getValidFrom() == null && object2.getValidFrom() != null) || (object1.getValidFrom() != null
						&& object2.getValidFrom() != null && !object1.getValidFrom().equals(object2.getValidFrom()))) {
			throw new VariableDataNotEqualsException(
					"'Valid From' information is not equals between '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getValidTo() != null && object2.getValidTo() == null)
				|| (object1.getValidTo() == null && object2.getValidTo() != null) || (object1.getValidTo() != null
						&& object2.getValidTo() != null && !object1.getValidTo().equals(object2.getValidTo()))) {
			throw new VariableDataNotEqualsException(
					"'Valid To' information is not equals between '" + object1 + "' and '" + object2 + "'.");
		}

		// Variable Data value is number, string or Date.
		if ((object1.getValue() != null && object2.getValue() == null)
				|| (object1.getValue() == null && object2.getValue() != null) || (object1.getValue() != null
						&& object2.getValue() != null && !object1.getValue().equals(object2.getValue()))) {
			throw new VariableDataNotEqualsException(
					"Value is not equals between '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(Expression object1, Expression object2)
			throws StorableObjectNotEqualsException, ExpressionNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, CustomVariableNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}
		if (!object1.getClass().equals(object2.getClass())) {
			throw new ExpressionNotEqualsException(
					"Classes are different between expressions '" + object1 + "' and '" + object2 + "'");
		}

		if (object1.isEditable() != object2.isEditable()) {
			throw new ExpressionNotEqualsException(
					"Editable fields are different between expressions '" + object1 + "' and '" + object2 + "'.");
		}
		if (object1.getSortSeq() != object2.getSortSeq()) {
			throw new ExpressionNotEqualsException(
					"SortSeq fields are different between expressions '" + object1 + "' and '" + object2 + "'.");
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
	private void compareExpressionValue(Object object1, Object object2)
			throws TreeObjectNotEqualsException, StorableObjectNotEqualsException, GroupNotEqualsException,
			QuestionNotEqualsException, ExpressionNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, CustomVariableNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}

		if (object1 == null || object2 == null) {
			throw new ExpressionNotEqualsException("Cannot compare '" + object1 + "' with '" + object2 + "'.");
		}

		if ((object1 != null && object2 == null) || (object1 == null && object2 != null)) {
			throw new ExpressionNotEqualsException(
					"Value fields are different: '" + object1 + "' and '" + object2 + "'");
		}
		// Hibernate replace arrays. Check if it is the case.
		if (!object1.getClass().getName().equals("org.hibernate.collection.internal.PersistentBag")
				&& !object2.getClass().getName().equals("org.hibernate.collection.internal.PersistentBag")) {
			if (!object1.getClass().equals(object2.getClass())) {
				throw new ExpressionNotEqualsException(
						"Classes are different: '" + object1 + "' and '" + object2 + "'");
			}
		}
		// Special cases.
		if (object1 instanceof TreeObject) {
			compare((TreeObject) object1, (TreeObject) object2);
		} else if (object1 instanceof ExpressionChain) {
			compare((ExpressionChain) object1, (ExpressionChain) object2);
		} else if (object1 instanceof GlobalVariable) {
			compare((GlobalVariable) object1, (GlobalVariable) object2);
		} else if (object1 instanceof List) {
			if (!((List<?>) object1).isEmpty() && ((List<?>) object1).get(0) instanceof Expression) {
				compare((List<Expression>) object1, (List<Expression>) object2);
			}
		} else if (!object1.equals(object2)) {
			throw new ExpressionNotEqualsException("Value fields are different: '" + object1 + "' and '" + object2
					+ "' -> " + object1.getClass().getName());
		}
	}

	private void compare(ExpressionChain object1, ExpressionChain object2)
			throws StorableObjectNotEqualsException, ExpressionNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, CustomVariableNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}

		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null) || (object1.getName() != null
						&& object2.getName() != null && !object1.getName().equals(object2.getName()))) {
			throw new ExpressionNotEqualsException(
					"Names are different between expressions '" + object1 + "' and '" + object2 + "'.");
		}

		// Compare Expressions
		Iterator<Expression> expressionsIterator1 = object1.getExpressions().iterator();
		Iterator<Expression> expressionsIterator2 = object2.getExpressions().iterator();
		while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext()) {
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
		}
	}

	private void compare(List<Expression> object1, List<Expression> object2)
			throws ExpressionNotEqualsException, StorableObjectNotEqualsException, CustomVariableNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		// Compare Expressions
		Iterator<Expression> expressionsIterator1 = object1.iterator();
		Iterator<Expression> expressionsIterator2 = object2.iterator();
		while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext()) {
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
		}
	}

	private void compare(TreeObject object1, TreeObject object2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}

		// Already compared because it is a child of a compared element.
		if (alreadyComparedForm1Element.contains(object1)) {
			return;
		}

		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| ((object1.getName() != null && object2.getName() != null)
						&& !object1.getName().equals(object2.getName()))) {
			throw new TreeObjectNotEqualsException(
					"Names are different between tree objects '" + object1 + "' and '" + object2 + "'.");
		}
		if ((object1.getLabel() != null && object2.getLabel() == null)
				|| (object1.getLabel() == null && object2.getLabel() != null)
				|| ((object1.getLabel() != null && object2.getLabel() != null)
						&& !object1.getLabel().equals(object2.getLabel()))) {
			throw new TreeObjectNotEqualsException(
					"Labels are different between tree objects '" + object1 + "' and '" + object2 + "'.");
		}

		// Compare parent.
		if (((object1.getParent() != null && object2.getParent() == null)
				|| (object1.getParent() == null && object2.getParent() != null)
				|| ((object1.getParent() != null && object2.getParent() != null)
						&& (!object1.getParent().getName().equals(object2.getParent().getName()))))) {
			throw new TreeObjectNotEqualsException("TreeObject '" + object1 + "' compared with '" + object2
					+ "' has different parents '" + object1.getParent() + "' and '" + object2.getParent() + "'.");
		}

		// Compare Children.
		if (object1.getChildren().size() != object2.getChildren().size()) {
			throw new TreeObjectNotEqualsException("TreeObject '" + object1 + "' has a different number of children '"
					+ object1.getChildren().size() + "' than TreeObject '" + object2 + "' that has '"
					+ object2.getChildren().size() + "' children.");
		}
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

	private void compare(TableRule object1, TableRule object2)
			throws StorableObjectNotEqualsException, TableRuleNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| ((object1.getName() != null && object2.getName() != null)
						&& !object1.getName().equals(object2.getName()))) {
			throw new TableRuleNotEqualsException(
					"Names are different between table rules '" + object1 + "' and '" + object2 + "'.");
		}

		// Compare table rules rows.
		Iterator<TableRuleRow> tableRuleRowIterator1 = object1.getRules().iterator();
		Iterator<TableRuleRow> tableRuleRowIterator2 = object2.getRules().iterator();
		while (tableRuleRowIterator1.hasNext() || tableRuleRowIterator2.hasNext()) {
			if (tableRuleRowIterator1.hasNext() != tableRuleRowIterator2.hasNext()) {
				throw new TableRuleNotEqualsException("Row list length differs!");
			}
			compare(tableRuleRowIterator1.next(), tableRuleRowIterator2.next());
		}
	}

	private void compare(TableRuleRow object1, TableRuleRow object2)
			throws StorableObjectNotEqualsException, TableRuleNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		compare(object1.getConditions(), object2.getConditions());
		compare(object1.getAction(), object2.getAction());
	}

	private void compare(Rule object1, Rule object2)
			throws StorableObjectNotEqualsException, RuleNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| ((object1.getName() != null && object2.getName() != null)
						&& !object1.getName().equals(object2.getName()))) {
			throw new RuleNotEqualsException(
					"Names are different between rules '" + object1 + "' and '" + object2 + "'.");
		}

		compare(object1.getConditions(), object2.getConditions());
		compare(object1.getActions(), object2.getActions());
	}

	private void compare(Size object1, Size object2) throws StorableObjectNotEqualsException, SizeNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if (object1.getWidth() != object2.getWidth()) {
			throw new SizeNotEqualsException(
					"Widths are different between Sizes '" + object1 + "' and '" + object2 + "'.");
		}

		if (object1.getHeight() != object2.getHeight()) {
			throw new SizeNotEqualsException(
					"Heights are different between Sizes '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(Point object1, Point object2)
			throws StorableObjectNotEqualsException, SizeNotEqualsException, PointNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if (object1.getX() != object2.getX()) {
			throw new PointNotEqualsException(
					"Widths are different between Points '" + object1 + "' and '" + object2 + "'.");
		}

		if (object1.getY() != object2.getY()) {
			throw new PointNotEqualsException(
					"Heights are different between Points '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(DiagramText object1, DiagramText object2) throws StorableObjectNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getText() != null && object2.getText() == null)
				|| (object1.getText() == null && object2.getText() != null)
				|| ((object1.getText() != null && object2.getText() != null)
						&& !object1.getText().equals(object2.getText()))) {
			throw new BiitTextNotEqualsException(
					"Text are different between BiitTexts '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getFill() != null && object2.getFill() == null)
				|| (object1.getFill() == null && object2.getFill() != null)
				|| ((object1.getFill() != null && object2.getFill() != null)
						&& !object1.getFill().equals(object2.getFill()))) {
			throw new BiitTextNotEqualsException(
					"Fill values are different between BiitTexts '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getFontSize() != null && object2.getFontSize() == null)
				|| (object1.getFontSize() == null && object2.getFontSize() != null)
				|| ((object1.getFontSize() != null && object2.getFontSize() != null)
						&& !object1.getFontSize().equals(object2.getFontSize()))) {
			throw new BiitTextNotEqualsException(
					"FontSize values are different between BiitTexts '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getStroke() != null && object2.getStroke() == null)
				|| (object1.getStroke() == null && object2.getStroke() != null)
				|| ((object1.getStroke() != null && object2.getStroke() != null)
						&& !object1.getStroke().equals(object2.getStroke()))) {
			throw new BiitTextNotEqualsException(
					"Stroke values are different between BiitTexts '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getStrokeWidth() != null && object2.getStrokeWidth() == null)
				|| (object1.getStrokeWidth() == null && object2.getStrokeWidth() != null)
				|| ((object1.getStrokeWidth() != null && object2.getStrokeWidth() != null)
						&& !object1.getStrokeWidth().equals(object2.getStrokeWidth()))) {
			throw new BiitTextNotEqualsException(
					"StrokeWidth values are different between BiitTexts '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(DiagramElement object1, DiagramElement object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, SizeNotEqualsException,
			PointNotEqualsException, BiitTextNotEqualsException {
		if ((object1.getTooltip() != null && object2.getTooltip() == null)
				|| (object1.getTooltip() == null && object2.getTooltip() != null)
				|| ((object1.getTooltip() != null && object2.getTooltip() != null)
						&& !object1.getTooltip().equals(object2.getTooltip()))) {
			throw new DiagramObjectNotEqualsException(
					"ToolTip are different between DiagramElements '" + object1 + "' and '" + object2 + "'.");
		}

		if (object1.getAngle() != object2.getAngle()) {
			throw new DiagramObjectNotEqualsException(
					"Anges are different between DiagramElements '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getSize() != null && object2.getSize() == null)
				|| (object1.getSize() == null && object2.getSize() != null)) {
			throw new DiagramObjectNotEqualsException(
					"Size are different between DiagramElements '" + object1 + "' and '" + object2 + "'.");
		}
		compare(object1.getSize(), object2.getSize());

		if ((object1.getPosition() != null && object2.getPosition() == null)
				|| (object1.getPosition() == null && object2.getPosition() != null)) {
			throw new DiagramObjectNotEqualsException(
					"Points are different between DiagramElements '" + object1 + "' and '" + object2 + "'.");
		}
		compare(object1.getPosition(), object2.getPosition());

		if ((object1.getText() != null && object2.getText() == null)
				|| (object1.getText() == null && object2.getText() != null)) {
			throw new DiagramObjectNotEqualsException(
					"BiitTexts are different between DiagramElements '" + object1 + "' and '" + object2 + "'.");
		}
		compare(object1.getText(), object2.getText());
	}

	private void compare(DiagramChild object1, DiagramChild object2) throws StorableObjectNotEqualsException,
			DiagramObjectNotEqualsException, SizeNotEqualsException, PointNotEqualsException,
			BiitTextNotEqualsException, DiagramNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, NodeNotEqualsException, TableRuleNotEqualsException,
			RuleNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}

		if (object1 == null || object2 == null) {
			throw new DiagramObjectNotEqualsException("Cannot compare '" + object1 + "' with '" + object2 + "'.");
		}

		compare((DiagramElement) object1, (DiagramElement) object2);
		if ((object1.getDiagram() != null && object2.getDiagram() == null)
				|| (object1.getDiagram() == null && object2.getDiagram() != null)) {
			throw new DiagramObjectNotEqualsException(
					"Diagrams are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}
		if (object1.getDiagram() != null && object2.getDiagram() != null) {
			compare(object1.getDiagram(), object2.getDiagram());
		}
	}

	private void compare(DiagramExpression object1, DiagramExpression object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);
		compare(object1.getExpression(), object2.getExpression());
	}

	private void compare(DiagramFork object1, DiagramFork object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);

		if ((object1.getReference() != null && object2.getReference() == null)
				|| (object1.getReference() == null && object2.getReference() != null)) {
			throw new DiagramObjectNotEqualsException(
					"ExpressionValueTreeObjectReference are different between diagram objects '" + object1 + "' and '"
							+ object2 + "'.");
		}
		compare(object1.getReference(), object2.getReference());
	}

	private void compare(Node object1, Node object2) throws StorableObjectNotEqualsException, NodeNotEqualsException {
		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getJointjsId() != null && object2.getJointjsId() == null)
				|| (object1.getJointjsId() == null && object2.getJointjsId() != null)
				|| ((object1.getJointjsId() != null && object2.getJointjsId() != null)
						&& !object1.getJointjsId().equals(object2.getJointjsId()))) {
			throw new NodeNotEqualsException(
					"JointjsId are different between Nodes '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getSelector() != null && object2.getSelector() == null)
				|| (object1.getSelector() == null && object2.getSelector() != null)
				|| ((object1.getSelector() != null && object2.getSelector() != null)
						&& !object1.getSelector().equals(object2.getSelector()))) {
			throw new NodeNotEqualsException(
					"Selectors are different between Nodes '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getPort() != null && object2.getPort() == null)
				|| (object1.getPort() == null && object2.getPort() != null)
				|| ((object1.getPort() != null && object2.getPort() != null)
						&& !object1.getPort().equals(object2.getPort()))) {
			throw new NodeNotEqualsException(
					"Ports are different between Nodes '" + object1 + "' and '" + object2 + "'.");
		}
	}

	private void compare(DiagramLink object1, DiagramLink object2) throws StorableObjectNotEqualsException,
			DiagramObjectNotEqualsException, ExpressionNotEqualsException, CustomVariableNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException, NodeNotEqualsException {

		if ((object1.getAttrs() != null && object2.getAttrs() == null)
				|| (object1.getAttrs() == null && object2.getAttrs() != null)
				|| ((object1.getAttrs() != null && object2.getAttrs() != null)
						&& !object1.getAttrs().equals(object2.getAttrs()))) {
			throw new DiagramObjectNotEqualsException(
					"Attrs are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getVertices() != null && object2.getVertices() == null)
				|| (object1.getVertices() == null && object2.getVertices() != null)
				|| ((object1.getVertices() != null && object2.getVertices() != null)
						&& !object1.getVertices().equals(object2.getVertices()))) {
			throw new DiagramObjectNotEqualsException(
					"Vertices are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.isManhattan() != object2.isManhattan())) {
			throw new DiagramObjectNotEqualsException(
					"Manhattan values are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.isSmooth() != object2.isSmooth())) {
			throw new DiagramObjectNotEqualsException(
					"Smooth values are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getText() != null && object2.getText() == null)
				|| (object1.getText() == null && object2.getText() != null)
				|| ((object1.getText() != null && object2.getText() != null)
						&& !object1.getText().equals(object2.getText()))) {
			throw new DiagramObjectNotEqualsException(
					"Texts are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		compare(object1.getSource(), object2.getSource());
		compare(object1.getTarget(), object2.getTarget());
		compare(object1.getExpressionChain(), object2.getExpressionChain());

	}

	private void compare(DiagramRepeat object1, DiagramRepeat object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, SizeNotEqualsException,
			PointNotEqualsException, BiitTextNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);
	}

	private void compare(DiagramRule object1, DiagramRule object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, SizeNotEqualsException,
			PointNotEqualsException, BiitTextNotEqualsException, RuleNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);
		if ((object1.getRule() != null && object2.getRule() == null)
				|| (object1.getRule() == null && object2.getRule() != null)) {
			throw new DiagramObjectNotEqualsException(
					"Rules are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}
		compare(object1.getRule(), object2.getRule());
	}

	private void compare(DiagramSink object1, DiagramSink object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException {
		compare((DiagramExpression) object1, (DiagramExpression) object2);
	}

	private void compare(DiagramSource object1, DiagramSource object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, SizeNotEqualsException,
			PointNotEqualsException, BiitTextNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);
	}

	private void compare(DiagramTable object1, DiagramTable object2) throws StorableObjectNotEqualsException,
			DiagramObjectNotEqualsException, SizeNotEqualsException, PointNotEqualsException,
			BiitTextNotEqualsException, TableRuleNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		compare((DiagramElement) object1, (DiagramElement) object2);
		if ((object1.getTable() != null && object2.getTable() == null)
				|| (object1.getTable() == null && object2.getTable() != null)) {
			throw new DiagramObjectNotEqualsException(
					"Tables are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}
		compare(object1.getTable(), object2.getTable());
	}

	private void compare(DiagramObject object1, DiagramObject object2)
			throws StorableObjectNotEqualsException, DiagramObjectNotEqualsException, ExpressionNotEqualsException,
			CustomVariableNotEqualsException, GlobalVariableNotEqualsException, VariableDataNotEqualsException,
			NodeNotEqualsException, SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException,
			TableRuleNotEqualsException, RuleNotEqualsException, DiagramNotEqualsException {

		if (object1 == null && object2 == null) {
			return;
		}
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getParent().getName() != null && object2.getParent().getName() == null)
				|| (object1.getParent().getName() == null && object2.getParent().getName() != null)
				|| ((object1.getParent() != null && object2.getParent() != null)
						&& !object1.getParent().getName().equals(object2.getParent().getName()))) {
			throw new DiagramObjectNotEqualsException(
					"Parents are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getType() != null && object2.getType() == null)
				|| (object1.getType() == null && object2.getType() != null)
				|| ((object1.getType() != null && object2.getType() != null)
						&& !object1.getType().equals(object2.getType()))) {
			throw new DiagramObjectNotEqualsException(
					"Types are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getJointjsId() != null && object2.getJointjsId() == null)
				|| (object1.getJointjsId() == null && object2.getJointjsId() != null)
				|| ((object1.getJointjsId() != null && object2.getJointjsId() != null)
						&& !object1.getJointjsId().equals(object2.getJointjsId()))) {
			throw new DiagramObjectNotEqualsException(
					"JointJsIds are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if ((object1.getEmbeds() != null && object2.getEmbeds() == null)
				|| (object1.getEmbeds() == null && object2.getEmbeds() != null)
				|| ((object1.getEmbeds() != null && object2.getEmbeds() != null)
						&& !object1.getEmbeds().equals(object2.getEmbeds()))) {
			throw new DiagramObjectNotEqualsException(
					"Embeds are different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		if (object1.getZ() != object2.getZ()) {
			throw new DiagramObjectNotEqualsException(
					"Z is different between diagram objects '" + object1 + "' and '" + object2 + "'.");
		}

		// For each diagram element.
		if (object1 instanceof DiagramSink) {
			compare((DiagramSink) object1, (DiagramSink) object2);
		} else if (object1 instanceof DiagramSource) {
			compare((DiagramSource) object1, (DiagramSource) object2);
		} else if (object1 instanceof DiagramRepeat) {
			compare((DiagramRepeat) object1, (DiagramRepeat) object2);
		} else if (object1 instanceof DiagramFork) {
			compare((DiagramFork) object1, (DiagramFork) object2);
		} else if (object1 instanceof DiagramLink) {
			compare((DiagramLink) object1, (DiagramLink) object2);
		} else if (object1 instanceof DiagramTable) {
			compare((DiagramTable) object1, (DiagramTable) object2);
		} else if (object1 instanceof DiagramRule) {
			compare((DiagramRule) object1, (DiagramRule) object2);
		} else if (object1 instanceof DiagramChild) {
			compare((DiagramChild) object1, (DiagramChild) object2);
		} else if (object1 instanceof DiagramElement) {
			compare((DiagramElement) object1, (DiagramElement) object2);
		}

	}

	private void compare(Diagram object1, Diagram object2)
			throws StorableObjectNotEqualsException, DiagramNotEqualsException, DiagramObjectNotEqualsException,
			ExpressionNotEqualsException, CustomVariableNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, NodeNotEqualsException, SizeNotEqualsException, PointNotEqualsException,
			BiitTextNotEqualsException, TableRuleNotEqualsException, RuleNotEqualsException {
		if (object1 instanceof StorableObject || object2 instanceof StorableObject) {
			compare((StorableObject) object1, (StorableObject) object2);
		}

		if ((object1.getName() != null && object2.getName() == null)
				|| (object1.getName() == null && object2.getName() != null)
				|| ((object1.getName() != null && object2.getName() != null)
						&& !object1.getName().equals(object2.getName()))) {
			throw new DiagramNotEqualsException(
					"Names are different between diagrams '" + object1 + "' and '" + object2 + "'.");
		}

		// Compare DiagramObjects
		List<DiagramObject> diagramObjectList1 = new ArrayList<>(object1.getDiagramObjects());
		Collections.sort(diagramObjectList1, new DiagramObjectsSorter());
		List<DiagramObject> diagramObjectList2 = new ArrayList<>(object2.getDiagramObjects());
		Collections.sort(diagramObjectList2, new DiagramObjectsSorter());

		Iterator<DiagramObject> diagramObjectsIterator1 = diagramObjectList1.iterator();
		Iterator<DiagramObject> diagramObjectsIterator2 = diagramObjectList2.iterator();
		while (diagramObjectsIterator1.hasNext() || diagramObjectsIterator2.hasNext()) {
			if (diagramObjectsIterator1.hasNext() != diagramObjectsIterator2.hasNext()) {
				throw new DiagramNotEqualsException("Diagram Objects list length differs!");
			}
			DiagramObject diagramObject1 = diagramObjectsIterator1.next();
			DiagramObject diagramObject2 = diagramObjectsIterator2.next();
			// ExpressionChain inside an ExpressionChain.
			if (diagramObject1 instanceof DiagramObject) {
				compare((DiagramObject) diagramObject1, (DiagramObject) diagramObject2);
			}
		}
	}

	/**
	 * Form1 is the previous version of Form2.
	 * 
	 * @param form1 one form
	 * @param form2 other form.
	 * @throws TreeObjectNotEqualsException     form element is not equals.
	 * @throws StorableObjectNotEqualsException form element is not equals.
	 * @throws FormNotEqualsException           form element is not equals.
	 * @throws QuestionNotEqualsException       form element is not equals.
	 * @throws GroupNotEqualsException          form element is not equals.
	 * @throws CustomVariableNotEqualsException form element is not equals.
	 * @throws ExpressionNotEqualsException     form element is not equals.
	 * @throws GlobalVariableNotEqualsException form element is not equals.
	 * @throws VariableDataNotEqualsException   form element is not equals.
	 * @throws TableRuleNotEqualsException      form element is not equals.
	 * @throws RuleNotEqualsException           form element is not equals.
	 * @throws DiagramNotEqualsException        form element is not equals.
	 * @throws DiagramObjectNotEqualsException  form element is not equals.
	 * @throws NodeNotEqualsException           form element is not equals.
	 * @throws SizeNotEqualsException           form element is not equals.
	 * @throws PointNotEqualsException          form element is not equals.
	 * @throws BiitTextNotEqualsException       form element is not equals.
	 */
	public void compare(Form form1, Form form2) throws TreeObjectNotEqualsException, StorableObjectNotEqualsException,
			FormNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException,
			CustomVariableNotEqualsException, ExpressionNotEqualsException, GlobalVariableNotEqualsException,
			VariableDataNotEqualsException, TableRuleNotEqualsException, RuleNotEqualsException,
			DiagramNotEqualsException, DiagramObjectNotEqualsException, NodeNotEqualsException, SizeNotEqualsException,
			PointNotEqualsException, BiitTextNotEqualsException {
		if ((form1 == null || form2 == null) && (form1 != null || form2 != null)) {
			throw new FormNotEqualsException("Obtained form is null");
		}

		if (form1 == null || form2 == null) {
			throw new FormNotEqualsException("Cannot compare '" + form1 + "' with '" + form2 + "'.");
		}

		if (form1.getChildren().size() != form2.getChildren().size()) {
			throw new FormNotEqualsException("Form has different children size!");
		}
		if (!form1.getName().equals(form2.getName())) {
			throw new FormNotEqualsException("Form has different name!");
		}
		if (!form1.getLabel().equals(form2.getLabel())) {
			throw new FormNotEqualsException("Form has different label!");
		}
		if (!form1.getStatus().equals(form2.getStatus())) {
			throw new FormNotEqualsException("Form has different status!");
		}

		compareVersions(form1, form2);

		if (!Objects.equals(form1.getOrganizationId(), form2.getOrganizationId())) {
			throw new FormNotEqualsException("Form's organizations are different!");
		}

		compareFormDates(form1, form2);

		// Compare treeObject hierarchy.
		for (int i = 0; i < form2.getChildren().size(); i++) {
			compare(form1.getChildren().get(i), form2.getChildren().get(i));
		}

		// Compare custom variables. First sort the set to be sure the
		// comparation is correct.
		List<CustomVariable> customVariables1 = new ArrayList<>(form1.getCustomVariables());
		Collections.sort(customVariables1, new CustomVariableSorter());
		List<CustomVariable> customVariables2 = new ArrayList<>(form2.getCustomVariables());
		Collections.sort(customVariables2, new CustomVariableSorter());
		Iterator<CustomVariable> variableIterator1 = customVariables1.iterator();
		Iterator<CustomVariable> variableIterator2 = customVariables2.iterator();
		while (variableIterator1.hasNext() || variableIterator2.hasNext()) {
			if (variableIterator1.hasNext() != variableIterator2.hasNext()) {
				throw new CustomVariableNotEqualsException("CustomVariables list length differs!");
			}
			compare(variableIterator1.next(), variableIterator2.next());
		}

		// Compare Expressions. First sort the set to be sure the comparation is
		// correct.
		List<ExpressionChain> expressionChain1 = new ArrayList<>(form1.getExpressionChains());
		Collections.sort(expressionChain1, new ExpressionSorter());
		List<ExpressionChain> expressionChain2 = new ArrayList<>(form2.getExpressionChains());
		Collections.sort(expressionChain2, new ExpressionSorter());
		Iterator<ExpressionChain> expressionsIterator1 = expressionChain1.iterator();
		Iterator<ExpressionChain> expressionsIterator2 = expressionChain2.iterator();
		while (expressionsIterator1.hasNext() || expressionsIterator2.hasNext()) {
			if (expressionsIterator1.hasNext() != expressionsIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("ExpressionChain list length differs!");
			}
			compare(expressionsIterator1.next(), expressionsIterator2.next());
		}

		// Compare TableRules
		List<TableRule> tableRules1 = new ArrayList<>(form1.getTableRules());
		Collections.sort(tableRules1, new TableRulesSorter());
		List<TableRule> tableRules2 = new ArrayList<>(form2.getTableRules());
		Collections.sort(tableRules2, new TableRulesSorter());
		Iterator<TableRule> tableRulesIterator1 = tableRules1.iterator();
		Iterator<TableRule> tableRulesIterator2 = tableRules2.iterator();
		while (tableRulesIterator1.hasNext() || tableRulesIterator2.hasNext()) {
			if (tableRulesIterator1.hasNext() != tableRulesIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("TableRules list length differs!");
			}
			compare(tableRulesIterator1.next(), tableRulesIterator2.next());
		}

		// Compare Rules
		List<Rule> rules1 = new ArrayList<>(form1.getRules());
		Collections.sort(rules1, new RulesSorter());
		List<Rule> rules2 = new ArrayList<>(form2.getRules());
		Collections.sort(rules2, new RulesSorter());
		Iterator<Rule> rulesIterator1 = rules1.iterator();
		Iterator<Rule> rulesIterator2 = rules2.iterator();
		while (rulesIterator1.hasNext() || rulesIterator2.hasNext()) {
			if (rulesIterator1.hasNext() != rulesIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("TableRules list length differs!");
			}
			compare(rulesIterator1.next(), rulesIterator2.next());
		}

		// Compare Diagrams
		List<Diagram> diagrams1 = new ArrayList<>(form1.getDiagrams());
		Collections.sort(diagrams1, new DiagramSorter());
		List<Diagram> diagrams2 = new ArrayList<>(form2.getDiagrams());
		Collections.sort(diagrams2, new DiagramSorter());
		Iterator<Diagram> diagramIterator1 = diagrams1.iterator();
		Iterator<Diagram> diagramIterator2 = diagrams2.iterator();
		while (diagramIterator1.hasNext() || diagramIterator2.hasNext()) {
			if (diagramIterator1.hasNext() != diagramIterator2.hasNext()) {
				throw new ExpressionNotEqualsException("TableRules list length differs!");
			}
			compare(diagramIterator1.next(), diagramIterator2.next());
		}
	}

	protected void compareVersions(Form form1, Form form2) throws FormNotEqualsException {
		if (form1.getVersion() != (int) form2.getVersion()) {
			throw new FormNotEqualsException("Form's versions are incorrect!");
		}
	}

	protected void compareFormDates(Form form1, Form form2) throws FormNotEqualsException {
		if ((form1.getAvailableTo() == null && form2.getAvailableTo() != null)
				|| (form1.getAvailableTo() != null && form2.getAvailableTo() == null)
				|| ((form1.getAvailableTo() != null && form2.getAvailableTo() != null)
						&& !form1.getAvailableTo().equals(form2.getAvailableTo()))) {
			throw new FormNotEqualsException("Form's validTo dates are different!");
		}

		if ((form1.getAvailableFrom() == null && form2.getAvailableFrom() != null)
				|| (form1.getAvailableFrom() != null && form2.getAvailableFrom() == null)
				|| ((form1.getAvailableFrom() != null && form2.getAvailableFrom() != null)
						&& !form1.getAvailableFrom().equals(form2.getAvailableFrom()))) {
			throw new FormNotEqualsException("Form's validFrom dates are different!");
		}
	}
}

class ExpressionSorter implements Comparator<ExpressionChain>, Serializable {
	private static final long serialVersionUID = 653352633255236891L;

	@Override
	public int compare(ExpressionChain arg0, ExpressionChain arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class CustomVariableSorter implements Comparator<CustomVariable>, Serializable {
	private static final long serialVersionUID = -992461663716218261L;

	@Override
	public int compare(CustomVariable arg0, CustomVariable arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class TableRulesSorter implements Comparator<TableRule>, Serializable {
	private static final long serialVersionUID = -4817850521307190863L;

	@Override
	public int compare(TableRule arg0, TableRule arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class RulesSorter implements Comparator<Rule>, Serializable {
	private static final long serialVersionUID = 2310666384054474895L;

	@Override
	public int compare(Rule arg0, Rule arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class DiagramSorter implements Comparator<Diagram>, Serializable {
	private static final long serialVersionUID = -8256559433128997978L;

	@Override
	public int compare(Diagram arg0, Diagram arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

class DiagramObjectsSorter implements Comparator<DiagramObject>, Serializable {
	private static final long serialVersionUID = -5659218882895088689L;

	@Override
	public int compare(DiagramObject arg0, DiagramObject arg1) {
		return arg0.getJointjsId().compareTo(arg1.getJointjsId());
	}
}
