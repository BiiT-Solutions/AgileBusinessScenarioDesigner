package com.biit.abcd.persistence.entity.diagram;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
    }

    @Override
    public DiagramText getText() {
        if (getTable() != null) {
            return new DiagramText(getTable().getName());
        } else {
            return new DiagramText("Table");
        }
    }

    public TableRule getTable() {
        return table;
    }

    public void setTable(TableRule table) {
        this.table = table;
        if (table != null) {
            getText().setText(table.getName());
        }
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
