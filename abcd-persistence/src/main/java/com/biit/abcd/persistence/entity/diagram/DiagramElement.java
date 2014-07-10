package com.biit.abcd.persistence.entity.diagram;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.gson.utils.DiagramCalculationDeserializer;
import com.biit.abcd.gson.utils.DiagramCalculationSerializer;
import com.biit.abcd.gson.utils.DiagramChildDeserializer;
import com.biit.abcd.gson.utils.DiagramChildSerializer;
import com.biit.abcd.gson.utils.DiagramElementDeserializer;
import com.biit.abcd.gson.utils.DiagramElementSerializer;
import com.biit.abcd.gson.utils.DiagramForkDeserializer;
import com.biit.abcd.gson.utils.DiagramForkSerializer;
import com.biit.abcd.gson.utils.DiagramLinkDeserializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.biit.abcd.gson.utils.DiagramObjectDeserializer;
import com.biit.abcd.gson.utils.DiagramObjectSerializer;
import com.biit.abcd.gson.utils.DiagramRepeatDeserializer;
import com.biit.abcd.gson.utils.DiagramRepeatSerializer;
import com.biit.abcd.gson.utils.DiagramRuleDeserializer;
import com.biit.abcd.gson.utils.DiagramRuleSerializer;
import com.biit.abcd.gson.utils.DiagramSinkDeserializer;
import com.biit.abcd.gson.utils.DiagramSinkSerializer;
import com.biit.abcd.gson.utils.DiagramSourceDeserializer;
import com.biit.abcd.gson.utils.DiagramSourceSerializer;
import com.biit.abcd.gson.utils.DiagramTableDeserializer;
import com.biit.abcd.gson.utils.DiagramTableSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.liferay.portal.model.User;

@Entity
@Table(name = "DIAGRAM_ELEMENTS")
public abstract class DiagramElement extends DiagramObject {

	@Expose
	private String tooltip;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Size size;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Point position;

	@Expose
	private float angle;
	@Expose
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private DiagramBiitText biitText;

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

	public DiagramBiitText getBiitText() {
		return biitText;
	}

	public void setBiitText(DiagramBiitText biitText) {
		this.biitText = biitText;
	}

	public static DiagramElement fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramCalculation.class, new DiagramCalculationDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramFork.class, new DiagramForkDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramChild.class, new DiagramChildDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramRule.class, new DiagramRuleDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramSink.class, new DiagramSinkDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramSource.class, new DiagramSourceDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramTable.class, new DiagramTableDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementDeserializer());
			gsonBuilder.registerTypeAdapter(DiagramRepeat.class, new DiagramRepeatDeserializer());
			Gson gson = gsonBuilder.create();
			DiagramElement object = gson.fromJson(jsonString, DiagramElement.class);
			return object;
		}
		return null;
	}

	@Override
	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectSerializer());
		gsonBuilder.registerTypeAdapter(DiagramCalculation.class, new DiagramCalculationSerializer());
		gsonBuilder.registerTypeAdapter(DiagramFork.class, new DiagramForkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramChild.class, new DiagramChildSerializer());
		gsonBuilder.registerTypeAdapter(DiagramRule.class, new DiagramRuleSerializer());
		gsonBuilder.registerTypeAdapter(DiagramSink.class, new DiagramSinkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramSource.class, new DiagramSourceSerializer());
		gsonBuilder.registerTypeAdapter(DiagramTable.class, new DiagramTableSerializer());
		gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementSerializer());
		gsonBuilder.registerTypeAdapter(DiagramRepeat.class, new DiagramRepeatSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

	@Override
	public void update(DiagramObject object, User user) {
		super.update(object,user);
		if (object instanceof DiagramElement) {
			DiagramElement element = (DiagramElement) object;

			tooltip = element.getTooltip();
			size.setWidth(element.getSize().getWidth());
			size.setHeight(element.getSize().getHeight());
			position.setX(element.getPosition().getX());
			position.setY(element.getPosition().getY());
			angle = element.getAngle();

			if (biitText == null && element.biitText != null) {
				biitText = element.biitText;
			} else {
				if (element.getBiitText().getText() != null) {
					biitText.setText(element.getBiitText().getText());
				}
				biitText.setFill(element.getBiitText().getFill());
				biitText.setFontSize(element.getBiitText().getFontSize());
				biitText.setStroke(element.getBiitText().getStroke());
				biitText.setStrokeWidth(element.getBiitText().getStrokeWidth());
			}
		}
	}

	@Override
	public String toString() {
		return "Element: " + getJointjsId() + " " + getType().getJsonType();
	}

	public List<DiagramLink> getOutgoingLinks() {
		return getParent().getOutgoingLinks(this);
	}
	
	@Override
	public void setCreatedBy(User user) {
		super.setCreatedBy(user);
		biitText.setCreatedBy(user);
	}
	
	@Override
	public void setUpdatedBy(User user){
		super.setUpdatedBy(user);
		biitText.setUpdatedBy(user);
	}
	
	@Override
	public void setUpdateTime(Timestamp dateUpdated){
		super.setUpdateTime(dateUpdated);
		biitText.setUpdateTime(dateUpdated);
	}
}
