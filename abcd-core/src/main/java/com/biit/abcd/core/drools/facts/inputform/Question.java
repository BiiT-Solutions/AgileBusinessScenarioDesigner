package com.biit.abcd.core.drools.facts.inputform;

import com.biit.abcd.core.drools.facts.interfaces.IQuestion;
import com.biit.abcd.core.drools.rules.VariablesMap;
import com.biit.abcd.persistence.entity.CustomVariableScope;

public class Question extends CommonAttributes implements IQuestion {

	public boolean isScoreSet() {
		if (VariablesMap.getInstance().getVariableValue(CustomVariableScope.QUESTION, getTag()) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet() {
		return !isScoreSet();
	}

	@Override
	public Object getCustomVariable() {
		return VariablesMap.getInstance().getVariableValue(CustomVariableScope.QUESTION, getTag());
	}

	@Override
	public void setCustomVariable(Object value) {
		VariablesMap.getInstance().addVariableValue(CustomVariableScope.QUESTION, getTag(), value);
	}
}
