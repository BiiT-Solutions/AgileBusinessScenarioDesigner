package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_table")
public class DiagramTable extends DiagramElement {
	private static final long serialVersionUID = 8876603849399308548L;
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

	@Override
	public void resetIds() {
		super.resetIds();
		if (table != null) {
			table.resetIds();
		}
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (table != null) {
			innerStorableObjects.add(table);
			innerStorableObjects.addAll(table.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramTable) {
			super.copyData(object);
			DiagramTable diagramTable = (DiagramTable) object;
			if (diagramTable.getTable() != null) {
				TableRule tableRule = new TableRule();
				tableRule.copyData(diagramTable.getTable());
				this.setTable(tableRule);
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramTable.");
		}
	}
}