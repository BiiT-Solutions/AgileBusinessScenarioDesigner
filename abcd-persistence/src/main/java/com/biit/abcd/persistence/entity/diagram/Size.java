package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.SizeDeserializer;
import com.biit.abcd.serialization.diagram.SizeSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;

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
    @Expose
    private int width;
    @Expose
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