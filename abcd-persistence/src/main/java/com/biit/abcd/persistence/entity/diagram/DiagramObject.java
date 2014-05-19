package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DIAGRAM_OBJECTS")
@Inheritance(strategy = InheritanceType.JOINED)
public class DiagramObject {

	// Id is not exposed because not used in JSON
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private Long databaseId;

	@Expose
	private String type;
	@SerializedName("id")
	@Expose
	private String jointjsId;
	@Expose
	private int z;

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public Long getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}
}
