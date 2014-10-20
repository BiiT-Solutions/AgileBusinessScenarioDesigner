package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.liferay.portal.model.User;

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
	private Set<ExpressionChain> expressionChains;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	// Cannot be JOIN
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "name ASC")
	@Cache(region = "rules", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Rule> rules;

//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	// Cannot be JOIN
//	@Fetch(FetchMode.SUBSELECT)
//	@OrderBy(value = "name ASC")
//	@Cache(region = "testScenarios", usage = CacheConcurrencyStrategy.READ_WRITE)
//	private Set<TestScenario> testScenarios;

	@Transient
	private transient boolean isLastVersion = true;

	public Form() {
		super();
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChains = new HashSet<>();
		rules = new HashSet<>();
//		testScenarios = new HashSet<>();
	}

	public Form(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChains = new HashSet<>();
		rules = new HashSet<>();
//		testScenarios = new HashSet<>();
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
		for (ExpressionChain expression : getExpressionChains()) {
			expression.resetIds();
		}
		for (Rule rule : getRules()) {
			rule.resetIds();
		}
//		for (TestScenario testScenario : getTestScenarios()) {
//			testScenario.resetIds();
//		}
	}

	public Form createNewVersion(User user) throws CharacterNotAllowedException, NotValidStorableObjectException {
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(getVersion() + 1);
		newVersion.resetIds();
		newVersion.setCreatedBy(user);
		newVersion.setUpdatedBy(user);
		newVersion.setCreationTime();
		newVersion.setUpdateTime();
		return newVersion;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
		Form form = (Form) object;
		// Copy basic info
		setAvailableFrom(form.getAvailableFrom());
		setAvailableTo(form.getAvailableTo());

		// ComparatorId -> New StorableObject.
		Map<String, TreeObject> formElements = new HashMap<>();
		Set<TreeObject> formElementsChildren = getAllChildrenInHierarchy(TreeObject.class);
		formElementsChildren.add(this);
		for (TreeObject children : formElementsChildren) {
			formElements.put(children.getComparationId(), children);
		}

		// Copy CustomVariables
		getCustomVariables().clear();
		for (CustomVariable variable : form.getCustomVariables()) {
			CustomVariable copiedVariable = new CustomVariable();
			copiedVariable.copyData(variable);
			copiedVariable.setForm(this);
			getCustomVariables().add(copiedVariable);
		}
		// ComparatorId -> New CustomVariable.
		Map<String, CustomVariable> formVariables = new HashMap<>();
		Set<CustomVariable> formVariablesChildren = getCustomVariables();
		for (CustomVariable children : formVariablesChildren) {
			formVariables.put(children.getComparationId(), children);
		}

		// Copy ExpressionChains (must be AFTER CustomVariables)
		getExpressionChains().clear();
		for (ExpressionChain expressionChain : form.getExpressionChains()) {
			ExpressionChain copiedExpressionChain = new ExpressionChain();
			copiedExpressionChain.copyData(expressionChain);
			// Update references to current form references.
			updateVariablesReferences(copiedExpressionChain.getAllInnerStorableObjects(), formVariables);
			updateTreeObjectReferences(copiedExpressionChain.getAllInnerStorableObjects(), formElements);
			getExpressionChains().add(copiedExpressionChain);
		}
		// ComparatorId -> New ExpressionChain.
		Map<String, ExpressionChain> formExpressionChains = new HashMap<>();
		Set<ExpressionChain> formExpressionChainsChildren = getExpressionChains();
		for (ExpressionChain children : formExpressionChainsChildren) {
			formExpressionChains.put(children.getComparationId(), children);
		}

		// Copy TableRules
		getTableRules().clear();
		for (TableRule tableRule : form.getTableRules()) {
			TableRule copiedTableRule = new TableRule();
			copiedTableRule.copyData(tableRule);
			// Update references to current form references.
			updateVariablesReferences(copiedTableRule.getAllInnerStorableObjects(), formVariables);
			updateTreeObjectReferences(copiedTableRule.getAllInnerStorableObjects(), formElements);
			getTableRules().add(copiedTableRule);
		}
		// ComparatorId -> New TableRule.
		Map<String, TableRule> formTableRules = new HashMap<>();
		Set<TableRule> formTableRulesChildren = getTableRules();
		for (TableRule children : formTableRulesChildren) {
			formTableRules.put(children.getComparationId(), children);
		}

		// Copy Rules
		getRules().clear();
		for (Rule rule : form.getRules()) {
			Rule copiedRule = new Rule();
			copiedRule.copyData(rule);
			// Update references to current form references.
			updateVariablesReferences(copiedRule.getAllInnerStorableObjects(), formVariables);
			updateTreeObjectReferences(copiedRule.getAllInnerStorableObjects(), formElements);
			getRules().add(copiedRule);
		}
		// ComparatorId -> New Rule.
		Map<String, Rule> formRules = new HashMap<>();
		Set<Rule> formRulesChildren = getRules();
		for (Rule children : formRulesChildren) {
			formRules.put(children.getComparationId(), children);
		}

		// // Copy TestScenarios
		// for (TestScenario testScenario : getTestScenarios()) {
		// TestScenario copiedTestScenario = new TestScenario();
		// copiedTestScenario.copyData(testScenario);
		// updateTreeObjectReferences((Set<StorableObject>) new
		// HashSet<StorableObject>(Arrays.asList(testScenario)),
		// formElements);
		// }

		// Copy Diagrams
		getDiagrams().clear();
		for (Diagram diagram : form.getDiagrams()) {
			Diagram copiedDiagram = new Diagram();
			copiedDiagram.copyData(diagram);
			// DiagramObject parent is updated when copied.

			// Update references
			Set<StorableObject> diagramObjects = copiedDiagram.getAllInnerStorableObjects();
			updateVariablesReferences(diagramObjects, formVariables);
			updateTreeObjectReferences(diagramObjects, formElements);
			updateDiagramTableRuleReferences(diagramObjects, formTableRules);
			updateDiagramRuleReferences(diagramObjects, formRules);
			updateDiagramExpressionReferences(diagramObjects, formExpressionChains);
			getDiagrams().add(copiedDiagram);
		}
		// ComparatorId -> New Diagram.
		Map<String, Diagram> formDiagrams = new HashMap<>();
		Set<Diagram> formDiagramsChildren = getDiagrams();
		for (Diagram children : formDiagramsChildren) {
			formDiagrams.put(children.getComparationId(), children);
		}

		// Update diagram relationship after all diagrams has been created.
		for (Diagram diagram : getDiagrams()) {
			updateDiagramDiagramReferences(diagram.getAllInnerStorableObjects(), formDiagrams);
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formTableRules
	 */
	private void updateDiagramDiagramReferences(Set<StorableObject> storableObjects, Map<String, Diagram> formDiagrams) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramChild) {
				System.out.println(child);
				DiagramChild diagramChild = (DiagramChild) child;
				if (formDiagrams.get(diagramChild.getChildDiagram().getComparationId()) != null) {
					diagramChild.setChildDiagram(formDiagrams.get(diagramChild.getChildDiagram().getComparationId()));
				} else {
					AbcdLogger.warning(this.getClass().getName(), "Adding diagram '" + diagramChild.getChildDiagram()
							+ "'.");
					formDiagrams.put(diagramChild.getChildDiagram().getComparationId(), diagramChild.getChildDiagram());
				}
			}
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formTableRules
	 */
	private void updateDiagramExpressionReferences(Set<StorableObject> storableObjects,
			Map<String, ExpressionChain> formExpressionChains) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramExpression) {
				DiagramExpression diagramExpression = (DiagramExpression) child;
				if (diagramExpression.getFormExpression() != null) {
					if (formExpressionChains.get(diagramExpression.getFormExpression().getComparationId()) != null) {
						diagramExpression.setFormExpression(formExpressionChains.get(diagramExpression
								.getFormExpression().getComparationId()));
					} else {
						AbcdLogger.warning(this.getClass().getName(),
								"Adding expression '" + diagramExpression.getFormExpression() + "'.");
						formExpressionChains.put(diagramExpression.getFormExpression().getComparationId(),
								diagramExpression.getFormExpression());
					}
				}
			}
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formTableRules
	 */
	private void updateDiagramRuleReferences(Set<StorableObject> storableObjects, Map<String, Rule> formRules) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramRule) {
				DiagramRule diagramRule = (DiagramRule) child;
				if (formRules.get(diagramRule.getRule().getComparationId()) != null) {
					diagramRule.setRule(formRules.get(diagramRule.getRule().getComparationId()));
				} else {
					AbcdLogger.warning(this.getClass().getName(), "Adding rule '" + diagramRule.getRule() + "'.");
					formRules.put(diagramRule.getRule().getComparationId(), diagramRule.getRule());
				}
			}
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formTableRules
	 */
	private void updateDiagramTableRuleReferences(Set<StorableObject> storableObjects,
			Map<String, TableRule> formTableRules) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramTable) {
				DiagramTable diagramTable = (DiagramTable) child;
				if (formTableRules.get(diagramTable.getTable().getComparationId()) != null) {
					diagramTable.setTable(formTableRules.get(diagramTable.getTable().getComparationId()));
				} else {
					AbcdLogger.warning(this.getClass().getName(), "Adding table '" + diagramTable.getTable() + "'.");
					formTableRules.put(diagramTable.getTable().getComparationId(), diagramTable.getTable());
				}
			}
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formElements
	 * @param formVariables
	 */
	private void updateTreeObjectReferences(Set<StorableObject> storableObjects, Map<String, TreeObject> formElements) {
		for (StorableObject child : storableObjects) {
			if (child instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference expressionValueTreeObjectReference = (ExpressionValueTreeObjectReference) child;
				if (formElements.get(expressionValueTreeObjectReference.getReference().getComparationId()) != null) {
					expressionValueTreeObjectReference.setReference(formElements.get(expressionValueTreeObjectReference
							.getReference().getComparationId()));
				} else {
					AbcdLogger.warning(this.getClass().getName(), "Adding reference '"
							+ expressionValueTreeObjectReference.getReference() + "'.");
					formElements.put(expressionValueTreeObjectReference.getReference().getComparationId(),
							expressionValueTreeObjectReference.getReference());
				}
			} 
			// else if (child instanceof TestScenario) {
			// TestScenario testScenario = ((TestScenario) child);
			// Map<TreeObject, TestAnswerList> questionTestAnswerRelationship =
			// new HashMap<>();
			// for (TreeObject question : testScenario.getData().keySet()) {
			// questionTestAnswerRelationship.put(formElements.get(question.getComparationId()),
			// testScenario
			// .getData().get(question));
			// }
			// testScenario.setData(questionTestAnswerRelationship);
			// }
		}
	}

	/**
	 * Replace all references to existing objects in the hashmaps.
	 * 
	 * @param storableObjects
	 * @param formElements
	 * @param formVariables
	 */
	private void updateVariablesReferences(Set<StorableObject> storableObjects,
			Map<String, CustomVariable> formVariables) {
		for (StorableObject child : storableObjects) {
			if (child instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) child;
				if (formVariables.get(expressionValueCustomVariable.getVariable().getComparationId()) != null) {
					expressionValueCustomVariable.setVariable(formVariables.get(expressionValueCustomVariable
							.getVariable().getComparationId()));
				} else {
					formVariables.put(expressionValueCustomVariable.getVariable().getComparationId(),
							expressionValueCustomVariable.getVariable());
				}
			} else if (child instanceof ExpressionValueGenericCustomVariable) {
				ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) child;
				if (formVariables.get(expressionValueGenericCustomVariable.getVariable().getComparationId()) != null) {
					expressionValueGenericCustomVariable.setVariable(formVariables
							.get(expressionValueGenericCustomVariable.getVariable().getComparationId()));
				} else {
					formVariables.put(expressionValueGenericCustomVariable.getVariable().getComparationId(),
							expressionValueGenericCustomVariable.getVariable());
				}
			}
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
		// Remove relationship between diagrams.
		for (Diagram diagramParent : getDiagrams()) {
			for (DiagramObject diagramObject : diagramParent.getDiagramObjects()) {
				if (diagramObject instanceof DiagramChild) {
					DiagramChild diagramChild = (DiagramChild) diagramObject;
					if (diagramChild.getChildDiagram().equals(diagram)) {
						diagramChild.setChildDiagram(null);
					}
				}
			}
		}
		// Remove diagram.
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

	public Set<ExpressionChain> getExpressionChains() {
		return expressionChains;
	}

	public void setExpressionChains(Set<ExpressionChain> expressions) {
		expressionChains = expressions;
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

//	public Set<TestScenario> getTestScenarios() {
//		return testScenarios;
//	}
//
//	public void setTestScenarios(Set<TestScenario> testScenarios) {
//		this.tableRules.clear();
//		this.testScenarios.addAll(testScenarios);
//	}
//
//	public void addTestScenario(TestScenario testScenario) {
//		testScenarios.add(testScenario);
//	}
//
//	public void removeTestScenario(TestScenario testScenario) {
//		testScenarios.remove(testScenario);
//	}

	public boolean isLastVersion() {
		return isLastVersion;
	}

	public void setLastVersion(boolean isLastVersion) {
		this.isLastVersion = isLastVersion;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.addAll(super.getAllInnerStorableObjects());

		for (Diagram diagram : getDiagrams()) {
			innerStorableObjects.add(diagram);
			innerStorableObjects.addAll(diagram.getAllInnerStorableObjects());
		}

		for (TableRule tableRule : getTableRules()) {
			innerStorableObjects.add(tableRule);
			innerStorableObjects.addAll(tableRule.getAllInnerStorableObjects());
		}

		for (CustomVariable customVariable : getCustomVariables()) {
			innerStorableObjects.add(customVariable);
			innerStorableObjects.addAll(customVariable.getAllInnerStorableObjects());
		}

		for (ExpressionChain expressionChain : getExpressionChains()) {
			innerStorableObjects.add(expressionChain);
			innerStorableObjects.addAll(expressionChain.getAllInnerStorableObjects());
		}

		for (Rule rule : getRules()) {
			innerStorableObjects.add(rule);
			innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
		}

//		for (TestScenario testScenario : getTestScenarios()) {
//			innerStorableObjects.add(testScenario);
//			innerStorableObjects.addAll(testScenario.getAllInnerStorableObjects());
//		}

		return innerStorableObjects;
	}
}
