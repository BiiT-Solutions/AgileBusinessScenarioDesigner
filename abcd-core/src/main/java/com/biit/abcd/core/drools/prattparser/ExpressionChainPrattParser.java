package com.biit.abcd.core.drools.prattparser;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.parselets.AssignParselet;
import com.biit.abcd.core.drools.prattparser.parselets.BinaryOperatorParselet;
import com.biit.abcd.core.drools.prattparser.parselets.CallParselet;
import com.biit.abcd.core.drools.prattparser.parselets.GroupParselet;
import com.biit.abcd.core.drools.prattparser.parselets.NameParselet;
import com.biit.abcd.core.drools.prattparser.parselets.PostfixOperatorParselet;
import com.biit.abcd.core.drools.prattparser.parselets.PrefixOperatorParselet;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * Extends the generic Parser class with support for parsing the actual Bantam
 * grammar.
 */
public class ExpressionChainPrattParser extends PrattParser {

	public ExpressionChainPrattParser(ExpressionChain expressionChain) {
		super(standardizeExpressions(expressionChain.getExpressions()));
		registerParselets();
	}

	public ExpressionChainPrattParser(List<Expression> expressions) {
		super(standardizeExpressions(expressions));
		registerParselets();
	}

	/**
	 * Register all of the parselets for the grammar.
	 */
	private void registerParselets() {
		// Register the ones that need special parselets.
		register(ExpressionTokenType.NAME, new NameParselet());
		register(ExpressionTokenType.ASSIGNATION, new AssignParselet());
		register(ExpressionTokenType.LEFT_BRACKET, new GroupParselet());
		register(ExpressionTokenType.BETWEEN, new CallParselet());
		register(ExpressionTokenType.IN, new CallParselet());
		register(ExpressionTokenType.MIN, new CallParselet());
		register(ExpressionTokenType.MAX, new CallParselet());
		register(ExpressionTokenType.AVG, new CallParselet());
		register(ExpressionTokenType.SUM, new CallParselet());
		register(ExpressionTokenType.CONCAT, new CallParselet());
		register(ExpressionTokenType.CONCAT_SEPARATOR, new CallParselet());
		register(ExpressionTokenType.ELEMENT_ID, new CallParselet());
		register(ExpressionTokenType.ELEMENT_NAME, new CallParselet());
		register(ExpressionTokenType.ELEMENT_PATH, new CallParselet());
		register(ExpressionTokenType.ELEMENT_XPATH, new CallParselet());
		register(ExpressionTokenType.PMT, new CallParselet());
		register(ExpressionTokenType.IF, new CallParselet());
		register(ExpressionTokenType.LOG, new CallParselet());
		// Special token for parsing plugin calls
		register(ExpressionTokenType.IPLUGIN, new CallParselet());

		// Register the simple operator parselets.
		prefix(ExpressionTokenType.NOT, Precedence.PREFIX);
		infixLeft(ExpressionTokenType.PLUS, Precedence.SUM);
		infixLeft(ExpressionTokenType.MINUS, Precedence.SUM);
		infixLeft(ExpressionTokenType.MULTIPLICATION, Precedence.PRODUCT);
		infixLeft(ExpressionTokenType.DIVISION, Precedence.PRODUCT);
		infixLeft(ExpressionTokenType.MODULE, Precedence.PRODUCT);
		infixLeft(ExpressionTokenType.AND, Precedence.LOGIC_AND);
		infixLeft(ExpressionTokenType.OR, Precedence.LOGIC_OR);
		infixLeft(ExpressionTokenType.EQUALS, Precedence.COMP_EQ_NEQ);
		infixLeft(ExpressionTokenType.NOT_EQUALS, Precedence.COMP_EQ_NEQ);
		infixLeft(ExpressionTokenType.GREATER_THAN, Precedence.COMP_LT_GT);
		infixLeft(ExpressionTokenType.GREATER_EQUALS, Precedence.COMP_LT_GT);
		infixLeft(ExpressionTokenType.LESS_THAN, Precedence.COMP_LT_GT);
		infixLeft(ExpressionTokenType.LESS_EQUALS, Precedence.COMP_LT_GT);
		infixRight(ExpressionTokenType.POW, Precedence.EXPONENT);
	}

	/**
	 * Registers a postfix unary operator parselet for the given token and
	 * precedence.
	 */
	public void postfix(ExpressionTokenType token, int precedence) {
		register(token, new PostfixOperatorParselet(precedence));
	}

	/**
	 * Registers a prefix unary operator parselet for the given token and
	 * precedence.
	 */
	public void prefix(ExpressionTokenType token, int precedence) {
		register(token, new PrefixOperatorParselet(precedence));
	}

	/**
	 * Registers a left-associative binary operator parselet for the given token
	 * and precedence.
	 */
	public void infixLeft(ExpressionTokenType token, int precedence) {
		register(token, new BinaryOperatorParselet(precedence, false));
	}

	/**
	 * Registers a right-associative binary operator parselet for the given
	 * token and precedence.
	 */
	public void infixRight(ExpressionTokenType token, int precedence) {
		register(token, new BinaryOperatorParselet(precedence, true));
	}

	/**
	 * Transforms the list of expression received (that can have expression
	 * chains inside) in a flat expression list, leaving all the expressions at
	 * the same level for the parser
	 * 
	 * @param expressions
	 * @return
	 */
	private static List<Expression> standardizeExpressions(List<Expression> expressions) {
		List<Expression> result = new ArrayList<Expression>();
		for (Expression expression : expressions) {
			if (expression instanceof ExpressionChain) {
				result.addAll(standardizeExpressions(((ExpressionChain) expression).getExpressions()));
			} else {
				result.add(expression);
			}
		}
		return result;
	}
}