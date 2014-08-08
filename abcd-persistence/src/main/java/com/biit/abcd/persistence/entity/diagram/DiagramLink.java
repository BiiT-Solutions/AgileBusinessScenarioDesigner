package com.biit.abcd.persistence.entity.diagram;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.gson.utils.DiagramLinkDeserializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.liferay.portal.model.User;

@Entity
@Table(name = "DIAGRAM_LINKS")
public class DiagramLink extends DiagramObject {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain expressionChain;

	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node source;

	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Node target;

	private String text;

	@Expose
	private boolean smooth;
	@Expose
	private boolean manhattan;

	@Column(length = 1000000)
	private String attrs;
	@Column(length = 1000000)
	private String vertices;

	public DiagramLink() {
		super();
		expressionChain = new ExpressionChain();
	}

	public DiagramLink(Node source, Node target) {
		super();
		expressionChain = new ExpressionChain();
		setSource(source);
		setTarget(target);
	}

	public Node getSource() {
		return source;
	}

	public DiagramElement getSourceElement() {
		if (getParent() == null) {
			throw new RuntimeException("Diagram Link getSourceElement used without diagram.");
		}
		String jointJsId = source.getJointjsId();
		return (DiagramElement) getParent().findDiagramObjectByJointJsId(jointJsId);
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public DiagramElement getTargetElement() {
		if (getParent() == null) {
			throw new RuntimeException("Diagram Link getTargetElement used without diagram.");
		}
		String jointJsId = target.getJointjsId();
		return (DiagramElement) getParent().findDiagramObjectByJointJsId(jointJsId);
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public boolean isSmooth() {
		return smooth;
	}

	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}

	public boolean isManhattan() {
		return manhattan;
	}

	public void setManhattan(boolean manhattan) {
		this.manhattan = manhattan;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setVertices(String vertices) {
		this.vertices = vertices;
	}

	public String getVertices() {
		return vertices;
	}

	public static DiagramLink fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkDeserializer());
			Gson gson = gsonBuilder.create();
			DiagramLink object = gson.fromJson(jsonString, DiagramLink.class);
			return object;
		}
		return null;
	}

	@Override
	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

	public boolean hasSourceFork() {
		if (getSourceElement() instanceof DiagramFork) {
			return true;
		}
		return false;
	}

	public boolean isOthers() {
		DiagramElement source = getSourceElement();
		if (source instanceof DiagramFork) {
			DiagramFork forkSource = (DiagramFork) source;
			if (!isAnswerEmpty()) {
				return false;
			}

			if (getParent() == null) {
				throw new RuntimeException("Diagram Link isOthers used without diagram.");
			}

			List<DiagramLink> links = getParent().getOutgoingLinks(forkSource);
			if (links.size() == 1) {
				return false;
			}
			int numOfEmptyLinks = 0;
			for (DiagramLink link : links) {
				if (link.isAnswerEmpty()) {
					numOfEmptyLinks++;
				}
			}
			if (numOfEmptyLinks > 1) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean isAnswerEmpty() {
		if ((expressionChain == null) || expressionChain.getExpressions().isEmpty()) {
			return true;
		}
		return false;
	}

	public String getCorrectedText() {
		if (hasSourceFork()) {
			// Source is Fork
			if (isAnswerEmpty()) {
				if (isOthers()) {
					return "others";
				} else {
					return "";
				}
			} else {
				if (expressionChain != null) {
					return expressionChain.getExpression();
				} else {
					return "";
				}
			}
		} else {
			return getText();
		}
	}

	public void clear() {
		expressionChain = null;
		setText("");
	}

	@Override
	public void setCreatedBy(User user) {
		super.setCreatedBy(user);
		source.setCreatedBy(user);
		target.setCreatedBy(user);
	}

	@Override
	public void setUpdatedBy(User user) {
		super.setUpdatedBy(user);
		source.setUpdatedBy(user);
		target.setUpdatedBy(user);
	}

	@Override
	public void setUpdateTime(Timestamp dateUpdated) {
		super.setUpdateTime(dateUpdated);
		source.setUpdateTime(dateUpdated);
		target.setUpdateTime(dateUpdated);
	}

	@Override
	public void update(DiagramObject object, User user) {
		super.update(object, user);
		if (object instanceof DiagramLink) {
			DiagramLink link = (DiagramLink) object;

			if (!link.expressionChain.getExpressions().isEmpty()) {
				expressionChain = link.expressionChain;
			}

			if (source == null) {
				source = new Node();
			}
			source.update(link.getSource());

			if (target == null) {
				target = new Node();
			}
			target.update(link.getTarget());

			if (link.getText() != null) {
				text = new String(link.getText());
			}
			smooth = link.isSmooth();
			manhattan = link.isManhattan();
			if (link.getAttrs() != null) {
				attrs = new String(link.getAttrs());
			}
			if (link.getVertices() != null) {
				vertices = new String(link.getVertices());
			}
		}
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}

	/**
	 * Avoid this method. Expression chain is a OneToOne relationship and
	 * currently Hibernate doesn't handle correctly the Orphan removal. Use
	 * setExpressions of ExpressionChain.
	 * 
	 * @param expressionChain
	 */
	public void setExpressionChain(ExpressionChain expressionChain) {
		this.expressionChain = expressionChain;
	}
}
