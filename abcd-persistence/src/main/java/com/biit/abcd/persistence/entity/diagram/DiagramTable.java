package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.rules.TableRule;

@Entity
@Table(name = "diagram_table")
public class DiagramTable extends DiagramElement{

	@ManyToOne(fetch = FetchType.EAGER)
	private TableRule table;

	public DiagramTable() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Table");
		setBiitText(biitText);
	}

	public TableRule getTable() {
		return table;
	}

	public void setTable(TableRule table) {
		this.table = table;
	}
}