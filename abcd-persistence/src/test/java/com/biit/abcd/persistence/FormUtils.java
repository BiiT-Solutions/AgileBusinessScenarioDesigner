package com.biit.abcd.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

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
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
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
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class FormUtils {
	private static Random random = new Random();

	public static Form createCompleteForm() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException {
		Map<String, CustomVariable> variableMap;
		Map<String, TableRule> tablesMap;
		Map<String, ExpressionChain> expressionsMap;
		Map<String, TreeObject> elementsMap;
		Map<String, Rule> rulesMap;
		Map<String, Diagram> diagramsMap;

		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(randomName("Form"));

		elementsMap = createCategory();
		form.addChild(elementsMap.get("Category1"));

		variableMap = addFormCustomVariables(form);
		for (CustomVariable variable : variableMap.values()) {
			form.getCustomVariables().add(variable);
		}

		expressionsMap = addFormExpressions(elementsMap, variableMap);
		for (ExpressionChain expression : expressionsMap.values()) {
			form.getExpressionChains().add(expression);
		}

		tablesMap = addFormTableRules(elementsMap, variableMap);
		for (TableRule tableRule : tablesMap.values()) {
			form.getTableRules().add(tableRule);
		}

		rulesMap = addFormRules(elementsMap, variableMap);
		for (Rule rule : rulesMap.values()) {
			form.getRules().add(rule);
		}

		diagramsMap = createComplexDiagram(elementsMap, variableMap, expressionsMap, tablesMap, rulesMap);
		for (Diagram diagram : diagramsMap.values()) {
			form.getDiagrams().add(diagram);
		}

		return form;
	}

	/**
	 * Create a Category called "Category1" with different groups and questions.
	 * 
	 * @return a hashmap with all elements.
	 * @throws NotValidChildException
	 * @throws FieldTooLongException
	 * @throws CharacterNotAllowedException
	 * @throws InvalidAnswerFormatException
	 */
	public static Map<String, TreeObject> createCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException {
		Map<String, TreeObject> elementsMap = new HashMap<>();
		Category category = new Category();
		category.setName("Category1");
		elementsMap.put("Category1", category);

		Group group1 = new Group();
		group1.setName("Group1");
		category.addChild(group1);
		elementsMap.put("Group1", group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category.addChild(group2);
		elementsMap.put("Group2", group2);

		Group group3 = new Group();
		group3.setName("Group3");
		category.addChild(group3);
		elementsMap.put("Group3", group3);

		Group group4 = new Group();
		group4.setName("Group4");
		category.addChild(group4);
		elementsMap.put("Group4", group4);

		Group group5 = new Group();
		group5.setName("Group5");
		category.addChild(group5);
		elementsMap.put("Group5", group5);

		Group group6 = new Group();
		group6.setName("Group6");
		category.addChild(group6);
		elementsMap.put("Group6", group6);

		Group group7 = new Group();
		group7.setName("Group7");
		category.addChild(group7);
		elementsMap.put("Group7", group7);

		Group group8 = new Group();
		group8.setName("Group8");
		category.addChild(group8);
		elementsMap.put("Group8", group8);

		Group group9 = new Group();
		group9.setName("Group9");
		category.addChild(group9);
		elementsMap.put("Group9", group9);

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

		Question question3 = new Question();
		question3.setName("question3");
		question3.setAnswerType(AnswerType.INPUT);
		group2.addChild(question3);
		elementsMap.put("question3", question3);

		Question question4 = new Question();
		question4.setName("question4");
		question4.setAnswerType(AnswerType.INPUT);
		group2.addChild(question4);
		elementsMap.put("question4", question4);

		Question question5 = new Question();
		question5.setName("question5");
		question5.setAnswerType(AnswerType.INPUT);
		group2.addChild(question5);
		elementsMap.put("question5", question5);

		Question question6 = new Question();
		question6.setName("question6");
		question6.setAnswerType(AnswerType.INPUT);
		group2.addChild(question6);
		elementsMap.put("question6", question6);

		Question question7 = new Question();
		question7.setName("question7");
		question7.setAnswerType(AnswerType.INPUT);
		group2.addChild(question7);
		elementsMap.put("question7", question7);

		Question question8 = new Question();
		question8.setName("question8");
		question8.setAnswerType(AnswerType.INPUT);
		group2.addChild(question8);
		elementsMap.put("question8", question8);

		Question question9 = new Question();
		question9.setName("question9");
		question9.setAnswerType(AnswerType.INPUT);
		group2.addChild(question9);
		elementsMap.put("question9", question9);

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
		Question insertDate = new Question();
		insertDate.setName("InsertDate");
		question1.setAnswerType(AnswerType.INPUT);
		question1.setAnswerFormat(AnswerFormat.DATE);
		group2.addChild(insertDate);
		elementsMap.put("InsertDate", insertDate);

		// Radio Button
		Question chooseMore = new Question();
		chooseMore.setName("ChooseMore");
		chooseMore.setAnswerType(AnswerType.MULTI_CHECKBOX);
		group2.addChild(chooseMore);
		elementsMap.put("ChooseMore", chooseMore);

		Answer answer5 = new Answer();
		answer5.setName("Answer5");
		chooseMore.addChild(answer5);

		Answer answer6 = new Answer();
		answer6.setName("Answer6");
		chooseMore.addChild(answer6);

		Answer answer7 = new Answer();
		answer7.setName("Answer7");
		chooseMore.addChild(answer7);

		Answer answer8 = new Answer();
		answer8.setName("answer8");
		chooseMore.addChild(answer8);

		Answer answer9 = new Answer();
		answer9.setName("Answer9");
		chooseMore.addChild(answer9);

		Answer answer10 = new Answer();
		answer10.setName("Answer10");
		chooseMore.addChild(answer10);

		Answer answer11 = new Answer();
		answer11.setName("Answer11");
		chooseMore.addChild(answer11);

		Answer answer12 = new Answer();
		answer12.setName("Answer12");
		chooseMore.addChild(answer12);

		return elementsMap;
	}

	public static Map<String, CustomVariable> addFormCustomVariables(Form form) {
		Map<String, CustomVariable> variableMap = new HashMap<>();
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		variableMap.put("cScore", customVarCategory);

		CustomVariable customVarQuestion = new CustomVariable(form, "bonus", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		variableMap.put("bonus", customVarQuestion);
		return variableMap;
	}

	public static Map<String, ExpressionChain> addFormExpressions(Map<String, TreeObject> elementsMap,
			Map<String, CustomVariable> variableMap) {
		Map<String, ExpressionChain> expressionsMap = new HashMap<>();
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
		expressionsMap.put("Expression1", expressionChain);

		// Category1.bonus=InsertDate(Y)
		ExpressionChain expressionChain2 = new ExpressionChain();
		expressionChain2.setName("Expression2");
		ExpressionValueCustomVariable customVariable2 = new ExpressionValueCustomVariable(elementsMap.get("Category1"),
				variableMap.get("bonus"));
		expressionChain2.addExpression(customVariable2);
		expressionChain2.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain2.addExpression(new ExpressionValueTreeObjectReference(elementsMap.get("InsertDate"),
				QuestionDateUnit.YEARS));
		expressionsMap.put("Expression2", expressionChain2);

		return expressionsMap;
	}

	public static Map<String, TableRule> addFormTableRules(Map<String, TreeObject> elementsMap,
			Map<String, CustomVariable> variableMap) {
		Map<String, TableRule> tablesMap = new HashMap<>();

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
		tableRuleRow1.getConditions().addExpression(expressionChain);

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
			((ExpressionValueTreeObjectReference) ((ExpressionChain) tableRuleRow.getConditions().getExpressions()
					.get(0)).getExpressions().get(2)).setReference(elementsMap.get("Answer" + i));
			// Category1.score=Category1.score+X;
			((ExpressionValueNumber) ((ExpressionChain) tableRuleRow.getAction().getExpressions().get(0))
					.getExpressions().get(4)).setValue(new Double(i));
			i++;
		}

		tablesMap.put("table1", tableRule);

		return tablesMap;
	}

	public static Map<String, Rule> addFormRules(Map<String, TreeObject> elementsMap,
			Map<String, CustomVariable> variableMap) {
		Map<String, Rule> rulesMap = new HashMap<>();

		Rule rule = new Rule();
		rule.setName("Rule1");

		// Condition Question1=Answer1
		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueTreeObjectReference questionReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("ChooseOne"));
		expressionChain.addExpression(questionReference);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		ExpressionValueTreeObjectReference answerReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("Answer1"));
		expressionChain.addExpression(answerReference);
		rule.setConditions(expressionChain);

		// Action Category1.score=Category1.score+1;
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

		rulesMap.put(rule.getName(), rule);
		return rulesMap;
	}

	/*-
	 *                             |-- (Answer1) -->   Table1    --> End1
	 * Start --> Fork (question2) -|
	 *                             |-- (Others)  --> Expression1 --> Diagram2 --> End2
	 * 
	 * @param form
	 */
	public static Map<String, Diagram> createComplexDiagram(Map<String, TreeObject> elementsMap,
			Map<String, CustomVariable> variableMap, Map<String, ExpressionChain> expressionsMap,
			Map<String, TableRule> tablesMap, Map<String, Rule> rulesMap) {
		Map<String, Diagram> diagramsMap = new HashMap<>();

		Diagram diagram = new Diagram("diagram1");

		DiagramSource startNode = new DiagramSource();
		startNode.setJointjsId(IdGenerator.createId());
		startNode.setType(DiagramObjectType.SOURCE);
		startNode.setSize(new Size(1, 1));
		startNode.setPosition(new Point(1, 1));
		Node nodeSource = new Node(startNode.getJointjsId());
		diagram.addDiagramObject(startNode);

		DiagramFork forkNode = new DiagramFork();
		ExpressionValueTreeObjectReference questionReference = new ExpressionValueTreeObjectReference(
				elementsMap.get("ChooseOne"));
		forkNode.setReference(questionReference);
		forkNode.setJointjsId(IdGenerator.createId());
		forkNode.setSize(new Size(2, 2));
		forkNode.setPosition(new Point(1, 1));
		forkNode.setType(DiagramObjectType.FORK);
		Node nodeFork = new Node(forkNode.getJointjsId());
		diagram.addDiagramObject(forkNode);

		DiagramLink startLink = new DiagramLink();
		startLink.setSource(nodeSource);
		startLink.setJointjsId(IdGenerator.createId());
		startLink.setType(DiagramObjectType.LINK);
		startLink.setTarget(nodeFork);
		diagram.addDiagramObject(startLink);

		DiagramTable table1Node = new DiagramTable();
		table1Node.setTable(tablesMap.get("table1"));
		table1Node.setJointjsId(IdGenerator.createId());
		table1Node.setSize(new Size(3, 3));
		table1Node.setPosition(new Point(1, 1));
		table1Node.setType(DiagramObjectType.TABLE);
		Node nodeTable = new Node(table1Node.getJointjsId());
		diagram.addDiagramObject(table1Node);

		DiagramLink answer1Link = new DiagramLink();
		answer1Link.setSource(nodeFork);
		answer1Link.setJointjsId(IdGenerator.createId());
		answer1Link.setType(DiagramObjectType.LINK);
		answer1Link.setTarget(nodeTable);
		diagram.addDiagramObject(answer1Link);

		DiagramSink diagramEndNode1 = new DiagramSink();
		diagramEndNode1.setJointjsId(IdGenerator.createId());
		diagramEndNode1.setType(DiagramObjectType.SINK);
		diagramEndNode1.setSize(new Size(3, 3));
		diagramEndNode1.setPosition(new Point(1, 1));
		Node nodeSink1 = new Node(diagramEndNode1.getJointjsId());
		diagram.addDiagramObject(diagramEndNode1);

		DiagramLink tableLink = new DiagramLink();
		tableLink.setSource(nodeTable);
		tableLink.setJointjsId(IdGenerator.createId());
		tableLink.setType(DiagramObjectType.LINK);
		tableLink.setTarget(nodeSink1);
		diagram.addDiagramObject(tableLink);

		DiagramExpression expressionNode = new DiagramExpression();
		expressionNode.setExpression(expressionsMap.get("Expression1"));
		expressionNode.setJointjsId(IdGenerator.createId());
		expressionNode.setSize(new Size(4, 4));
		expressionNode.setPosition(new Point(1, 1));
		expressionNode.setType(DiagramObjectType.CALCULATION);
		Node nodeExpression = new Node(expressionNode.getJointjsId());
		diagram.addDiagramObject(expressionNode);

		DiagramLink answer2Link = new DiagramLink();
		answer2Link.setSource(nodeFork);
		answer2Link.setJointjsId(IdGenerator.createId());
		answer2Link.setType(DiagramObjectType.LINK);
		answer2Link.setTarget(nodeExpression);
		diagram.addDiagramObject(answer2Link);

		diagramsMap.putAll(createSimpleDiagram(rulesMap));
		DiagramChild subDiagramNode = new DiagramChild();
		subDiagramNode.setDiagram(diagramsMap.get("Diagram2"));
		subDiagramNode.setJointjsId(IdGenerator.createId());
		subDiagramNode.setSize(new Size(5, 5));
		subDiagramNode.setPosition(new Point(1, 1));
		subDiagramNode.setType(DiagramObjectType.SINK);
		Node diagramNode = new Node(subDiagramNode.getJointjsId());
		diagram.addDiagramObject(subDiagramNode);

		DiagramLink diagramLink = new DiagramLink();
		diagramLink.setSource(nodeExpression);
		diagramLink.setJointjsId(IdGenerator.createId());
		diagramLink.setType(DiagramObjectType.LINK);
		diagramLink.setTarget(diagramNode);
		diagram.addDiagramObject(diagramLink);

		DiagramSink diagramEndNode2 = new DiagramSink();
		diagramEndNode2.setJointjsId(IdGenerator.createId());
		diagramEndNode2.setType(DiagramObjectType.SINK);
		diagramEndNode2.setSize(new Size(6, 6));
		diagramEndNode2.setPosition(new Point(1, 1));
		Node nodeSink2 = new Node(diagramEndNode2.getJointjsId());
		diagram.addDiagramObject(diagramEndNode2);

		DiagramLink expressionLink = new DiagramLink();
		expressionLink.setSource(diagramNode);
		expressionLink.setJointjsId(IdGenerator.createId());
		expressionLink.setType(DiagramObjectType.LINK);
		expressionLink.setTarget(nodeSink2);
		diagram.addDiagramObject(expressionLink);

		diagramsMap.put(diagram.getName(), diagram);
		return diagramsMap;
	}

	/*-
	 * Start --> Rule1 --> End
	 */
	public static Map<String, Diagram> createSimpleDiagram(Map<String, Rule> rulesMap) {
		Map<String, Diagram> diagramsMap = new HashMap<>();
		Diagram diagram = new Diagram("Diagram2");

		DiagramSource startNode = new DiagramSource();
		startNode.setJointjsId(IdGenerator.createId());
		startNode.setType(DiagramObjectType.SOURCE);
		startNode.setSize(new Size(1, 1));
		startNode.setPosition(new Point(1, 1));
		Node nodeSource = new Node(startNode.getJointjsId());
		diagram.addDiagramObject(startNode);

		DiagramRule ruleNode = new DiagramRule();
		ruleNode.setRule(rulesMap.get("Rule1"));
		ruleNode.setJointjsId(IdGenerator.createId());
		ruleNode.setSize(new Size(2, 2));
		ruleNode.setPosition(new Point(1, 1));
		ruleNode.setType(DiagramObjectType.RULE);
		Node nodeRule = new Node(ruleNode.getJointjsId());
		diagram.addDiagramObject(ruleNode);

		DiagramLink startLink = new DiagramLink();
		startLink.setSource(nodeSource);
		startLink.setJointjsId(IdGenerator.createId());
		startLink.setType(DiagramObjectType.LINK);
		startLink.setTarget(nodeRule);
		diagram.addDiagramObject(startLink);

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setSize(new Size(3, 3));
		diagramEndNode.setPosition(new Point(1, 1));
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());
		diagram.addDiagramObject(diagramEndNode);

		DiagramLink expressionLink = new DiagramLink();
		expressionLink.setSource(nodeRule);
		expressionLink.setJointjsId(IdGenerator.createId());
		expressionLink.setType(DiagramObjectType.LINK);
		expressionLink.setTarget(nodeSink);
		diagram.addDiagramObject(expressionLink);

		diagramsMap.put(diagram.getName(), diagram);
		return diagramsMap;
	}

	private static String randomName(String prefix) {
		return prefix + Long.toString(random.nextLong(), 36);
	}
}
