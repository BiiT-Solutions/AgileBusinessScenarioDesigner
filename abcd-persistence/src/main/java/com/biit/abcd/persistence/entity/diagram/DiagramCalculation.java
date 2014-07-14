package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

@Entity
@Table(name = "DIAGRAM_CALCULATION")
public class DiagramCalculation extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private ExpressionChain formExpression;

	public DiagramCalculation() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}

	public ExpressionChain getFormExpression() {
		return formExpression;
	}

	public void setFormExpression(ExpressionChain formExpression) {
		this.formExpression = formExpression;
	}
}