package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.entity.serialization.AnswerDeserializer;
import com.biit.abcd.persistence.entity.serialization.AnswerSerializer;
import com.biit.abcd.persistence.entity.serialization.BaseRepeatableGroupDeserializer;
import com.biit.abcd.persistence.entity.serialization.BaseRepeatableGroupSerializer;
import com.biit.abcd.persistence.entity.serialization.FormDeserializer;
import com.biit.abcd.persistence.entity.serialization.FormSerializer;
import com.biit.abcd.persistence.entity.serialization.QuestionDeserializer;
import com.biit.abcd.persistence.entity.serialization.QuestionSerializer;
import com.biit.abcd.persistence.entity.serialization.StorableObjectDeserializer;
import com.biit.abcd.persistence.entity.serialization.TreeObjectDeserializer;
import com.biit.abcd.persistence.entity.serialization.TreeObjectSerializer;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.json.serialization.hibernate.HibernateProxyTypeAdapter;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name = "tree_forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "version",
		"organization_id" }) })
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH, columnDefinition = "varchar("
		+ StorableObject.MAX_UNIQUE_COLUMN_LENGTH + ")"))
@Cacheable(true)
public class Form extends BaseForm {
	private static final long serialVersionUID = 185712950929311653L;

	@Column(name = "available_from", nullable = false)
	private Timestamp availableFrom;

	@Column(name = "available_to")
	private Timestamp availableTo;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "tree_forms_diagram", joinColumns = @JoinColumn(name = "form", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "diagram", referencedColumnName = "id"))
	private Set<Diagram> diagrams;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "tree_forms_rule_decision_table", joinColumns = @JoinColumn(name = "form", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "table_rule", referencedColumnName = "id"))
	private Set<TableRule> tableRules;

	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CustomVariable> customVariables;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "tree_forms_expressions_chain", joinColumns = @JoinColumn(name = "form", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "expression_chain", referencedColumnName = "id"))
	private Set<ExpressionChain> expressionChains;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "tree_forms_rule", joinColumns = @JoinColumn(name = "form", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rule", referencedColumnName = "id"))
	private Set<Rule> rules;

	// For avoiding
	// "ObjectDeletedException: deleted object would be re-saved by cascade (remove deleted object from associations)"
	// launch when removing a customvariable and other is renamed as this one,
	// we need to disable orphanRemoval=true of
	// children and implement ourselves.
	@Transient
	private Set<CustomVariable> customVariablesToDelete;

	@Transient
	private boolean isLastVersion = true;

	@Enumerated(EnumType.STRING)
	private FormWorkStatus status = FormWorkStatus.DESIGN;

	public Form() {
		super();
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChains = new HashSet<>();
		rules = new HashSet<>();
		customVariablesToDelete = new HashSet<>();
		setAvailableFrom(new Timestamp(new Date().getTime()));
	}

	public Form(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		diagrams = new HashSet<>();
		tableRules = new HashSet<>();
		customVariables = new HashSet<>();
		expressionChains = new HashSet<>();
		rules = new HashSet<>();
		customVariablesToDelete = new HashSet<>();
		setAvailableFrom(new Timestamp(new Date().getTime()));
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
	}

	public Form createNewVersion(IUser<Long> user) throws CharacterNotAllowedException, NotValidStorableObjectException {
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(getVersion() + 1);
		newVersion.resetIds();
		newVersion.setCreatedBy(user);
		newVersion.setUpdatedBy(user);
		newVersion.setCreationTime();
		newVersion.setUpdateTime();
		// Update ValidTo of current version: 84600000 milliseconds in a day
		long newValidTo = newVersion.getAvailableFrom().getTime() - 84600000;
		Timestamp validTo = newValidTo > getAvailableFrom().getTime() ? new Timestamp(newValidTo) : getAvailableFrom();
		setAvailableTo(validTo);
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
			formElements.put(children.getOriginalReference(), children);
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

	private void updateTreeObjectReferences() {

		// ComparatorId -> New StorableObject.
		Map<String, TreeObject> formElements = new HashMap<>();
		Set<TreeObject> formElementsChildren = getAllChildrenInHierarchy(TreeObject.class);
		formElementsChildren.add(this);
		for (TreeObject children : formElementsChildren) {
			formElements.put(children.getOriginalReference(), children);
		}

		for (ExpressionChain expressionChain : getExpressionChains()) {
			updateTreeObjectReferences(expressionChain.getAllInnerStorableObjects(), formElements);
		}

		for (TableRule tableRule : getTableRules()) {
			updateTreeObjectReferences(tableRule.getAllInnerStorableObjects(), formElements);
		}

		for (Rule rule : getRules()) {
			updateTreeObjectReferences(rule.getAllInnerStorableObjects(), formElements);
		}

		for (Diagram diagram : getDiagrams()) {
			updateTreeObjectReferences(diagram.getAllInnerStorableObjects(), formElements);
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
				DiagramChild diagramChild = (DiagramChild) child;
				if (diagramChild.getDiagram() != null) {
					if (formDiagrams.get(diagramChild.getDiagram().getComparationId()) != null) {
						diagramChild.setDiagram(formDiagrams.get(diagramChild.getDiagram().getComparationId()));
					} else {
						AbcdLogger.warning(this.getClass().getName(), "Adding diagram '" + diagramChild.getDiagram()
								+ "'.");
						formDiagrams.put(diagramChild.getDiagram().getComparationId(), diagramChild.getDiagram());
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
	private void updateDiagramExpressionReferences(Set<StorableObject> storableObjects,
			Map<String, ExpressionChain> formExpressionChains) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramExpression) {
				DiagramExpression diagramExpression = (DiagramExpression) child;
				if (diagramExpression.getExpression() != null) {
					if (formExpressionChains.get(diagramExpression.getExpression().getComparationId()) != null) {
						diagramExpression.setExpression(formExpressionChains.get(diagramExpression.getExpression()
								.getComparationId()));
					} else {
						AbcdLogger.warning(this.getClass().getName(),
								"Adding expression '" + diagramExpression.getExpression() + "'.");
						formExpressionChains.put(diagramExpression.getExpression().getComparationId(),
								diagramExpression.getExpression());
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
				if (diagramRule != null && diagramRule.getRule() != null) {
					if (formRules.get(diagramRule.getRule().getComparationId()) != null) {
						diagramRule.setRule(formRules.get(diagramRule.getRule().getComparationId()));
					} else {
						AbcdLogger.warning(this.getClass().getName(), "Adding rule '" + diagramRule.getRule() + "'.");
						formRules.put(diagramRule.getRule().getComparationId(), diagramRule.getRule());
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
	private void updateDiagramTableRuleReferences(Set<StorableObject> storableObjects,
			Map<String, TableRule> formTableRules) {
		for (StorableObject child : storableObjects) {
			if (child instanceof DiagramTable) {
				DiagramTable diagramTable = (DiagramTable) child;
				if (diagramTable != null && diagramTable.getTable() != null) {
					if (formTableRules.get(diagramTable.getTable().getComparationId()) != null) {
						diagramTable.setTable(formTableRules.get(diagramTable.getTable().getComparationId()));
					} else {
						AbcdLogger
								.warning(this.getClass().getName(), "Adding table '" + diagramTable.getTable() + "'.");
						formTableRules.put(diagramTable.getTable().getComparationId(), diagramTable.getTable());
					}
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
				if (expressionValueTreeObjectReference.getReference() != null) {
					if (formElements.get(expressionValueTreeObjectReference.getReference().getOriginalReference()) != null) {
						expressionValueTreeObjectReference.setReference(formElements
								.get(expressionValueTreeObjectReference.getReference().getOriginalReference()));
					} else {
						AbcdLogger.warning(this.getClass().getName(), "Adding reference '"
								+ expressionValueTreeObjectReference.getReference() + "'.");
						formElements.put(expressionValueTreeObjectReference.getReference().getOriginalReference(),
								expressionValueTreeObjectReference.getReference());
					}
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
	private void updateVariablesReferences(Set<StorableObject> storableObjects,
			Map<String, CustomVariable> formVariables) {
		for (StorableObject child : storableObjects) {
			if (child instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) child;
				if (expressionValueCustomVariable.getVariable() != null) {
					if (formVariables.get(expressionValueCustomVariable.getVariable().getComparationId()) != null) {
						expressionValueCustomVariable.setVariable(formVariables.get(expressionValueCustomVariable
								.getVariable().getComparationId()));
					} else {
						formVariables.put(expressionValueCustomVariable.getVariable().getComparationId(),
								expressionValueCustomVariable.getVariable());
					}
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
			// locale-specific
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateCreated);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long time = cal.getTimeInMillis();
			availableFrom = new Timestamp(time);
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
					if (diagramChild.getDiagram() != null && diagramChild.getDiagram().equals(diagram)) {
						diagramChild.setDiagram(null);
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
	 * @param treeObject the element to retrieve the variables.
	 * @return the list of variables.
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
	 * @param treeObjectClass
	 *            the class
	 * @return the list of variables.
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
	 * @param name the name of the variable.
	 * @param scope the context of the variable.
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
	 * Returns the parent diagram of a Diagram if it has or null if it is a root
	 * diagram.
	 * 
	 * @param diagram
	 *            the diagram.
	 * @return the parent.
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

		return innerStorableObjects;
	}

	public Set<CustomVariable> getCustomVariablesToDelete() {
		return customVariablesToDelete;
	}

	public void setCustomVariablesToDelete(Set<CustomVariable> customVariablesToDelete) {
		this.customVariablesToDelete = customVariablesToDelete;
	}

	public void remove(CustomVariable customVariableToDelete) {
		customVariables.remove(customVariableToDelete);
		customVariablesToDelete.add(customVariableToDelete);
		customVariableToDelete.setForm(null);
	}

	public FormWorkStatus getStatus() {
		return status;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}

	public void add(CustomVariable formCustomVariable) {
		customVariables.add(formCustomVariable);
		formCustomVariable.setForm(this);
	}

	public void remove(Rule rule) {
		rules.remove(rule);
	}

	@Override
	public void updateChildrenSortSeqs() {
		super.updateChildrenSortSeqs();
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.

		// Sort the expressions
		Set<ExpressionChain> expressionChainList = getExpressionChains();
		if (expressionChainList != null && !expressionChainList.isEmpty()) {
			for (ExpressionChain expressionChain : expressionChainList) {
				expressionChain.updateChildrenSortSeqs();
			}
		}

		// Sort the rules
		Set<Rule> rulesList = getRules();
		if (rulesList != null && !rulesList.isEmpty()) {
			for (Rule rule : rulesList) {
				rule.getConditions().updateChildrenSortSeqs();
				rule.getActions().updateChildrenSortSeqs();
			}
		}
		// Sort the table rule rows
		Set<TableRule> tableRules = getTableRules();
		if (tableRules != null && !tableRules.isEmpty()) {
			for (TableRule tableRule : tableRules) {
				List<TableRuleRow> tableRuleRows = tableRule.getRules();
				if (tableRuleRows != null && !tableRuleRows.isEmpty()) {
					for (TableRuleRow tableRuleRow : tableRuleRows) {
						tableRuleRow.getConditions().updateChildrenSortSeqs();
						tableRuleRow.getAction().updateChildrenSortSeqs();
					}
				}
			}
		}

		Set<Diagram> diagrams = getDiagrams();
		if (diagrams != null && !diagrams.isEmpty()) {
			for (Diagram diagram : diagrams) {
				Set<DiagramObject> nodes = diagram.getDiagramObjects();
				if (nodes != null && !nodes.isEmpty()) {
					for (DiagramObject node : nodes) {
						if (node instanceof DiagramLink) {
							DiagramLink nodeLink = (DiagramLink) node;
							nodeLink.getExpressionChain().updateChildrenSortSeqs();
						}
					}
				}
			}
		}
	}

	/**
	 * Disable lazy behavior of Form element. Needed for using spring cache.
	 */
	@Override
	public void initializeSets() {
		super.initializeSets();
		// Initializes the sets for lazy-loading (within the same session)
		getDiagrams().size();
		getTableRules().size();
		getCustomVariables().size();
		getExpressionChains().size();
		getRules().size();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	public synchronized static TreeObject move(TreeObject objectToMove, TreeObject toParent)
			throws ChildrenNotFoundException, NotValidChildException, ElementIsReadOnly {
		if (!Objects.equals(objectToMove.getAncestor(Form.class), toParent.getAncestor(Form.class))) {
			throw new NotValidChildException("Root form for each element is different");
		}
		TreeObject newInstanceOfObjectToMove = TreeObject.move(objectToMove, toParent);

		Form form = (Form) objectToMove.getAncestor(Form.class);
		form.updateTreeObjectReferences();

		return newInstanceOfObjectToMove;
	}

	/**
	 * Transforms the form classes in Json strings.<br>
	 * Only transforms the form structure, it doesn't add rules, expressions,
	 * variables ...
	 * 
	 * @return the json representation.
	 */
	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		gsonBuilder.registerTypeAdapter(Form.class, new FormSerializer());
		gsonBuilder.registerTypeAdapter(SimpleFormView.class, new FormSerializer());
		gsonBuilder.registerTypeAdapter(Category.class, new TreeObjectSerializer<Category>());
		gsonBuilder.registerTypeAdapter(Group.class, new BaseRepeatableGroupSerializer<Group>());
		gsonBuilder.registerTypeAdapter(Question.class, new QuestionSerializer());
		gsonBuilder.registerTypeAdapter(Answer.class, new AnswerSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}

	public static Form fromJson(String jsonString) {
		return getFormDeserializer().fromJson(jsonString, Form.class);
	}

	public static Form[] fromJsonList(String jsonString) {
		return getFormDeserializer().fromJson(jsonString, Form[].class);
	}

	private static Gson getFormDeserializer() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TreeObject.class, new StorableObjectDeserializer<TreeObject>());
		gsonBuilder.registerTypeAdapter(Form.class, new FormDeserializer());
		gsonBuilder.registerTypeAdapter(Category.class, new TreeObjectDeserializer<Category>(Category.class));
		gsonBuilder.registerTypeAdapter(Group.class, new BaseRepeatableGroupDeserializer<Group>(Group.class));
		gsonBuilder.registerTypeAdapter(Question.class, new QuestionDeserializer());
		gsonBuilder.registerTypeAdapter(Answer.class, new AnswerDeserializer());
		return gsonBuilder.create();
	}
}
