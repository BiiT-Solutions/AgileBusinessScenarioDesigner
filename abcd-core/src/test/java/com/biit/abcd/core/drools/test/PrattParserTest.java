package com.biit.abcd.core.drools.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementPrintVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;

public class PrattParserTest {

	private ExpressionValueTreeObjectReference expValQU1 = null;
	private ExpressionValueTreeObjectReference expValQU1A1 = null;
	private ExpressionValueTreeObjectReference expValQU1A2 = null;
	private ExpressionValueCustomVariable expValCVar = null;
	private ExpressionValueCustomVariable expValQVar = null;
	private ExpressionValueCustomVariable expValFormScore = null;
	private ExpressionValueNumber expValNumber = null;
	private ExpressionValueGenericCustomVariable expValGenericCatScore = null;
	private ExpressionValueGenericCustomVariable expValGenericQuestScore = null;
	private Category category = null;
	private Question father = null;
	private Question mother = null;
	private Question birthDate = null;
	private CustomVariable cVar = null;

	private void createSimpleForm() {
		try {
			Form form = new Form("form");
			setCategory(new Category("categoryTest"));
			form.addChild(getCategory());
			Question question = new Question("QU1");
			getCategory().addChild(question);
			Answer answer1 = new Answer("QU1A1");
			question.addChild(answer1);
			Answer answer2 = new Answer("QU1A2");
			question.addChild(answer2);
			setcVar(new CustomVariable(form, "catVar", CustomVariableType.NUMBER, CustomVariableScope.CATEGORY));
			CustomVariable qVar = new CustomVariable(form, "questVar", CustomVariableType.NUMBER,
					CustomVariableScope.QUESTION);
			CustomVariable fVar = new CustomVariable(form, "formVar", CustomVariableType.NUMBER,
					CustomVariableScope.FORM);

			setFather(new Question("fatherHeight"));
			getFather().setAnswerType(AnswerType.INPUT);
			getFather().setAnswerFormat(AnswerFormat.NUMBER);
			getCategory().addChild(getFather());

			setMother(new Question("motherHeight"));
			getMother().setAnswerType(AnswerType.INPUT);
			getMother().setAnswerFormat(AnswerFormat.NUMBER);
			getCategory().addChild(getMother());

			setBirthDate(new Question("birthdate"));
			getBirthDate().setAnswerType(AnswerType.INPUT);
			getBirthDate().setAnswerFormat(AnswerFormat.DATE);
			getCategory().addChild(getBirthDate());

			setExpValQU1(new ExpressionValueTreeObjectReference(question));
			setExpValQU1A1(new ExpressionValueTreeObjectReference(answer1));
			setExpValQU1A2(new ExpressionValueTreeObjectReference(answer2));
			setExpValCVar(new ExpressionValueCustomVariable(getCategory(), getcVar()));
			setExpValQVar(new ExpressionValueCustomVariable(question, qVar));
			setExpValFormScore(new ExpressionValueCustomVariable(form, fVar));
			setExpValNumber(new ExpressionValueNumber(5.));
			// Generics
			setExpValGenericCatScore(new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, getcVar()));
			setExpValGenericQuestScore(new ExpressionValueGenericCustomVariable(
					GenericTreeObjectType.QUESTION_CATEGORY, qVar));

		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "droolsPrattParser" })
	public void findSimpleExpressionsTest() throws NotCompatibleTypeException {
		createSimpleForm();
		// AND|OR conditions combination
		List<ExpressionChain> expressionChainList = parseAndTryVistior(new ExpressionChain(new ExpressionSymbol(
				AvailableSymbol.LEFT_BRACKET), getExpValQU1(), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				getExpValQU1A1(), new ExpressionOperatorLogic(AvailableOperator.OR), getExpValQU1(),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), getExpValQU1A2(), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.AND),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), getExpValQU1(), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), getExpValQU1A2(), new ExpressionOperatorLogic(AvailableOperator.OR),
				getExpValQU1(), new ExpressionOperatorLogic(AvailableOperator.EQUALS), getExpValQU1A1(),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		List<ExpressionChain> expectedList = new ArrayList<ExpressionChain>();
		expectedList.add(new ExpressionChain(new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A1())));
		expectedList.add(new ExpressionChain(new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A2())));
		expectedList.add(new ExpressionChain(new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A2())));
		expectedList.add(new ExpressionChain(new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A1())));

		int i = 0;
		for (ExpressionChain expression : expressionChainList) {
			removeExpressionChainNames(expression);
			Assert.assertEquals(expression.toString(), expectedList.get(i).toString());
			i++;
		}
	}

	@Test(groups = { "droolsPrattParser" })
	public void treeObjectNegationTest() {
		createSimpleForm();
		// Simple Negation TreeObject (e.g. NOT(QU1 == A1) )
		String actual = parseDrools(new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), getExpValQU1(), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), getExpValQU1A1(),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionChain(
						new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
						new ExpressionChain(getExpValQU1A1())), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void treeObjectScoreEqualsExpressionValueNumberTest() {
		createSimpleForm();
		// Simple TreeObject.Score equals ValueNumber (e.g. Cat1.score == 10)
		String actual = parseDrools(new ExpressionChain(getExpValCVar(), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), getExpValNumber()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValCVar()),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(getExpValNumber()));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void simpleAndConditionTest() {
		createSimpleForm();
		// Simple AND condition
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionOperatorLogic(
				AvailableOperator.AND), getExpValQU1()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionChain(getExpValQU1()));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void andConditionTest() {
		createSimpleForm();
		// AND condition (e.g. Q==A AND Q==A)
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), getExpValQU1A1(), new ExpressionOperatorLogic(AvailableOperator.AND),
				getExpValQU1(), new ExpressionOperatorLogic(AvailableOperator.EQUALS), getExpValQU1A1()));

		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A1())),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionChain(new ExpressionChain(
						getExpValQU1()), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(
						getExpValQU1A1())));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void simpleOrConditionTest() {
		createSimpleForm();
		// Simple OR condition
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionOperatorLogic(
				AvailableOperator.OR), getExpValQU1()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionOperatorLogic(AvailableOperator.OR), new ExpressionChain(getExpValQU1()));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void andOrCCombinationTest() {
		createSimpleForm();
		// AND|OR conditions combination
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionOperatorLogic(
				AvailableOperator.EQUALS), getExpValQU1A1(), new ExpressionOperatorLogic(AvailableOperator.OR),
				getExpValQU1(), new ExpressionOperatorLogic(AvailableOperator.EQUALS), getExpValQU1A1(),
				new ExpressionOperatorLogic(AvailableOperator.AND), getExpValQU1(), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), getExpValQU1A1()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A1())),
				new ExpressionOperatorLogic(AvailableOperator.OR), new ExpressionChain(new ExpressionChain(
						new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
						new ExpressionChain(getExpValQU1A1())), new ExpressionOperatorLogic(AvailableOperator.AND),
						new ExpressionChain(new ExpressionChain(getExpValQU1()), new ExpressionOperatorLogic(
								AvailableOperator.EQUALS), new ExpressionChain(getExpValQU1A1()))));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void treeObjectBetweenValueNumbersTest() {
		createSimpleForm();
		// Simple TreeObject Between Function (e.g. QU1 between(0, 18))
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionFunction(
				AvailableFunction.BETWEEN), new ExpressionValueNumber(0.), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(18.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionChain(new ExpressionValueNumber(0.)),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(new ExpressionValueNumber(18.)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void customVariableBetweenValueNumbersTest() {
		createSimpleForm();
		// Simple CustomVariable Between Function (e.g. QU1 between(0, 18))
		String actual = parseDrools(new ExpressionChain(getExpValCVar(), new ExpressionFunction(
				AvailableFunction.BETWEEN), new ExpressionValueNumber(0.), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(18.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValCVar()),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionChain(new ExpressionValueNumber(0.)),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(new ExpressionValueNumber(18.)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void questionAnswersInFunctionTest() {
		createSimpleForm();
		// IN function (e.g. Q IN(A, A, A) AND Q==A)
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionFunction(AvailableFunction.IN),
				getExpValQU1A1(), new ExpressionSymbol(AvailableSymbol.COMMA), getExpValQU1A2(), new ExpressionSymbol(
						AvailableSymbol.COMMA), getExpValQU1A2(), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorLogic(AvailableOperator.AND), getExpValQU1(), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), getExpValQU1A1()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(getExpValQU1()),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionChain(getExpValQU1A1()),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(getExpValQU1A2()),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(getExpValQU1A2()),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionChain(new ExpressionChain(
						getExpValQU1()), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(
						getExpValQU1A1())));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void functionsExpressionCombinationTest() {
		createSimpleForm();
		// Q IN (A1, A2) AND Q IN (A1, A2) AND Q==A
		String actual = parseDrools(new ExpressionChain(getExpValQU1(), new ExpressionFunction(AvailableFunction.IN),
				getExpValQU1A1(), new ExpressionSymbol(AvailableSymbol.COMMA), getExpValQU1A2(), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.AND),
				getExpValQU1(), new ExpressionFunction(AvailableFunction.IN), getExpValQU1A1(), new ExpressionSymbol(
						AvailableSymbol.COMMA), getExpValQU1A2(), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorLogic(AvailableOperator.AND), getExpValQU1(), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), getExpValQU1A1()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(
				new ExpressionChain(getExpValQU1()), new ExpressionFunction(AvailableFunction.IN), new ExpressionChain(
						getExpValQU1A1()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(
						getExpValQU1A2()), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionChain(new ExpressionChain(
						getExpValQU1()), new ExpressionFunction(AvailableFunction.IN), new ExpressionChain(
						getExpValQU1A1()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(
						getExpValQU1A2()), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET))),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionChain(new ExpressionChain(
						getExpValQU1()), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(
						getExpValQU1A1())));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void treeObjectScoreMinFunctionTest() {
		createSimpleForm();
		// Cat.score = Min(q.score, q.score)
		String actual = parseDrools(new ExpressionChain(getExpValCVar(), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN), getExpValQVar(),
				new ExpressionSymbol(AvailableSymbol.COMMA), getExpValQVar(), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValCVar()),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionChain(getExpValQVar()),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(getExpValQVar()),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void treeObjectScoresComparationTest() {
		createSimpleForm();
		// Cat.score = Quest.score
		String actual = parseDrools(new ExpressionChain(getExpValCVar(), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), getExpValQVar()));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValCVar()),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionChain(getExpValQVar()));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void genericsTest() {
		createSimpleForm();
		// Generics test
		String actual = parseDrools(new ExpressionChain(getExpValGenericCatScore(), new ExpressionFunction(
				AvailableFunction.MIN), getExpValGenericQuestScore(), new ExpressionSymbol(
				AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValGenericCatScore()),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionChain(getExpValGenericQuestScore()),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void mathematicalExpressionOneTest() {
		createSimpleForm();
		// Mathematical test
		String actual = parseDrools(new ExpressionChain(new ExpressionValueCustomVariable(getCategory(), getcVar()),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(10.),
				new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getMother()), new ExpressionOperatorMath(
						AvailableOperator.MULTIPLICATION), new ExpressionValueNumber(0.6), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorMath(AvailableOperator.PLUS),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getFather()), new ExpressionOperatorMath(
						AvailableOperator.MULTIPLICATION), new ExpressionValueNumber(0.4), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionValueCustomVariable(
				getCategory(), getcVar())), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionChain(new ExpressionChain(new ExpressionChain(new ExpressionValueNumber(10.)),
						new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionChain(new ExpressionSymbol(
								AvailableSymbol.LEFT_BRACKET),
								new ExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
										getMother())), new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION),
										new ExpressionChain(new ExpressionValueNumber(0.6))), new ExpressionSymbol(
										AvailableSymbol.RIGHT_BRACKET))), new ExpressionOperatorMath(
						AvailableOperator.PLUS), new ExpressionChain(
						new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
						new ExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(getFather())),
								new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new ExpressionChain(
										new ExpressionValueNumber(0.4))), new ExpressionSymbol(
								AvailableSymbol.RIGHT_BRACKET))));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void equalsAndLessThanTest() {
		createSimpleForm();
		// Mathematical test
		String actual = parseDrools(new ExpressionChain(new ExpressionValueTreeObjectReference(getBirthDate(),
				QuestionDateUnit.YEARS), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(4.), new ExpressionOperatorLogic(AvailableOperator.AND),
				getExpValFormScore(), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
				new ExpressionValueNumber(13.)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(
				new ExpressionValueTreeObjectReference(getBirthDate(), QuestionDateUnit.YEARS)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(
						new ExpressionValueNumber(4.))), new ExpressionOperatorLogic(AvailableOperator.AND),
				new ExpressionChain(new ExpressionChain(getExpValFormScore()), new ExpressionOperatorLogic(
						AvailableOperator.LESS_THAN), new ExpressionChain(new ExpressionValueNumber(13.))));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void equalsAndBetweenTest() {
		createSimpleForm();
		// Mathematical test
		String actual = parseDrools(new ExpressionChain(new ExpressionValueTreeObjectReference(getBirthDate(),
				QuestionDateUnit.YEARS), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(4.), new ExpressionOperatorLogic(AvailableOperator.AND),
				getExpValFormScore(), new ExpressionChain(new ExpressionFunction(AvailableFunction.BETWEEN),
						new ExpressionValueNumber(13.1), new ExpressionSymbol(AvailableSymbol.COMMA),
						new ExpressionValueNumber(19.2), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET))));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(new ExpressionChain(
				new ExpressionValueTreeObjectReference(getBirthDate(), QuestionDateUnit.YEARS)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionChain(
						new ExpressionValueNumber(4.))), new ExpressionOperatorLogic(AvailableOperator.AND),
				new ExpressionChain(new ExpressionChain(getExpValFormScore()), new ExpressionFunction(
						AvailableFunction.BETWEEN), new ExpressionChain(new ExpressionValueNumber(13.1)),
						new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(
								new ExpressionValueNumber(19.2)), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void functionNegationTest() {
		createSimpleForm();
		// Negation of a Function
		String actual = parseDrools(new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getFather()), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(13.1), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(19.2), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionChain(
						new ExpressionChain(new ExpressionValueTreeObjectReference(getFather())),
						new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionChain(
								new ExpressionValueNumber(13.1)), new ExpressionSymbol(AvailableSymbol.COMMA),
						new ExpressionChain(new ExpressionValueNumber(19.2)), new ExpressionSymbol(
								AvailableSymbol.RIGHT_BRACKET)), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void bracketsTest() {
		createSimpleForm();
		String actual = parseDrools(new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getFather()), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(13.1), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(19.2), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorLogic(AvailableOperator.OR), new ExpressionValueTreeObjectReference(getFather()),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(13.1),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(19.2), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionChain(
						new ExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(father)),
								new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionChain(
										new ExpressionValueNumber(13.1)), new ExpressionSymbol(AvailableSymbol.COMMA),
								new ExpressionChain(new ExpressionValueNumber(19.2)), new ExpressionSymbol(
										AvailableSymbol.RIGHT_BRACKET)), new ExpressionOperatorLogic(
								AvailableOperator.OR), new ExpressionChain(new ExpressionChain(
								new ExpressionValueTreeObjectReference(father)), new ExpressionFunction(
								AvailableFunction.BETWEEN), new ExpressionChain(new ExpressionValueNumber(13.1)),
								new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(
										new ExpressionValueNumber(19.2)), new ExpressionSymbol(
										AvailableSymbol.RIGHT_BRACKET))), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void ifFunctionTest() {
		createSimpleForm();
		String actual = parseDrools(new ExpressionChain("ifExpression", new ExpressionFunction(AvailableFunction.IF),
				new ExpressionValueTreeObjectReference(getFather()), new ExpressionOperatorLogic(
						AvailableOperator.LESS_THAN), new ExpressionValueNumber(25.), new ExpressionSymbol(
						AvailableSymbol.COMMA), getExpValFormScore(), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.1), new ExpressionSymbol(
						AvailableSymbol.COMMA), getExpValFormScore(), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueNumber(1.7), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));
		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionFunction(AvailableFunction.IF),
				new ExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(getFather())),
						new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionChain(
								new ExpressionValueNumber(25.))), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionChain(new ExpressionChain(getExpValFormScore()), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionChain(new ExpressionValueNumber(7.1))),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(new ExpressionChain(
						getExpValFormScore()), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
						new ExpressionChain(new ExpressionValueNumber(1.7))), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void ifFunctionTestWithFunctionsInConditions() {
		createSimpleForm();
		String actual = parseDrools(new ExpressionChain("ifExpression", new ExpressionFunction(AvailableFunction.IF),
				getExpValGenericCatScore(), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueNumber(25.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						29.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(65.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),

				new ExpressionSymbol(AvailableSymbol.COMMA), getExpValFormScore(), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.1),

				new ExpressionSymbol(AvailableSymbol.COMMA), getExpValFormScore(), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueNumber(1.7), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));

		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionFunction(AvailableFunction.IF),
				new ExpressionChain(new ExpressionChain(getExpValGenericCatScore()), new ExpressionFunction(
						AvailableFunction.IN), new ExpressionChain(new ExpressionValueNumber(25.)),
						new ExpressionSymbol(AvailableSymbol.COMMA),
						new ExpressionChain(new ExpressionValueNumber(29.)),
						new ExpressionSymbol(AvailableSymbol.COMMA),
						new ExpressionChain(new ExpressionValueNumber(65.)), new ExpressionSymbol(
								AvailableSymbol.RIGHT_BRACKET)),

				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionChain(new ExpressionChain(
						getExpValFormScore()), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
						new ExpressionChain(new ExpressionValueNumber(7.1))), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionChain(new ExpressionChain(getExpValFormScore()),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionChain(
								new ExpressionValueNumber(1.7))), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		Assert.assertEquals(actual, expectedResult.toString());
	}

	@Test(groups = { "droolsPrattParser" })
	public void pluginFunctionsTest() {
		final Class<?> DROOLS_PLUGIN_INTERFACE = com.biit.plugins.interfaces.IDroolsRulePlugin.class;
		final String DROOLS_PLUGIN_NAME = "DroolsFunctions";
		final String DROOLS_PLUGIN_METHOD = "methodSumParameters";
		createSimpleForm();
		String actual = parseDrools(new ExpressionChain("helloWorldExpression", getExpValFormScore(),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionPluginMethod(
						DROOLS_PLUGIN_INTERFACE, DROOLS_PLUGIN_NAME, DROOLS_PLUGIN_METHOD), new ExpressionValueNumber(
						1.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(2.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(3.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(4.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));

		// Check result
		ExpressionChain expectedResult = new ExpressionChain(new ExpressionChain(getExpValFormScore()),
				new ExpressionPluginMethod(DROOLS_PLUGIN_INTERFACE, DROOLS_PLUGIN_NAME, DROOLS_PLUGIN_METHOD),
				new ExpressionChain(new ExpressionValueNumber(1.)), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionChain(new ExpressionValueNumber(2.)), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionChain(new ExpressionValueNumber(3.)), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionChain(new ExpressionValueNumber(4.)), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		
		Assert.assertEquals(actual, expectedResult.toString());
	}

	/**
	 * Parses the given chunk of code and returns pretty-printed result.
	 * 
	 * @throws NotCompatibleTypeException
	 */
	public static String parse(ExpressionChain source) throws NotCompatibleTypeException {
		PrattParser parser = new ExpressionChainPrattParser(source);
		TreeElementPrintVisitor treePrint = null;
		try {
			ITreeElement resultVisitor = parser.parseExpression();
			treePrint = new TreeElementPrintVisitor();
			resultVisitor.accept(treePrint);

		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(PrattParser.class.getName(), ex);
		}
		if (treePrint != null) {
			return treePrint.getBuilder().toString();
		} else {
			return "";
		}
	}

	/**
	 * Parses the given chunk of code and verifies that it matches the expected
	 * pretty-printed result.
	 */
	public static String parseDrools(ExpressionChain source) {
		PrattParser parser = new ExpressionChainPrattParser(source);
		try {
			ExpressionChain expressionChain = parser.parseExpression().getExpressionChain();
			removeExpressionChainNames(expressionChain);
			return expressionChain.toString();
		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(PrattParser.class.getName(), ex);
		}
		return "";
	}

	/**
	 * Sets the expressionChain names to null<br>
	 * The visitor generates unique expression chain names that are not needed
	 * and interfere to check the solution
	 * 
	 * @param expressionChain
	 */
	private static void removeExpressionChainNames(ExpressionChain expressionChain) {
		expressionChain.setName(null);
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				removeExpressionChainNames((ExpressionChain) expression);
			}
		}
	}

	/**
	 * Parses the given chunk of code and returns pretty-printed result.
	 * 
	 * @throws NotCompatibleTypeException
	 */
	public static List<ExpressionChain> parseAndTryVistior(ExpressionChain source) throws NotCompatibleTypeException {
		PrattParser parser = new ExpressionChainPrattParser(source);
		TreeElementGroupConditionFinderVisitor treeVisitor = null;
		try {
			ITreeElement resultVisitor = parser.parseExpression();
			treeVisitor = new TreeElementGroupConditionFinderVisitor();
			resultVisitor.accept(treeVisitor);

		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(PrattParser.class.getName(), ex);
		}
		if (treeVisitor != null) {
			return treeVisitor.getConditions();
		}
		return null;
	}

	public ExpressionValueTreeObjectReference getExpValQU1() {
		return expValQU1;
	}

	public void setExpValQU1(ExpressionValueTreeObjectReference expValQU1) {
		this.expValQU1 = expValQU1;
	}

	public ExpressionValueTreeObjectReference getExpValQU1A1() {
		return expValQU1A1;
	}

	public void setExpValQU1A1(ExpressionValueTreeObjectReference expValQU1A1) {
		this.expValQU1A1 = expValQU1A1;
	}

	public ExpressionValueTreeObjectReference getExpValQU1A2() {
		return expValQU1A2;
	}

	public void setExpValQU1A2(ExpressionValueTreeObjectReference expValQU1A2) {
		this.expValQU1A2 = expValQU1A2;
	}

	public ExpressionValueCustomVariable getExpValCVar() {
		return expValCVar;
	}

	public void setExpValCVar(ExpressionValueCustomVariable expValCVar) {
		this.expValCVar = expValCVar;
	}

	public ExpressionValueCustomVariable getExpValQVar() {
		return expValQVar;
	}

	public void setExpValQVar(ExpressionValueCustomVariable expValQVar) {
		this.expValQVar = expValQVar;
	}

	public ExpressionValueCustomVariable getExpValFormScore() {
		return expValFormScore;
	}

	public void setExpValFormScore(ExpressionValueCustomVariable expValFormScore) {
		this.expValFormScore = expValFormScore;
	}

	public ExpressionValueNumber getExpValNumber() {
		return expValNumber;
	}

	public void setExpValNumber(ExpressionValueNumber expValNumber) {
		this.expValNumber = expValNumber;
	}

	public ExpressionValueGenericCustomVariable getExpValGenericCatScore() {
		return expValGenericCatScore;
	}

	public void setExpValGenericCatScore(ExpressionValueGenericCustomVariable expValGenericCatScore) {
		this.expValGenericCatScore = expValGenericCatScore;
	}

	public ExpressionValueGenericCustomVariable getExpValGenericQuestScore() {
		return expValGenericQuestScore;
	}

	public void setExpValGenericQuestScore(ExpressionValueGenericCustomVariable expValGenericQuestScore) {
		this.expValGenericQuestScore = expValGenericQuestScore;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Question getFather() {
		return father;
	}

	public void setFather(Question father) {
		this.father = father;
	}

	public Question getMother() {
		return mother;
	}

	public void setMother(Question mother) {
		this.mother = mother;
	}

	public CustomVariable getcVar() {
		return cVar;
	}

	public void setcVar(CustomVariable cVar) {
		this.cVar = cVar;
	}

	public Question getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Question birthDate) {
		this.birthDate = birthDate;
	}
}
