package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;
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

	//@SerializedName("cells")
	@Transient
	private Collection<DiagramObject> diagramObjects;

	protected Diagram() {

	}

	public Diagram(Form form) throws NotValidFormException {
		if (form == null) {
			throw new NotValidFormException("Null forms not allowed here.");
		}
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

//	private void translateJson() {
//		if (diagramAsJson != null) {
//			Gson gson = new Gson();
//			Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
//			diagramObjects = gson.fromJson(diagramAsJson, new TypeReference<List<DiagramObject>>() });
//		}
//
//	}
}
