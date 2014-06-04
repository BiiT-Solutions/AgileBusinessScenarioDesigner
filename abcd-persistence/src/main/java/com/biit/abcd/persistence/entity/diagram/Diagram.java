package com.biit.abcd.persistence.entity.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.biit.abcd.gson.utils.DiagramDeserializer;
import com.biit.abcd.gson.utils.DiagramElementSerializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.biit.abcd.gson.utils.DiagramObjectSerializer;
import com.biit.abcd.gson.utils.DiagramSerializer;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.StorableObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DIAGRAM")
public class Diagram extends StorableObject {

	@ManyToOne(fetch = FetchType.EAGER)
	private Form form;

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

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
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
		gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectSerializer());
		gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementSerializer());
		gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkSerializer());
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
