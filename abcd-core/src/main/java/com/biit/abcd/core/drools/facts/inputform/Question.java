package com.biit.abcd.core.drools.facts.inputform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;

public class Question extends SubmittedFormObject implements IQuestion, IDroolsForm {

	private String answer;
	private String name;
	private IGroup groupParent;
	private ICategory categoryParent;

	public Question(String tag) {
		setTag(tag);
		setText(tag);
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
				parsedValue = new SimpleDateFormat("yyyy-MM-dd").parse(this.answer);
			} catch (Exception e1) {
				parsedValue = this.answer;
			}
		}
		return parsedValue;
	}

	public Object getAnswer(String answerFormat) {
		if (answerFormat == null) {
			return null;
		}
		if (answerFormat.isEmpty()) {
			return getAnswer();
		}

		Object parsedValue = null;
		switch (answerFormat) {
		case "NUMBER":
			if (answer != null && !answer.isEmpty()) {
				try {
					return Double.parseDouble(this.answer);
				} catch (Exception e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					return 0.0;
				}
			} else {
				return 0.0;
			}

		case "POSTAL_CODE":
		case "TEXT":
			return answer;

		case "DATE":
			if (answer != null && !answer.isEmpty()) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(answer);

				} catch (ParseException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					// Default, create tomorrow's date
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DAY_OF_YEAR, 1);
					Date tomorrow = cal.getTime();
					return new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
				}
			} else {
				// Default, create tomorrow's date
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_YEAR, 1);
				Date tomorrow = cal.getTime();
				return new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
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
		return isScoreSet(this, varName);
	}

	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((Category) getParent()).isScoreSet(submittedFormTreeObject, varName);
		} else {
			return ((Group) getParent()).isScoreSet(submittedFormTreeObject, varName);
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !isScoreSet(varName);
	}

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public Object getVariableValue(Object submmitedFormObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((Category) this.getParent()).getVariableValue(this, varName);
		} else {
			return ((Group) this.getParent()).getVariableValue(this, varName);
		}
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		if (this.getParent() instanceof ICategory) {
			((Category) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		} else {
			((Group) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		}
	}
}
