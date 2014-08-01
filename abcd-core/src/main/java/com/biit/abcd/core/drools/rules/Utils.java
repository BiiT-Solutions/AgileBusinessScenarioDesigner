package com.biit.abcd.core.drools.rules;

public class Utils {

	public static String getStartRuleString(String name){
		return "rule \"" + name + "\"\n";
	}

	public static String getWhenRuleString(){
		return "when\n";
	}

	public static String getThenRuleString(){
		return "then\n";
	}

	public static String getEndRuleString(){
		return "end\n";
	}
}
