package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.elements.diagrambuilder.AbcdDiagramBuilder;
import com.biit.abcd.webpages.elements.diagrambuilder.AbcdDiagramBuilder.DiagramObjectPickedListener;
import com.biit.abcd.webpages.elements.diagrambuilder.AbcdDiagramBuilder.DiagramUpdated;
import com.biit.abcd.webpages.elements.diagrambuilder.DiagramBuilderElementPicked;
import com.biit.abcd.webpages.elements.diagrambuilder.DiagramBuilderTable;
import com.biit.abcd.webpages.elements.diagrambuilder.FormDiagramBuilderUpperMenu;
import com.biit.abcd.webpages.elements.diagrambuilder.JsonPropertiesComponent;
import com.biit.abcd.webpages.elements.diagrambuilder.WindowNewDiagram;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

public class FormDiagramBuilder extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private DiagramBuilderTable diagramBuilderTable;
	private AbcdDiagramBuilder diagramBuilder;
	private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;
	private JsonPropertiesComponent propertiesContainer;
	private Diagram previousDiagram;
	private Diagram currentDiagram;

	public FormDiagramBuilder() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		HorizontalLayout rootDiagramBuilder = new HorizontalLayout();
		rootDiagramBuilder.setSpacing(true);

		propertiesContainer = new JsonPropertiesComponent();
		propertiesContainer.setSizeFull();

		diagramBuilderTable = new DiagramBuilderTable();
		diagramBuilderTable.setSizeFull();
		diagramBuilderTable.setImmediate(true);
		diagramBuilderTable.setSelectable(true);
		diagramBuilderTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8142953635201344140L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				previousDiagram = currentDiagram;
				currentDiagram = (Diagram) event.getProperty().getValue();
				propertiesContainer.updatePropertiesComponent(null);

				if (diagramBuilder.getDiagram() != null) {
					diagramBuilder.updateDiagram(new DiagramUpdated() {
						@Override
						public void updated(Diagram diagram) {
							if (currentDiagram == null) {
								diagramBuilder.setEnabled(false);
								diagramBuilder.setDiagram(null);
							} else {
								diagramBuilder.setEnabled(true);
								diagramBuilder.clear();
								diagramBuilder.setDiagram(currentDiagram);
							}
						}
					});
				} else {
					if (currentDiagram == null) {
						diagramBuilder.setEnabled(false);
						diagramBuilder.setDiagram(null);
					} else {
						diagramBuilder.setEnabled(true);
						diagramBuilder.clear();
						diagramBuilder.setDiagram(currentDiagram);
					}
				}
			}
		});

		diagramBuilder = new AbcdDiagramBuilder();
		diagramBuilder.setSizeFull();
		diagramBuilder.addDiagramObjectPickedListener(new DiagramObjectPickedListener() {

			@Override
			public void diagramObjectPicked(DiagramObject object) {
				propertiesContainer.focus();
				propertiesContainer.updatePropertiesComponent(object);
			}
		});
		propertiesContainer.addPropertyUpdateListener(new PropertieUpdateListener() {

			@Override
			public void propertyUpdate(Object element) {
				System.out.println("Property updateJson: " + ((DiagramObject) element).toJson());
				diagramBuilder.updateChangesToDiagram((DiagramObject) element);
			}
		});

		rootDiagramBuilder.addComponent(diagramBuilder);
		rootDiagramBuilder.setExpandRatio(diagramBuilder, 0.80f);
		rootDiagramBuilder.addComponent(propertiesContainer);
		rootDiagramBuilder.setExpandRatio(propertiesContainer, 0.20f);

		rootLayout.setMenu(diagramBuilderTable);
		rootLayout.setContent(rootDiagramBuilder);

		getWorkingAreaLayout().addComponent(rootLayout);

		initDiagrams();
		initUpperMenu();
	}

	private void initDiagrams() {
		List<Diagram> diagrams = UserSessionHandler.getFormController().getForm().getDiagrams();
		for (Diagram diagram : diagrams) {
			diagramBuilderTable.addDiagram(diagram);
		}
		diagramBuilderTable.setValue(null);
		diagramBuilder.setEnabled(false);
	}

	private void initUpperMenu() {
		final FormDiagramBuilder thisPage = this;
		diagramBuilderUpperMenu = new FormDiagramBuilderUpperMenu();
		diagramBuilderUpperMenu.addNewDiagramButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4071103244551097590L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new WindowNewDiagram(thisPage, LanguageCodes.FORM_DIAGRAM_BUILDER_NEW_DIAGRAM_CAPTION,
								LanguageCodes.FORM_DIAGRAM_BUILDER_NEW_DIAGRAM_TEXTFIELD));
			}
		});
		diagramBuilderUpperMenu.addDeleteNewDiagramButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8948171519257161439L;

			@Override
			public void buttonClick(ClickEvent event) {
				deleteDiagram();
			}
		});
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

	private void deleteDiagram() {
		UserSessionHandler.getFormController().getForm().removeDiagram(currentDiagram);
		diagramBuilderTable.removeItem(currentDiagram);
		diagramBuilderTable.setValue(null);
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void save() {
		if (diagramBuilder.getDiagram() != null) {
			diagramBuilder.updateDiagram(new DiagramUpdated() {
				@Override
				public void updated(Diagram diagram) {
					// Wait until the diagram has been updated.
					try {
						System.out.println("first element biitText: "
								+ ((DiagramElement) diagramBuilder.getDiagram().getDiagramObjects().get(0))
										.getBiitText());
						UserSessionHandler.getFormController().save();
						MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
					} catch (Exception e) {
						MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
						AbcdLogger.errorMessage(FormDiagramBuilder.class.getName(), e);
					}
				}
			});
		} else {
			try {
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
				AbcdLogger.errorMessage(FormDiagramBuilder.class.getName(), e);
			}
		}

	}

	public void addDiagram(Diagram newDiagram) {
		diagramBuilderTable.addDiagram(newDiagram);
		diagramBuilderTable.setValue(newDiagram);
	}

	public void sortTableMenu() {
		diagramBuilderTable.sort();
	}
}