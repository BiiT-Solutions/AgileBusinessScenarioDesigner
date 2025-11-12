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

import com.biit.abcd.serialization.diagram.SizeDeserializer;
import com.biit.abcd.serialization.diagram.SizeSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = SizeDeserializer.class)
@JsonSerialize(using = SizeSerializer.class)
@Table(name = "diagram_sizes")
@Cacheable(true)
public class Size extends StorableObject {
    private static final long serialVersionUID = -6707522501849361356L;

    private int width;
    private int height;

    public Size() {
        super();
    }

    public Size(int width, int heigh) {
        super();
        setWidth(width);
        setHeight(heigh);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "(w:" + width + ", h:" + height + ")";
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof Size) {
            super.copyBasicInfo(object);
            Size size = (Size) object;
            width = size.getWidth();
            height = size.getHeight();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Size.");
        }
    }
}
