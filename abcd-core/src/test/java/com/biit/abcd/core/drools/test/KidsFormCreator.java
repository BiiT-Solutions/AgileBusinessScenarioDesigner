package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.importer.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
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
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class KidsFormCreator {

	private final static String APP = "Application1";
	protected final static String CATEGORY_NAME = "Algemeen";
	private final static String FORM = "Form1";

	private CustomVariable formNumberCustomVariable = null;
	private CustomVariable formTextCustomVariable = null;
	private CustomVariable categoryNumberCustomVariable = null;
	private CustomVariable groupNumberCustomVariable = null;
	private CustomVariable questionNumberCustomVariable = null;
	private Form form = null;
	private Category category = null;
	private Group group = null;
	private Question question = null;

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private List<DiagramElement> diagramElements = null;
	private GlobalVariable globalVariableNumber = null;
	private List<GlobalVariable> globalVariables = null;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	private ISubmittedForm submittedForm;

	public DroolsForm createAndRunDroolsRules() {
		// Generate the drools rules.
		try {
			FormToDroolsExporter formDrools = new FormToDroolsExporter();
			DroolsRulesGenerator rulesGenerator = formDrools.generateDroolRules(getForm(), getGlobalVariables());
			readStaticSubmittedForm();
			// Test the rules with the submitted form and returns a DroolsForm
			return formDrools.applyDrools(getSubmittedForm(), rulesGenerator.getRules(),
					rulesGenerator.getGlobalVariables());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a category custom variable and assigns a value of 10
	 * 
	 * @param customVariableName
	 */
	protected void createCategoryNumberCustomVariableExpression(String customVariableName) {
		categoryNumberCustomVariable = new CustomVariable(getForm(), customVariableName, CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		ExpressionChain expression = new ExpressionChain(customVariableName, new ExpressionValueCustomVariable(
				getCategory(), categoryNumberCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(10.));
		getForm().getExpressionChains().add(expression);
		createExpressionNode(expression);
	}

	/**
	 * Creates a group custom variable and assigns a value of 10
	 * 
	 * @param customVariableName
	 */
	protected void createGroupNumberCustomVariableExpression(String customVariableName) {
		groupNumberCustomVariable = new CustomVariable(getForm(), customVariableName, CustomVariableType.NUMBER,
				CustomVariableScope.GROUP);
		ExpressionChain expression = new ExpressionChain(customVariableName, new ExpressionValueCustomVariable(
				getGroup(), groupNumberCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(10.));
		getForm().getExpressionChains().add(expression);
		createExpressionNode(expression);
	}

	/**
	 * Creates a question custom variable and assigns a value of 10
	 * 
	 * @param customVariableName
	 */
	protected void createQuestionNumberCustomVariableExpression(String customVariableName) {
		questionNumberCustomVariable = new CustomVariable(getForm(), customVariableName, CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		ExpressionChain expression = new ExpressionChain(customVariableName, new ExpressionValueCustomVariable(
				getQuestion(), questionNumberCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(10.));
		getForm().getExpressionChains().add(expression);
		createExpressionNode(expression);
	}

	/**
	 * Creates a diagram based in the elements of the arra diagramElements
	 * 
	 */
	protected void createDiagram() {
		Diagram mainDiagram = new Diagram("main");

		// Create the nodes of the diagram
		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());
		mainDiagram.addDiagramObject(diagramStartNode);

		List<Node> diagramNodes = new ArrayList<Node>();
		for (DiagramElement element : diagramElements) {
			diagramNodes.add(new Node(element.getJointjsId()));
			mainDiagram.addDiagramObject(element);
		}

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());
		mainDiagram.addDiagramObject(diagramEndNode);

		// Create the links between nodes
		if (!diagramNodes.isEmpty()) {
			DiagramLink start_firstNode = new DiagramLink(nodeSource, diagramNodes.get(0));
			start_firstNode.setJointjsId(IdGenerator.createId());
			start_firstNode.setType(DiagramObjectType.LINK);
			mainDiagram.addDiagramObject(start_firstNode);

			for (int i = 1; i < diagramNodes.size(); i++) {
				DiagramLink node_node = new DiagramLink(diagramNodes.get(i - 1), diagramNodes.get(i));
				node_node.setJointjsId(IdGenerator.createId());
				node_node.setType(DiagramObjectType.LINK);
				mainDiagram.addDiagramObject(node_node);
			}

			DiagramLink lastNode_End = new DiagramLink(diagramNodes.get(diagramNodes.size() - 1), nodeSink);
			lastNode_End.setJointjsId(IdGenerator.createId());
			lastNode_End.setType(DiagramObjectType.LINK);
			mainDiagram.addDiagramObject(lastNode_End);
		}

		getForm().addDiagram(mainDiagram);
	}

	/**
	 * Adds a new expression element to the diagram
	 * 
	 * @param tableRule
	 */
	protected void createExpressionNode(ExpressionChain expressionChain) {
		DiagramExpression diagramExpression = new DiagramExpression();
		diagramExpression.setExpression(expressionChain);
		diagramExpression.setJointjsId(IdGenerator.createId());
		diagramExpression.setType(DiagramObjectType.CALCULATION);
		diagramElements.add(diagramExpression);
	}

	protected void createRuleNode(Rule rule) {
		DiagramRule diagramRule = new DiagramRule();
		diagramRule.setRule(rule);
		diagramRule.setJointjsId(IdGenerator.createId());
		diagramRule.setType(DiagramObjectType.RULE);
		diagramElements.add(diagramRule);
	}

	protected Diagram createExpressionsDiagram() {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramChild subDiagramExpressionNode = new DiagramChild();
		subDiagramExpressionNode.setDiagram(createExpressionsSubdiagram());
		subDiagramExpressionNode.setJointjsId(IdGenerator.createId());
		subDiagramExpressionNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeTable = new Node(subDiagramExpressionNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startExpression = new DiagramLink(nodeSource, nodeTable);
		startExpression.setJointjsId(IdGenerator.createId());
		startExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionEnd = new DiagramLink(nodeTable, nodeSink);
		expressionEnd.setJointjsId(IdGenerator.createId());
		expressionEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(subDiagramExpressionNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startExpression);
		mainDiagram.addDiagramObject(expressionEnd);

		return mainDiagram;
	}

	private Diagram createExpressionsSubdiagram() {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : getForm().getExpressionChains()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramExpression diagramExpression = new DiagramExpression();
			diagramExpression.setExpression(expressionChain);
			diagramExpression.setJointjsId(IdGenerator.createId());
			diagramExpression.setType(DiagramObjectType.CALCULATION);
			Node nodeRule = new Node(diagramExpression.getJointjsId());

			DiagramSink diagramSink = new DiagramSink();
			diagramSink.setJointjsId(IdGenerator.createId());
			diagramSink.setType(DiagramObjectType.SINK);
			Node nodeSink = new Node(diagramSink.getJointjsId());

			DiagramLink diagramLinkSourceRule = new DiagramLink(nodeSource, nodeRule);
			diagramLinkSourceRule.setJointjsId(IdGenerator.createId());
			diagramLinkSourceRule.setType(DiagramObjectType.LINK);
			DiagramLink diagramLinkRuleSink = new DiagramLink(nodeRule, nodeSink);
			diagramLinkRuleSink.setJointjsId(IdGenerator.createId());
			diagramLinkRuleSink.setType(DiagramObjectType.LINK);

			subDiagram.addDiagramObject(diagramSource);
			subDiagram.addDiagramObject(diagramExpression);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}

	/**
	 * Creates a custom variable and assigns a value of 10 to it
	 */
	protected void createFormNumberCustomVariableExpression(String customVariableName) {
		formNumberCustomVariable = new CustomVariable(getForm(), customVariableName, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain(customVariableName, new ExpressionValueCustomVariable(
				getForm(), formNumberCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(10.));
		getForm().getExpressionChains().add(expression);
		createExpressionNode(expression);
	}

	/**
	 * Creates a custom variable and assigns a value of "test"
	 */
	protected void createFormTextCustomVariableExpression(String customVariableName) {
		formTextCustomVariable = new CustomVariable(getForm(), customVariableName, CustomVariableType.STRING,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain(customVariableName, new ExpressionValueCustomVariable(
				getForm(), formTextCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString("test"));
		getForm().getExpressionChains().add(expression);
		createExpressionNode(expression);
	}

	private void createGlobalvariables() throws NotValidTypeInVariableData, FieldTooLongException {
		List<GlobalVariable> globalVarList = new ArrayList<GlobalVariable>();
		Timestamp validFrom = Timestamp.valueOf("2007-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");
		Timestamp validToPast = Timestamp.valueOf("2008-09-23 0:0:0.0");
		Timestamp validToFuture = Timestamp.valueOf("2018-09-23 0:0:0.0");

		// Should get the second value
		globalVariableNumber = new GlobalVariable(AnswerFormat.NUMBER);
		globalVariableNumber.setName("IVA");
		globalVariableNumber.addVariableData(19.0, validFrom, validToPast);
		globalVariableNumber.addVariableData(21.0, validToPast, null);
		// Should not represent this constant
		GlobalVariable globalVariableText = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText.setName("TestText");
		globalVariableText.addVariableData("Hello", validFromFuture, validToFuture);
		// Should get the value
		GlobalVariable globalVariablePostalCode = new GlobalVariable(AnswerFormat.POSTAL_CODE);
		globalVariablePostalCode.setName("TestPC");
		globalVariablePostalCode.addVariableData("Postal", validFrom, validToFuture);
		// Should enter a valid date as constant
		GlobalVariable globalVariableDate = new GlobalVariable(AnswerFormat.DATE);
		globalVariableDate.setName("TestDate");
		globalVariableDate.addVariableData(new Date(), validFrom, validToFuture);

		globalVarList.add(globalVariableNumber);
		globalVarList.add(globalVariableText);
		globalVarList.add(globalVariablePostalCode);
		globalVarList.add(globalVariableDate);

		setGlobalVariables(globalVarList);
	}

	/**
	 * Adds a new table rule element to the diagram
	 * 
	 * @param tableRule
	 */
	protected void createTableRuleNode(TableRule tableRule) {
		DiagramTable diagramTable = new DiagramTable();
		diagramTable.setTable(tableRule);
		diagramTable.setJointjsId(IdGenerator.createId());
		diagramTable.setType(DiagramObjectType.TABLE);
		diagramElements.add(diagramTable);
	}

	public TreeObject getAnswer(String QuestionName, String answerName) {
		TreeObject question = getTreeObject(QuestionName);
		// Look for the name in the question children
		if (question instanceof Question) {
			for (TreeObject questionChild : ((Question) question).getChildren()) {
				if (questionChild.getName().equals(answerName)) {
					return questionChild;
				}
			}
		}
		return null;
	}

	public Category getCategory() {
		return category;
	}

	public CustomVariable getCategoryNumberCustomVariable() {
		return categoryNumberCustomVariable;
	}

	public CustomVariable getGroupNumberCustomVariable() {
		return groupNumberCustomVariable;
	}

	public CustomVariable getQuestionNumberCustomVariable() {
		return questionNumberCustomVariable;
	}

	public CustomVariable getFormTextCustomVariable() {
		return formTextCustomVariable;
	}

	public ExpressionValueCustomVariable getFormNumberExpressionValueCustomVariable() {
		return new ExpressionValueCustomVariable(getForm(), getFormNumberCustomVariable());
	}

	public ExpressionValueCustomVariable getFormTextExpressionValueCustomVariable() {
		return new ExpressionValueCustomVariable(getForm(), getFormTextCustomVariable());
	}

	public ExpressionValueCustomVariable getCategoryExpressionValueCustomVariable() {
		return new ExpressionValueCustomVariable(getCategory(), getCategoryNumberCustomVariable());
	}

	public ExpressionValueCustomVariable getGroupExpressionValueCustomVariable() {
		return new ExpressionValueCustomVariable(getGroup(), getGroupNumberCustomVariable());
	}

	public ExpressionValueCustomVariable getQuestionExpressionValueCustomVariable() {
		return new ExpressionValueCustomVariable(getQuestion(), getQuestionNumberCustomVariable());
	}

	public Form getForm() {
		return form;
	}

	public Group getGroup() {
		return group;
	}

	public Question getQuestion() {
		return question;
	}

	public CustomVariable getFormNumberCustomVariable() {
		return formNumberCustomVariable;
	}

	public GlobalVariable getGlobalVariableNumber() {
		return globalVariableNumber;
	}

	public List<GlobalVariable> getGlobalVariables() {
		return globalVariables;
	}

	public Object getGlobalVariableValue(GlobalVariable globalVariable) {
		// First check if the data inside the variable has a valid date
		List<VariableData> varDataList = globalVariable.getVariableData();
		if ((varDataList != null) && !varDataList.isEmpty()) {
			for (VariableData variableData : varDataList) {
				Timestamp currentTime = new Timestamp(new Date().getTime());
				Timestamp initTime = variableData.getValidFrom();
				Timestamp endTime = variableData.getValidTo();
				// Sometimes endtime can be null, meaning that the
				// variable data has no ending time
				if ((currentTime.after(initTime) && (endTime == null))
						|| (currentTime.after(initTime) && currentTime.before(endTime))) {
					return variableData.getValue();
				}
			}
		}
		return null;
	}

	public ISubmittedForm getSubmittedForm() {
		return submittedForm;
	}

	/**
	 * Returns the tree object with the name specified. <br>
	 * In our test scenario the names are unique
	 * 
	 * @param name
	 * @return
	 */
	public TreeObject getTreeObject(String name) {
		// Look for the name in the categories
		for (TreeObject category : getForm().getChildren()) {
			if (category.getName().equals(name)) {
				return category;
			}
			// Look for the name in the category children
			if (category instanceof Category) {
				for (TreeObject categoryChild : ((Category) category).getChildren()) {
					if (categoryChild.getName().equals(name)) {
						return categoryChild;
					}
					// Look for the name in the group children
					if (categoryChild instanceof Group) {
						for (TreeObject groupChild : ((Group) categoryChild).getChildren()) {
							if (groupChild.getName().equals(name)) {
								return groupChild;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public void initForm() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		form = new Form("KidsScreen");

		category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question name = new Question("name");
		name.setAnswerType(AnswerType.INPUT);
		name.setAnswerFormat(AnswerFormat.TEXT);
		category.addChild(name);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		category.addChild(birthdate);

		Question gender = new Question("gender");
		Answer male = new Answer("M");
		Answer female = new Answer("F");
		gender.setAnswerType(AnswerType.RADIO);
		gender.addChild(male);
		gender.addChild(female);
		category.addChild(gender);

		Question height = new Question("height");
		height.setAnswerType(AnswerType.INPUT);
		height.setAnswerFormat(AnswerFormat.NUMBER);
		category.addChild(height);

		Question heightFather = new Question("heightFather");
		heightFather.setAnswerType(AnswerType.INPUT);
		heightFather.setAnswerFormat(AnswerFormat.NUMBER);
		category.addChild(heightFather);

		Question heightMother = new Question("heightMother");
		heightMother.setAnswerType(AnswerType.INPUT);
		heightMother.setAnswerFormat(AnswerFormat.NUMBER);
		category.addChild(heightMother);

		Question weight = new Question("weight");
		weight.setAnswerType(AnswerType.INPUT);
		weight.setAnswerFormat(AnswerFormat.NUMBER);
		category.addChild(weight);

		Category gezondheid = new Category("Gezondheid");
		form.addChild(gezondheid);

		Question health = new Question("health");
		Answer yes = new Answer("Y");
		Answer no = new Answer("N");
		health.setAnswerType(AnswerType.RADIO);
		health.addChild(yes);
		health.addChild(no);
		gezondheid.addChild(health);

		Category lifestyle = new Category("Lifestyle");
		form.addChild(lifestyle);

		group = new Group("voeding");
		lifestyle.addChild(group);

		Question breakfast = new Question("breakfast");
		Answer breakfastA = new Answer("a");
		Answer breakfastB = new Answer("b");
		Answer breakfastC = new Answer("c");
		Answer breakfastD = new Answer("d");
		Answer breakfastE = new Answer("e");
		breakfast.setAnswerType(AnswerType.RADIO);
		breakfast.addChild(breakfastA);
		breakfast.addChild(breakfastB);
		breakfast.addChild(breakfastC);
		breakfast.addChild(breakfastD);
		breakfast.addChild(breakfastE);
		group.addChild(breakfast);

		question = new Question("fruit");
		Answer fruitA = new Answer("a");
		Answer fruitB = new Answer("b");
		Answer fruitC = new Answer("c");
		Answer fruitD = new Answer("d");
		Answer fruitE = new Answer("e");
		question.setAnswerType(AnswerType.RADIO);
		question.addChild(fruitA);
		question.addChild(fruitB);
		question.addChild(fruitC);
		question.addChild(fruitD);
		question.addChild(fruitE);
		group.addChild(question);

		Question fruitAmount = new Question("fruitAmount");
		fruitAmount.setAnswerType(AnswerType.INPUT);
		fruitAmount.setAnswerFormat(AnswerFormat.NUMBER);
		group.addChild(fruitAmount);

		Question vegetables = new Question("vegetables");
		Answer vegetablesA = new Answer("a");
		Answer vegetablesB = new Answer("b");
		Answer vegetablesC = new Answer("c");
		Answer vegetablesD = new Answer("d");
		Answer vegetablesE = new Answer("e");
		vegetables.setAnswerType(AnswerType.RADIO);
		vegetables.addChild(vegetablesA);
		vegetables.addChild(vegetablesB);
		vegetables.addChild(vegetablesC);
		vegetables.addChild(vegetablesD);
		vegetables.addChild(vegetablesE);
		group.addChild(vegetables);

		Question vegetablesAmount = new Question("vegetablesAmount");
		vegetablesAmount.setAnswerType(AnswerType.INPUT);
		vegetablesAmount.setAnswerFormat(AnswerFormat.NUMBER);
		group.addChild(vegetablesAmount);

		Question drinks = new Question("drinks");
		Answer drinksA = new Answer("a");
		Answer drinksB = new Answer("b");
		Answer drinksC = new Answer("c");
		Answer drinksD = new Answer("d");
		drinks.setAnswerType(AnswerType.RADIO);
		drinks.addChild(drinksA);
		drinks.addChild(drinksB);
		drinks.addChild(drinksC);
		drinks.addChild(drinksD);
		group.addChild(drinks);

		// Extra for testing generics
		Group voeding2 = new Group("voeding2");
		group.addChild(voeding2);

		Question drinks1 = new Question("drinks1");
		Answer drinksA1 = new Answer("a");
		Answer drinksB1 = new Answer("b");
		Answer drinksC1 = new Answer("c");
		Answer drinksD1 = new Answer("d");
		drinks1.setAnswerType(AnswerType.RADIO);
		drinks1.addChild(drinksA1);
		drinks1.addChild(drinksB1);
		drinks1.addChild(drinksC1);
		drinks1.addChild(drinksD1);
		voeding2.addChild(drinks1);

		// Creation of the global variables
		createGlobalvariables();

		// Init the array of elements to create the diagram
		diagramElements = new ArrayList<>();
	}

	@Test(groups = { "orbeon" })
	public void readStaticSubmittedForm() throws DocumentException, IOException {
		submittedForm = new DroolsSubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/kidScreen.xml", StandardCharsets.UTF_8);
		orbeonImporter.readXml(xmlFile, submittedForm);
		Assert.assertNotNull(submittedForm);
		Assert.assertFalse(submittedForm.getChildren().isEmpty());
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setCategoryNumberCustomVariable(CustomVariable categoryNumberCustomVariable) {
		this.categoryNumberCustomVariable = categoryNumberCustomVariable;
	}

	public void setFormNumberCustomVariable(CustomVariable formNumberCustomVariable) {
		this.formNumberCustomVariable = formNumberCustomVariable;
	}

	public void setGlobalVariableNumber(GlobalVariable globalVariableNumber) {
		this.globalVariableNumber = globalVariableNumber;
	}

	public void setGlobalVariables(List<GlobalVariable> globalVariables) {
		this.globalVariables = globalVariables;
	}
}
