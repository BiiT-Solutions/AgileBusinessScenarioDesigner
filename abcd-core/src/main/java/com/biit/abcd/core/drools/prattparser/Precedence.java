package com.biit.abcd.core.drools.prattparser;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
  public static final int SQRT_ABS    = 10;
  public static final int PREFIX      = 11;
  public static final int POSTFIX     = 12;
  public static final int CALL        = 13;
}
