package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_LINKS")
public class DiagramLink extends DiagramObject {

	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node source;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node target;

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
