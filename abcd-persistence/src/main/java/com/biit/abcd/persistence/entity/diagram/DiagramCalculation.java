package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DIAGRAM_CALCULATION")
public class DiagramCalculation extends DiagramElement{
	
	public DiagramCalculation() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}
}