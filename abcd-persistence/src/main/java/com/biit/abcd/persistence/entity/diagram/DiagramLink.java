package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.gson.utils.DiagramLinkDeserializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_LINKS")
public class DiagramLink extends DiagramObject {

	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node source;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node target;

	private String text;

	@Expose
	private boolean smooth;
	@Expose
	private boolean manhattan;

	@Column(length = 1000000)
	private String attrs;
	@Column(length = 1000000)
	private String vertices;

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public boolean isSmooth() {
		return smooth;
	}

	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}

	public boolean isManhattan() {
		return manhattan;
	}

	public void setManhattan(boolean manhattan) {
		this.manhattan = manhattan;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setVertices(String vertices) {
		this.vertices = vertices;
	}

	public String getVertices() {
		return vertices;
	}

	public static DiagramLink fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramLinkDeserializer());
			Gson gson = gsonBuilder.create();
			DiagramLink object = gson.fromJson(jsonString, DiagramLink.class);
			return object;
		}
		return null;
	}

	@Override
	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

}
