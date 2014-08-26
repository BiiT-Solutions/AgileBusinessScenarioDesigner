package com.biit.abcd.core.drools.prattparser;


/**
 * Defines the different precendence levels used by the infix parsers. These
 * determine how a series of infix expressions will be grouped. For example,
 * "a + b * c - d" will be parsed as "(a + (b * c)) - d" because "*" has higher
 * precedence than "+" and "-". Here, bigger numbers mean higher precedence.
 */
public class Precedence {
  // Ordered in increasing precedence.
  public static final int ASSIGNMENT  = 1;
  public static final int CONDITIONAL = 2;
  public static final int LOGIC_OR    = 3;
  public static final int LOGIC_AND   = 4;
  public static final int COMP_EQ_NEQ = 5;
  public static final int COMP_LT_GT  = 6;
  public static final int SUM         = 7;
  public static final int PRODUCT     = 8;
  public static final int EXPONENT    = 9;
  public static final int PREFIX      = 10;
  public static final int POSTFIX     = 11;
  public static final int CALL        = 12;
}
