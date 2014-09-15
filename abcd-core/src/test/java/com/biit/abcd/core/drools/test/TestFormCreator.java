package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.OrbeonCategoryTranslator;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class TestFormCreator {

	private ISubmittedForm submittedForm;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();
	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private Form form = null;

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public TestFormCreator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {

		orbeonImporter = new OrbeonSubmittedAnswerImporter();

		form = new Form("KidsScreen");

		Category algemeen = new Category("Algemeen");
		form.addChild(algemeen);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		algemeen.addChild(birthdate);

		Question gender = new Question("gender");
		Answer male = new Answer("M");
		Answer female = new Answer("F");
		gender.setAnswerType(AnswerType.RADIO);
		gender.addChild(male);
		gender.addChild(female);
		algemeen.addChild(gender);

		Question height = new Question("height");
		height.setAnswerType(AnswerType.INPUT);
		height.setAnswerFormat(AnswerFormat.NUMBER);
		algemeen.addChild(height);

		Question heightFather = new Question("heightFather");
		heightFather.setAnswerType(AnswerType.INPUT);
		heightFather.setAnswerFormat(AnswerFormat.NUMBER);
		algemeen.addChild(heightFather);

		Question heightMother = new Question("heightMother");
		heightMother.setAnswerType(AnswerType.INPUT);
		heightMother.setAnswerFormat(AnswerFormat.NUMBER);
		algemeen.addChild(heightMother);

		Question weight = new Question("weight");
		weight.setAnswerType(AnswerType.INPUT);
		weight.setAnswerFormat(AnswerFormat.NUMBER);
		algemeen.addChild(weight);

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

		Group voeding = new Group("voeding");
		lifestyle.addChild(voeding);

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
		voeding.addChild(breakfast);

		Question fruit = new Question("fruit");
		Answer fruitA = new Answer("a");
		Answer fruitB = new Answer("b");
		Answer fruitC = new Answer("c");
		Answer fruitD = new Answer("d");
		Answer fruitE = new Answer("e");
		fruit.setAnswerType(AnswerType.RADIO);
		fruit.addChild(fruitA);
		fruit.addChild(fruitB);
		fruit.addChild(fruitC);
		fruit.addChild(fruitD);
		fruit.addChild(fruitE);
		voeding.addChild(fruit);

		Question fruitAmount = new Question("fruitAmount");
		fruitAmount.setAnswerType(AnswerType.INPUT);
		fruitAmount.setAnswerFormat(AnswerFormat.NUMBER);
		voeding.addChild(fruitAmount);

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
		voeding.addChild(vegetables);

		Question vegetablesAmount = new Question("vegetablesAmount");
		vegetablesAmount.setAnswerType(AnswerType.INPUT);
		vegetablesAmount.setAnswerFormat(AnswerFormat.NUMBER);
		voeding.addChild(vegetablesAmount);

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
		voeding.addChild(drinks);
	}

	public ISubmittedForm createAndRunDroolsRules() throws ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation {
		// Generate the drools rules.
		FormToDroolsExporter formDrools = new FormToDroolsExporter();
		DroolsRulesGenerator rulesGenerator = formDrools.generateDroolRules(getForm(), null);
		readStaticSubmittedForm();
		translateFormCategories();
		// Test the rules with the submitted form and returns a DroolsForm
		return formDrools.applyDrools(getSubmittedForm(), rulesGenerator.getRules(), null);
	}

	public Form getForm() {
		return form;
	}

	public ISubmittedForm getSubmittedForm() {
		return submittedForm;
	}

	@Test(groups = { "orbeon" })
	public void readStaticSubmittedForm() throws DocumentException, IOException {
		submittedForm = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/kidScreen.xml", StandardCharsets.UTF_8);
		orbeonImporter.readXml(xmlFile, submittedForm);
		Assert.assertNotNull(submittedForm);
		Assert.assertFalse(submittedForm.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readStaticSubmittedForm" })
	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		String xmlStructure = readFile("./src/test/resources/kidScreen.xhtml", StandardCharsets.UTF_8);
		OrbeonCategoryTranslator.getInstance().readXml(submittedForm, xmlStructure);
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
}
