package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "diagram_points")
public class Point extends StorableObject {
	private static final long serialVersionUID = 5426903602493769403L;
	@Expose
	private int x;
	@Expose
	private int y;

	public Point() {
		super();
	}

	public Point(int x, int y) {
		super();
		setX(x);
		setY(y);
	}

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
		if (object instanceof Point) {
			super.copyBasicInfo(object);
			Point point = (Point) object;
			x = point.getX();
			y = point.getY();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Point.");
		}
	}
}