package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.FormExpression;

@Entity
@Table(name = "DIAGRAM_CALCULATION")
public class DiagramCalculation extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private FormExpression formExpression;

	public DiagramCalculation() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}

	public FormExpression getFormExpression() {
		return formExpression;
	}

	public void setFormExpression(FormExpression formExpression) {
		this.formExpression = formExpression;
	}
}