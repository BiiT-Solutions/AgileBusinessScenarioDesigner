package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_rule")
public class DiagramRule extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private Rule rule;

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public DiagramRule() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Rule");
		setBiitText(biitText);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (rule != null) {
			rule.resetIds();
		}
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (rule != null) {
			innerStorableObjects.add(rule);
			innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramRule) {
			super.copyData(object);
			DiagramRule diagramRule = (DiagramRule) object;

			Rule rule = new Rule();
			rule.copyData(diagramRule.getRule());
			setRule(rule);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramRule.");
		}
	}
}