package com.biit.abcd.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import com.biit.abcd.core.utils.TableRuleUtils;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class FormUtils {
	private static HashMap<String, CustomVariable> variableMap = new HashMap<>();
	private static HashMap<String, TableRule> tableMap = new HashMap<>();
	private static HashMap<String, ExpressionChain> expressionsMap = new HashMap<>();
	private static HashMap<String, TreeObject> elementsMap = new HashMap<>();
	private static HashMap<String, Rule> rulesMap = new HashMap<>();
	private static Random random = new Random();

	public static Form createCompleteForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(randomName("Form"));

		addFormStructure(form);
		addFormCustomVariables(form);
		addFormExpressions(form);
		addFormTableRules(form);
		addFormRules(form);

		return form;
	}

	public static void addFormStructure(Form form) throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException {
		elementsMap = new HashMap<>();
		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);
		elementsMap.put("Category1", category);

		Category category2 = new Category();
		category2.setName("Category2");
		form.addChild(category2);
		elementsMap.put("Category2", category2);

		Category category3 = new Category();
		category3.setName("Category3");
		form.addChild(category3);
		elementsMap.put("Category3", category3);

		Group group1 = new Group();
		group1.setName("Group1");
		category2.addChild(group1);
		elementsMap.put("Group1", group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category2.addChild(group2);
		elementsMap.put("Group2", group2);

		Group group3 = new Group();
		group3.setName("Group3");
		category2.addChild(group3);
		elementsMap.put("Group3", group3);

		// Input field text.
		Question question1 = new Question();
		question1.setName("InsertText");
		question1.setAnswerType(AnswerType.INPUT);
		question1.setAnswerFormat(AnswerFormat.TEXT);
		group2.addChild(question1);
		elementsMap.put("InsertText", question1);

		// Radio Button
		Question question2 = new Question();
		question2.setName("ChooseOne");
		question2.setAnswerType(AnswerType.RADIO);
		group2.addChild(question2);
		elementsMap.put("ChooseOne", question2);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question2.addChild(answer1);
		elementsMap.put("Answer1", answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);
		elementsMap.put("Answer2", answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);
		elementsMap.put("Answer3", answer3);

		Answer answer4 = new Answer();
		answer4.setName("Answer4");
		question2.addChild(answer4);
		elementsMap.put("Answer4", answer4);

		// Date
		Question question3 = new Question();
		question3.setName("InsertDate");
		question1.setAnswerType(AnswerType.INPUT);
		question1.setAnswerFormat(AnswerFormat.DATE);
		group2.addChild(question3);
		elementsMap.put("InsertDate", question3);

		// Radio Button
		Question question4 = new Question();
		question4.setName("ChooseMore");
		question4.setAnswerType(AnswerType.MULTI_CHECKBOX);
		group2.addChild(question4);
		elementsMap.put("ChooseMore", question4);

		Answer answer5 = new Answer();
		answer5.setName("Answer5");
		question4.addChild(answer5);

		Answer answer6 = new Answer();
		answer6.setName("Answer6");
		question4.addChild(answer6);

		Answer answer7 = new Answer();
		answer7.setName("Answer7");
		question4.addChild(answer7);
	}

	private static void addFormCustomVariables(Form form) {
		variableMap = new HashMap<>();
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		form.getCustomVariables().add(customVarCategory);
		variableMap.put("cScore", customVarCategory);

		CustomVariable customVarQuestion = new CustomVariable(form, "bonus", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		form.getCustomVariables().add(customVarQuestion);
		variableMap.put("bonus", customVarQuestion);
	}

	private static void addFormExpressions(Form form) {
		ExpressionChain expressionChain = new ExpressionChain();
		expressionChain.setName("Expression1");
		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(elementsMap.get("Category1"),
				variableMap.get("cScore"));
		// Category.Score=1+1;
		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueNumber(1d));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionValueNumber(1d));
		form.getExpressionChains().add(expressionChain);
		expressionsMap.put("Expression1", expressionChain);

		// Category2.bonus=InsertDate(Y)
		ExpressionChain expressionChain2 = new ExpressionChain();
		expressionChain2.setName("Expression2");
		ExpressionValueCustomVariable customVariable2 = new ExpressionValueCustomVariable(elementsMap.get("Category2"),
				variableMap.get("bonus"));
		expressionChain2.addExpression(customVariable2);
		expressionChain2.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain2.addExpression(new ExpressionValueTreeObjectReference(elementsMap.get("InsertDate"),
				QuestionDateUnit.YEARS));
		form.getExpressionChains().add(expressionChain2);
		expressionsMap.put("Expression2", expressionChain2);
	}

	private static void addFormTableRules(Form form) {
		TableRule tableRule = new TableRule();
		tableRule.setName("Table1");

		TableRuleRow tableRuleRow1 = new TableRuleRow();
		// Question1=Answer1 -> Category1.score=Category1.score+1;
		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueTreeObjectReference questionReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("ChooseOne"));
		expressionChain.addExpression(questionReference);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		ExpressionValueTreeObjectReference answerReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("Answer1"));
		expressionChain.addExpression(answerReference);
		tableRuleRow1.getConditions().getExpressions().add(expressionChain);

		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(elementsMap.get("Category1"),
				variableMap.get("cScore"));
		// Category1.score=Category1.score+1;
		expressionChain = new ExpressionChain();
		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueCustomVariable(elementsMap.get("Category1"), variableMap
				.get("cScore")));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionValueNumber(1d));
		tableRuleRow1.getAction().addExpression(expressionChain);
		tableRule.getRules().add(tableRuleRow1);

		// Copy row (total 2 rows).
		Collection<TableRuleRow> rowsToCopy = tableRule.getRules();
		List<TableRuleRow> copiedRows = TableRuleUtils.copyTableRuleRows(tableRule, rowsToCopy);
		TableRuleUtils.pasteTableRuleRows(tableRule, copiedRows);
		Assert.assertEquals(2, tableRule.getRules().size());

		// Copy rows (total 4 rows).
		rowsToCopy = tableRule.getRules();
		copiedRows = TableRuleUtils.copyTableRuleRows(tableRule, rowsToCopy);
		TableRuleUtils.pasteTableRuleRows(tableRule, copiedRows);
		Assert.assertEquals(4, tableRule.getRules().size());

		// Modify rules.
		int i = 1;
		for (TableRuleRow tableRuleRow : tableRule.getRules()) {
			// Question1=AnswerX
			((ExpressionValueTreeObjectReference) ((ExpressionChain) tableRuleRow.getConditions().getExpressions().get(0))
					.getExpressions().get(2)).setReference(elementsMap.get("Answer" + i));
			// Category1.score=Category1.score+X;
			((ExpressionValueNumber) ((ExpressionChain) tableRuleRow.getAction().getExpressions().get(0))
					.getExpressions().get(4)).setValue(i);
			i++;
		}

		form.getTableRules().add(tableRule);
		tableMap.put("Table1", tableRule);
	}

	private static void addFormRules(Form form) {
		Rule rule = new Rule();
		rule.setName("rule1");

		// Condition Question1=Answer1
		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueTreeObjectReference questionReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("ChooseOne"));
		expressionChain.addExpression(questionReference);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		ExpressionValueTreeObjectReference answerReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("Answer1"));
		expressionChain.addExpression(answerReference);
		rule.setCondition(expressionChain);

		
		//Action Category1.score=Category1.score+1;
		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(elementsMap.get("Category1"),
				variableMap.get("cScore"));
		// Category1.score=Category1.score+1;
		expressionChain = new ExpressionChain();
		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueCustomVariable(elementsMap.get("Category1"), variableMap
				.get("cScore")));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionValueNumber(1d));
		rule.setActions(expressionChain);
		
		form.getRules().add(rule);
		rulesMap.put("rule1", rule);
	}

	private static String randomName(String prefix) {
		return prefix + Long.toString(random.nextLong(), 36);
	}
}
