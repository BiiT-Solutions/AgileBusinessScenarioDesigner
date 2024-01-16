package com.biit.abcd.persistence.entity.diagram;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "diagram_objects")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class DiagramObject extends StorableObject {
    private static final long serialVersionUID = -6312500925414596116L;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent")
    private Diagram parent;


    @Enumerated(EnumType.STRING)
    private DiagramObjectType type;


    @Column(name = "jointjs_id")
    private String jointjsId;


    private String embeds;


    private int z;

    public DiagramObjectType getType() {
        return type;
    }

    public void setType(DiagramObjectType type) {
        this.type = type;
    }

    public String getJointjsId() {
        return jointjsId;
    }

    public void setJointjsId(String id) {
        jointjsId = id;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getEmbeds() {
        return embeds;
    }

    public void setEmbeds(String embeds) {
        this.embeds = embeds;
    }

    public void update(DiagramObject object, IUser<Long> user) {
        embeds = object.embeds;
        z = object.z;
        setUpdatedBy(user);
        setUpdateTime();
    }

    public Diagram getParent() {
        return parent;
    }

    public void setParent(Diagram parent) {
        this.parent = parent;
    }

    public static DiagramObject fromJson(String jsonString) {
        try {
            return ObjectMapperFactory.getObjectMapper().readValue(jsonString, DiagramObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        // Parent element is ignored.
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramObject) {
            super.copyBasicInfo(object);
            DiagramObject diagramObject = (DiagramObject) object;
            type = diagramObject.getType();
            jointjsId = diagramObject.getJointjsId();
            embeds = diagramObject.getEmbeds();
            z = diagramObject.getZ();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramObject.");
        }
    }
}
