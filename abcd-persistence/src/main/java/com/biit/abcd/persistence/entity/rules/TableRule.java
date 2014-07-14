package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.utils.ITableCellEditable;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE")
public class TableRule extends StorableObject implements ITableCellEditable{

	private String name;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TableRuleRow> rules;

	public TableRule() {
		rules = new ArrayList<>();
	}

	public List<TableRuleRow> getRules() {
		return rules;
	}

	public void setRules(List<TableRuleRow> rules) {
		this.rules.clear();
		this.rules.addAll(rules);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName() + rules;
	}

	public TableRuleRow addRow() {
		TableRuleRow row = new TableRuleRow();
		row.addAction(new ActionExpression());
		getRules().add(row);
		if(getRules().size()>1) {
			for(int i=0; i<getConditionNumber(); i++){
				row.addCondition(new ExpressionValueTreeObjectReference());
			}
		}
		return row;
	}

	public void addEmptyExpressionPair(){
		for(TableRuleRow row : getRules()){
			row.addCondition(new ExpressionValueTreeObjectReference());
			row.addCondition(new ExpressionValueTreeObjectReference());
		}
	}

	public void removeRule(TableRuleRow rule) {
		rules.remove(rule);
	}

	public void removeConditions(TableRuleRow row, List<Expression> values){
		for (Expression value : values) {
			row.getConditions().remove(value);
		}
	}

	public int getConditionNumber(){
		if(getRules().size()>0) {
			return getRules().get(0).getConditionNumber();
		}else{
			return 0;
		}
	}
}
