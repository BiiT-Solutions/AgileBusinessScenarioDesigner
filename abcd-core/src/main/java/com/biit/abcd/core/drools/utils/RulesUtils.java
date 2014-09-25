package com.biit.abcd.core.drools.utils;

import java.util.HashSet;
import java.util.UUID;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

public class RulesUtils {

	public static String getRuleName(String name, ExpressionChain extraConditions) {
		if (extraConditions == null) {
			return "rule \"" + name + "_" + getUniqueId() + "\"\n";
		} else {
			return "rule \"" + name + "_" + extraConditions.getExpression().replaceAll("[^a-zA-Z0-9_]", "_") + "_"
					+ getUniqueId() + "\"\n";
		}
	}

	public static String getWhenRuleString() {
		return "when\n";
	}

	public static String getAttributes() {
		return "";
	}

	public static String getThenRuleString() {
		return "then\n";
	}

	public static String getEndRuleString() {
		return "end\n\n";
	}

	private static String getUniqueId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * Due to the independent parsing of the conditions of the rule, sometimes the algorithm generates repeated rules <br>
	 * This method the lines that are equals in the rule<br>
	 * It should be used before sending the rules to the engine <br>
	 * 
	 * @param ruleCore
	 * @return
	 */
	public static String removeDuplicateLines(String ruleCore) {
		StringBuilder result = new StringBuilder();
		// Parse the resulting rule to delete lines that are equal
		String[] auxSplit = ruleCore.split("\n");
		for (int i = 0; i < auxSplit.length; i++) {
			if ((auxSplit[i] != null)) {
				String compareTo = auxSplit[i];
				for (int j = i + 1; j < auxSplit.length; j++) {
					if ((auxSplit[j] != null) && !auxSplit[i].equals("and") && !auxSplit[i].equals("(")
							&& !auxSplit[i].equals(")")) {
						if (auxSplit[j].equals(compareTo)) {
							auxSplit[j] = null;
						}
					}
				}
				// In the last row, remove the ands (if any)
				if ((i < (auxSplit.length - 1)) && (auxSplit[i + 1] != null) && auxSplit[i + 1].equals("then")
						&& compareTo.endsWith("and")) {
					result.append(compareTo.substring(0, compareTo.length() - 3) + "\n");
				} else {
					result.append(compareTo + "\n");
				}
			}
		}
		return result.toString();
	}

	public static String checkForDuplicatedVariables(String ruleCore) {
		String cleanedResults = "";
		boolean insideRHS = false;
		HashSet<String> variablesAssigned = new HashSet<String>();
		String[] lines = ruleCore.split("\n");
		int sameVariableIndex = 0;
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex];
			if (line.equals("then")) {
				insideRHS = true;
			}
			String[] auxRuleArray = line.split(" : ");
			if (auxRuleArray.length > 1) {
				if (variablesAssigned.contains(auxRuleArray[0])) {
					if (insideRHS) {
						auxRuleArray[0] = auxRuleArray[0] + "0";
					} else {
						auxRuleArray[0] = auxRuleArray[0] + sameVariableIndex;
						sameVariableIndex++;
					}
				} else {
					variablesAssigned.add(auxRuleArray[0]);
				}
				for (int i = 0; i < auxRuleArray.length; i++) {
					if (i == (auxRuleArray.length - 1)) {
						cleanedResults += auxRuleArray[i] + "\n";
					} else {
						cleanedResults += auxRuleArray[i] + " : ";
					}
				}
			} else {
				cleanedResults += line + "\n";
			}
		}
		return cleanedResults;
	}

	public static String removeExtraParenthesis(String ruleCore) {
		String cleanedResults = "";
		String[] lines = ruleCore.split("\n");
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex];
			if (line.equals(")")) {
				// If the previous line was also a parenthesis remove them
				if (lineIndex > 0 && lines[lineIndex - 1].equals("(")) {
					lines[lineIndex - 1] = null;
					lines[lineIndex] = null;
				}
			}
		}
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex];
			if (line != null) {
				cleanedResults += line + "\n";
			}
		}
		return cleanedResults;
	}

	public static String addThenIfNeeded(String ruleCore) {
		String cleanedResults = "";
		boolean thenClause = false;
		String[] lines = ruleCore.split("\n");
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex];
			if (line.equals("then")) {
				thenClause = true;
			}
		}
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			if ((lineIndex == (lines.length - 1)) && (!thenClause)) {
				System.out.println("ENTRA EN IF");

				cleanedResults += getThenRuleString();
			}
			cleanedResults += lines[lineIndex] + "\n";
		}
		return cleanedResults;
	}

	public static String removeLastNLines(String ruleCore, int n) {
		String cleanedResults = "";
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < (lines.length - n); i++) {
			cleanedResults += lines[i] + "\n";
		}
		return cleanedResults;
	}
}
