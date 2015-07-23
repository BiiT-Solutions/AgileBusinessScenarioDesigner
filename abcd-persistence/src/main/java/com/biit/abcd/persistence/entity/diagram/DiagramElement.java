package com.biit.abcd.persistence.entity.diagram;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "diagram_elements")
public abstract class DiagramElement extends DiagramObject {
	private static final long serialVersionUID = -2842225781954290086L;
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

	@Override
	public void resetIds() {
		super.resetIds();
		if (size != null) {
			size.resetIds();
		}
		if (position != null) {
			position.resetIds();
		}
		if (biitText != null) {
			biitText.resetIds();
		}
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
			gsonBuilder.registerTypeAdapter(DiagramExpression.class, new DiagramCalculationDeserializer());
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
		gsonBuilder.registerTypeAdapter(DiagramExpression.class, new DiagramCalculationSerializer());
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
	public void update(DiagramObject object, IUser<Long> user) {
		super.update(object, user);
		if (object instanceof DiagramElement) {
			DiagramElement element = (DiagramElement) object;

			tooltip = element.getTooltip();
			size.setWidth(element.getSize().getWidth());
			size.setHeight(element.getSize().getHeight());
			position.setX(element.getPosition().getX());
			position.setY(element.getPosition().getY());
			angle = element.getAngle();

			if ((biitText == null) && (element.getBiitText() != null)) {
				biitText = element.getBiitText();
			} else {
				if (element.getBiitText().getText() != null) {
					biitText.setText(element.getBiitText().getText());
				}
				if (element.getBiitText().getFill() != null) {
					biitText.setFill(element.getBiitText().getFill());
				}
				if (element.getBiitText().getFontSize() != null) {
					biitText.setFontSize(element.getBiitText().getFontSize());
				}
				if (element.getBiitText().getStroke() != null) {
					biitText.setStroke(element.getBiitText().getStroke());
				}
				if (element.getBiitText().getStrokeWidth() != null) {
					biitText.setStrokeWidth(element.getBiitText().getStrokeWidth());
				}
			}
		}
	}

	public List<DiagramLink> getOutgoingLinks() {
		return getParent().getOutgoingLinks(this);
	}

	public List<DiagramLink> getIncomingLinks() {
		return getParent().getIncomingLinks(this);
	}

	@Override
	public void setCreatedBy(IUser<Long> user) {
		super.setCreatedBy(user);
		biitText.setCreatedBy(user);
	}

	@Override
	public void setUpdatedBy(IUser<Long> user) {
		super.setUpdatedBy(user);
		biitText.setUpdatedBy(user);
	}

	@Override
	public void setUpdateTime(Timestamp dateUpdated) {
		super.setUpdateTime(dateUpdated);
		biitText.setUpdateTime(dateUpdated);
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (size != null) {
			innerStorableObjects.add(size);
			innerStorableObjects.addAll(size.getAllInnerStorableObjects());
		}
		if (position != null) {
			innerStorableObjects.add(position);
			innerStorableObjects.addAll(position.getAllInnerStorableObjects());
		}
		if (biitText != null) {
			innerStorableObjects.add(biitText);
			innerStorableObjects.addAll(biitText.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramElement) {
			super.copyData(object);

			DiagramElement diagramSource = (DiagramElement) object;
			tooltip = diagramSource.getTooltip();
			angle = diagramSource.getAngle();
			biitText = new DiagramBiitText();
			biitText.copyData(diagramSource.getBiitText());

			Size size = new Size();
			size.copyData(diagramSource.getSize());
			setSize(size);

			Point point = new Point();
			point.copyData(diagramSource.getPosition());
			setPosition(point);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramElement.");
		}
	}
}
