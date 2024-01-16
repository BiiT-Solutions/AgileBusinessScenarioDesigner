package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.NodeDeserializer;
import com.biit.abcd.serialization.diagram.NodeSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = NodeDeserializer.class)
@JsonSerialize(using = NodeSerializer.class)
@Table(name = "diagram_nodes")
@Cacheable(true)
public class Node extends StorableObject {
    private static final long serialVersionUID = -5481806008119969483L;

    @Column(name = "jointjs_id")
    private String jointjsId;

    private String selector;

    private String port;

    public Node() {
    }

    public Node(String jointjsId) {
        setJointjsId(jointjsId);
    }

    public String getJointjsId() {
        return jointjsId;
    }

    public void setJointjsId(String jointjsId) {
        this.jointjsId = jointjsId;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void update(Node object) {
        if (object != null) {
            jointjsId = object.jointjsId;
            selector = object.selector;
            port = object.port;
        }
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
        if (object instanceof Node) {
            super.copyBasicInfo(object);
            Node node = (Node) object;
            jointjsId = node.getJointjsId();
            selector = node.getSelector();
            port = node.getPort();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Node.");
        }
    }
}