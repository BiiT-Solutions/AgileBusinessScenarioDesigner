package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.ElementPickedListener;

public class AbcdDiagramBuilder extends DiagramBuilder {

	private static final long serialVersionUID = 153463654983722973L;

	private Diagram diagram;
	private HashMap<String, DiagramObject> diagramElements;
	private List<DiagramObjectPickedListener> listeners;

	public AbcdDiagramBuilder() {
		super();
		diagramElements = new HashMap<>();
		listeners = new ArrayList<AbcdDiagramBuilder.DiagramObjectPickedListener>();
		addElementPickedListener(new ElementPickedListener() {

			@Override
			public void nodePickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					return;
				}
				DiagramElement tempElement = DiagramElement.fromJson(jsonString);
				if (diagramElements.containsKey(tempElement.getJointjsId())) {
					DiagramElement currentElement = (DiagramElement) diagramElements.get(tempElement.getJointjsId());
					fireDiagramObjectPickedListeners(currentElement);
				} else {
					diagram.getDiagramObjects().add(tempElement);
					diagramElements.put(tempElement.getJointjsId(), tempElement);
					fireDiagramObjectPickedListeners(tempElement);
				}
			}

			@Override
			public void connectionPickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					return;
				}
				DiagramLink tempLink = DiagramLink.fromJson(jsonString);
				if (diagramElements.containsKey(tempLink.getJointjsId())) {
					fireDiagramObjectPickedListeners(diagramElements.get(tempLink.getJointjsId()));
				} else {
					diagram.getDiagramObjects().add(tempLink);
					diagramElements.put(tempLink.getJointjsId(), tempLink);
					fireDiagramObjectPickedListeners(tempLink);
				}
			}
		});
	}

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
		if (diagram != null) {
			// Initialize the map of diagramElements.
			diagramElements = createMapOfDiagramObjects(diagram);
			fromJson(diagram.toJson());
		} else {
			clear();
		}
	}

	public Diagram getDiagram() {
		return diagram;
	}

	private HashMap<String, DiagramObject> createMapOfDiagramObjects(Diagram newDiagram) {
		HashMap<String, DiagramObject> newDiagramElements = new HashMap<>();
		System.out.println("Current diagram objects");
		for (DiagramObject element : newDiagram.getDiagramObjects()) {
			newDiagramElements.put(element.getJointjsId(), element);
			System.out.println("Id: " + element.getJointjsId() + " " + element.getType().getJsonType());
		}
		return newDiagramElements;
	}

	public void updateDiagram(final DiagramUpdated callback) {
		System.out.println("Update started");
		toJson(new DiagramBuilderJsonGenerationListener() {

			@Override
			public void generatedJsonString(String jsonString) {
				// Create the new diagram from Json and get the map of elements.
				Diagram tempDiagram = Diagram.fromJson(jsonString);
				HashMap<String, DiagramObject> newElements = createMapOfDiagramObjects(tempDiagram);
				// Remove any key in the original mapping and diagram that do
				// not exist in the new
				Iterator<String> itr = diagramElements.keySet().iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					if (!newElements.containsKey(key)) {
						DiagramObject objectToRemove = diagramElements.get(key);
						diagram.getDiagramObjects().remove(objectToRemove);
						itr.remove();
					}
				}
				// Now insert new elements and update old ones.
				for (DiagramObject object : tempDiagram.getDiagramObjects()) {
					if (diagramElements.containsKey(object.getJointjsId())) {
						// Already exist, update
						DiagramObject currentValue = diagramElements.get(object.getJointjsId());
						currentValue.update(object);
					} else {
						// Doesn't exist, insert
						diagram.addDiagramObject(object);
						diagramElements.put(object.getJointjsId(), object);
					}
				}
				System.out.println("Update ended");
				if (callback != null) {
					callback.updated(diagram);
				}
			}
		});
	}

	public interface DiagramUpdated {
		void updated(Diagram diagram);
	}

	public interface DiagramObjectPickedListener {
		void diagramObjectPicked(DiagramObject object);
	}

	public void addDiagramObjectPickedListener(DiagramObjectPickedListener listener) {
		listeners.add(listener);
	}

	public void removeDiagramObjectPickedListener(DiagramObjectPickedListener listener) {
		listeners.remove(listener);
	}

	public void fireDiagramObjectPickedListeners(DiagramObject pickedObject) {
		for (DiagramObjectPickedListener listener : listeners) {
			listener.diagramObjectPicked(pickedObject);
		}
	}

	public void updateChangesToDiagram(DiagramObject element) {
		if (element instanceof DiagramElement) {
			updateCellJson(((DiagramObject) element).toJson());
		} else {
			updateLinkJson(((DiagramObject) element).toJson());
		}
	}
}
