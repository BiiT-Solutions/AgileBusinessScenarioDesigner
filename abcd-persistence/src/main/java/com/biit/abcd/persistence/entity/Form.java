package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.liferay.portal.model.UserGroup;

@Entity
@Table(name = "tree_forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version" }) })
public class Form extends BaseForm {

	@Column(nullable = false)
	private Timestamp availableFrom;
	private Timestamp availableTo;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<Diagram> diagrams;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<TableRule> tableRules;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<CustomVariable> customVariables;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<ExpressionChain> expressionChain;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<Rule> rules;

	public Form() {
		super();
		diagrams = new ArrayList<>();
		tableRules = new ArrayList<>();
		customVariables = new ArrayList<>();
		expressionChain = new ArrayList<>();
		rules = new ArrayList<>();
	}

	public Form(String name) throws FieldTooLongException {
		super(name);
		diagrams = new ArrayList<>();
		tableRules = new ArrayList<>();
		customVariables = new ArrayList<>();
		expressionChain = new ArrayList<>();
		rules = new ArrayList<>();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (Diagram diagram : getDiagrams()) {
			diagram.resetIds();
		}
		for (TableRule tableRule : getTableRules()) {
			tableRule.resetIds();
		}
		for (CustomVariable customVariable : this.getCustomVariables()) {
			customVariable.resetIds();
		}
		for (ExpressionChain expression : getExpressionChain()) {
			expression.resetIds();
		}
		for (Rule rule : getRules()) {
			rule.resetIds();
		}
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		if (availableFrom == null) {
			availableFrom = dateCreated;
		}
		super.setCreationTime(dateCreated);
	}

	public Timestamp getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Timestamp availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Timestamp getAvailableTo() {
		return availableTo;
	}

	public void setAvailableTo(Timestamp availableTo) {
		this.availableTo = availableTo;
	}

	public void addDiagram(Diagram diagram) {
		diagrams.add(diagram);
	}

	public void removeDiagram(Diagram diagram) {
		diagrams.remove(diagram);
	}

	public List<Diagram> getDiagrams() {
		return diagrams;
	}

	public void setDiagrams(List<Diagram> diagrams) {
		this.diagrams.clear();
		this.diagrams.addAll(diagrams);
	}

	public List<TableRule> getTableRules() {
		return tableRules;
	}

	public void setTableRules(List<TableRule> tableRules) {
		this.tableRules.clear();
		this.tableRules.addAll(tableRules);
	}

	public List<CustomVariable> getCustomVariables() {
		return customVariables;
	}

	/**
	 * Get Custom variables for a specific tree Object.
	 * 
	 * @param treeObject
	 * @return
	 */
	public List<CustomVariable> getCustomVariables(TreeObject treeObject) {
		List<CustomVariable> customVariablesInThisElement = new ArrayList<CustomVariable>();
		for (CustomVariable customVariable : customVariables) {
			if (customVariable.getScope().getScope().equals(treeObject.getClass())) {
				customVariablesInThisElement.add(customVariable);
			}
		}
		return customVariablesInThisElement;
	}

	/**
	 * Get Custom variables for a generic tree Object.
	 * 
	 * @param treeObject
	 * @return
	 */
	public List<CustomVariable> getCustomVariables(Class<?> treeObjectClass) {
		List<CustomVariable> customVariablesInThisElement = new ArrayList<CustomVariable>();
		for (CustomVariable customVariable : customVariables) {
			if (customVariable.getScope().getScope().equals(treeObjectClass)) {
				customVariablesInThisElement.add(customVariable);
			}
		}
		return customVariablesInThisElement;
	}

	/**
	 * Looks for the custom variable with the specified scope and name.
	 * 
	 * @return the custom variable or null if not found
	 */
	public CustomVariable getCustomVariable(String name, String scope) {
		for (CustomVariable customVariable : this.getCustomVariables()) {
			if (customVariable.getName().equals(name) && customVariable.getScope().toString().equals(scope)) {
				return customVariable;
			}
		}
		return null;
	}

	public void setCustomVariables(List<CustomVariable> customVariables) {
		this.customVariables.clear();
		this.customVariables.addAll(customVariables);
	}

	public List<ExpressionChain> getExpressionChain() {
		return expressionChain;
	}

	public void setExpressionChain(List<ExpressionChain> expressions) {
		expressionChain = expressions;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * Returns the parent diagram of a Diagram if it has or null if it is a root diagram.
	 * 
	 * @param diagram
	 */
	public Diagram getDiagramParent(Diagram diagram) {
		for (Diagram parentDiagram : getDiagrams()) {
			List<Diagram> childDiagrams = parentDiagram.getChildDiagrams();
			for (Diagram childDiagram : childDiagrams) {
				if (childDiagram.equals(diagram)) {
					return parentDiagram;
				}
			}
		}
		return null;
	}

	public UserGroup getUserGroup() {
		// TODO
		return null;
	}
}
