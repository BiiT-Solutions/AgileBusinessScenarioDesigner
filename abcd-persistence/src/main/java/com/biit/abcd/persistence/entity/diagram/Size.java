package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
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
		if (object instanceof Size) {
			super.copyBasicInfo(object);
			Size size = (Size) object;
			width = size.getWidth();
			height = size.getHeight();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Size.");
		}
	}
}