package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DIAGRAM_BIIT_TEXT")
public class DiagramBiitText extends StorableObject {

	@Expose
	private String text;
	@Expose
	private String fill;

	@Expose
	@SerializedName("font-size")
	private String fontSize;

	@Expose
	private String stroke;

	@Expose
	@SerializedName("stroke-width")
	private String strokeWidth;

	public DiagramBiitText() {
		//text = "";
		fill = "#000000";
		fontSize = "16";
		stroke = "#000000";
		strokeWidth = "0";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getStroke() {
		return stroke;
	}

	public void setStroke(String stroke) {
		this.stroke = stroke;
	}

	public String getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(String strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	@Override
	public String toString() {
		return "{text: " + text + ", fill: " + fill + ", font-size:" + fontSize + ", stroke:" + stroke
				+ ", stroke-width:" + strokeWidth;
	}
}
