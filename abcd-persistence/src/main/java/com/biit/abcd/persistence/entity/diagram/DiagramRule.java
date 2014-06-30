package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DIAGRAM_RULE")
public class DiagramRule extends DiagramElement{
	
	public DiagramRule() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Rule");
		setBiitText(biitText);
	}
}