package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Diagram;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder.DiagramBuilderJsonGenerationListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

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
		diagramBuilder = new DiagramBuilder();
		diagramBuilder.setSizeFull();
		getWorkingAreaLayout().addComponent(diagramBuilder);

		initUpperMenu();
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		if (form != null) {
			diagram = diagramDao.read(form);
		}
		// New diagram
		if (diagram == null) {
			try {
				diagram = new Diagram(form);
			} catch (NotValidFormException e) {
				diagram = null;
			}
		} else {
			// Refresh jointjs
			diagramBuilder.fromJson(diagram.getDiagramAsJson());
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
				diagram.setDiagramAsJson(jsonString);
				diagramDao.makePersistent(diagram);
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.ERROR_DATA_NOT_STORED);
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}