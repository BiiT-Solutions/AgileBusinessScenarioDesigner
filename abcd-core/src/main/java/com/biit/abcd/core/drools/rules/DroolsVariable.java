package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class DroolsVariable {

	private String droolsId;
	private String droolsCode;
	private DroolsVariableScope scope;
	private List<DroolsVariable> previousConditions;
	private ExpressionValueTreeObjectReference value;
	private TreeObject treeObject;

	public DroolsVariable() {
		previousConditions = new ArrayList<DroolsVariable>();
	}

	public DroolsVariable(TreeObject treeObject) {
		previousConditions = new ArrayList<DroolsVariable>();
		this.treeObject = treeObject;
		createPreviousConditions(treeObject);
	}

	public DroolsVariable(ExpressionValueTreeObjectReference value) {
		this.value = value;
		treeObject = value.getReference();
		createPreviousConditions(treeObject);
	}

	public List<DroolsVariable> getPreviousConditions() {
		return previousConditions;
	}

	public void setPreviousConditions(List<DroolsVariable> previousConditions) {
		this.previousConditions = previousConditions;
	}

	public void addPreviousCondition(DroolsVariable previousCondition) {
		getPreviousConditions().add(previousCondition);
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

	private void createPreviousConditions(TreeObject treeObject) {
		if (treeObject instanceof Form) {
			setScope(DroolsVariableScope.FORM);
			previousConditions.add(new DroolsVariable(treeObject));

		} else if (treeObject instanceof Category) {
			setScope(DroolsVariableScope.CATEGORY);
			previousConditions.add(new DroolsVariable(treeObject.getParent()));

		} else if (treeObject instanceof Group) {
			setScope(DroolsVariableScope.GROUP);
			previousConditions.add(new DroolsVariable(treeObject.getParent()));

		} else if (treeObject instanceof Question) {
			setScope(DroolsVariableScope.QUESTION);
			previousConditions.add(new DroolsVariable(treeObject.getParent()));
		}
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
