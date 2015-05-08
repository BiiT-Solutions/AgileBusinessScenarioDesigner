package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.liferay.portal.model.User;

@Entity
@Table(name = "diagram_objects")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class DiagramObject extends StorableObject {
	private static final long serialVersionUID = -6312500925414596116L;

	@ManyToOne(fetch = FetchType.EAGER)
	private Diagram parent;

	@Expose
	@Enumerated(EnumType.STRING)
	private DiagramObjectType type;
	@Expose
	private String jointjsId;
	@Expose
	private String embeds;
	@Expose
	private int z;

	public DiagramObjectType getType() {
		return type;
	}

	public void setType(DiagramObjectType type) {
		this.type = type;
	}

	public String getJointjsId() {
		return jointjsId;
	}

	public void setJointjsId(String id) {
		jointjsId = id;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getEmbeds() {
		return embeds;
	}

	public void setEmbeds(String embeds) {
		this.embeds = embeds;
	}

	public void update(DiagramObject object, User user) {
		embeds = object.embeds;
		z = object.z;
		setUpdatedBy(user);
		setUpdateTime();
	}

	public Diagram getParent() {
		return parent;
	}

	public void setParent(Diagram parent) {
		this.parent = parent;
	}

	public static DiagramObject fromJson(String jsonString) {
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
			DiagramObject object = gson.fromJson(jsonString, DiagramObject.class);
			return object;
		}
		return null;
	}

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

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		// Parent element is ignored.
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramObject) {
			super.copyBasicInfo(object);
			DiagramObject diagramObject = (DiagramObject) object;
			type = diagramObject.getType();
			jointjsId = diagramObject.getJointjsId();
			embeds = diagramObject.getEmbeds();
			z = diagramObject.getZ();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramObject.");
		}
	}
}
