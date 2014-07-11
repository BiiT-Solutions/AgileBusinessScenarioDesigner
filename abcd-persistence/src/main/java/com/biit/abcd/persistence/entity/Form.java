package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.liferay.portal.model.UserGroup;

@Entity
@Table(name = "TREE_FORMS", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version" }) })
public class Form extends TreeObject {
	private static final String DEFAULT_NAME = "New Form";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Category.class));

	private Integer version = 1;

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
		diagrams = new ArrayList<>();
		tableRules = new ArrayList<>();
		customVariables = new ArrayList<>();
		expressionChain = new ArrayList<>();
		setName(DEFAULT_NAME);
	}

	/**
	 * Gets all children of the form. This annotations are in the method because
	 * overwrites the TreeObject. Forms' children must use FetchType.LAZY.
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "CHILDRENS_RELATIONSHIP")
	@OrderColumn(name = "children_index")
	public List<TreeObject> getChildren() {
		return super.getChildren();
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return null;
	}

	@Override
	public void setParent(TreeObject parent) throws NotValidParentException {
		throw new NotValidParentException("Forms cannot have a parent.");
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void increaseVersion() {
		this.version++;
		// Force to be stored as a new record
		this.resetIds();
		for (Diagram diagram : getDiagrams()) {
			diagram.resetIds();
		}
		for (TableRule tableRule : getTableRules()) {
			tableRule.resetIds();
		}
		for (CustomVariable customVariable : getCustomVariables()) {
			customVariable.resetIds();
		}
	}

	public UserGroup getUserGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		if (availableFrom == null) {
			this.availableFrom = dateCreated;
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

	public void setCustomVariables(List<CustomVariable> customVariables) {
		this.customVariables.clear();
		this.customVariables.addAll(customVariables);
	}

	public List<ExpressionChain> getExpressionChain() {
		return expressionChain;
	}

	public void setExpressionChain(List<ExpressionChain> expressions) {
		this.expressionChain = expressions;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * Returns the parent diagram of a Diagram if it has or null if it is a root
	 * diagram.
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

}
