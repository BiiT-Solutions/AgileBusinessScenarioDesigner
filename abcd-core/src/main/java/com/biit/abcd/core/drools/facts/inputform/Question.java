package com.biit.abcd.core.drools.facts.inputform;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;

public class Question extends CommonAttributes implements IQuestion {

	private String answer;
	private String name;
	private IGroup groupParent;
	private ICategory categoryParent;

	public Question(String tag) {
		this.setTag(tag);
	}

	@Override
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public Object getAnswer() {
		Object parsedValue = null;
		try {
			parsedValue = Double.parseDouble(this.answer);
		} catch (Exception e) {
			try {
				parsedValue = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this.answer);
			} catch (Exception e1) {
				parsedValue = this.answer;
			}
		}
		return parsedValue;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(Object parent) {
		if (parent instanceof ICategory) {
			this.categoryParent = (ICategory) parent;
		} else {
			this.groupParent = (IGroup) parent;
		}
	}

	public Object getParent() {
		if (this.groupParent != null) {
			return this.groupParent;
		} else {
			return this.categoryParent;
		}
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		if (this.getParent() instanceof ICategory) {
			if (((SubmittedForm) ((Category) this.getParent()).getParent()).hasScoreSet(this, varName)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (((SubmittedForm) ((Category) ((Group) this.getParent()).getParent()).getParent()).hasScoreSet(this,
					varName)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !this.isScoreSet(varName);
	}

	public boolean isScoreNotSet() {
		return !this.isScoreSet("");
	}

	public Object getVariableValue(String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((SubmittedForm) ((Category) this.getParent()).getParent()).getVariableValue(this, varName);
		} else {
			return ((SubmittedForm) ((Category) ((Group) this.getParent()).getParent()).getParent()).getVariableValue(
					this, varName);
		}
	}

	public Number getNumberVariableValue(String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((SubmittedForm) ((Category) this.getParent()).getParent()).getNumberVariableValue(this, varName);
		} else {
			return ((SubmittedForm) ((Category) ((Group) this.getParent()).getParent()).getParent())
					.getNumberVariableValue(this, varName);
		}
	}

	public void setVariableValue(String varName, Object value) {
		if (this.getParent() instanceof ICategory) {
			((SubmittedForm) ((Category) this.getParent()).getParent()).setVariableValue(this, varName, value);
		} else {
			((SubmittedForm) ((Category) ((Group) this.getParent()).getParent()).getParent()).setVariableValue(this,
					varName, value);
		}

	}
}
