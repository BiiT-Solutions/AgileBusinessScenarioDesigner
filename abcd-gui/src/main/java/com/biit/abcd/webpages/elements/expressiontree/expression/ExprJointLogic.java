package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprJointLogic extends ExprJoint {

	public ExprJointLogic() {
		getAcceptedValues().add(new JointValue("AND", "AND"));
		getAcceptedValues().add(new JointValue("OR", "OR"));
	}

	@Override
	public String getValueNullCaption() {
		return "logic-joint";
	}

}
