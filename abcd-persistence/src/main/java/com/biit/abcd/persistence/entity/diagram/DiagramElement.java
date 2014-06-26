package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.gson.utils.DiagramElementDeserializer;
import com.biit.abcd.gson.utils.DiagramElementSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_ELEMENTS")
public abstract class DiagramElement extends DiagramObject {

	@Expose
	private String tooltip;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Size size;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Point position;

	@Expose
	private float angle;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private DiagramBiitText biitText;	

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}
	
	public DiagramBiitText getBiitText() {
		return biitText;
	}

	public void setBiitText(DiagramBiitText biitText) {
		this.biitText = biitText;
	}

	public static DiagramElement fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementDeserializer());
			Gson gson = gsonBuilder.create();
			DiagramElement object = gson.fromJson(jsonString, DiagramElement.class);
			return object;
		}
		return null;
	}

	@Override
	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}
}
