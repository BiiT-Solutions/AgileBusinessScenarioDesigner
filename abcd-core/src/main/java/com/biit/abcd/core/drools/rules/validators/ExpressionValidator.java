package com.biit.abcd.core.drools.rules.validators;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementExpressionValidatorVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.persistence.entity.Answer;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.plugins.PluginController;
import com.biit.form.entity.TreeObject;
import com.biit.plugins.interfaces.IPlugin;

public class ExpressionValidator {

	/**
	 * Returns true if the rule is valid, false otherwise
	 * 
	 * @param rule
	 * @throws PrattParserException
	 * @throws InvalidExpressionException
	 * @throws NotCompatibleTypeException
	 */
	public static void validateRule(Rule rule) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		validateConditions(rule.getConditions());
		validateActions(rule.getActions());
	}

	public static void validateConditions(ExpressionChain expressionChain) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		if (expressionChain != null && expressionChain.getExpressions().size() > 1) {
			ExpressionChain cleanedExpression = removeNewLineSymbols(RuleGenerationUtils.flattenExpressionChain(expressionChain));
			// If there is a NOT expression, we have to add the remaining
			// parenthesis
			RuleGenerationUtils.fixNotConditions(cleanedExpression);
			ITreeElement rootTreeElement = calculatePrattParserResult(cleanedExpression);
			rootTreeElement.accept(new TreeElementExpressionValidatorVisitor());
			ExpressionChain prattExpressionChain = rootTreeElement.getExpressionChain();
			if (hasPluginMethodExpression(cleanedExpression)) {
				throw new InvalidExpressionException();
			} else {
				// Check the number of elements returned by the Pratt parser
				// (sometimes,
				// the parser doesn't fail, it only skips the invalid
				// characters)
				int parsedElements = countElementsInExpressionChain(prattExpressionChain);
				if (cleanedExpression.getExpressions().size() != parsedElements) {
					throw new InvalidExpressionException();
				}
			}
		} else {
			throw new InvalidExpressionException();
		}
	}

	public static void validateActions(ExpressionChain expressionChain) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		if (expressionChain != null) {
			ValueType leftVariableFormat = null;
			ExpressionChain cleanedExpression = removeNewLineSymbols(RuleGenerationUtils.flattenExpressionChain(expressionChain));
			// If there is a NOT expression, we have to add the remaining
			// parenthesis
			RuleGenerationUtils.fixNotConditions(cleanedExpression);
			ITreeElement rootTreeElement = calculatePrattParserResult(cleanedExpression);
			rootTreeElement.accept(new TreeElementExpressionValidatorVisitor());
			ExpressionChain prattExpressionChain = rootTreeElement.getExpressionChain();

			// The left value must be a variable
			if (!(cleanedExpression.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
					&& !(cleanedExpression.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)) {
				throw new InvalidExpressionException();
			}
			// Assign the value of the left variable
			leftVariableFormat = getExpressionValueType((ExpressionValue<?>) cleanedExpression.getExpressions().get(0));
			// The second expression of the expression chain must be ALWAYS an
			// assignation operator unless it's an IN or BETWEEN function
			if (!(cleanedExpression.getExpressions().get(1) instanceof ExpressionOperatorMath)
					|| !(((ExpressionOperatorMath) cleanedExpression.getExpressions().get(1)).getValue().equals(AvailableOperator.ASSIGNATION))) {
				throw new InvalidExpressionException();
			}
			Integer parsedElements = null;
			// If the third expression is a function, the pratt parser removes
			// the
			// assignation expression
			if (prattExpressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
				// If the expression is a function, we have to check the values
				// inside
				checkExpressionFunctionParameters(leftVariableFormat, prattExpressionChain);
				// Due to the removal of the equals by the parser
				parsedElements = countElementsInExpressionChain(prattExpressionChain) + 1;

			}
			// Plugin calls validation
			else if (prattExpressionChain.getExpressions().get(1) instanceof ExpressionPluginMethod) {
				// If the expression is a function, we have to check the values
				// inside
				validatePluginCall(prattExpressionChain);
				// Due to the removal of the equals by the parser
				parsedElements = countElementsInExpressionChain(prattExpressionChain) + 1;
			}

			else {
				// Check the number of elements returned by the Pratt parser
				// (sometimes, the parser doesn't fail, it only skips the
				// invalid
				// characters)
				parsedElements = countElementsInExpressionChain(prattExpressionChain);
			}
			if (cleanedExpression.getExpressions().size() != parsedElements) {
				throw new InvalidExpressionException();
			}
		} else {
			throw new InvalidExpressionException();
		}
	}

	private static void checkExpressionFunctionParameters(ValueType leftVariableFormat, ExpressionChain prattExpressionChain) throws InvalidExpressionException {
		// The last expression must be a right parenthesis
		if (!(prattExpressionChain.getExpressions().get(prattExpressionChain.getExpressions().size() - 1) instanceof ExpressionSymbol)
				|| !(((ExpressionSymbol) prattExpressionChain.getExpressions().get(prattExpressionChain.getExpressions().size() - 1)).getValue()
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
	private static void checkMethodParameters(ValueType leftVariableFormat, ExpressionChain prattExpressionChain) throws InvalidExpressionException {
		int numberOfParameters = 0;
		ValueType parameterType = null;

		// Check that the parameter type matches
		for (int expressionIndex = 2; expressionIndex < prattExpressionChain.getExpressions().size(); expressionIndex++) {
			Expression expression = prattExpressionChain.getExpressions().get(expressionIndex);
			if ((expression instanceof ExpressionChain) && (((ExpressionChain) expression).getExpressions().get(0) instanceof ExpressionValue<?>)) {
				ExpressionValue<?> expressionValue = (ExpressionValue<?>) ((ExpressionChain) expression).getExpressions().get(0);
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
			if (!leftVariableFormat.equals(ValueType.NUMBER) && !parameterType.equals(ValueType.NUMBER)) {
				throw new InvalidExpressionException();
			}
			break;
		case IF:
			if (prattExpressionChain.getExpressions().size() > 6) {
				if ((prattExpressionChain.getExpressions().get(4) instanceof ExpressionChain)
						&& (prattExpressionChain.getExpressions().get(6) instanceof ExpressionChain)) {
					ExpressionChain thenExpressionChain = (ExpressionChain) prattExpressionChain.getExpressions().get(4);
					ExpressionChain elseExpressionChain = (ExpressionChain) prattExpressionChain.getExpressions().get(6);
					if ((thenExpressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)
							&& (elseExpressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)) {
						ExpressionValue<?> thenExpressionValue = (ExpressionValue<?>) thenExpressionChain.getExpressions().get(0);
						ExpressionValue<?> elseExpressionValue = (ExpressionValue<?>) elseExpressionChain.getExpressions().get(0);
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
	private static ValueType getExpressionValueType(ExpressionValue<?> expression) {
		if (expression instanceof ExpressionValueBoolean) {
			return ValueType.NUMBER;
		} else if (expression instanceof ExpressionValueNumber) {
			return ValueType.NUMBER;
		} else if (expression instanceof ExpressionValuePostalCode) {
			return ValueType.POSTAL_CODE;
		} else if (expression instanceof ExpressionValueString) {
			return ValueType.TEXT;
		} else if ((expression instanceof ExpressionValueTimestamp) || (expression instanceof ExpressionValueSystemDate)) {
			return ValueType.DATE;
		} else if (expression instanceof ExpressionValueGlobalConstant) {
			GlobalVariable globalVariable = ((ExpressionValueGlobalConstant) expression).getValue();
			switch (globalVariable.getFormat()) {
			case MULTI_TEXT:
			case TEXT:
				return ValueType.TEXT;
			case POSTAL_CODE:
				return ValueType.POSTAL_CODE;
			case NUMBER:
				return ValueType.NUMBER;
			case DATE:
				return ValueType.DATE;
			}
		} else if (expression instanceof ExpressionValueCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return ValueType.TEXT;
			case NUMBER:
				return ValueType.NUMBER;
			case DATE:
				return getValueTypeWithUnitDate((ExpressionValueTreeObjectReference) expression);
			}
		} else if (expression instanceof ExpressionValueGenericCustomVariable) {
			CustomVariable customVariable = ((ExpressionValueGenericCustomVariable) expression).getVariable();
			switch (customVariable.getType()) {
			case STRING:
				return ValueType.TEXT;
			case NUMBER:
				return ValueType.NUMBER;
			case DATE:
				return ValueType.DATE;
			}
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			TreeObject treeObject = ((ExpressionValueTreeObjectReference) expression).getReference();
			if (treeObject instanceof Question) {
				return getQuestionValueType((ExpressionValueTreeObjectReference) expression);

			} else if (treeObject instanceof Answer) {
				TreeObject parent = ((Answer) treeObject).getParent();
				if (parent instanceof Answer) {
					// This is a subAnswer
					TreeObject grandParent = ((Answer) parent).getParent();
					return getQuestionValueType(new ExpressionValueTreeObjectReference(grandParent));
				}
				return getQuestionValueType(new ExpressionValueTreeObjectReference(parent));
			}
		}
		return null;
	}

	/**
	 * Returns the type of the question inside the expression value <br>
	 * If the tree object inside is not a question returns null
	 * 
	 * @param expressionValue
	 * @return
	 */
	private static ValueType getQuestionValueType(ExpressionValueTreeObjectReference expressionValue) {
		TreeObject treeObject = expressionValue.getReference();
		if (treeObject instanceof Question) {
			switch (((Question) treeObject).getAnswerType()) {
			case RADIO:
				return ValueType.RADIO;
			case MULTI_CHECKBOX:
				return ValueType.MULTI_CHECKBOX;
			case INPUT:
				switch (((Question) treeObject).getAnswerFormat()) {
				case TEXT:
				case MULTI_TEXT:
					return ValueType.TEXT;
				case POSTAL_CODE:
					return ValueType.POSTAL_CODE;
				case NUMBER:
					return ValueType.NUMBER;
				case DATE:
					return getValueTypeWithUnitDate(expressionValue);
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
	private static ValueType getValueTypeWithUnitDate(ExpressionValueTreeObjectReference expressionValue) {
		if (((ExpressionValueTreeObjectReference) expressionValue).getUnit() != null) {
			switch (((ExpressionValueTreeObjectReference) expressionValue).getUnit()) {
			case YEARS:
			case MONTHS:
			case DAYS:
			case ABSOLUTE_DAYS:
			case ABSOLUTE_MONTHS:
			case ABSOLUTE_YEARS:
				return ValueType.NUMBER;
			case DATE:
				return ValueType.DATE;
			}
		}
		// Default value, if there is no unit selected
		return ValueType.DATE;
	}

	/**
	 * Parses and expressionChain using the Pratt parser
	 * 
	 * @param expressionChain
	 * @return An object with the expression chain parsed inside;
	 * @throws InvalidExpressionException
	 */
	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) throws PrattParserException, InvalidExpressionException {
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
	 * Returns the answer format of the expression value inside a expression
	 * chain of one element
	 * 
	 * @param expressionChain
	 * @return
	 */
	public static ValueType getValueInsideExpressionChain(ExpressionChain expressionChain) {
		int expressionChainSize = expressionChain.getExpressions().size();
		if ((expressionChainSize == 1) && (expressionChain.getExpressions().get(0) instanceof ExpressionValue<?>)) {
			return getExpressionValueType((ExpressionValue<?>) expressionChain.getExpressions().get(0));
		}
		return null;
	}

	/**
	 * Returns the answer format of the expression value inside a expression
	 * chain
	 * 
	 * @param expressionChain
	 * @return
	 */
	public static ValueType getFirstValueInsideExpressionChain(ExpressionChain expressionChain) {
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionValue<?>) {
				return getExpressionValueType((ExpressionValue<?>) expressionChain.getExpressions().get(0));
			} else if (expression instanceof ExpressionChain) {
				return getFirstValueInsideExpressionChain((ExpressionChain) expression);
			}
		}
		return null;
	}

	/**
	 * Removes the 'PILCROW' symbol (new lines) of the expressions
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static ExpressionChain removeNewLineSymbols(ExpressionChain expressionChain) {
		ExpressionChain cleanedExpression = (ExpressionChain) expressionChain.generateCopy();
		for (int index = 0; index < cleanedExpression.getExpressions().size(); index++) {
			if ((cleanedExpression.getExpressions().get(index) instanceof ExpressionSymbol)
					&& (((ExpressionSymbol) cleanedExpression.getExpressions().get(index)).getValue().equals(AvailableSymbol.PILCROW))) {
				cleanedExpression.getExpressions().remove(index);
				index--;
			}
		}
		return cleanedExpression;
	}

	/**
	 * Returns true if the plugin method call is well formed and false otherwise
	 * 
	 * @param expressionChain
	 * @return
	 * @throws InvalidExpressionException
	 */
	private static void validatePluginCall(ExpressionChain prattExpressionChain) throws InvalidExpressionException {
		// The last expression must be a right parenthesis
		if (!(prattExpressionChain.getExpressions().get(prattExpressionChain.getExpressions().size() - 1) instanceof ExpressionSymbol)
				|| !(((ExpressionSymbol) prattExpressionChain.getExpressions().get(prattExpressionChain.getExpressions().size() - 1)).getValue()
						.equals(AvailableSymbol.RIGHT_BRACKET))) {
			throw new InvalidExpressionException();
		}
		checkPluginMethodParameters(prattExpressionChain);
	}

	/**
	 * Checks that the parameters used in the expression chain matches the ones
	 * needed by the plugin method
	 * 
	 * @param expressionChain
	 * @return
	 * @throws InvalidExpressionException
	 */
	private static void checkPluginMethodParameters(ExpressionChain prattExpressionChain) throws InvalidExpressionException {
		ExpressionPluginMethod pluginMethod = (ExpressionPluginMethod) prattExpressionChain.getExpressions().get(1);
		List<Class<?>> parameters = new ArrayList<>();
		for (int expressionIndex = 2; expressionIndex < prattExpressionChain.getExpressions().size(); expressionIndex++) {
			Expression expression = prattExpressionChain.getExpressions().get(expressionIndex);
			if ((expression instanceof ExpressionChain) && (((ExpressionChain) expression).getExpressions().get(0) instanceof ExpressionValue<?>)) {
				ExpressionValue<?> expressionValue = (ExpressionValue<?>) ((ExpressionChain) expression).getExpressions().get(0);
				ValueType valueType = getExpressionValueType(expressionValue);
				if (valueType != null) {
					parameters.add(valueType.getClassType());
				}
			}
		}
		IPlugin pluginInterface = PluginController.getInstance().getPlugin(pluginMethod.getPluginInterface(), pluginMethod.getPluginName());
		if (pluginInterface == null) {
			throw new InvalidExpressionException("Plugin interface: '" + pluginMethod.getPluginInterface() + "' not found for plugin: '"
					+ pluginMethod.getPluginName() + "'");
		}
		try {
			pluginInterface.getPluginMethod(pluginMethod.getPluginMethodName(), listToArray(parameters));
		} catch (NoSuchMethodException e) {
			// If the method is not found, the parameters don't match
			throw new InvalidExpressionException();
		}
	}

	private static Class<?>[] listToArray(List<Class<?>> parameterList) {
		Class<?>[] parameters = new Class<?>[parameterList.size()];
		for (int index = 0; index < parameterList.size(); index++) {
			parameters[index] = parameterList.get(index);
		}
		return parameters;
	}
}
