package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "diagram_text")
@Cacheable(true)
public class DiagramText extends StorableObject {
	private static final long serialVersionUID = 1533578154303621298L;
	@Expose
	private String text;
	@Expose
	private String fill;

	@Expose
	@SerializedName("font_size")
	@Column(name="font_size")
	private String fontSize;

	@Expose
	private String stroke;

	@Expose
	@SerializedName("stroke_width")
	@Column(name="stroke_width")
	private String strokeWidth;

	public DiagramText() {
		// text = "";
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

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramText) {
			super.copyBasicInfo(object);
			DiagramText diagramBiitText = (DiagramText) object;
			text = diagramBiitText.getText();
			fill = diagramBiitText.getFill();
			fontSize = diagramBiitText.getFontSize();
			stroke = diagramBiitText.getStroke();
			strokeWidth = diagramBiitText.getStrokeWidth();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramBiitText.");
		}
	}
}
