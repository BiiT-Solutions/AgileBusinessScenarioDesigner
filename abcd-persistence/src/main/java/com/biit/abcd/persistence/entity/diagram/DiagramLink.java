package com.biit.abcd.persistence.entity.diagram;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.gson.utils.DiagramLinkDeserializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.biit.abcd.persistence.entity.Answer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "DIAGRAM_LINKS")
public class DiagramLink extends DiagramObject {

	// This can change if later this wants to be changed to an expression that
	// unifies both elements.
	@ManyToOne(fetch = FetchType.EAGER)
	private Answer answer;
	@Column(length = 1000000)
	private String answerExpression;

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

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public String getAnswerExpression() {
		return answerExpression;
	}

	public void setAnswerExpression(String answerExpression) {
		this.answerExpression = answerExpression;
	}

	public void clearAnswerAndAnswerExpression() {
		setAnswer(null);
		setAnswerExpression(new String());
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

			List<DiagramLink> links = getParent().getOutgoingLinks((DiagramElement) forkSource);
			if(links.size()==1){
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
		if (answer == null && (answerExpression == null || answerExpression.isEmpty())) {
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
				if (getAnswer() != null) {
					return getAnswer().getName();
				} else {
					return getAnswerExpression();
				}
			}
		} else {
			return getText();
		}
	}

	public void clear() {
		answer = null;
		answerExpression = null;
		setText("");
	}

	@Override
	public void update(DiagramObject object) {
		super.update(object);
		if (object instanceof DiagramLink) {
			DiagramLink link = (DiagramLink) object;

			if (link.getAnswer() != null) {
				answer = link.getAnswer();
			}
			if (link.getAnswerExpression() != null) {
				answerExpression = new String(link.getAnswerExpression());
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
}
