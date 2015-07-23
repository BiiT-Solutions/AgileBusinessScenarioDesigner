package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.listeners.DoubleClickListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementActionListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementPickedListener;
import com.vaadin.server.VaadinServlet;

public class AbcdDiagramBuilder extends DiagramBuilder {

	private static final long serialVersionUID = 153463654983722973L;

	private Diagram diagram;
	private HashMap<String, DiagramObject> diagramElements;
	private List<DiagramObjectPickedListener> pickListeners;
	private List<JumpToListener> jumpToListeners;
	private List<DiagramObjectAddedListener> objectAddedListeners;
	private List<DiagramObjectUpdatedListener> objectUpdatedListeners;
	
	private IAbcdFormAuthorizationService securityService;

	public AbcdDiagramBuilder() {
		super();
		diagramElements = new HashMap<>();
		pickListeners = new ArrayList<AbcdDiagramBuilder.DiagramObjectPickedListener>();
		jumpToListeners = new ArrayList<JumpToListener>();
		objectAddedListeners = new ArrayList<>();
		objectUpdatedListeners = new ArrayList<>();
		
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");

		addElementActionListener(new ElementActionListener() {

			@Override
			public void removeElement(String jsonString) {
				if(diagram!=null){
					removeObjectOfDiagram(jsonString);
				}
			}

			@Override
			public void addElement(String jsonString) {
				if(diagram!=null){
					DiagramObject elementAdded = addObjectToDiagram(jsonString);
					fireDiagramObjectAddedListener(elementAdded);
				}
			}

			@Override
			public void updateElement(String jsonString) {
				if(diagram!=null){
					DiagramObject elementUpdated = updateObjectOfDiagram(jsonString);
					fireDiagramObjectUpdatedListener(elementUpdated);
				}
			}
		});

		addElementPickedListener(new ElementPickedListener() {

			@Override
			public void nodePickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
							+ "' Cleared its selection.");
					return;
				}
				DiagramObject element = getObjectOfDiagram(jsonString);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' Diagram element: " + element.getClass() + " picked'.");
				fireDiagramObjectPickedListeners(element);
			}

			@Override
			public void connectionPickedListener(String jsonString) {
				if (jsonString == null) {
					fireDiagramObjectPickedListeners(null);
					return;
				}
				DiagramObject element = getObjectOfDiagram(jsonString);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' Diagram link: " + element.getClass() + " picked'.");
				fireDiagramObjectPickedListeners(element);
			}
		});
		addDoubleClickListener(new DoubleClickListener() {

			@Override
			public void doubleClick(String jsonString) {
				DiagramObject object = getObjectOfDiagram(jsonString);
				switch (object.getType()) {
				case CALCULATION:
					if (((DiagramExpression) object).getExpression() != null) {
						fireJumpToListener(((DiagramExpression) object).getExpression());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case FORK:
					if (((DiagramFork) object).getReference() != null) {
						fireJumpToListener(((DiagramFork) object).getReference());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case DIAGRAM_CHILD:
					if (((DiagramChild) object).getDiagram() != null) {
						fireJumpToListener(((DiagramChild) object).getDiagram());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case RULE:
					if (((DiagramRule) object).getRule() != null) {
						fireJumpToListener(((DiagramRule) object).getRule());
					} else {
						MessageManager.showWarning(LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED,
								LanguageCodes.FORM_DIAGRAM_BUILDER_ELEMENT_NOT_ASSIGNED_DESCRIPTION);
					}
					break;
				case SINK:
					if (((DiagramSink) object).getExpression() != null) {
						fireJumpToListener(((DiagramSink) object).getExpression());
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
	 * Gets Element of diagram from a json String. If it doesn't exist on the diagram, we add it first.
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
	private DiagramObject addObjectToDiagram(DiagramObject element) {
		if (!diagramElements.containsKey(element.getJointjsId())) {
			diagram.addDiagramObject(element);
			element.setCreatedBy(UserSessionHandler.getUser());
			element.setUpdatedBy(UserSessionHandler.getUser());
			element.setUpdateTime();
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
			diagramElements.put(element.getJointjsId(), element);
			return element;
		} else {
			return diagramElements.get(element.getJointjsId());
		}
	}

	/**
	 * Add a new Diagram object to the diagram if it doesn't exist.
	 * 
	 * @param element
	 */
	private DiagramObject addObjectToDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		DiagramObject elementOfDiagram = addObjectToDiagram(element);
		updateLinkVisualization(element);

		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' Diagram element: " + element.getClass() + " added'.");
		return elementOfDiagram;
	}

	private void removeObjectOfDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		if (diagramElements.containsKey(element.getJointjsId())) {
			DiagramObject originalElement = diagramElements.get(element.getJointjsId());
			diagram.removeDiagramObject(originalElement);
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
			diagramElements.remove(element.getJointjsId());
			
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' Diagram element: " + element.getClass() + " removed'.");
		}
	}

	private DiagramObject updateObjectOfDiagram(String jsonString) {
		DiagramObject element = DiagramObject.fromJson(jsonString);
		if (diagramElements.containsKey(element.getJointjsId())) {
			DiagramObject originalElement = diagramElements.get(element.getJointjsId());
			originalElement.update(element, UserSessionHandler.getUser());
			diagram.setUpdatedBy(UserSessionHandler.getUser());
			diagram.setUpdateTime();
			element = originalElement;
		}
		return element;
	}

	private void updateLinkVisualization(DiagramObject object) {
		if (object instanceof DiagramLink) {
			DiagramObject source = ((DiagramLink) object).getSourceElement();
			if (source instanceof DiagramFork) {
				for (DiagramLink link : ((DiagramFork) source).getOutgoingLinks()) {
					updateChangesToDiagram(link);
				}
			}
		}
	}

	public void setDiagram(Diagram diagram) {
		clearSilently();
		this.diagram = diagram;
		if (diagram != null) {
			// Initialize the map of diagramElements.
			if (!securityService.isFormReadOnly(
					UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
				setEnabled(true);
			}
			diagramElements = createMapOfDiagramObjects(diagram);
			fromJson(diagram.toJson());
		} else {
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
						diagram.removeDiagramObject(objectToRemove);
						itr.remove();
					}
				}
				// Now insert new elements and update old ones.
				for (DiagramObject object : tempDiagram.getDiagramObjects()) {
					if (diagramElements.containsKey(object.getJointjsId())) {
						// Already exist, update
						DiagramObject currentValue = diagramElements.get(object.getJointjsId());
						currentValue.update(object, UserSessionHandler.getUser());
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
		pickListeners.add(listener);
	}

	public void removeDiagramObjectPickedListener(DiagramObjectPickedListener listener) {
		pickListeners.remove(listener);
	}

	private void fireDiagramObjectPickedListeners(DiagramObject pickedObject) {
		for (DiagramObjectPickedListener listener : pickListeners) {
			listener.diagramObjectPicked(pickedObject);
		}
	}

	public void updateChangesToDiagram(DiagramObject element) {
		if(diagram.getDiagramObjects().contains(element)){
			if (element instanceof DiagramElement) {
				updateCellJson(element.toJson());
			} else {
				updateLinkJson(element.toJson());
			}
		}
	}

	public void addJumpToListener(JumpToListener jumpToListener) {
		jumpToListeners.add(jumpToListener);
	}

	public void removeJumpToListener(JumpToListener jumpToListener) {
		jumpToListeners.remove(jumpToListener);
	}

	private void fireJumpToListener(Object object) {
		for (JumpToListener listener : jumpToListeners) {
			listener.jumpTo(object);
		}
	}

	public void addDiagramObjectAddedListener(DiagramObjectAddedListener listener) {
		objectAddedListeners.add(listener);
	}

	public void removeDiagramObjectAddedListener(DiagramObjectAddedListener listener) {
		objectAddedListeners.remove(listener);
	}

	private void fireDiagramObjectAddedListener(DiagramObject elementAdded) {
		for (DiagramObjectAddedListener listener : objectAddedListeners) {
			listener.elementAdded(elementAdded);
		}
	}

	public void addDiagramObjectUpdatedListener(DiagramObjectUpdatedListener listener) {
		objectUpdatedListeners.add(listener);
	}

	public void removeDiagramObjectUpdatedListener(DiagramObjectUpdatedListener listener) {
		objectUpdatedListeners.remove(listener);
	}

	private void fireDiagramObjectUpdatedListener(DiagramObject elementUpdated) {
		for (DiagramObjectUpdatedListener listener : objectUpdatedListeners) {
			listener.elementUpdated(elementUpdated);
		}
	}
}
