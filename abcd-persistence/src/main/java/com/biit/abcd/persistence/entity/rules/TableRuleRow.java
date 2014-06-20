package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE_ROW")
public class TableRuleRow extends StorableObject {

//	// Due to bug https://hibernate.atlassian.net/browse/HHH-8839, map must use @LazyCollection.
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@JoinTable(name = "RULE_DECISION_CONDITIONS_MAP", joinColumns = { @JoinColumn(name = "rule_decision_id") })
//	@MapKeyColumn(name = "condition_id")
	@Transient
	private List<QuestionAndAnswerValue> conditions;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Action> actions;

	public TableRuleRow() {
		conditions = new ArrayList<>();
		actions = new ArrayList<>();
	}

	public void addCondition(QuestionAndAnswerValue questionAndAnswerValue) {
		conditions.add(questionAndAnswerValue);
	}

	public void removeCondition(QuestionAndAnswerValue questionAndAnswerValue) {
		conditions.remove(questionAndAnswerValue);
	}

	public List<QuestionAndAnswerValue> getConditions() {
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
