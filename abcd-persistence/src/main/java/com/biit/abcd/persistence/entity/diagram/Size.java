package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "diagram_sizes")
public class Size extends StorableObject {

	@Expose
	private int width;
	@Expose
	private int height;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "(w:" + width + ", h:" + height + ")";
	}
}