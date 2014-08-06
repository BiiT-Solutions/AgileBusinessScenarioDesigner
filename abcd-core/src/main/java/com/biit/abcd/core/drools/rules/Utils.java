package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.CustomVariableScope;

public class Utils {

	public static String getStartRuleString(String name){
		return "rule \"" + name + "\"\n";
	}

	public static String getWhenRuleString(){
		return "when\n";
	}

	public static String getAttributes() {
		return "";
	}

	public static String getThenRuleString(){
		return "then\n";
	}

	public static String getEndRuleString(){
		return "end\n";
	}

	public static String getVariableScope(CustomVariableScope var) {
		if (var.equals(CustomVariableScope.FORM)) {
			return "$form";
		} else if (var.equals(CustomVariableScope.CATEGORY)) {
			return "$category";
		} else if (var.equals(CustomVariableScope.GROUP)) {
			return "$group";
		} else if (var.equals(CustomVariableScope.QUESTION)) {
			return "$question";
		}
		return "";
	}
}
