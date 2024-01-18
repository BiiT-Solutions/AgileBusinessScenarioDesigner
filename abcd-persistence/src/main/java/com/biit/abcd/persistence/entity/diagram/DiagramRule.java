package com.biit.abcd.persistence.entity.diagram;

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
        DiagramText biitText = new DiagramText();
        biitText.setText("Rule");
        setText(biitText);
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