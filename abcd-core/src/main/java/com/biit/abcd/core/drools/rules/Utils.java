package com.biit.abcd.core.drools.rules;

public class Utils {

	public static String getStartRuleString(String name){
		return "rule \"" + name + "\"\n";
	}

	public static String getWhenRuleString(){
		String str = "when\n" +
				"	$form : SubmittedForm()\n";
		return str;
	}

	public static String getThenRuleString(){
		return "then\n";
	}

	public static String getEndRuleString(){
		return "end\n";
	}
}
