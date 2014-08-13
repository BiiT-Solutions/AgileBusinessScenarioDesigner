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

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "TREE_FORMS")
public class Form extends BaseForm {

	@Column(nullable = false)
	private Timestamp availableFrom;
	private Timestamp availableTo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Diagram> diagrams;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<TableRule> tableRules;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<CustomVariable> customVariables;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<ExpressionChain> expressionChain;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Rule> rules;

	public Form() {
		super();
		this.diagrams = new ArrayList<>();
		this.tableRules = new ArrayList<>();
		this.customVariables = new ArrayList<>();
		this.expressionChain = new ArrayList<>();
		this.rules = new ArrayList<>();
		try {
			this.setName(DEFAULT_NAME);
		} catch (FieldTooLongException ftle) {
			// Default name is not so long.
		}
	}

	public Form(String name) throws FieldTooLongException {
		super(name);
		this.diagrams = new ArrayList<>();
		this.tableRules = new ArrayList<>();
		this.customVariables = new ArrayList<>();
		this.expressionChain = new ArrayList<>();
		this.rules = new ArrayList<>();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (Diagram diagram : this.getDiagrams()) {
			diagram.resetIds();
		}
		for (TableRule tableRule : this.getTableRules()) {
			tableRule.resetIds();
		}
		for (CustomVariable customVariable : this.getCustomVariables()) {
			customVariable.resetIds();
		}
		for (ExpressionChain expression : this.getExpressionChain()) {
			expression.resetIds();
		}
		for (Rule rule : this.getRules()) {
			rule.resetIds();
		}
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		if (this.availableFrom == null) {
			this.availableFrom = dateCreated;
		}
		super.setCreationTime(dateCreated);
	}

	public Timestamp getAvailableFrom() {
		return this.availableFrom;
	}

	public void setAvailableFrom(Timestamp availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Timestamp getAvailableTo() {
		return this.availableTo;
	}

	public void setAvailableTo(Timestamp availableTo) {
		this.availableTo = availableTo;
	}

	public void addDiagram(Diagram diagram) {
		this.diagrams.add(diagram);
	}

	public void removeDiagram(Diagram diagram) {
		this.diagrams.remove(diagram);
	}

	public List<Diagram> getDiagrams() {
		return this.diagrams;
	}

	public void setDiagrams(List<Diagram> diagrams) {
		this.diagrams.clear();
		this.diagrams.addAll(diagrams);
	}

	public List<TableRule> getTableRules() {
		return this.tableRules;
	}

	public void setTableRules(List<TableRule> tableRules) {
		this.tableRules.clear();
		this.tableRules.addAll(tableRules);
	}

	public List<CustomVariable> getCustomVariables() {
		return this.customVariables;
	}

	/**
	 * Get Custom variables for a specific tree Object.
	 * 
	 * @param treeObject
	 * @return
	 */
	public List<CustomVariable> getCustomVariables(TreeObject treeObject) {
		List<CustomVariable> customVariablesInThisElement = new ArrayList<CustomVariable>();
		for (CustomVariable customVariable : this.customVariables) {
			if (customVariable.getScope().getScope().equals(treeObject.getClass())) {
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
		return this.expressionChain;
	}

	public void setExpressionChain(List<ExpressionChain> expressions) {
		this.expressionChain = expressions;
	}

	public List<Rule> getRules() {
		return this.rules;
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
		for (Diagram parentDiagram : this.getDiagrams()) {
			List<Diagram> childDiagrams = parentDiagram.getChildDiagrams();
			for (Diagram childDiagram : childDiagrams) {
				if (childDiagram.equals(diagram)) {
					return parentDiagram;
				}
			}
		}
		return null;
	}
}
