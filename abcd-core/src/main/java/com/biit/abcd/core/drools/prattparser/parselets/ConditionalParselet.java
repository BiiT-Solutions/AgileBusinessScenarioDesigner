package com.biit.abcd.core.drools.prattparser.parselets;
//package com.biit.parser.parselets;
//
//
//import com.biit.parser.Parser;
//import com.biit.parser.Precedence;
//import com.biit.parser.ExpressionToken;
//import com.biit.parser.ExpressionTokenType;
//import com.biit.parser.expressions.ConditionalExpression;
//import com.biit.parser.expressions.ExpressionInterface;
//
///**
// * Parselet for the condition or "ternary" operator, like "a ? b : c".
// */
//public class ConditionalParselet implements InfixParselet {
//  @Override
//public ExpressionInterface parse(Parser parser, ExpressionInterface left, ExpressionToken token) {
//    ExpressionInterface thenArm = parser.parseExpression();
//    parser.consume(ExpressionTokenType.COLON);
//    ExpressionInterface elseArm = parser.parseExpression(Precedence.CONDITIONAL - 1);
//
//    return new ConditionalExpression(left, thenArm, elseArm);
//  }
//
//  @Override
//public int getPrecedence() {
//    return Precedence.CONDITIONAL;
//  }
// }