package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DIAGRAM_NODES")
public class Node extends StorableObject {
	@Expose
	@SerializedName("id")
	private String jointjsId;
	@Expose
	private String selector;
	@Expose
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
}