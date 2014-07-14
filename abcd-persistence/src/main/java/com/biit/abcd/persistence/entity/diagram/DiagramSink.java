package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

@Entity
@Table(name = "DIAGRAM_SINK")
public class DiagramSink  extends DiagramElement{
	
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
}