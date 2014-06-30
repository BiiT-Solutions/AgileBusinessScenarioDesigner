package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_OBJECTS")
public abstract class DiagramObject extends StorableObject {

	@Expose
	@Enumerated(EnumType.STRING)
	private DiagramObjectType type;
	@Expose
	private String jointjsId;
	@Expose
	private String embeds;
	@Expose
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
		this.jointjsId = id;
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

	public abstract String toJson();

	public void update(DiagramObject object) {
		embeds = object.embeds;
		z = object.z;
	}
}
