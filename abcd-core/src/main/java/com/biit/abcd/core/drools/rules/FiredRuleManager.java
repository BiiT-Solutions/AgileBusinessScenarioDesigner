package com.biit.abcd.core.drools.rules;

//import java.util.ArrayList;
//import java.util.List;
//
//import org.kie.api.definition.type.PropertyReactive;
//
//@PropertyReactive
//public class FiredRuleManager {
//
//	private List<String> rulesFired = null;
//
//	public FiredRuleManager() {
//		rulesFired = new ArrayList<String>();
//	}
//
//	public void addRuleFired(String ruleName) {
//		System.out.println("ADD RULE: " + ruleName);
//		rulesFired.add(ruleName);
//	}
//
//	/**
//	 * Returns true if the rule not matches the name passed
//	 * 
//	 * @param ruleName
//	 * @return
//	 */
//	public boolean isRuleFired(String ruleName) {
//		System.out.println("SEARCH FOR: " + ruleName);
//		for (String ruleFiredName : rulesFired) {
//			if (ruleFiredName.equals(ruleName)) {
//				return true;
//			}
//		}
//		return false;
//	}
//}
