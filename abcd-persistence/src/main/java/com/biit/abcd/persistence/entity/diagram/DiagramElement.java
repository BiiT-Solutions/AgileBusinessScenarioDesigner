package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.DiagramElementDeserializer;
import com.biit.abcd.serialization.diagram.DiagramElementSerializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = DiagramElementDeserializer.class)
@JsonSerialize(using = DiagramElementSerializer.class)
@Table(name = "diagram_elements")
public abstract class DiagramElement extends DiagramObject {
    private static final long serialVersionUID = -2842225781954290086L;
    @Expose
    private String tooltip;
    @Expose
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "size")
    private Size size;

    @Expose
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "position")
    private Point position;

    @Expose
    private float angle;

    @Expose
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "text")
    private DiagramText text;

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (size != null) {
            size.resetIds();
        }
        if (position != null) {
            position.resetIds();
        }
        if (text != null) {
            text.resetIds();
        }
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public DiagramText getText() {
        return text;
    }

    public void setText(DiagramText biitText) {
        this.text = biitText;
    }

    public static DiagramElement fromJson(String jsonString) {
        try {
            return ObjectMapperFactory.getObjectMapper().readValue(jsonString, DiagramElement.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(DiagramObject object, IUser<Long> user) {
        super.update(object, user);
        if (object instanceof DiagramElement) {
            DiagramElement element = (DiagramElement) object;

            tooltip = element.getTooltip();
            size.setWidth(element.getSize().getWidth());
            size.setHeight(element.getSize().getHeight());
            position.setX(element.getPosition().getX());
            position.setY(element.getPosition().getY());
            angle = element.getAngle();

            if ((text == null) && (element.getText() != null)) {
                text = element.getText();
            } else {
                if (element.getText().getText() != null) {
                    text.setText(element.getText().getText());
                }
                if (element.getText().getFill() != null) {
                    text.setFill(element.getText().getFill());
                }
                if (element.getText().getFontSize() != null) {
                    text.setFontSize(element.getText().getFontSize());
                }
                if (element.getText().getStroke() != null) {
                    text.setStroke(element.getText().getStroke());
                }
                if (element.getText().getStrokeWidth() != null) {
                    text.setStrokeWidth(element.getText().getStrokeWidth());
                }
            }
        }
    }

    public List<DiagramLink> getOutgoingLinks() {
        return getParent().getOutgoingLinks(this);
    }

    public List<DiagramLink> getIncomingLinks() {
        return getParent().getIncomingLinks(this);
    }

    @Override
    public void setCreatedBy(IUser<Long> user) {
        super.setCreatedBy(user);
        text.setCreatedBy(user);
    }

    @Override
    public void setUpdatedBy(IUser<Long> user) {
        super.setUpdatedBy(user);
        text.setUpdatedBy(user);
    }

    @Override
    public void setUpdateTime(Timestamp dateUpdated) {
        super.setUpdateTime(dateUpdated);
        text.setUpdateTime(dateUpdated);
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (size != null) {
            innerStorableObjects.add(size);
            innerStorableObjects.addAll(size.getAllInnerStorableObjects());
        }
        if (position != null) {
            innerStorableObjects.add(position);
            innerStorableObjects.addAll(position.getAllInnerStorableObjects());
        }
        if (text != null) {
            innerStorableObjects.add(text);
            innerStorableObjects.addAll(text.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramElement) {
            super.copyData(object);

            DiagramElement diagramSource = (DiagramElement) object;
            tooltip = diagramSource.getTooltip();
            angle = diagramSource.getAngle();
            text = new DiagramText();
            text.copyData(diagramSource.getText());

            Size size = new Size();
            size.copyData(diagramSource.getSize());
            setSize(size);

            Point point = new Point();
            point.copyData(diagramSource.getPosition());
            setPosition(point);
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramElement.");
        }
    }

    @Override
    public String toString() {
        if (getText() != null) {
            return getText().getText();
        }
        return super.toString();
    }
}
