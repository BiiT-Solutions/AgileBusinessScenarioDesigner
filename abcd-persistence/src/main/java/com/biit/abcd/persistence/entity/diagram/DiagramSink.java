package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "diagram_sink")
public class DiagramSink extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private ExpressionChain formExpression;

	public ExpressionChain getFormExpression() {
		return formExpression;
	}

	public void setFormExpression(ExpressionChain formExpression) {
		this.formExpression = formExpression;
	}

	public DiagramSink() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("End");
		setBiitText(biitText);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(formExpression);
		innerStorableObjects.addAll(formExpression.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

}