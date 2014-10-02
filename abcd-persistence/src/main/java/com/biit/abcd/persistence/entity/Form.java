package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.liferay.portal.model.UserGroup;

@Entity
@Table(name = "tree_forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "version" }) })
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH))
@Cache(region = "forms", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Form extends BaseForm {

	@Column(nullable = false)
	private Timestamp availableFrom;
	private Timestamp availableTo;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.JOIN)
	@OrderBy(value = "name ASC")
	@Cache(region = "diagrams", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Diagram> diagrams;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "name ASC")
	@Cache(region = "tableRules", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<TableRule> tableRules;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	// Cannot be JOIN
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "name ASC")
	@Cache(region = "customVariables", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<CustomVariable> customVariables;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "name ASC")
	@Cache(region = "expressionChains", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<ExpressionChain> expressionChain;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	// Cannot be JOIN
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "name ASC")
	@Cache(region = "rules", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Rule> rules;

	public Form() {
		super();
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChain = new HashSet<>();
		rules = new HashSet<>();
	}

	public Form(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChain = new HashSet<>();
		rules = new HashSet<>();
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

	public Set<Diagram> getDiagrams() {
		return diagrams;
	}

	public void setDiagrams(Set<Diagram> diagrams) {
		this.diagrams.clear();
		this.diagrams.addAll(diagrams);
	}

	public Set<TableRule> getTableRules() {
		return tableRules;
	}

	public void setTableRules(List<TableRule> tableRules) {
		this.tableRules.clear();
		this.tableRules.addAll(tableRules);
	}

	public Set<CustomVariable> getCustomVariables() {
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
		for (CustomVariable customVariable : getCustomVariables()) {
			if (customVariable.getName().equals(name) && customVariable.getScope().toString().equals(scope)) {
				return customVariable;
			}
		}
		return null;
	}

	public void setCustomVariables(Set<CustomVariable> customVariables) {
		this.customVariables.clear();
		this.customVariables.addAll(customVariables);
	}

	public Set<ExpressionChain> getExpressionChain() {
		return expressionChain;
	}

	public void setExpressionChain(Set<ExpressionChain> expressions) {
		expressionChain = expressions;
	}

	public Set<Rule> getRules() {
		return rules;
	}

	public void setRules(Set<Rule> rules) {
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

	public Long getOrganizationId() {
		// TODO Auto-generated method stub
		return null;
	}
}
