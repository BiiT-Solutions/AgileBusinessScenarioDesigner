package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

public class GroupRuleFired {

	private List<String> rulesFired = null;

	public GroupRuleFired() {
		rulesFired = new ArrayList<String>();
	}

	public void addRuleFired(String ruleName) {
		rulesFired.add(ruleName);
	}

	/**
	 * Returns true if the rule not matches the name passed
	 * 
	 * @param ruleName
	 * @return
	 */
	public boolean isRuleFired(String ruleName) {
		for (String ruleFiredName : rulesFired) {
			if (ruleFiredName.equals(ruleName)) {
				return true;
			}
		}
		return false;
	}
}
