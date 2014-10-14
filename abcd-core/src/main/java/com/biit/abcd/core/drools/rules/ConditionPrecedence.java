package com.biit.abcd.core.drools.rules;

/**
 * Specified the precedence of the conditions.<br>
 * The precedence is compared when converting to string to avoid collisions
 * 
 */
public class ConditionPrecedence {
	public static final int MAIN = 1;
	public static final int PREVIOUS = 2;
}
