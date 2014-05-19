package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.abcd.json.DiagramDeserializer;
import com.biit.abcd.json.DiagramSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.liferay.portal.model.User;

@Entity
@Table(name = "DIAGRAM")
public class Diagram {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(length = 1048576)
	private String diagramAsJson;

	@ManyToOne(fetch = FetchType.EAGER)
	private Form form;

	private Timestamp creationDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long createdBy = null;
	private Timestamp updatedDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long updatedBy = null;

	@Transient
	@SerializedName("cells")
	private List<DiagramElement> diagramElements;

	public Diagram() {

	}

	public Diagram(Form form) {
		this.form = form;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDiagramAsJson() {
		return diagramAsJson;
	}

	public void setDiagramAsJson(String diagramAsJson) {
		this.diagramAsJson = diagramAsJson;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public Timestamp getCreationTime() {
		if (creationDate != null) {
			return creationDate;
		} else {
			creationDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return creationDate;
		}
	}

	public void setUpdateTime() {
		setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	}

	public Timestamp getUpdateTime() {
		if (updatedDate != null) {
			return updatedDate;
		} else {
			updatedDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return updatedDate;
		}
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedBy(User user) {
		if (user != null) {
			this.createdBy = user.getUserId();
		}
	}

	public void setCreationTime(Timestamp dateCreated) {
		this.creationDate = dateCreated;
	}

	public void setUpdateTime(Timestamp dateUpdated) {
		this.updatedDate = dateUpdated;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedBy(User user) {
		if (user != null) {
			this.updatedBy = user.getUserId();
		}
	}

	public static Diagram fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramDeserializer());
			Gson gson = gsonBuilder.create();
			Diagram object = gson.fromJson(jsonString, Diagram.class);
			return object;
		}
		return null;
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

	public List<DiagramElement> getDiagramElements() {
		return diagramElements;
	}

	public void setDiagramElements(List<DiagramElement> objects) {
		this.diagramElements = objects;
	}
}
