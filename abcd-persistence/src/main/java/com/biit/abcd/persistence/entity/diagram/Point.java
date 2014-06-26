package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_POINTS")
public class Point extends StorableObject {

	@Expose
	private int x;
	@Expose
	private int y;

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return "(x:" + x + ", y:" + y + ")";
	}
}