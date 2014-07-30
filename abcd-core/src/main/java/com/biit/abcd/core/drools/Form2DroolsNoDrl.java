package com.biit.abcd.core.drools;

import java.io.PrintStream;
import java.util.Arrays;

import com.biit.abcd.core.drools.facts.inputform.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;
import com.biit.abcd.core.drools.rules.FormParser;
import com.biit.abcd.persistence.entity.Form;

@SuppressWarnings("rawtypes")
public class Form2DroolsNoDrl {

	private KieManager km;

	public void parse(Form form) throws ExpressionInvalidException {
		km = new KieManager();

		FormParser formRules;
		try {
			formRules = new FormParser(form);
			System.out.println(formRules.getRules());
			km.buildSessionRules(formRules.getRules());
			km.setGlobalVariables(Arrays.asList(getGlobalVar(System.out)));

		} catch (ExpressionInvalidException e) {
			throw e;
		}
	}

	public void go(ISubmittedForm form){
		km.setFacts(Arrays.asList(form));
		km.execute();
	}

	private DroolsGlobalVariable getGlobalVar(PrintStream out){
		return new DroolsGlobalVariable<PrintStream>("out", out);
	}


	//	private String getTestRules() {
	//		String r1 = "" +
	//
	//				"rule \"rule 8\" when \n" +
	//				"	$f : Form()\n" +
	//				"	$tr : TableRule() from $f.getTableRules()\n" +
	//				"	$trr : TableRuleRow() from $tr.getRules()\n" +
	//				// Get questions
	//				"	$qTo : ExpressionValueTreeObjectReference() from $trr.getConditions()\n" +
	//				"	$qr : Question( name == \"Question1\") from $qTo.reference\n" +
	//				// Get BETWEEN Answer
	//				"	$ec : ExpressionChain() from $trr.getConditions()\n" +
	//				"	$ef : ExpressionFunction( getValue().equals(AvailableFunction.BETWEEN)) from $ec.getExpressions()\n" +
	//				"	$minValue : Expression() from $ec.getExpressions().get(1)\n" +
	//				"	$maxValue : Expression() from $ec.getExpressions().get(3)\n" +
	//				"	$ar : Answer( name == \"Answer1\") from $aTo.reference\n" +
	//				"then\n" +
	//				"	out.println( \"Answers:\" + $minValue.toString()); \n" +
	//				"end \n";
	//
	//		return r1;
	//	}
}
