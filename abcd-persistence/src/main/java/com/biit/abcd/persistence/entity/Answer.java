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
import com.biit.abcd.serialization.AnswerDeserializer;
import com.biit.abcd.serialization.AnswerSerializer;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@JsonDeserialize(using = AnswerDeserializer.class)
@JsonSerialize(using = AnswerSerializer.class)
@Table(name = "tree_answers")
@Cacheable(true)
public class Answer extends BaseAnswer {
    private static final long serialVersionUID = -7358559199240262641L;
    private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<Class<? extends TreeObject>>(
            Arrays.asList(Answer.class));

    public Answer() {
    }

    public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super();
        setName(name);
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        copyBasicInfo(object);
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        CheckDependencies.checkTreeObjectDependencies(this);
    }

    @Override
    public String getSimpleAsciiName() {
        return getName().replaceAll("[^a-zA-Z0-9.]", "");
    }

    @Override
    protected List<Class<? extends TreeObject>> getAllowedChildren() {
        return ALLOWED_CHILDREN;
    }

    /**
     * Checks if this answer is a subanswer by looking if it has a parent and if
     * it has, if it is an answer.
     *
     * @return true is is a subanswer.
     */
    public boolean isSubanswer() {
        if (getParent() == null || !(getParent() instanceof Answer)) {
            return false;
        }
        return true;
    }

    /**
     * Calculates a unique name of an answer or subanswer in the a question.
     */
    @Override
    public String getDefaultName(TreeObject parent, int startingIndex) {
        String name;
        if (parent != null) {
            name = getDefaultTechnicalName() + startingIndex;
            for (TreeObject child : parent.getChildren()) {
                if ((child.getClass() == this.getClass()) && (child.getName() != null) && child.getName().equals(name)) {
                    return getDefaultName(parent, startingIndex + 1);
                }
                for (TreeObject subanswer : child.getChildren()) {
                    if ((subanswer.getClass() == this.getClass()) && (subanswer.getName() != null)
                            && subanswer.getName().equals(name)) {
                        return getDefaultName(parent, startingIndex + 1);
                    }
                }
            }
        } else {
            name = getDefaultTechnicalName();
        }
        return name;
    }
}
