package com.biit.abcd.persistence.entity;

public class DiagramLink extends DiagramElement {

	private Node source;
	private Node target;

	public class Node {
		String id;
		String selector;
		String port;
	}

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
}
