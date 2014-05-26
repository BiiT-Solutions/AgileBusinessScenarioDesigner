package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.elements.diagramBuilder.DiagramBuilderElementPicked;
import com.biit.abcd.webpages.elements.diagramBuilder.FormDiagramBuilderUpperMenu;
import com.biit.abcd.webpages.elements.diagramBuilder.JsonPropertiesComponent;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder.DiagramBuilderJsonGenerationListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class FormDiagramBuilder extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private DiagramBuilder diagramBuilder;
	private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;

	private Form form;
	private Diagram diagram;

	private IDiagramDao diagramDao;

	public FormDiagramBuilder() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		diagramDao = (IDiagramDao) helper.getBean("diagramDao");
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
	public void securedEnter(ViewChangeEvent event) {
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
				System.out.println("property update Listener");
				if (element instanceof DiagramElement) {
					System.out.println(((DiagramObject) element).toJson());
					diagramBuilder.updateCellJson(((DiagramObject) element).toJson());
				} else {
					System.out.println(((DiagramObject) element).toJson());
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

	@Override
	public void setForm(Form form) {
		this.form = form;
		if (form != null) {
			diagram = diagramDao.read(form);
			// Quick fix, this has to be changed when the full "diagrams" tree
			// structure is decided.
			if (diagram != null) {
				diagram.setCreatedBy(UserSessionHandler.getUser());
				diagram.setCreationTime(new java.sql.Timestamp(new java.util.Date().getTime()));
			}
		}
		// New diagram
		if (diagram == null) {
			diagram = new Diagram(form);
		} else {
			// Refresh jointjs
			diagramBuilder.fromJson(diagram.toJson());
		}
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void save() {
		if (diagramBuilder != null && getForm() != null && diagram != null) {
			diagramBuilder.toJson(new SaveJson());
		}
	}

	class SaveJson implements DiagramBuilderJsonGenerationListener {

		@Override
		public void generatedJsonString(String jsonString) {
			try {
				// Get childs from json string.
				Diagram tempDiagram = Diagram.fromJson(jsonString);
				if (diagram.getDiagramObjects() != null && !diagram.getDiagramObjects().isEmpty()) {
					// Remove old ones from database.
					diagram.getDiagramObjects().removeAll(diagram.getDiagramObjects());
				}
				diagram.addDiagramObjects(tempDiagram.getDiagramObjects());
				// Updater
				diagram.setUpdatedBy(UserSessionHandler.getUser());
				diagram.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
				diagramDao.makePersistent(diagram);
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.ERROR_DATA_NOT_STORED);
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}