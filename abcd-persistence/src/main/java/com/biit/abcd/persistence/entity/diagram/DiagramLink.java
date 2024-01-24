package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.serialization.diagram.DiagramLinkDeserializer;
import com.biit.abcd.serialization.diagram.DiagramLinkSerializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramLinkDeserializer.class)
@JsonSerialize(using = DiagramLinkSerializer.class)
@Table(name = "diagram_links")
public class DiagramLink extends DiagramObject {
    private static final long serialVersionUID = 5533529349122059755L;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "expression_chain")
    private ExpressionChain expressionChain;


    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "source")
    private Node source;


    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "target")
    private Node target;

    private String text;


    private boolean smooth;


    private boolean manhattan;

    @Column(length = 1000000)
    private String attrs;

    @Column(length = 1000000)
    private String vertices;

    public DiagramLink() {
        super();
        expressionChain = new ExpressionChain();
    }

    public DiagramLink(Node source, Node target) {
        super();
        expressionChain = new ExpressionChain();
        setSource(source);
        setTarget(target);
    }

    @Override
    public void resetIds() {
        super.resetIds();
        expressionChain.resetIds();
        if (source != null) {
            source.resetIds();
        }
        if (target != null) {
            target.resetIds();
        }
    }

    public Node getSource() {
        return source;
    }

    public DiagramElement getSourceElement() {
        if (getParent() == null) {
            throw new RuntimeException("Diagram Link getSourceElement used without diagram.");
        }
        if (source == null) {
            return null;
        }
        String jointJsId = source.getJointjsId();
        return (DiagramElement) getParent().findDiagramObjectByJointJsId(jointJsId);
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public DiagramElement getTargetElement() {
        if (getParent() == null) {
            throw new RuntimeException("Diagram Link getTargetElement used without diagram.");
        }
        String jointJsId = target.getJointjsId();
        return (DiagramElement) getParent().findDiagramObjectByJointJsId(jointJsId);
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public boolean isManhattan() {
        return manhattan;
    }

    public void setManhattan(boolean manhattan) {
        this.manhattan = manhattan;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setVertices(String vertices) {
        this.vertices = vertices;
    }

    public String getVertices() {
        return vertices;
    }

    @Override
    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasSourceFork() {
        if (getSourceElement() instanceof DiagramFork) {
            return true;
        }
        return false;
    }

    public boolean isOthers() {
        DiagramElement source = getSourceElement();
        if (source instanceof DiagramFork) {
            DiagramFork forkSource = (DiagramFork) source;
            if (!isAnswerEmpty()) {
                return false;
            }

            if (getParent() == null) {
                throw new RuntimeException("Diagram Link isOthers used without diagram.");
            }

            List<DiagramLink> links = getParent().getOutgoingLinks(forkSource);
            if (links.size() == 1) {
                return false;
            }
            int numOfEmptyLinks = 0;
            for (DiagramLink link : links) {
                if (link.isAnswerEmpty()) {
                    numOfEmptyLinks++;
                }
            }
            if (numOfEmptyLinks > 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean isAnswerEmpty() {
        if ((expressionChain == null) || expressionChain.getExpressions().isEmpty()) {
            return true;
        } else if (hasSourceFork()) {
            if (expressionChain.getExpressions().size() <= 1) {
                return true;
            }
        }
        return false;
    }

    public String getCorrectedText() {
        if (hasSourceFork()) {
            // Source is Fork
            if (isAnswerEmpty()) {
                if (isOthers()) {
                    return "others";
                } else {
                    return "";
                }
            } else {
                if (expressionChain != null) {
                    return getTextWithoutFirstExpression();
                    // return expressionChain.getExpression();
                } else {
                    return "";
                }
            }
        } else {
            return getText();
        }
    }

    public void clear() {
        expressionChain = null;
        setText("");
    }

    @Override
    public void setCreatedBy(IUser<Long> user) {
        super.setCreatedBy(user);
        if (source != null) {
            source.setCreatedBy(user);
        }
        if (target != null) {
            target.setCreatedBy(user);
        }
    }

    @Override
    public void setUpdatedBy(IUser<Long> user) {
        super.setUpdatedBy(user);
        if (source != null) {
            source.setUpdatedBy(user);
        }
        if (target != null) {
            target.setUpdatedBy(user);
        }
    }

    @Override
    public void setUpdateTime(Timestamp dateUpdated) {
        super.setUpdateTime(dateUpdated);
        if (source != null) {
            source.setUpdateTime(dateUpdated);
        }
        if (target != null) {
            target.setUpdateTime(dateUpdated);
        }
    }

    @Override
    public void update(DiagramObject object, IUser<Long> user) {
        super.update(object, user);
        if (object instanceof DiagramLink) {
            DiagramLink link = (DiagramLink) object;

            if (!link.expressionChain.getExpressions().isEmpty()) {
                expressionChain = link.expressionChain;
            }

            if (source == null) {
                source = new Node();
            }
            source.update(link.getSource());

            if (target == null) {
                target = new Node();
            }
            target.update(link.getTarget());

            if (link.getText() != null) {
                text = new String(link.getText());
            }
            smooth = link.isSmooth();
            manhattan = link.isManhattan();
            if (link.getAttrs() != null) {
                attrs = new String(link.getAttrs());
            }
            if (link.getVertices() != null) {
                vertices = new String(link.getVertices());
            }
        }
    }

    public ExpressionChain getExpressionChain() {
        return expressionChain;
    }

    /**
     * Avoid this method. Expression chain is a OneToOne relationship and
     * currently Hibernate doesn't handle correctly the Orphan removal. Use
     * setExpressions of ExpressionChain.
     *
     * @param expressionChain the expression
     */
    public void setExpressionChain(ExpressionChain expressionChain) {
        this.expressionChain = expressionChain;
    }

    /**
     * Replace any existing expressions with this expression.
     *
     * @param expression the expression
     */
    public void replaceExpressions(Expression expression) {
        getExpressionChain().setExpressions(Arrays.asList(expression));
    }

    public String getTextWithoutFirstExpression() {
        List<Expression> auxExps = expressionChain.getExpressions();
        if (auxExps.size() > 0) {
            List<Expression> expressions = auxExps.subList(1, auxExps.size());
            if (expressions.isEmpty()) {
                if (isOthers()) {
                    return "others";
                } else {
                    return "";
                }
            }
            String result = "";
            for (Expression expression : expressions) {
                result += expression.getRepresentation(true) + " ";
            }
            return result.trim();
        }
        return "";
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (source != null) {
            innerStorableObjects.add(source);
            innerStorableObjects.addAll(source.getAllInnerStorableObjects());
        }
        if (target != null) {
            innerStorableObjects.add(target);
            innerStorableObjects.addAll(target.getAllInnerStorableObjects());
        }
        if (expressionChain != null) {
            innerStorableObjects.add(expressionChain);
            innerStorableObjects.addAll(expressionChain.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramLink) {
            super.copyData(object);
            DiagramLink diagramLink = (DiagramLink) object;

            if (diagramLink.getExpressionChain() != null) {
                ExpressionChain expressionChain = new ExpressionChain();
                expressionChain.copyData(diagramLink.getExpressionChain());
                setExpressionChain(expressionChain);
            }

            if (diagramLink.getSource() != null) {
                Node source = new Node();
                source.copyData(diagramLink.getSource());
                setSource(source);
            }

            if (diagramLink.getTarget() != null) {
                Node target = new Node();
                target.copyData(diagramLink.getTarget());
                setTarget(target);
            }

            text = diagramLink.getText();
            manhattan = diagramLink.isManhattan();
            smooth = diagramLink.isSmooth();
            vertices = diagramLink.getVertices();
            attrs = diagramLink.getAttrs();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramLink.");
        }
    }

    @Override
    public String toString() {
        return "DiagramLink{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }
}
