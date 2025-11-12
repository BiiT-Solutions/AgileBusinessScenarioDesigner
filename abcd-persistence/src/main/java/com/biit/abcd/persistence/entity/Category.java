package com.biit.abcd.persistence.entity;

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

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.serialization.CategoryDeserializer;
import com.biit.abcd.serialization.CategorySerializer;
import com.biit.form.entity.BaseCategory;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = CategoryDeserializer.class)
@JsonSerialize(using = CategorySerializer.class)
@Table(name = "tree_categories")
@Cacheable(true)
public class Category extends BaseCategory {
    private static final long serialVersionUID = -244939595326795141L;

    public Category() {
        super();
    }

    public Category(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super(name);
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        copyBasicInfo(object);
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        CheckDependencies.checkTreeObjectDependencies(this);
    }
}
