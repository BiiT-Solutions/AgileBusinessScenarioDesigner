package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

@Entity
@Table(name = "DIAGRAM_FORK")
public class DiagramFork extends DiagramElement {

	//TODO right now this leaves some orphan references when changing the reference.
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionValueTreeObjectReference reference;

	public DiagramFork() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Fork");
		setBiitText(biitText);
	}

	public ExpressionValueTreeObjectReference getReference() {
		return reference;
	}

	public void setReference(ExpressionValueTreeObjectReference reference) {
		this.reference = reference;
	}
}