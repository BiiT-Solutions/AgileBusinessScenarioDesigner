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

import com.biit.abcd.serialization.diagram.DiagramChildDeserializer;
import com.biit.abcd.serialization.diagram.DiagramChildSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramChildDeserializer.class)
@JsonSerialize(using = DiagramChildSerializer.class)
@Table(name = "diagram_child")
public class DiagramChild extends DiagramElement {
    private static final long serialVersionUID = -5504613126614498746L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diagram")
    private Diagram diagram;

    public DiagramChild() {
        super();
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (diagram != null) {
            diagram.resetIds();
        }
    }

    @Override
    public DiagramText getText() {
        if (getDiagram() != null) {
            return new DiagramText(diagram.getName());
        } else {
            return new DiagramText("Diagram");
        }
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (diagram != null) {
            innerStorableObjects.add(diagram);
            innerStorableObjects.addAll(diagram.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramChild) {
            super.copyData(object);
            DiagramChild diagramChild = (DiagramChild) object;
            if (diagramChild.getDiagram() != null) {
                Diagram childDiagram = new Diagram();
                childDiagram.copyData(diagramChild.getDiagram());
                setDiagram(childDiagram);
            } else {
                setDiagram(null);
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramChild.");
        }
    }
}
