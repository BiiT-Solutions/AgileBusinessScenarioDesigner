package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.serialization.diagram.DiagramForkDeserializer;
import com.biit.abcd.serialization.diagram.DiagramForkSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramForkDeserializer.class)
@JsonSerialize(using = DiagramForkSerializer.class)
@Table(name = "diagram_fork")
public class DiagramFork extends DiagramElement {
    private static final long serialVersionUID = -998601407465207830L;
    // Due to bug (https://hibernate.atlassian.net/browse/HHH-5559)
    // orphanRemoval is not working correctly in @OneToOne.
    // We change a @OneToMany list with only one element.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "diagram_fork_references", joinColumns = @JoinColumn(name = "diagram_fork", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "reference", referencedColumnName = "id"))
    private List<ExpressionValueTreeObjectReference> references;

    public DiagramFork() {
        super();
        DiagramText biitText = new DiagramText();
        biitText.setText("Fork");
        setText(biitText);
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (references != null) {
            for (ExpressionValueTreeObjectReference expressionValueTreeObjectReference : references) {
                expressionValueTreeObjectReference.resetIds();
            }
        }
    }

    public List<ExpressionValueTreeObjectReference> getReferences() {
        return references;
    }

    public void setReferences(List<ExpressionValueTreeObjectReference> references) {
        this.references = references;
    }

    public ExpressionValueTreeObjectReference getReference() {
        if ((references == null) || references.isEmpty()) {
            return null;
        }
        return references.get(0);
    }

    public void setReference(ExpressionValueTreeObjectReference reference) {
        if (references == null) {
            references = new ArrayList<>();
        }
        if (!references.isEmpty()) {
            references.clear();
        }
        references.add(reference);
    }

    /**
     * When the Diagram Fork changes, all outgoing links must be updated. I.e.
     * links first expression element must be updated.
     */
    public void resetOutgoingLinks() {
        for (DiagramLink outLink : getOutgoingLinks()) {
            if (getReference() != null) {
                Expression expression = getReference().generateCopy();
                expression.resetIds();
                expression.setEditable(false);
                outLink.replaceExpressions(expression);
            }
        }
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (references != null) {
            for (ExpressionValueTreeObjectReference child : references) {
                if (child != null) {
                    innerStorableObjects.add(child);
                    innerStorableObjects.addAll(child.getAllInnerStorableObjects());
                }
            }
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramFork) {
            super.copyData(object);
            DiagramFork diagramFork = (DiagramFork) object;
            if (references == null) {
                references = new ArrayList<>();
            }
            references.clear();
            ExpressionValueTreeObjectReference newReference;
            try {
                if (diagramFork.getReference() != null) {
                    newReference = diagramFork.getReference().getClass().newInstance();
                    newReference.copyData(diagramFork.getReference());
                    references.add(newReference);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramFork.");
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramFork.");
        }
    }
}