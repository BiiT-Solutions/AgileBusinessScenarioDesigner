package com.biit.abcd.core.drools.rules.validators;

import java.sql.Date;
import java.sql.Timestamp;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementExpressionValidatorVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValue;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.TreeObject;

public class ExpressionValidator {

	/**
	 * Returns true if the rule is valid, false otherwise
	 * 
	 * @param rule
	 * @throws PrattParserException
	 * @throws InvalidExpressionException
	 * @throws NotCompatibleTypeException
	 */
	public static void validateRule(Rule rule) throws PrattParserException, InvalidExpressionException,
			NotCompatibleTypeException {
		validateConditions(rule.getConditions());
		validateActions(rule.getActions());
	}

	public static void validateConditions(ExpressionChain expressionChain) throws PrattParserException,
			InvalidExpressionException, NotCompatibleTypeException {

		ExpressionChain cleanedExpression = removePilcrow(expressionChain);
		
		ITreeElement rootTreeElement = calculatePrattParserResult(cleanedExpression);
		rootTreeElement.accept(new TreeElementExpressionValidatorVisitor());

		ExpressionChain prattExpressionChain = rootTreeElement.getExpressionChain();
		if (hasPluginMethodExpression(cleanedExpression)) {
			throw new InvalidExpressionException();
		} else {
			// Check the number of elements returned by the Pratt parser
			// (sometimes,
			// the parser doesn't fail, it only skips the invalid characters)
			int parsedElements = countElementsInExpressionChain(prattExpressionChain);
			System.out.println("NUMBER OF ELEMENTS PARSED: " + parsedElements);
			System.out.println("NUMBER OF ELEMENTS IN CHAIN: " + cleanedExpression.getExpressions().size());
			if (cleanedExpression.getExpressions().size() != parsedElements) {
				throw new InvalidExpressionException();
			}
		}
		System.out.println("CONDITION CHAIN: " + prattExpressionChain);

	}

	public static void validateActions(ExpressionChain expressionChain) throws PrattParserException,
			InvalidExpressionException {
		AnswerFormat leftVariableFormat = null;
		ExpressionChain prattExpressionChain = calculatePrattParserResult(expressionChain).getExpressionChain();

		System.out.println("ACTION CHAIN: " + prattExpressionChain);

		// The left value must be a variable
		if (!(expressionChain.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
				&& !(expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)) {
			throw new InvalidExpressionException();
		}
		// Assign the value of the left variable
		leftVariableFormat = getExpressionValueType((ExpressionValue<?>) expressionChain.getExpressions().get(0));
		// The second expression of the expression chain must be ALWAYS an
		// assignation operator unless it's an IN or BETWEEN function
		if (!(expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				|| !(((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))) {
			if (expressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
				ExpressionFunction expressionFunction = (ExpressionFunction) expressionChain.getExpressions().get(1);
				switch (expressionFunction.getValue()) {
				case IN:
				case BETWEEN:
					// Do nothing
					break;
				default:
					throw new InvalidExpressionException();
				}
			} else {
				throw new InvalidExpressionException();
			}
		}
		Integer parsedElements = null;
		// If the third expression is a function, the pratt parser removes the
		// assignation expression
		if (prattExpressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
			// If the expression is a function, we have to check the values
			// inside
			checkExpressionFunctionParameters(leftVariableFormat, prattExpressionChain);
			// Due to the removal of the equals by the parser
			parsedElements = countElementsInExpressionChain(prattExpressionChain) + 1;
		} else {
			// Check the number of elements returned by the Pratt parser
			// (sometimes, the parser doesn't fail, it only skips the invalid
			// characters)
			parsedElements = countElementsInExpressionChain(prattExpressionChain);
		}
		if (expressionChain.getExpressions().size() != parsedElements) {
			throw new InvalidExpressionException();
		}
	}

	private static void checkExpressionFunctionParameters(AnswerFormat leftVariableFormat,
			ExpressionChain prattExpressionChain) throws InvalidExpressionException {
		// The last expression must be a right parenthesis
		if (!(prattExpressionChain.getExpressions().get(prattExpressionChain.getExpressions().size() - 1) instanceof ExpressionSymbol)
				|| !(((ExpressionSymbol) prattExpressionChain.getExpressions().get(
						prattExpressionChain.getExpressions().size() - 1)).getValue()
						.equals(AvailableSymbol.RIGHT_BRACKET))) {
			throw new InvalidExpressionException();
		}
		checkMethodParameters(leftVariableFormat, prattExpressionChain);
	}

	/**
	 * Checks that the parameters used in the expression chain match between
	 * them
	 * 
	 * @param prattExpressionChain
	 * @return
	 * @throws InvalidExpressionException
	 */
	private static void checkMethodParameters(AnswerFormat leftVariableFormat, ExpressionChain prattExpressionChain)
			throws InvalidExpressionException {
		int numberOfParameters = 0;
		AnswerFormat parameterType = null;

		// Check that the parameter type matches
		for (int expressionIndex = 2; expressionIndex < prattExpressionChain.getExpressions().size(); expressionIndex++) {
			Expression expression = prattExpressionChain.getExpressions().get(expressionIndex);
			if ((expression instanceof ExpressionChain)
					&& (((ExpressionChain) expression).getExpressions().get(0) instanceof ExpressionValue<?>)) {
				ExpressionValue<?> expressionValue = (ExpressionValue<?>) ((ExpressionChain) expression)
						.getExpressions().get(0);
				numberOfParameters++;
				// First parameter
				if (parameterType == null) {
					parameterType = getExpressionValueType(expressionValue);
				} else {
					// After the parameterType being set, the rest of the
					// parameters must match the value type
					if (!parameterType.equals(getExpressionValueType(expressionValue))) {
						throw new InvalidExpressionException();
					}
				}
			}
		}

		// Check that the number of parameters is correct
		switch (((ExpressionFunction) prattExpressionChain.getExpressions().get(1)).getValue()) {
		case BETWEEN:
			if (numberOfParameters != 2) {
				throw new InvalidExpressionException();
			}
			break;
		case PMT:
			if (numberOfParameters != 3) {
				throw new InvalidExpressionException();
			}
		default:
			if (numberOfParameters == 0) {
				throw new InvalidExpressionException();
			}
		}

		// Check that the left variable type matches the function type
		switch (((ExpressionFunction) prattExpressionChain.getExpressions().get(1)).getValue()) {
		case PMT:
		case AVG:
		case MAX:
		case MIN:
		case SUM:
			if (!leftVariableFormat.equals(AnswerFormat.NUMBER) && !parameterType.equals(AnswerFormat.NUMBER)) {
				throw new InvalidExpressionException();
			}
			break;
		case IF:
			if (prattExpressionChain.getExpressions().size() > 6) {
				if ((prattExpressionChain.getExpressions().get(4) instanceof ExpressionChain)
						&& (prattExpressionChain.getExpressions().get(6) instanceof ExpressionChain)) {
					ExpressionChain thenExpressionChain = (ExpressionChain) prattExpressionChain.getExpressions()
							.get(4);
					ExpressionChain elseExpressionChain = (ExpressionChain) prattExpressionChain.getExpressions()
							.get(6);
					if ((thenExpressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)
							&& (elseExpressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)) {
						ExpressionValue<?> thenExpressionValue = (ExpressionValue<?>) thenExpressionChain
								.getExpressions().get(0);
						ExpressionValue<?> elseExpressionValue = (ExpressionValue<?>) elseExpressionChain
								.getExpressions().get(0);
						if (!leftVariableFormat.equals(getExpressionValueType(thenExpressionValue))
								&& !parameterType.equals(getExpressionValueType(elseExpressionValue))) {
							throw new InvalidExpressionException();
						}
					} else {
						throw new InvalidExpressionException();
					}
				} else {
					throw new InvalidExpressionException();
				}
			} else {
				throw new InvalidExpressionException();
			}
			break;
		default:
			if (!leftVariableFormat.equals(parameterType)) {
				throw new InvalidExpressionException();
			}
		}
	}

	/**
	 * Returns the type of the value inside the expression passed (if there is
	 * any)
	 * 
	 * @param expression
	 * @return
	 */
	private static AnswerFormat getExpressionValueType(ExpressionValue<?> expression) {
		if (expression instanceof ExpressionValueBoolean) {
			return AnswerFormat.NUMBER;
		} else if (expression instanceof ExpressionValueNumber) {
			return AnswerFormat.NUMBER;
		} else if (expression instanceof ExpressionValuePostalCode) {
			return AnswerFormat.POSTAL_CODE;
		} else if (expression instanceof ExpressionValueString) {
			return AnswerFormat.TEXT;
		} else if ((expression instanceof ExpressionValueTimestamp)
				|| (expression instanceof ExpressionValueSystemDate)) {
			return AnswerFormat.DATE;
		} else if (expression instanceof ExpressionValueCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return AnswerFormat.TEXT;
			case NUMBER:
				return AnswerFormat.NUMBER;
			case DATE:
				return getAnswerFormatWithUnitDate((ExpressionValueTreeObjectReference) expression);
			}
		} else if (expression instanceof ExpressionValueGenericCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueGenericCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return AnswerFormat.TEXT;
			case NUMBER:
				return AnswerFormat.NUMBER;
			case DATE:
				return AnswerFormat.DATE;
			}
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			TreeObject treeObject = ((ExpressionValueTreeObjectReference) expression).getReference();
			if (!(treeObject instanceof Question)
					|| !(((Question) treeObject).getAnswerType().equals(AnswerType.INPUT))) {
				return null;
			} else {
				switch (((Question) treeObject).getAnswerFormat()) {
				case TEXT:
				case POSTAL_CODE:
				case NUMBER:
					return ((Question) treeObject).getAnswerFormat();
				case DATE:
					return getAnswerFormatWithUnitDate((ExpressionValueTreeObjectReference) expression);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the correct answer format based on the unit selected in the
	 * expression created
	 * 
	 * @param expressionValue
	 * @return
	 */
	private static AnswerFormat getAnswerFormatWithUnitDate(ExpressionValueTreeObjectReference expressionValue) {
		if (((ExpressionValueTreeObjectReference) expressionValue).getUnit() != null) {
			switch (((ExpressionValueTreeObjectReference) expressionValue).getUnit()) {
			case YEARS:
			case MONTHS:
			case DAYS:
				return AnswerFormat.NUMBER;
			case DATE:
				return AnswerFormat.DATE;
			}
		}
		// Default value, if there is no unit selected
		return AnswerFormat.DATE;
	}

	/**
	 * Returns the class that represents the value inside the expression passed
	 * (if there is any)
	 * 
	 * @param expression
	 * @return
	 */
	private static Class<?> getExpressionValueClass(ExpressionValue<?> expression) {
		if (expression instanceof ExpressionValueBoolean) {
			return Boolean.class;
		} else if (expression instanceof ExpressionValueNumber) {
			return Double.class;
		} else if ((expression instanceof ExpressionValuePostalCode) || (expression instanceof ExpressionValueString)) {
			return String.class;
		} else if ((expression instanceof ExpressionValueTimestamp)
				|| (expression instanceof ExpressionValueSystemDate)) {
			return Timestamp.class;
		} else if (expression instanceof ExpressionValueCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return String.class;
			case NUMBER:
				return Double.class;
			case DATE:
				return Date.class;
			}
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			TreeObject treeObject = ((ExpressionValueTreeObjectReference) expression).getReference();
			if (!(treeObject instanceof Question)
					|| !(((Question) treeObject).getAnswerType().equals(AnswerType.INPUT))) {
				return null;
			} else {
				switch (((Question) treeObject).getAnswerFormat()) {
				case TEXT:
				case POSTAL_CODE:
					return String.class;
				case NUMBER:
					return Double.class;
				case DATE:
					if (((ExpressionValueTreeObjectReference) expression).getUnit() != null) {
						switch (((ExpressionValueTreeObjectReference) expression).getUnit()) {
						case YEARS:
						case MONTHS:
						case DAYS:
							return Integer.class;
						case DATE:
							return Date.class;
						}
					} else {
						return Date.class;
					}
					break;
				}
			}
		}
		return null;
	}

	/**
	 * Parses and expressionChain using the Pratt parser
	 * 
	 * @param expressionChain
	 * @return An object with the expression chain parsed inside;
	 * @throws InvalidExpressionException
	 */
	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain)
			throws PrattParserException, InvalidExpressionException {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		prattParserResult = prattParser.parseExpression();
		if ((prattParserResult == null) || (prattParserResult.getExpressionChain() == null)
				|| (prattParserResult.getExpressionChain().getExpressions().isEmpty())) {
			throw new InvalidExpressionException();
		}
		return prattParserResult;
	}

	protected static boolean hasPluginMethodExpression(ExpressionChain expressions) {
		if (expressions != null) {
			for (Expression expression : expressions.getExpressions()) {
				if (expression instanceof ExpressionPluginMethod) {
					return true;
				} else if (expression instanceof ExpressionChain) {
					return hasPluginMethodExpression((ExpressionChain) expression);
				}
			}
		}
		return false;
	}

	private static int countElementsInExpressionChain(ExpressionChain expressionChain) {
		int numberOfElements = 0;
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				numberOfElements += countElementsInExpressionChain((ExpressionChain) expression);
			} else {
				numberOfElements++;
			}
		}
		return numberOfElements;
	}

	/**
	 * Returns the answer format of the expression value inside the expression
	 * chain
	 * 
	 * @param expressionChain
	 * @return
	 */
	public static AnswerFormat getValueInsideExpressionChain(ExpressionChain expressionChain) {
		int expressionChainSize = expressionChain.getExpressions().size();
		if ((expressionChainSize == 1) && (expressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)) {
			return getExpressionValueType((ExpressionValue<?>) expressionChain.getExpressions().get(0));
		}
		return null;
	}

	// private static AnswerFormat
	// checkValuesInsideExpressionChain(ExpressionChain expressionChain) {
	// int expressionChainSize = expressionChain.getExpressions().size();
	// if ((expressionChainSize == 1) &&
	// (expressionChain.getExpressions().get(0) instanceof ExpressionValue<?>))
	// {
	// return getExpressionValueType((ExpressionValue<?>)
	// expressionChain.getExpressions().get(0));
	// } else if ((expressionChainSize == 3)
	// && (expressionChain.getExpressions().get(0) instanceof
	// ExpressionValue<?>)
	// && (expressionChain.getExpressions().get(2) instanceof
	// ExpressionValue<?>)) {
	// AnswerFormat leftValue = getExpressionValueType((ExpressionValue<?>)
	// expressionChain.getExpressions()
	// .get(0));
	// AnswerFormat rightValue = getExpressionValueType((ExpressionValue<?>)
	// expressionChain.getExpressions().get(
	// 2));
	// if (leftValue != rightValue)
	// return;
	// }
	// }

	// if (hasPluginMethodExpression(expressions)) {
	// // For the plugin we also check that the parameters match the method
	// if (PluginController.getInstance().validateExpressionChain(expressions))
	// {
	// evaluatorOutput.setStyleName("expression-valid");
	// evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_VALID));
	// } else {
	// evaluatorOutput.setStyleName("expression-invalid");
	// evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_INVALID));
	// }
	// } else {
	// try {
	// expressions.getExpressionEvaluator().eval();
	// evaluatorOutput.setStyleName("expression-valid");
	// evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_VALID));
	// } catch (Exception e) {
	// AbcdLogger.debug(ExpressionViewer.class.getName(), e.getMessage());
	// evaluatorOutput.setStyleName("expression-invalid");
	// evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_INVALID));
	// }
	// }

	private static ExpressionChain removePilcrow(ExpressionChain expressionChain) {
		ExpressionChain cleanedExpression = (ExpressionChain) expressionChain.generateCopy();
		for (int index = 0; index < cleanedExpression.getExpressions().size(); index++) {

			if ((cleanedExpression.getExpressions().get(index) instanceof ExpressionSymbol)
					&& (((ExpressionSymbol) cleanedExpression.getExpressions().get(index)).getValue()
							.equals(AvailableSymbol.PILCROW))) {
				cleanedExpression.getExpressions().remove(index);
				index--;
			}
		}
		return cleanedExpression;
	}
}
