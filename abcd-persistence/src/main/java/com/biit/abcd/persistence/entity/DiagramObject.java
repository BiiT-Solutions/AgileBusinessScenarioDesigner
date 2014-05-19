package com.biit.abcd.persistence.entity;

public class DiagramObject {
	private String type;
	private String toolType;
	private Point position;
	private int z;
	private float angle;
	private String id;
	
	

	public class Point {
		private int x;
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
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getToolType() {
		return toolType;
	}



	public void setToolType(String toolType) {
		this.toolType = toolType;
	}



	public Point getPosition() {
		return position;
	}



	public void setPosition(Point position) {
		this.position = position;
	}



	public int getZ() {
		return z;
	}



	public void setZ(int z) {
		this.z = z;
	}



	public float getAngle() {
		return angle;
	}



	public void setAngle(float angle) {
		this.angle = angle;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}
}
