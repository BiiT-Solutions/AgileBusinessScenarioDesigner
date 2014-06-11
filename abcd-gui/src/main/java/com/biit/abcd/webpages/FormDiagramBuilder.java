package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.elements.diagrambuilder.DiagramBuilderElementPicked;
import com.biit.abcd.webpages.elements.diagrambuilder.FormDiagramBuilderUpperMenu;
import com.biit.abcd.webpages.elements.diagrambuilder.JsonPropertiesComponent;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder.DiagramBuilderJsonGenerationListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class FormDiagramBuilder extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private DiagramBuilder diagramBuilder;
	private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;

	public FormDiagramBuilder() {
		updateButtons(true);
		addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -4725913087209115156L;

			@Override
			public void detach(DetachEvent event) {
				// Update diagram object if modified.
				if (diagramBuilder != null && UserSessionHandler.getFormController().getForm() != null) {
					diagramBuilder.toJson(new ObtainJson());
				}
			}

		});
	}

	@Override
	protected void initContent() {
		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		HorizontalLayout rootDiagramBuilder = new HorizontalLayout();
		rootDiagramBuilder.setSpacing(true);

		JsonPropertiesComponent propertiesContainer = new JsonPropertiesComponent();
		propertiesContainer.setSizeFull();

		diagramBuilder = new DiagramBuilder();
		diagramBuilder.setSizeFull();
		diagramBuilder.addElementPickedListener(new DiagramBuilderElementPicked(propertiesContainer));

		propertiesContainer.addPropertyUpdateListener(new PropertieUpdateListener() {

			@Override
			public void propertyUpdate(Object element) {
				if (element instanceof DiagramElement) {
					diagramBuilder.updateCellJson(((DiagramObject) element).toJson());
				} else {
					diagramBuilder.updateLinkJson(((DiagramObject) element).toJson());
				}
			}
		});

		rootDiagramBuilder.addComponent(diagramBuilder);
		rootDiagramBuilder.setExpandRatio(diagramBuilder, 0.80f);
		rootDiagramBuilder.addComponent(propertiesContainer);
		rootDiagramBuilder.setExpandRatio(propertiesContainer, 0.20f);

		rootLayout.setContent(rootDiagramBuilder);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();
	}

	private void initUpperMenu() {
		diagramBuilderUpperMenu = new FormDiagramBuilderUpperMenu();
		diagramBuilderUpperMenu.addClearButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3419227544702101097L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.clear();
			}
		});
		diagramBuilderUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3692380302089994511L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});
		diagramBuilderUpperMenu.addUndoButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4071103244551097590L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.undo();
			}
		});
		diagramBuilderUpperMenu.addRedoButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8948171519257161439L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.redo();
			}
		});
		diagramBuilderUpperMenu.addToFrontButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 10874132205961162L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.toFront();
			}
		});
		diagramBuilderUpperMenu.addToBackButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6793636700517664421L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.toBack();
			}
		});
		diagramBuilderUpperMenu.addToSvgButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6867911525454122263L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.openAsSvg();
			}
		});
		diagramBuilderUpperMenu.addToPngButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -2961691826072098281L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.openAsPng();
			}
		});

		setUpperMenu(diagramBuilderUpperMenu);
	}

	@Override
	public void setForm(Form form) {
		if (form != null) {
			// Quick fix, this has to be changed when the full "diagrams" tree
			// structure is decided.
			if (!UserSessionHandler.getFormController().getForm().getDiagrams().isEmpty()) {
				UserSessionHandler.getFormController().getForm().getDiagrams().get(getSelectedDiagram())
						.setUpdatedBy(UserSessionHandler.getUser());
				UserSessionHandler.getFormController().getForm().getDiagrams().get(getSelectedDiagram())
						.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
			}
		}
		// New diagram
		if (UserSessionHandler.getFormController().getForm().getDiagrams().isEmpty()) {
			UserSessionHandler.getFormController().getForm().getDiagrams().add(new Diagram(form));
		}
		updateDiagramDesigner();
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void save() {
		if (diagramBuilder != null && UserSessionHandler.getFormController().getForm() != null
				&& UserSessionHandler.getFormController().getForm().getDiagrams() != null) {
			diagramBuilder.toJson(new SaveJson());
		}
	}

	/**
	 * Current diagram selected by the user in the left menu.
	 * 
	 * @return
	 */
	private int getSelectedDiagram() {
		return 0;
	}

	private void updateDiagramDesigner() {
		// Refresh jointjs
		diagramBuilder.fromJson(UserSessionHandler.getFormController().getForm().getDiagrams()
				.get(getSelectedDiagram()).toJson());
	}

	private void updateDiagram(String jsonString) {
		// Get childs from json string.
		Diagram tempDiagram = Diagram.fromJson(jsonString);
		UserSessionHandler.getFormController().getForm().getDiagrams().get(getSelectedDiagram())
				.setDiagramObjects(tempDiagram.getDiagramObjects());
		// Updater
		UserSessionHandler.getFormController().getForm().getDiagrams().get(getSelectedDiagram())
				.setUpdatedBy(UserSessionHandler.getUser());
		UserSessionHandler.getFormController().getForm().getDiagrams().get(getSelectedDiagram())
				.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	}

	/**
	 * Updates diagram objects from the jointjs widget.
	 */
	class ObtainJson implements DiagramBuilderJsonGenerationListener {

		@Override
		public void generatedJsonString(String jsonString) {
			try {
				updateDiagram(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	/**
	 * Updates and save diagram objects from the jointjs widget.
	 */
	class SaveJson implements DiagramBuilderJsonGenerationListener {

		@Override
		public void generatedJsonString(String jsonString) {
			try {
				updateDiagram(jsonString);
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.ERROR_DATA_NOT_STORED);
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}