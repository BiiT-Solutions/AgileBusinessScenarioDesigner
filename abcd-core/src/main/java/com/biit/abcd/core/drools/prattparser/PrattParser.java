package com.biit.abcd.core.drools.prattparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biit.abcd.core.drools.prattparser.parselets.InfixParselet;
import com.biit.abcd.core.drools.prattparser.parselets.PrefixParselet;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.interfaces.IExpressionType;

public class PrattParser {

	private final Iterator<ExpressionToken> mTokens;
	private final List<ExpressionToken> mRead = new ArrayList<ExpressionToken>();
	private final Map<ExpressionTokenType, PrefixParselet> mPrefixParselets = new HashMap<ExpressionTokenType, PrefixParselet>();
	private final Map<ExpressionTokenType, InfixParselet> mInfixParselets = new HashMap<ExpressionTokenType, InfixParselet>();
	private final HashMap<String, ExpressionTokenType> mPunctuators = new HashMap<String, ExpressionTokenType>();

	public PrattParser(List<Expression> tokens) {
		this.mTokens = this.preParsing(tokens);
	}

	/**
	 * Simplifies the posterior calculus<br>
	 * Transforms the list of expressions in a list of expression tokens, with
	 * the type more accessible
	 * 
	 * @param tokens
	 * @return
	 */
	private Iterator<ExpressionToken> preParsing(List<Expression> tokens) {
		List<ExpressionToken> expTokenList = new ArrayList<ExpressionToken>();

		// Register all of the TokenTypes that are explicit punctuators.
		for (ExpressionTokenType type : ExpressionTokenType.values()) {
			String punctuator = type.punctuator();
			if (punctuator != null) {
				this.mPunctuators.put(punctuator, type);
			}
		}

		// for (Entry<String, ExpressionTokenType> entry :
		// this.mPunctuators.entrySet()) {
		// System.out.println("Punctuator KEY: " + entry.getKey() + " TYPE: " +
		// entry.getValue() + " CLASS: "
		// + entry.getKey().getClass());
		// }

		for (int expIndex = 0; expIndex < tokens.size(); expIndex++) {
			Expression expression = tokens.get(expIndex);
			if ((expression instanceof ExpressionOperatorMath)
					&& ((ExpressionOperatorMath) expression).getValue().equals(AvailableOperator.ASSIGNATION)
					&& ((expIndex + 1) <= tokens.size())) {
				Expression auxExp = tokens.get(expIndex + 1);
				if ((auxExp instanceof ExpressionFunction) || (auxExp instanceof ExpressionPluginMethod)) {
					// We skip the assignation, because the function needs to be
					// an infix operator
					continue;
				}
			}
			// Ignore new line symbols.
			if (expression instanceof ExpressionSymbol) {
				if (((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.PILCROW)) {
					continue;
				}
			} 
//			else if ((expression instanceof ExpressionFunction)
//					&& (((ExpressionFunction) expression).getValue().equals(AvailableFunction.NOT))) {
//				expTokenList.add(new ExpressionToken(ExpressionTokenType.NOT, expression));
//				expTokenList.add(new ExpressionToken(ExpressionTokenType.LEFT_BRACKET, new ExpressionSymbol(
//						AvailableSymbol.LEFT_BRACKET)));
//			}
			// If it is an operator
			if (expression instanceof IExpressionType<?>) {
				// System.out.println("EXPRESSION TYPE : " + expression);
				String expressionType = ((IExpressionType<?>) expression).getValue().toString();
				if (this.mPunctuators.containsKey(expressionType)) {
					expTokenList.add(new ExpressionToken(this.mPunctuators.get(expressionType), expression));
				}
			} else if (expression instanceof ExpressionPluginMethod) {
				// System.out.println("PLUGIN METHOD : " + expression);

				if (this.mPunctuators.containsKey("IPlugin")) {
					expTokenList.add(new ExpressionToken(this.mPunctuators.get("IPlugin"), expression));
				}
			} else if (expression instanceof ExpressionChain) {
				// System.out.println("INTERNAL EXPRESSION CHAIN: " +
				// ((ExpressionChain) expression).getExpressions());

				preParsing(((ExpressionChain) expression).getExpressions());
			} else {
				// All the other posibilities
				expTokenList.add(new ExpressionToken(ExpressionTokenType.NAME, expression));
			}
		}
		// for (ExpressionToken expToken : expTokenList) {
		// System.out.println("EXPRESSION: " + expToken + " - TYPE: "
		// + expToken.getType());
		// }
		expTokenList.add(new ExpressionToken(ExpressionTokenType.EOF, new ExpressionValueString("")));
		// System.out.println(expTokenList);
		return expTokenList.iterator();
	}

	public void register(ExpressionTokenType token, PrefixParselet parselet) {
		this.mPrefixParselets.put(token, parselet);
	}

	public void register(ExpressionTokenType token, InfixParselet parselet) {
		this.mInfixParselets.put(token, parselet);
	}

	public ITreeElement parseExpression(int precedence) throws PrattParserException {
		ExpressionToken token = this.consume();

		// System.out.println("TOKEN CONSUMED 1: " + token);

		PrefixParselet prefix = this.mPrefixParselets.get(token.getType());

		// System.out.println("TOKEN CONSUMED 2: " + prefix);

		if (prefix == null) {
			throw new PrattParserException("Could not parse \"" + token.toString() + "\".");
		}

		ITreeElement left = prefix.parse(this, token);

		while (precedence < this.getPrecedence()) {
			token = this.consume();

			// System.out.println("TOKEN CONSUMED 3: " + token);

			InfixParselet infix = this.mInfixParselets.get(token.getType());

			// System.out.println("TOKEN CONSUMED 4: " + infix);
			left = infix.parse(this, left, token);
		}
		return left;
	}

	public ITreeElement parseExpression() throws PrattParserException {
		return this.parseExpression(0);
	}

	public boolean match(ExpressionTokenType expected) {
		ExpressionToken token = this.lookAhead(0);
		if (token.getType() != expected) {
			return false;
		}
		this.consume();
		return true;
	}

	public ExpressionToken consume(ExpressionTokenType expected) {
		ExpressionToken token = this.lookAhead(0);
		if (token.getType() != expected) {
			throw new RuntimeException("Expected token " + expected + " and found " + token.getType());
		}
		return this.consume();
	}

	public ExpressionToken consume() {
		// Make sure we've read the token.
		this.lookAhead(0);

		return this.mRead.remove(0);
	}

	private ExpressionToken lookAhead(int distance) {
		// Read in as many as needed.
		while (distance >= this.mRead.size()) {
			// System.out.println("BEFORE NEXT");
			ExpressionToken et = this.mTokens.next();
			// System.out.println("AFTER NEXT  - ADD TO MREAD " + et);
			this.mRead.add(et);
		}

		// Get the queued token.
		return this.mRead.get(distance);
	}

	private int getPrecedence() {
		InfixParselet parser = this.mInfixParselets.get(this.lookAhead(0).getType());
		if (parser != null) {
			return parser.getPrecedence();
		}

		return 0;
	}

}
