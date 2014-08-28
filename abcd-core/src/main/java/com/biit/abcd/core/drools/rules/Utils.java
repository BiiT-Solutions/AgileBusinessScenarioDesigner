package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

public class Utils {

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
}
