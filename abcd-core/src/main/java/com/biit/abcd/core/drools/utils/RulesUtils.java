package com.biit.abcd.core.drools.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RulesUtils {

	public static String getStartRuleString(String name) {
		return "rule \"" + name + "\"\n";
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
		return "\nend\n";
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
		// Parse the resulting rule to delete lines that are equal
		String[] auxSplit = ruleCore.split("\n");
		List<String> auxRule = new ArrayList<String>();
		for (int i = 0; i < auxSplit.length; i++) {
			if (i != 0) {
				boolean stringRepeated = false;
				for (int j = 0; j < auxRule.size(); j++) {
					if (auxRule.get(j).equals("(") && auxRule.get(j).equals(")") && auxRule.get(j).equals(auxSplit[i])) {
						stringRepeated = true;
						break;
					}
				}
				if (!stringRepeated) {
					auxRule.add(auxSplit[i]);
				}
			} else {
				auxRule.add(auxSplit[i]);
			}
		}
		// Parse the resulting rule to add an index to separate equal
		// assignation
		// Example $cat : ... \n $cat : ... \n will be converted to $cat : ...
		// \n $cat1 : ... \n
		// (Separated from the previous to make it more understandable)
		String previousVariable = "";
		String auxRuleCore = "";
		int indexVariable = 1;

		for (String auxPart : auxRule) {
			if (!auxPart.contains("accumulate(") && !auxPart.contains("then") && !auxPart.contains("(")
					&& !auxPart.contains(")")) {
				String[] auxRuleArray = auxPart.split(" : ");
				if (auxRuleArray[0].equals(previousVariable)) {
					auxRuleArray[0] = auxRuleArray[0] + indexVariable;
					indexVariable++;
				} else {
					previousVariable = auxRuleArray[0];
				}
				if (auxRuleArray.length > 1) {
					auxRuleCore += auxRuleArray[0] + " : " + auxRuleArray[1] + "\n";
				} else {
					auxRuleCore += auxRuleArray[0];
				}
			} else {
				auxRuleCore += auxPart + "\n";
			}
		}
		return auxRuleCore;
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
	public static String newRemoveDuplicateLines(String ruleCore) {
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

	// rule "getBmiMale_row_2"
	// when
	//
	// (
	// $4db44f24d9db4d72ab0e2a884e09ff28 : SubmittedForm() and
	// $21798d15524a4ab9a3a757e24efb4144 : Category() from
	// $4db44f24d9db4d72ab0e2a884e09ff28.getCategory('Algemeen') and
	// $308d01df47ff4babb2d7dba4515ec5b1 : Question(getAnswer() instanceof Date,
	// DateUtils.returnYearDistanceFromDate(getAnswer()) == 4) from
	// $21798d15524a4ab9a3a757e24efb4144.getQuestions()
	// and
	// $4db44f24d9db4d72ab0e2a884e09ff28 : SubmittedForm( isScoreSet('BMI'),
	// getNumberVariableValue('BMI') >= '17.5' || < '19.2')
	// )
	// then
	// $4db44f24d9db4d72ab0e2a884e09ff28.setVariableValue('BmiClassification',
	// 'overweight');
	// AbcdLogger.debug("DroolsRule",
	// "Variable set (KidsScreen, BmiClassification, overweight)");
	//
	// end

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
}
