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

import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.serialization.diagram.DiagramRuleDeserializer;
import com.biit.abcd.serialization.diagram.DiagramRuleSerializer;
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
@JsonDeserialize(using = DiagramRuleDeserializer.class)
@JsonSerialize(using = DiagramRuleSerializer.class)
@Table(name = "diagram_rule")
public class DiagramRule extends DiagramElement {
    private static final long serialVersionUID = -1428491567791916924L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule")
    private Rule rule;

    //For json conversion.
    @Transient
    private transient String ruleId;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public DiagramRule() {
        super();
    }

    @Override
    public DiagramText getText() {
        if (getRule() != null) {
            return new DiagramText(getRule().getName());
        } else {
            return new DiagramText("Rule");
        }
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (rule != null) {
            rule.resetIds();
        }
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (rule != null) {
            innerStorableObjects.add(rule);
            innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramRule) {
            super.copyData(object);
            DiagramRule diagramRule = (DiagramRule) object;

            if (diagramRule.getRule() != null) {
                Rule rule = new Rule();
                rule.copyData(diagramRule.getRule());
                setRule(rule);
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramRule.");
        }
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
