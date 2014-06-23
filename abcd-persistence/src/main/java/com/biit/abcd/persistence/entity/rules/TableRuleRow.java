package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE_ROW")
public class TableRuleRow extends StorableObject {

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "condition_index")
	private List<QuestionAndAnswerCondition> conditions;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Action> actions;

	public TableRuleRow() {
		conditions = new ArrayList<>();
		actions = new ArrayList<>();
	}

	public void addCondition(QuestionAndAnswerCondition questionAndAnswerValue) {
		conditions.add(questionAndAnswerValue);
	}

	public void removeCondition(QuestionAndAnswerCondition questionAndAnswerValue) {
		conditions.remove(questionAndAnswerValue);
	}

	public List<QuestionAndAnswerCondition> getConditions() {
		return conditions;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public List<Action> getActions() {
		return actions;
	}

	@Override
	public String toString() {
		return conditions.toString();
	}

}
