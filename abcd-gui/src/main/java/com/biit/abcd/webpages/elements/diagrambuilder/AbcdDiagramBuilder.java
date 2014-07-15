package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.listeners.DoubleClickListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementActionListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementPickedListener;

public class AbcdDiagramBuilder extends DiagramBuilder {

	private static final long serialVersionUID = 153463654983722973L;

	private Diagram diagram;
	private HashMap<String, DiagramObject> diagramElements;
	private List<DiagramObjectPickedListener> listeners;
	private List<JumpToListener> jumpToListeners;

	public AbcdDiagramBuilder() {
		super();
		diagramElements = new HashMap<>();
		listeners = new ArrayList<AbcdDiagramBuilder.DiagramObjectPickedListener>();
		jumpToListeners = new ArrayList<JumpToListener>();

		addElementActionListener(new ElementActionListener() {

			@Override
			public void updateElement(String jsonString) {
				updateObjectOfDiagram(jsonString);
			}

			@Override
			public void removeElement(String jsonString) {
				removeObjectOfDiagram(jsonString);
			}

			@Override
			public void addElement(String jsonString) {
				addObjectToDiagram(jsonString);
			}
		});
		addElementPickedListener(new ElementPickedListener() {

			@Override
			public void nodePickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					return;
				}
				DiagramObject element = getObjectOfDiagram(jsonString);
				fireDiagramObjectPickedListeners(element);
			}

			@Override
			public void connectionPickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					return;
				}
				DiagramObject element = getObjectOfDiagram(jsonString);
				fireDiagramObjectPickedListeners(element);
			}
		});
		addDoubleClickListener(new DoubleClickListener() {

			@Override
			public void doubleClick(String jsonString) {
				DiagramObject object = getObjectOfDiagram(jsonString);
				switch (object.getType()) {
				case CALCULATION:
					if (((DiagramCalculation) object).getFormExpression() != null) {
						fireJumpToListener(((DiagramCalculation) object).getFormExpression());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case FORK:
					if (((DiagramFork) object).getQuestion() != null) {
						fireJumpToListener(((DiagramFork) object).getQuestion());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case DIAGRAM_CHILD:
					if (((DiagramChild) object).getChildDiagram() != null) {
						fireJumpToListener(((DiagramChild) object).getChildDiagram());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case RULE:
					if (((DiagramRule) object).getRule() != null) {
						fireJumpToListener(((DiagramRule) object).getRule());
					}else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case SINK:
					if (((DiagramSink) object).getFormExpression() != null) {
						fireJumpToListener(((DiagramSink) object).getFormExpression());
					}
					break;
				case TABLE:
					if (((DiagramTable) object).getTable() != null) {
						fireJumpToListener(((DiagramTable) object).getTable());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * Gets Element of diagram from a json String. If it doesn't exist on the
	 * diagram, we add it first.
	 * 
	 * @param jsonString
	 * @return
	 */
	private DiagramObject getObjectOfDiagram(String jsonString) {
		DiagramObject tempElement = DiagramObject.fromJson(jsonString);
		if (diagramElements.containsKey(tempElement.getJointjsId())) {
			DiagramObject currentElement = diagramElements.get(tempElement.getJointjsId());
			return currentElement;
		} else {
			addObjectToDiagram(tempElement);
			return tempElement;
		}
	}

	/**
	 * Add a new Diagram object to the diagram if it doesn't exist.
	 * 
	 * @param element
	 */
	private void addObjectToDiagram(DiagramObject element) {
		if (!diagramElements.containsKey(element.getJointjsId())) {
			diagram.addDiagramObject(element);
			element.setCreatedBy(UserSessionHandler.getUser());
			element.setUpdatedBy(UserSessionHandler.getUser());
			element.setUpdateTime();
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
			diagramElements.put(element.getJointjsId(), element);
		}
	}

	/**
	 * Add a new Diagram object to the diagram if it doesn't exist.
	 * 
	 * @param element
	 */
	private void addObjectToDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		addObjectToDiagram(element);
	}

	private void removeObjectOfDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		if (diagramElements.containsKey(element.getJointjsId())) {
			DiagramObject originalElement = diagramElements.get(element.getJointjsId());
			diagram.getDiagramObjects().remove(originalElement);
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
			diagramElements.remove(element.getJointjsId());
		}
	}

	private void updateObjectOfDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		if (diagramElements.containsKey(element.getJointjsId())) {
			DiagramObject originalElement = diagramElements.get(element.getJointjsId());
			originalElement.update(element,UserSessionHandler.getUser());
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
		}
	}

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
		if (diagram != null) {
			// Initialize the map of diagramElements.
			setEnabled(true);
			diagramElements = createMapOfDiagramObjects(diagram);
			fromJson(diagram.toJson());
		} else {
			clear();
			setEnabled(false);
		}
	}

	public Diagram getDiagram() {
		return diagram;
	}

	private HashMap<String, DiagramObject> createMapOfDiagramObjects(Diagram newDiagram) {
		HashMap<String, DiagramObject> newDiagramElements = new HashMap<>();
		for (DiagramObject element : newDiagram.getDiagramObjects()) {
			newDiagramElements.put(element.getJointjsId(), element);
		}
		return newDiagramElements;
	}

	public void updateDiagram(final DiagramUpdated callback) {
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
						currentValue.update(object,UserSessionHandler.getUser());
					} else {
						// Doesn't exist, insert
						diagram.addDiagramObject(object);
						diagramElements.put(object.getJointjsId(), object);
					}
				}
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

	public void addJumpToListener(JumpToListener jumpToListener) {
		jumpToListeners.add(jumpToListener);
	}

	public void removeJumpToListener(JumpToListener jumpToListener) {
		jumpToListeners.remove(jumpToListener);
	}

	public void fireJumpToListener(Object object) {
		for (JumpToListener listener : jumpToListeners) {
			listener.jumpTo(object);
		}
	}
}
