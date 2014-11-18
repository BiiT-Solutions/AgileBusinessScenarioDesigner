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
	 * Due to the independent parsing of the conditions of the rule, sometimes
	 * the algorithm generates repeated rules <br>
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
					if ((auxSplit[j] != null) && !auxSplit[i].equals("\tand") && !auxSplit[i].equals("\t(")
							&& !auxSplit[i].equals("\t)") && !auxSplit[i].trim().equals("(")
							&& !auxSplit[i].trim().equals(")") && !auxSplit[i].equals("\tor")
							&& !auxSplit[i].equals("\tnot(")) {
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
		// int sameVariableIndex = 0;
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex];
			if (line.equals("then")) {
				insideRHS = true;
			}
			String[] auxRuleArray = line.split(" : ");
			if (auxRuleArray.length > 1) {
				if (variablesAssigned.contains(auxRuleArray[0].replace("(\t", ""))) {
					auxRuleArray[0] = "\t";
					// if (insideRHS) {
					// auxRuleArray[0] = auxRuleArray[0] + "0";
					// } else {
					// auxRuleArray[0] = auxRuleArray[0] + sameVariableIndex;
					// sameVariableIndex++;
					// }
				} else {
					variablesAssigned.add(auxRuleArray[0].replace("(\t", ""));
				}

				if (!auxRuleArray[0].equals("\t")) {
					cleanedResults += auxRuleArray[0] + " : ";
				} else {
					cleanedResults += auxRuleArray[0];
				}
				for (int i = 1; i < auxRuleArray.length; i++) {
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

	public static Integer getNumberOfLines(String ruleCore) {
		String[] lines = ruleCore.split("\n");
		return lines.length;
	}

	public static String replaceLine(String ruleCore, String lineToSet, int lineNumber) {
		String cleanedResults = "";
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i == lineNumber) {
				cleanedResults += lineToSet + "\n";
			} else {
				cleanedResults += lines[i] + "\n";
			}
		}
		return cleanedResults;
	}

	public static String replaceLastLine(String ruleCore, String lineToSet) {
		String cleanedResults = "";
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i == (lines.length - 1)) {
				cleanedResults += lineToSet + "\n";
			} else {
				cleanedResults += lines[i] + "\n";
			}
		}
		return cleanedResults;
	}

	public static String getLine(String ruleCore, int lineNumber) {
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i == lineNumber) {
				return lines[i];
			}
		}
		return "";
	}

	public static String getLastLine(String ruleCore) {
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i == (lines.length - 1)) {
				return lines[i];
			}
		}
		return "";
	}

	public static String fixOrCondition(String ruleCore) {
		String cleanedResults = "";
		int finishCondition = -1;
		HashSet<Integer> skipLines = new HashSet<Integer>();

		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].equals("\tor")) {
				skipLines.add(i - 1);
				skipLines.add(i);
				skipLines.add(i + 1);
			} else if (lines[i].equals("\tnot(")) {
				skipLines.add(i);
			} else if (lines[i].equals("then")) {
				skipLines.add(i - 1);
				finishCondition = i;
				break;
			}
		}
		for (int i = 0; i < lines.length; i++) {
			// In the action part, stop the algorithm
			if (finishCondition == i) {
				break;
			}
			// If the previous line belongs to an OR but it has more lines after
			// it, add an and
			if (skipLines.contains(i - 1) && !skipLines.contains(i) && !lines[i - 1].equals("\tnot(")) {
				lines[i - 1] = lines[i - 1] + " and";
				lines[i] = lines[i] + " and";
			}// If the line don't belong to an or, add an and
			else if (!skipLines.contains(i)) {
				lines[i] = lines[i] + " and";
			}
		}
		for (int i = 0; i < lines.length; i++) {
			cleanedResults += lines[i] + "\n";
		}

		return cleanedResults;
	}

	public static String addAndToMultipleConditionsAction(String ruleCore) {
		String cleanedResults = "";
		String[] lines = ruleCore.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].equals("then")) {
				if (i > 0) {
					// Remove the "and"
					lines[i - 1] = lines[i - 1].substring(0, lines[i - 1].length() - 3);
				}
				break;
			}else{
				lines[i] = lines[i].concat(" and");
			}
		}
		for (int i = 0; i < lines.length; i++) {
			cleanedResults += lines[i] + "\n";
		}
		return cleanedResults;
	}
}
