package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Expressions;

@Entity
@Table(name = "DIAGRAM_CALCULATION")
public class DiagramCalculation extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private Expressions formExpression;

	public DiagramCalculation() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}

	public Expressions getFormExpression() {
		return formExpression;
	}

	public void setFormExpression(Expressions formExpression) {
		this.formExpression = formExpression;
	}
}