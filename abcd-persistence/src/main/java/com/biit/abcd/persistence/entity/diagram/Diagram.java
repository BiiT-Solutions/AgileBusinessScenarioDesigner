package com.biit.abcd.persistence.entity.diagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.biit.abcd.gson.utils.DiagramCalculationSerializer;
import com.biit.abcd.gson.utils.DiagramChildSerializer;
import com.biit.abcd.gson.utils.DiagramDeserializer;
import com.biit.abcd.gson.utils.DiagramElementSerializer;
import com.biit.abcd.gson.utils.DiagramForkSerializer;
import com.biit.abcd.gson.utils.DiagramLinkSerializer;
import com.biit.abcd.gson.utils.DiagramObjectSerializer;
import com.biit.abcd.gson.utils.DiagramRepeatSerializer;
import com.biit.abcd.gson.utils.DiagramRuleSerializer;
import com.biit.abcd.gson.utils.DiagramSerializer;
import com.biit.abcd.gson.utils.DiagramSinkSerializer;
import com.biit.abcd.gson.utils.DiagramSourceSerializer;
import com.biit.abcd.gson.utils.DiagramTableSerializer;
import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DIAGRAM")
public class Diagram extends StorableObject implements INameAttribute {

	private String name;

	@SerializedName("cells")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "ELEMENTS_OF_DIAGRAM")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DiagramObject> diagramElements;

	public Diagram() {
		diagramElements = new ArrayList<>();
	}

	public Diagram(String name) {
		this.name = name;
		diagramElements = new ArrayList<>();
	}

	public static Diagram fromJson(String jsonString) {
		if (jsonString != null) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
			gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramDeserializer());
			Gson gson = gsonBuilder.create();
			Diagram object = gson.fromJson(jsonString, Diagram.class);
			return object;
		}
		return null;
	}

	public void updateFromJson(String jsonString) {
		Diagram tempDiagram = Diagram.fromJson(jsonString);
		diagramElements.clear();
		diagramElements.addAll(tempDiagram.getDiagramObjects());
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramSerializer());
		gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectSerializer());
		gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementSerializer());
		gsonBuilder.registerTypeAdapter(DiagramCalculation.class, new DiagramCalculationSerializer());
		gsonBuilder.registerTypeAdapter(DiagramFork.class, new DiagramForkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramChild.class, new DiagramChildSerializer());
		gsonBuilder.registerTypeAdapter(DiagramRule.class, new DiagramRuleSerializer());
		gsonBuilder.registerTypeAdapter(DiagramSink.class, new DiagramSinkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramSource.class, new DiagramSourceSerializer());
		gsonBuilder.registerTypeAdapter(DiagramTable.class, new DiagramTableSerializer());
		gsonBuilder.registerTypeAdapter(DiagramLink.class, new DiagramLinkSerializer());
		gsonBuilder.registerTypeAdapter(DiagramRepeat.class, new DiagramRepeatSerializer());
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(this);
		return json;
	}

	/**
	 * Function to get the list of diagram object elements. Do not add elements to this list, use the appropriate
	 * functions.
	 * 
	 * @return
	 */
	public List<DiagramObject> getDiagramObjects() {
		return Collections.unmodifiableList(diagramElements);
	}

	/**
	 * Only for using with hibernate.
	 * 
	 * @return
	 */
	public List<DiagramObject> getDiagramObjectForInitializeSet() {
		return diagramElements;
	}

	public void removeDiagramObject(DiagramObject object) {
		diagramElements.remove(object);
		// Some orphan removal are not working correctly. Force it!
		if (object instanceof DiagramFork) {
			((DiagramFork) object).setReference(null);
		}
		if (object instanceof DiagramLink) {
			((DiagramLink) object).setExpressionChain(null);
		}
	}

	public void setDiagramObjects(List<DiagramObject> objects) {
		this.diagramElements.clear();
		addDiagramObjects(objects);
	}

	public void addDiagramObjects(List<DiagramObject> objects) {
		if (objects != null) {
			diagramElements.addAll(objects);
			for (DiagramObject object : objects) {
				object.setParent(this);
			}
		}
	}

	public void addDiagramObject(DiagramObject object) {
		if (object != null) {
			diagramElements.add(object);
			object.setParent(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DiagramObject findDiagramObjectByJointJsId(String jointJsId) {
		for (DiagramObject element : diagramElements) {
			if (element.getJointjsId().equals(jointJsId)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Retrieves all links of a specific Diagram Element.
	 * 
	 * @param source
	 * @return
	 */
	public List<DiagramLink> getOutgoingLinks(DiagramElement source) {
		List<DiagramLink> links = new ArrayList<>();
		for (DiagramObject element : diagramElements) {
			if (element instanceof DiagramLink) {
				DiagramLink link = (DiagramLink) element;
				if (link.getSource().getJointjsId().equals(source.getJointjsId())) {
					links.add(link);
				}
			}
		}
		return links;
	}

	public List<Diagram> getChildDiagrams() {
		List<Diagram> childDiagrams = new ArrayList<Diagram>();
		for (DiagramObject object : diagramElements) {
			if (object instanceof DiagramChild) {
				DiagramChild diagramChild = (DiagramChild) object;
				if (diagramChild.getChildDiagram() != null) {
					childDiagrams.add(diagramChild.getChildDiagram());
				}
			}
		}
		return childDiagrams;
	}

}
