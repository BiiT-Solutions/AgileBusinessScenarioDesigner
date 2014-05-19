package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_ELEMENTS")
public class DiagramElement extends DiagramObject {

	@Expose
	private String tooltip;
	@Expose
	@OneToOne(fetch = FetchType.EAGER)
	private Size size;
	@Expose
	@OneToOne(fetch = FetchType.EAGER)
	private Point position;

	@Expose
	private float angle;

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
}
