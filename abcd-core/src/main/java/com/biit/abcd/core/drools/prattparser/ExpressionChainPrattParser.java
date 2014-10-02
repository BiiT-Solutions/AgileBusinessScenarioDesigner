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
		this.registerParselets();
	}

	public ExpressionChainPrattParser(List<Expression> expressions) {
		super(standardizeExpressions(expressions));
		this.registerParselets();
	}

	/**
	 * Register all of the parselets for the grammar.
	 */
	private void registerParselets() {
		// Register the ones that need special parselets.
		this.register(ExpressionTokenType.NAME, new NameParselet());
		this.register(ExpressionTokenType.ASSIGNATION, new AssignParselet());
		this.register(ExpressionTokenType.LEFT_BRACKET, new GroupParselet());
		this.register(ExpressionTokenType.LEFT_BRACKET, new CallParselet());
		this.register(ExpressionTokenType.BETWEEN, new CallParselet());
		this.register(ExpressionTokenType.IN, new CallParselet());
		this.register(ExpressionTokenType.MIN, new CallParselet());
		this.register(ExpressionTokenType.MAX, new CallParselet());
		this.register(ExpressionTokenType.AVG, new CallParselet());
		this.register(ExpressionTokenType.SUM, new CallParselet());
		this.register(ExpressionTokenType.PMT, new CallParselet());
//		// Although it uses the CallParselet, we defined some special conditions
//		// for the IF
//		this.register(ExpressionTokenType.IF, new CallParselet());

		// Register the simple operator parselets.
		this.prefix(ExpressionTokenType.PLUS, Precedence.PREFIX);
		this.prefix(ExpressionTokenType.MINUS, Precedence.PREFIX);
		this.prefix(ExpressionTokenType.NOT, Precedence.PREFIX);

		// // For kicks, we'll make "!" both prefix and postfix, kind of like
		// ++.
		// postfix(ExpressionTokenType.NOT, Precedence.POSTFIX);

		this.infixLeft(ExpressionTokenType.PLUS, Precedence.SUM);
		this.infixLeft(ExpressionTokenType.MINUS, Precedence.SUM);
		this.infixLeft(ExpressionTokenType.MULTIPLICATION, Precedence.PRODUCT);
		this.infixLeft(ExpressionTokenType.DIVISION, Precedence.PRODUCT);
		this.infixLeft(ExpressionTokenType.MODULE, Precedence.PRODUCT);
		this.infixLeft(ExpressionTokenType.AND, Precedence.LOGIC_AND);
		this.infixLeft(ExpressionTokenType.OR, Precedence.LOGIC_OR);
		this.infixLeft(ExpressionTokenType.EQUALS, Precedence.COMP_EQ_NEQ);
		this.infixLeft(ExpressionTokenType.NOT_EQUALS, Precedence.COMP_EQ_NEQ);
		this.infixLeft(ExpressionTokenType.GREATER_THAN, Precedence.COMP_LT_GT);
		this.infixLeft(ExpressionTokenType.GREATER_EQUALS, Precedence.COMP_LT_GT);
		this.infixLeft(ExpressionTokenType.LESS_THAN, Precedence.COMP_LT_GT);
		this.infixLeft(ExpressionTokenType.LESS_EQUALS, Precedence.COMP_LT_GT);
		this.infixRight(ExpressionTokenType.POW, Precedence.EXPONENT);
	}

	/**
	 * Registers a postfix unary operator parselet for the given token and
	 * precedence.
	 */
	public void postfix(ExpressionTokenType token, int precedence) {
		this.register(token, new PostfixOperatorParselet(precedence));
	}

	/**
	 * Registers a prefix unary operator parselet for the given token and
	 * precedence.
	 */
	public void prefix(ExpressionTokenType token, int precedence) {
		this.register(token, new PrefixOperatorParselet(precedence));
	}

	/**
	 * Registers a left-associative binary operator parselet for the given token
	 * and precedence.
	 */
	public void infixLeft(ExpressionTokenType token, int precedence) {
		this.register(token, new BinaryOperatorParselet(precedence, false));
	}

	/**
	 * Registers a right-associative binary operator parselet for the given
	 * token and precedence.
	 */
	public void infixRight(ExpressionTokenType token, int precedence) {
		this.register(token, new BinaryOperatorParselet(precedence, true));
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