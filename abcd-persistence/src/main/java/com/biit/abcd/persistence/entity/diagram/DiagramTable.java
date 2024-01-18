package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.serialization.diagram.DiagramTableDeserializer;
import com.biit.abcd.serialization.diagram.DiagramTableSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramTableDeserializer.class)
@JsonSerialize(using = DiagramTableSerializer.class)
@Table(name = "diagram_table")
public class DiagramTable extends DiagramElement {
    private static final long serialVersionUID = 8876603849399308548L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_rule")
    private TableRule table;

    //For json conversion.
    @Transient
    private transient String tableId;

    public DiagramTable() {
        super();
        DiagramText biitText = new DiagramText();
        biitText.setText("Table");
        setText(biitText);
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}