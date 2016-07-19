package com.biit.abcd.persistence.entity.diagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

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
import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "diagram")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public class Diagram extends StorableObject implements INameAttribute {
	private static final long serialVersionUID = 3846733629137367843L;

	private String name;

	@SerializedName("cells")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "elements_of_diagram")
	@BatchSize(size = 20)
	private Set<DiagramObject> diagramObjects;

	public Diagram() {
		super();
		diagramObjects = new HashSet<>();
	}

	public Diagram(String name) {
		super();
		this.name = name;
		diagramObjects = new HashSet<>();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (DiagramObject diagramObject : diagramObjects) {
			diagramObject.resetIds();
		}
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
		diagramObjects.clear();
		diagramObjects.addAll(tempDiagram.getDiagramObjects());
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(Diagram.class, new DiagramSerializer());
		gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectSerializer());
		gsonBuilder.registerTypeAdapter(DiagramElement.class, new DiagramElementSerializer());
		gsonBuilder.registerTypeAdapter(DiagramExpression.class, new DiagramCalculationSerializer());
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
	 * Function to get the list of diagram object elements. Do not add elements
	 * to this list, use the appropriate functions.
	 * 
	 * @return
	 */
	public Set<DiagramObject> getDiagramObjects() {
		return Collections.unmodifiableSet(diagramObjects);
	}

	public void removeDiagramObject(DiagramObject object) {
		diagramObjects.remove(object);
		// Some orphan removal are not working correctly. Force it!
		if (object instanceof DiagramFork) {
			((DiagramFork) object).setReference(null);
		}
		if (object instanceof DiagramLink) {
			((DiagramLink) object).setExpressionChain(null);
		}
	}

	public void setDiagramObjects(List<DiagramObject> objects) {
		diagramObjects.clear();
		addDiagramObjects(objects);
	}

	public void addDiagramObjects(List<DiagramObject> objects) {
		if (objects != null) {
			diagramObjects.addAll(objects);
			for (DiagramObject object : objects) {
				object.setParent(this);
			}
		}
	}

	public void addDiagramObject(DiagramObject object) {
		if (object != null) {
			diagramObjects.add(object);
			object.setParent(this);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public DiagramObject findDiagramObjectByJointJsId(String jointJsId) {
		for (DiagramObject element : diagramObjects) {
			if (element.getJointjsId().equals(jointJsId)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Retrieves all outgoing links of a specific Diagram Element.
	 * 
	 * @param source
	 * @return
	 */
	public List<DiagramLink> getOutgoingLinks(DiagramElement source) {
		List<DiagramLink> links = new ArrayList<>();
		for (DiagramObject element : diagramObjects) {
			if (element instanceof DiagramLink) {
				DiagramLink link = (DiagramLink) element;
				if (link.getSource().getJointjsId().equals(source.getJointjsId())) {
					links.add(link);
				}
			}
		}
		return links;
	}

	/**
	 * Retrieves all incoming links of a specific Diagram Element.
	 * 
	 * @param source
	 * @return
	 */
	public List<DiagramLink> getIncomingLinks(DiagramElement source) {
		List<DiagramLink> links = new ArrayList<>();
		for (DiagramObject element : diagramObjects) {
			if (element instanceof DiagramLink) {
				DiagramLink link = (DiagramLink) element;
				if (link.getTarget().getJointjsId().equals(source.getJointjsId())) {
					links.add(link);
				}
			}
		}
		return links;
	}

	public List<Diagram> getChildDiagrams() {
		List<Diagram> childDiagrams = new ArrayList<Diagram>();
		for (DiagramObject object : diagramObjects) {
			if (object instanceof DiagramChild) {
				DiagramChild diagramChild = (DiagramChild) object;
				if (diagramChild.getDiagram() != null) {
					childDiagrams.add(diagramChild.getDiagram());
				}
			}
		}
		return childDiagrams;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		for (DiagramObject child : getDiagramObjects()) {
			innerStorableObjects.add(child);
			innerStorableObjects.addAll(child.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Diagram) {
			super.copyBasicInfo(object);
			Diagram diagram = (Diagram) object;
			name = diagram.getName();

			diagramObjects.clear();
			for (DiagramObject child : diagram.getDiagramObjects()) {
				try {
					DiagramObject diagramElement = child.getClass().newInstance();
					diagramElement.copyData(child);
					addDiagramObject(diagramElement);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new NotValidStorableObjectException("Object '" + object + "' has a problem copying '" + child + "'.");
				}
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Diagram.");
		}
	}

	@Override
	public String toString() {
		return getName();
	}


}
