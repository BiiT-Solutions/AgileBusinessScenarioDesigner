package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Rule;

@Entity
@Table(name = "DIAGRAM_RULE")
public class DiagramRule extends DiagramElement{
	
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
}