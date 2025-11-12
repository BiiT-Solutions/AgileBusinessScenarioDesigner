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

import com.biit.abcd.serialization.diagram.DiagramSinkDeserializer;
import com.biit.abcd.serialization.diagram.DiagramSinkSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = DiagramSinkDeserializer.class)
@JsonSerialize(using = DiagramSinkSerializer.class)
@Table(name = "diagram_sink")
public class DiagramSink extends DiagramExpression {
    private static final long serialVersionUID = 1993423029316963730L;

    public DiagramSink() {
        super();
    }

    @Override
    public DiagramText getText() {
        return new DiagramText("End");
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramSink) {
            super.copyData(object);
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramSink.");
        }
    }

}
