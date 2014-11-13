package com.biit.abcd.core.drools.facts.inputform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;

public class SubmittedQuestion extends SubmittedFormObject implements IQuestion, ISubmittedFormElement {

	// Date format based on the input received by the Orbeon forms
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private String answer;
	private IGroup groupParent;
	private ICategory categoryParent;

	public SubmittedQuestion(String tag) {
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
				parsedValue = new SimpleDateFormat(DATE_FORMAT).parse(this.answer);
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
					return new SimpleDateFormat(DATE_FORMAT).parse(answer);

				} catch (ParseException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					// Default, create tomorrow's date
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DAY_OF_YEAR, 1);
					Date tomorrow = cal.getTime();
					return new SimpleDateFormat(DATE_FORMAT).format(tomorrow);
				}
			} else {
				// Default, create tomorrow's date
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_YEAR, 1);
				Date tomorrow = cal.getTime();
				return new SimpleDateFormat(DATE_FORMAT).format(tomorrow);
			}
		}
		return parsedValue;
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
			return ((SubmittedCategory) getParent()).isScoreSet(submittedFormTreeObject, varName);
		} else {
			return ((SubmittedGroup) getParent()).isScoreSet(submittedFormTreeObject, varName);
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
			return ((SubmittedCategory) this.getParent()).getVariableValue(this, varName);
		} else {
			return ((SubmittedGroup) this.getParent()).getVariableValue(this, varName);
		}
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		if (this.getParent() instanceof ICategory) {
			((SubmittedCategory) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		} else {
			((SubmittedGroup) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		}
	}

	@Override
	public String generateXML(String tabs) {
		return tabs + "<" + getTag() + " type=\"" + this.getClass().getSimpleName() + "\"" + ">" + answer + "</"
				+ getTag() + ">\n";
	}

	@Override
	public String getName() {
		return getTag();
	}

	@Override
	public String getOriginalValue() {
		return answer;
	}

	@Override
	public List<ISubmittedFormElement> getChildren() {
		return null;
	}

	@Override
	public CustomVariableScope getVariableScope() {
		return CustomVariableScope.QUESTION;
	}
}
