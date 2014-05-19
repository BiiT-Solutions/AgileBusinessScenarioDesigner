package com.biit.abcd.persistence.entity.diagram;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.biit.abcd.gson.utils.DiagramDeserializer;
import com.biit.abcd.gson.utils.DiagramSerializer;
import com.biit.abcd.persistence.entity.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.liferay.portal.model.User;

@Entity
@Table(name = "DIAGRAM")
public class Diagram {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Form form;

	private Timestamp creationDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long createdBy = null;
	private Timestamp updatedDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long updatedBy = null;

	@SerializedName("cells")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "ELEMENTS_OF_DIAGRAM")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DiagramObject> diagramElements;

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
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramDeserializer());
			Gson gson = gsonBuilder.create();
			Diagram object = gson.fromJson(jsonString, Diagram.class);
			return object;
		}
		return null;
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

	public List<DiagramObject> getDiagramObjects() {
		return diagramElements;
	}

	public void setDiagramObjects(List<DiagramObject> objects) {
		this.diagramElements = objects;
	}

	public void addDiagramObjects(List<DiagramObject> objects) {
		if (diagramElements == null) {
			diagramElements = new ArrayList<>();
		}
		if (objects != null) {
			diagramElements.addAll(objects);
		}
	}

}
