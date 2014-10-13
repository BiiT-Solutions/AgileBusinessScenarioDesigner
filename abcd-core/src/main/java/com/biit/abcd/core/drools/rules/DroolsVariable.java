package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class DroolsVariable {

	private String droolsId;
	private String droolsCode;
	private DroolsVariableScope scope;
	private ExpressionValueTreeObjectReference value;
	private TreeObject treeObject;
	private CustomVariable customVariable;

	public DroolsVariable() {
	}

	public DroolsVariable(TreeObject treeObject) {
		setTreeObject(treeObject);
		setDroolsId(treeObject.getUniqueNameReadable());
		setScope(treeObject);
	}

	public DroolsVariable(ExpressionValueTreeObjectReference value) {
		setValue(value);
		setTreeObject(value.getReference());
		setDroolsId(value.getReference().getUniqueNameReadable());
		setScope(value.getReference());
	}

	public DroolsVariable(ExpressionValueCustomVariable value) {
		setValue(value);
		setTreeObject(value.getReference());
		setDroolsId(value.getReference().getUniqueNameReadable());
		setCustomVariable(value.getVariable());
		setScope(value.getReference());
	}

	public String getName() {
		if (customVariable != null) {
			return customVariable.getName();
		} else
			return treeObject.getName();
	}

	private void setScope(TreeObject treeObject) {
		if (treeObject instanceof Form) {
			setScope(DroolsVariableScope.FORM);

		} else if (treeObject instanceof Category) {
			setScope(DroolsVariableScope.CATEGORY);

		} else if (treeObject instanceof Group) {
			setScope(DroolsVariableScope.GROUP);

		} else if (treeObject instanceof Question) {
			setScope(DroolsVariableScope.QUESTION);
		}
	}

	public DroolsVariableScope getScope() {
		return scope;
	}

	public void setScope(DroolsVariableScope scope) {
		this.scope = scope;
	}

	public String getDroolsId() {
		return droolsId;
	}

	public void setDroolsId(String droolsId) {
		this.droolsId = droolsId;
	}

	public ExpressionValueTreeObjectReference getValue() {
		return value;
	}

	public void setValue(ExpressionValueTreeObjectReference value) {
		this.value = value;
	}

	public String getDroolsCode() {
		return droolsCode;
	}

	public void setDroolsCode(String droolsCode) {
		this.droolsCode = droolsCode;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}

	public void setTreeObject(TreeObject treeObject) {
		this.treeObject = treeObject;
	}
	
	public CustomVariable getCustomVariable() {
		return customVariable;
	}

	public void setCustomVariable(CustomVariable customVariable) {
		this.customVariable = customVariable;
	}

	private void transformToDroolsCode(TreeObject treeObject) {
		String id = treeObject.getUniqueNameReadable();
		setDroolsId(id);
		// If it is a question of input type
		if ((treeObject instanceof Question) && ((Question) treeObject).getAnswerType().equals(AnswerType.INPUT)) {
			switch (((Question) treeObject).getAnswerFormat()) {
			case NUMBER:
				setDroolsCode("(Double)$" + id + ".getAnswer('" + AnswerFormat.NUMBER.toString() + "')");
				break;
			case DATE:
				if ((value != null) && (value.getUnit() != null)) {
					switch (value.getUnit()) {
					case YEARS:
						setDroolsCode("DateUtils.returnYearsDistanceFromDate( $" + id + ".getAnswer('"
								+ AnswerFormat.DATE.toString() + "'))");
						break;
					case MONTHS:
						setDroolsCode("DateUtils.returnMonthsDistanceFromDate( $" + id + ".getAnswer('"
								+ AnswerFormat.DATE.toString() + "'))");
						break;
					case DAYS:
						setDroolsCode("DateUtils.returnDaysDistanceFromDate( $" + id + ".getAnswer('"
								+ AnswerFormat.DATE.toString() + "'))");
						break;
					case DATE:
						setDroolsCode("$" + id + ".getAnswer('" + AnswerFormat.DATE.toString() + "')");
						break;
					}
				}
				break;
			case TEXT:
				setDroolsCode("$" + id + ".getAnswer('" + AnswerFormat.TEXT.toString() + "')");
				break;
			case POSTAL_CODE:
				setDroolsCode("$" + id + ".getAnswer('" + AnswerFormat.POSTAL_CODE.toString() + "')");
				break;
			}
		}
	}

}
