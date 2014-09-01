package com.biit.abcd.core.drools.test;

import org.testng.annotations.Test;

import com.biit.abcd.core.drools.prattparser.ExpressionChainParser;
import com.biit.abcd.core.drools.prattparser.ParseException;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementPrintVisitor;
import com.biit.abcd.persistence.entity.Answer;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class PrattParserTest {
	@Test(groups = { "simpleParser" })
	public static void testParser() throws FieldTooLongException, NotValidChildException {

		Form form = new Form("testForm");
		Category category = new Category("categoryTest");
		form.addChild(category);
		Question question = new Question("Q1");
		category.addChild(question);
		Answer answer1 = new Answer("Q1A1");
		question.addChild(answer1);
		Answer answer2 = new Answer("Q1A2");
		question.addChild(answer2);
		CustomVariable cVar = new CustomVariable(form, "catVar", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable qVar = new CustomVariable(form, "questVar", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		ExpressionValueTreeObjectReference expValQ1 = new ExpressionValueTreeObjectReference(question);
		ExpressionValueTreeObjectReference expValQ1A1 = new ExpressionValueTreeObjectReference(answer1);
		ExpressionValueTreeObjectReference expValQ1A2 = new ExpressionValueTreeObjectReference(answer2);
		ExpressionValueCustomVariable expValCVar = new ExpressionValueCustomVariable(category, cVar);
		ExpressionValueCustomVariable expValQVar = new ExpressionValueCustomVariable(question, qVar);
		ExpressionValueNumber expValNumber = new ExpressionValueNumber(5.);
		// Generics
		ExpressionValueGenericCustomVariable expValGenericCatScore = new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, cVar);
		ExpressionValueGenericCustomVariable expValGenericQuestScore = new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.QUESTION_CATEGORY, qVar);

		// // Simple TreeObject equals TreeObject (e.g. Q1 == A1)
		// String actual = parseDrools(new ExpressionChain(expValQ1,
		// new ExpressionOperatorLogic(AvailableOperator.EQUALS), expValQ1A1));
		// Assert.assertEquals(actual, "null[null[Q1], ==, null[Q1A1]]");
		//
		// // Simple TreeObject.Score equals ValueNumber (e.g. Cat1.score == 10)
		// actual = parseDrools(new ExpressionChain(expValCVar, new
		// ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValNumber));
		// Assert.assertEquals(actual,
		// "null[null[categoryTest.catVar], ==, null[5]]");
		//
		// // Simple AND condition
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.AND), expValQ1));
		// Assert.assertEquals(actual, "null[null[Q1], &&, null[Q1]]");
		//
		// // Simple OR condition
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.OR), expValQ1));
		// Assert.assertEquals(actual, "null[null[Q1], ||, null[Q1]]");
		//
		// // AND|OR conditions combination
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValQ1A1, new ExpressionOperatorLogic(AvailableOperator.OR),
		// expValQ1, new ExpressionOperatorLogic(
		// AvailableOperator.EQUALS), expValQ1A1, new
		// ExpressionOperatorLogic(AvailableOperator.AND),
		// expValQ1, new ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValQ1A1));
		// Assert.assertEquals(actual,
		// "null[null[null[Q1], ==, null[Q1A1]], ||, null[null[null[Q1], ==, null[Q1A1]], &&, null[null[Q1], ==, null[Q1A1]]]]");
		//
		// // Simple TreeObject Between Function (e.g. Q1 between(0, 18))
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionFunction(AvailableFunction.BETWEEN),
		// new ExpressionValueNumber(0.), new
		// ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
		// 18.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Assert.assertEquals(actual,
		// "null[null[Q1], BETWEEN(, null[0], null[18]]");
		//
		// // Simple CustomVariable Between Function (e.g. Q1 between(0, 18))
		// actual = parseDrools(new ExpressionChain(expValCVar, new
		// ExpressionFunction(AvailableFunction.BETWEEN),
		// new ExpressionValueNumber(0.), new
		// ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
		// 18.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// Assert.assertEquals(actual,
		// "null[null[categoryTest.catVar], BETWEEN(, null[0], null[18]]");
		//
		// // IN function (e.g. Q IN(A, A, A) AND Q==A)
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionFunction(AvailableFunction.IN), expValQ1A1,
		// new ExpressionSymbol(AvailableSymbol.COMMA), expValQ1A2, new
		// ExpressionSymbol(AvailableSymbol.COMMA),
		// expValQ1A2, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new
		// ExpressionOperatorLogic(
		// AvailableOperator.AND), expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValQ1A1));
		// Assert.assertEquals(actual,
		// "null[null[null[Q1], IN(, null[Q1A1], null[Q1A2], null[Q1A2]], &&, null[null[Q1], ==, null[Q1A1]]]");
		//
		// // AND function (e.g. Q==A AND Q==A)
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValQ1A1, new ExpressionOperatorLogic(AvailableOperator.AND),
		// expValQ1, new ExpressionOperatorLogic(
		// AvailableOperator.EQUALS), expValQ1A1));
		// Assert.assertEquals(actual,
		// "null[null[null[Q1], ==, null[Q1A1]], &&, null[null[Q1], ==, null[Q1A1]]]");
		//
		// // Q IN (A1, A2) AND Q IN (A1, A2) AND Q==A
		// actual = parseDrools(new ExpressionChain(expValQ1, new
		// ExpressionFunction(AvailableFunction.IN), expValQ1A1,
		// new ExpressionSymbol(AvailableSymbol.COMMA), expValQ1A2, new
		// ExpressionSymbol(
		// AvailableSymbol.RIGHT_BRACKET), new
		// ExpressionOperatorLogic(AvailableOperator.AND), expValQ1,
		// new ExpressionFunction(AvailableFunction.IN), expValQ1A1, new
		// ExpressionSymbol(AvailableSymbol.COMMA),
		// expValQ1A2, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new
		// ExpressionOperatorLogic(
		// AvailableOperator.AND), expValQ1, new
		// ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// expValQ1A1));
		// Assert.assertEquals(actual,
		// "null[null[null[null[Q1], IN(, null[Q1A1], null[Q1A2]], "
		// + "&&, null[null[Q1], IN(, null[Q1A1], null[Q1A2]]], " +
		// "&&, null[null[Q1], ==, null[Q1A1]]]");
		//
		String actual = parseDrools(new ExpressionChain(expValCVar, new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN), expValQVar,
				new ExpressionSymbol(AvailableSymbol.COMMA), expValQVar, new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));

//		actual = parseDrools(new ExpressionChain(expValCVar, new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
//				expValQVar));
		//
		// System.out.println(actual);

		 // Generics test
		 actual = parseDrools(new ExpressionChain(expValGenericCatScore, new
				 ExpressionOperatorMath(
		 AvailableOperator.ASSIGNATION), new
		 ExpressionFunction(AvailableFunction.MIN), expValGenericQuestScore,
		 new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));

		 System.out.println(actual);

	}

	/**
	 * Parses the given chunk of code and returns pretty-printed result.
	 */
	public static String parse(ExpressionChain source) {
		Parser parser = new ExpressionChainParser(source);
		TreeElementPrintVisitor treePrint = null;
		try {
			ITreeElement resultVisitor = parser.parseExpression();
			treePrint = new TreeElementPrintVisitor();
			resultVisitor.accept(treePrint);

		} catch (ParseException ex) {
			System.out.println(" Error: " + ex.getMessage());
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
		Parser parser = new ExpressionChainParser(source);
		try {
			return parser.parseExpression().getExpressionChain().toString();
		} catch (ParseException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		return "";
	}
}
