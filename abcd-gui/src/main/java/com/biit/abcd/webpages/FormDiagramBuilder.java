package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.Expressions;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.components.SelectDiagramTable;
import com.biit.abcd.webpages.elements.diagrambuilder.AbcdDiagramBuilder;
import com.biit.abcd.webpages.elements.diagrambuilder.AbcdDiagramBuilder.DiagramObjectPickedListener;
import com.biit.abcd.webpages.elements.diagrambuilder.FormDiagramBuilderUpperMenu;
import com.biit.abcd.webpages.elements.diagrambuilder.JsonPropertiesComponent;
import com.biit.abcd.webpages.elements.diagrambuilder.JumpToListener;
import com.biit.abcd.webpages.elements.diagrambuilder.WindowNewDiagram;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

public class FormDiagramBuilder extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private SelectDiagramTable diagramBuilderTable;
	private AbcdDiagramBuilder diagramBuilder;
	private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;
	private JsonPropertiesComponent propertiesContainer;
	private DiagramTableValueChange diagramTableValueChange;

	public FormDiagramBuilder() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel(false);
		rootLayout.setSizeFull();

		HorizontalLayout rootDiagramBuilder = new HorizontalLayout();
		rootDiagramBuilder.setSpacing(true);

		propertiesContainer = new JsonPropertiesComponent();
		propertiesContainer.setSizeFull();

		diagramBuilderTable = new SelectDiagramTable();
		diagramBuilderTable.setSizeFull();
		diagramBuilderTable.setImmediate(true);
		diagramBuilderTable.setSelectable(true);
		diagramTableValueChange = new DiagramTableValueChange();

		diagramBuilder = new AbcdDiagramBuilder();
		diagramBuilder.setSizeFull();
		diagramBuilder.addDiagramObjectPickedListener(new DiagramObjectPickedListener() {

			@Override
			public void diagramObjectPicked(DiagramObject object) {
				propertiesContainer.focus();
				propertiesContainer.updatePropertiesComponent(object);
			}
		});
		diagramBuilder.addJumpToListener(new JumpToListener() {

			@Override
			public void jumpTo(Object element) {
				// TODO
				if (element == null) {
					return;
				}
				if (element instanceof Question) {
					ApplicationFrame.navigateTo(WebMap.TREE_DESIGNER);
					FormDesigner formDesigner = (FormDesigner) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
					formDesigner.selectComponent((TreeObject) element);
				}
				if (element instanceof TableRule) {
					ApplicationFrame.navigateTo(WebMap.DECISSION_TABLE_EDITOR);
					DecisionTableEditor decisionTable = (DecisionTableEditor) ((ApplicationFrame) UI.getCurrent())
							.getCurrentView();
					decisionTable.selectComponent((TableRule) element);
				}
				if (element instanceof Expressions) {
					ApplicationFrame.navigateTo(WebMap.EXPRESSION_EDITOR);
					ExpressionEditor expressionEditor = (ExpressionEditor) ((ApplicationFrame) UI.getCurrent())
							.getCurrentView();
					expressionEditor.selectComponent((Expressions) element);
				}
				if (element instanceof Diagram) {
					selectComponent((Diagram) element);
				}
			}

		});
		propertiesContainer.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				// Switch limitation with instanceof.
				if (element instanceof DiagramLink) {
					// If we are updating a link, then we must update all the
					// links from the same source.
					DiagramLink currentLink = (DiagramLink) element;
					List<DiagramLink> links = currentLink.getSourceElement().getOutgoingLinks();
					for (DiagramLink link : links) {
						diagramBuilder.updateChangesToDiagram(link);
					}
					return;
				}
				if (element instanceof DiagramFork) {
					DiagramFork currentFork = (DiagramFork) element;
					List<DiagramLink> links = currentFork.getOutgoingLinks();
					for (DiagramLink link : links) {
						link.clear();
					}
					for (DiagramLink link : links) {
						diagramBuilder.updateChangesToDiagram(link);
					}
					diagramBuilder.updateChangesToDiagram(currentFork);
					return;
				}
				if (element instanceof DiagramChild) {
					DiagramChild diagramChild = (DiagramChild) element;
					diagramBuilder.updateChangesToDiagram(diagramChild);

					initializeDiagramsTable();
					diagramBuilderTable.setValue(diagramChild.getParent());
					return;
				}
				// Anyone else
				diagramBuilder.updateChangesToDiagram((DiagramObject) element);
			}
		});

		rootDiagramBuilder.addComponent(diagramBuilder);
		rootDiagramBuilder.setExpandRatio(diagramBuilder, 0.80f);
		rootDiagramBuilder.addComponent(propertiesContainer);
		rootDiagramBuilder.setExpandRatio(propertiesContainer, 0.20f);

		rootLayout.createMenu(diagramBuilderTable);
		rootLayout.setContent(rootDiagramBuilder);

		getWorkingAreaLayout().addComponent(rootLayout);

		initializeDiagramsTableAndSelectFirst();
		initUpperMenu();
	}

	protected void selectComponent(Diagram element) {
		diagramBuilderTable.setValue(element);
	}

	private void initializeDiagramsTable() {
		List<Diagram> diagrams = UserSessionHandler.getFormController().getForm().getDiagrams();
		diagramBuilderTable.removeValueChangeListener(diagramTableValueChange);
		diagramBuilderTable.setValue(null);
		diagramBuilderTable.removeAllItems();
		diagramBuilderTable.addRows(diagrams);
		diagramBuilderTable.addValueChangeListener(diagramTableValueChange);
	}

	private void initializeDiagramsTableAndSelectFirst() {
		initializeDiagramsTable();
		if (UserSessionHandler.getFormController().getLastAccessDiagram() != null) {
			diagramBuilderTable.setValue(UserSessionHandler.getFormController().getLastAccessDiagram());
		} else {
			diagramBuilderTable.selectFirstRow();
		}
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
		if (diagramBuilderTable.getValue() != null) {
			UserSessionHandler.getFormController().getForm().removeDiagram((Diagram) diagramBuilderTable.getValue());
			diagramBuilderTable.removeItem(diagramBuilderTable.getValue());
			diagramBuilderTable.setValue(null);
		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void save() {
		try {
			UserSessionHandler.getFormController().save();
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(FormDiagramBuilder.class.getName(), e);
		}
	}

	public void addDiagram(Diagram newDiagram) {
		diagramBuilderTable.addDiagram(newDiagram);
		diagramBuilderTable.setValue(newDiagram);
	}

	public void sortTableMenu() {
		diagramBuilderTable.sort();
	}

	private class DiagramTableValueChange implements ValueChangeListener {
		private static final long serialVersionUID = 8142953635201344140L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			final Diagram currentDiagram = (Diagram) event.getProperty().getValue();
			propertiesContainer.setFireListeners(false);
			propertiesContainer.updatePropertiesComponent(null);
			diagramBuilder.setDiagram(currentDiagram);
			propertiesContainer.setFireListeners(true);
			UserSessionHandler.getFormController().setLastAccessDiagram(currentDiagram);
		}
	}
}