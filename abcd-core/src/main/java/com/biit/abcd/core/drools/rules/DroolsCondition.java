package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.Form;

/**
 * Contains a condition of the drools rule
 * 
 */
public class DroolsCondition {

	private DroolsVariable variable = null;
	private DroolsCondition previousCondition = null;
	private ConditionPrecedence precendece = null;

	public DroolsCondition() {
	}

	public DroolsCondition(DroolsVariable variable, int precedence) {
		setVariable(variable);
		createPreviousCondition(variable);
	}

	public DroolsVariable getVariable() {
		return variable;
	}

	public void setVariable(DroolsVariable variable) {
		this.variable = variable;
	}

	public String getDroolsCondition() {
		String droolsCondition = "";
		
		if (variable != null) {
			droolsCondition = "$" + variable.getDroolsId() + " : ";
		}
		return droolsCondition;
	}

	/**
	 * Create the previous conditions hierarchy based on the variable scope
	 * 
	 * @param variable
	 */
	private void createPreviousCondition(DroolsVariable variable) {
		if ((variable != null) && (variable.getTreeObject() != null) && !(variable.getTreeObject() instanceof Form)) {
			previousCondition = new DroolsCondition(new DroolsVariable(variable.getTreeObject().getParent()),
					ConditionPrecedence.PREVIOUS);
		}
	}

	public ConditionPrecedence getPrecendece() {
		return precendece;
	}

	public void setPrecendece(ConditionPrecedence precendece) {
		this.precendece = precendece;
	}
}
