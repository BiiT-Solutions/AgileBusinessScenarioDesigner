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
import com.biit.orbeon.form.ISubmittedObject;

public class SubmittedQuestion extends com.biit.form.submitted.SubmittedQuestion implements ISubmittedFormElement {

	// Date format based on the input received by the Orbeon forms
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public SubmittedQuestion(String tag) {
		super(tag);
	}

	public Object getAnswer(String answerFormat) {
		if (answerFormat == null) {
			return null;
		}
		if (answerFormat.isEmpty()) {
			Object parsedValue = null;
			try {
				parsedValue = Double.parseDouble(getAnswer());
			} catch (Exception e) {
				try {
					parsedValue = new SimpleDateFormat(DATE_FORMAT).parse(getAnswer());
				} catch (Exception e1) {
					parsedValue = getAnswer();
				}
			}
			return parsedValue;
		}

		Object parsedValue = null;
		switch (answerFormat) {
		case "NUMBER":
			if (getAnswer() != null && !getAnswer().isEmpty()) {
				try {
					return Double.parseDouble(getAnswer());
				} catch (Exception e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					return 0.0;
				}
			} else {
				return 0.0;
			}

		case "POSTAL_CODE":
		case "TEXT":
			return getAnswer();

		case "DATE":
			if (getAnswer() != null && !getAnswer().isEmpty()) {
				try {
					return new SimpleDateFormat(DATE_FORMAT).parse(getAnswer());

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

	@Override
	public boolean isScoreSet(String varName) {
		return isScoreSet(this, varName);
	}

	@Override
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

	@Override
	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	@Override
	public Object getVariableValue(Class<?> type, String varName) {
		List<ISubmittedObject> childs = getChildren(type);

		if (childs != null && !childs.isEmpty()) {
			return getVariableValue(childs.get(0), varName);
		}
		return null;
	}

	@Override
	public Object getVariableValue(Class<?> type, String treeObjectName, String varName) {
		ISubmittedObject child = getChild(type, treeObjectName);

		if (child != null) {
			return getVariableValue(child, varName);
		}
		return null;
	}

	@Override
	public Object getVariableValue(Object submmitedFormObject, String varName) {
		return ((ISubmittedFormElement) this.getParent()).getVariableValue(submmitedFormObject, varName);
	}

	@Override
	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	@Override
	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		((ISubmittedFormElement) getParent()).setVariableValue(submmitedFormObject, varName, value);
	}

	@Override
	public String generateXML(String tabs) {
		return tabs + "<" + getTag() + " type=\"" + this.getClass().getSimpleName() + "\"" + ">" + getAnswer() + "</"
				+ getTag() + ">\n";
	}

	@Override
	public String getName() {
		return getTag();
	}

	@Override
	public String getOriginalValue() {
		return getAnswer();
	}

	@Override
	public CustomVariableScope getVariableScope() {
		return CustomVariableScope.QUESTION;
	}
}
