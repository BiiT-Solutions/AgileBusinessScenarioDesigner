package com.biit.abcd.webpages.components;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.utils.DateManager;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.elements.decisiontable.Cell;
import com.biit.abcd.webpages.elements.decisiontable.CellRowSelector;
import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TreeTable;

public class SelectDiagramTable extends TreeTable {
	private static final long serialVersionUID = -2420087674159328133L;

	private CellRowSelector cellRowSelector;

	enum MenuProperties {
		DIAGRAM_NAME, UPDATE_TIME;
	};

	public SelectDiagramTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		addContainerProperty(MenuProperties.DIAGRAM_NAME, EditCellComponent.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_DIAGRAM_BUILDER_TABLE_DIAGRAM_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_DIAGRAM_BUILDER_TABLE_DIAGRAM_UPDATE), null, Align.LEFT);

		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.DIAGRAM_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.DIAGRAM_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.DIAGRAM_NAME);
		setSortAscending(false);
	}

	@SuppressWarnings("unchecked")
	public void addDiagram(final Diagram diagram) {
		Item item = addItem(diagram);
		EditCellComponent editCellComponent = new SelectTableEditCell();
		editCellComponent.setOnlyEdit(true);
		item.getItemProperty(MenuProperties.DIAGRAM_NAME).setValue(editCellComponent);
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToStringWithHours((diagram.getUpdateTime())));
		editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener(diagram));
		editCellComponent.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = -4750839674064167369L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				setValue(diagram);
			}
		});
		setChildrenAllowed(diagram, false);
		updateItemInGui(diagram);
	}

	public void addRow(Diagram diagram) {
		addDiagram(diagram);
		updateItemInGui(diagram);
		sort();
	}

	public void addRows(Set<Diagram> diagrams) {
		for (Diagram diagram : diagrams) {
			addRow(diagram);
		}
		for (Diagram diagram : diagrams) {
			List<Diagram> childDiagrams = diagram.getChildDiagrams();
			if (!childDiagrams.isEmpty()) {
				setChildrenAllowed(diagram, true);
				setCollapsed(diagram, false);
				for (Diagram childDiagram : childDiagrams) {
					setParent(childDiagram, diagram);
				}
			}
		}
		sort();
	}

	public void selectFirstRow() {
		Collection<?> ids = getItemIds();
		if (!ids.isEmpty()) {
			Object object = ids.iterator().next();
			setValue(object);
		}
	}

	public Diagram getSelectedDiagram() {
		return (Diagram) getValue();
	}

	@SuppressWarnings("unchecked")
	protected void updateItemInGui(Diagram diagram) {
		Item row = getItem(diagram);
		SelectTableEditCell tableCell = ((SelectTableEditCell) row.getItemProperty(MenuProperties.DIAGRAM_NAME)
				.getValue());
		row.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToStringWithHours(diagram.getUpdateTime()));
		tableCell.setLabel(diagram);
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private Diagram diagram;

		public CellEditButtonClickListener(Diagram diagram) {
			this.diagram = diagram;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final TableCellLabelEditWindow newTableCellEditWindow = new TableCellLabelEditWindow(
					ServerTranslate.translate(LanguageCodes.WINDOW_EDIT_TABLE_CELL_LABEL));

			newTableCellEditWindow.setValue(diagram.getName());
			newTableCellEditWindow.showCentered();
			newTableCellEditWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					for (Diagram existingDiagram : UserSessionHandler.getFormController().getForm().getDiagrams()) {
						if (existingDiagram != diagram
								&& existingDiagram.getName().equals(newTableCellEditWindow.getValue())) {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_DROOLS_RULE_NAME);
							return;
						}
					}
					diagram.setName(newTableCellEditWindow.getValue());
					diagram.setUpdateTime();
					updateItemInGui(diagram);
					newTableCellEditWindow.close();
				}
			});
		}
	}

	@Override
	public void setValue(Object itemId) {
		if (itemId != null) {
			Set<Cell> cells = new HashSet<Cell>();
			for (Object colId : getContainerPropertyIds()) {
				Cell tempCell = new Cell(itemId, colId);
				cells.add(tempCell);
			}
			cellRowSelector.setCurrentSelectedCells(this, cells, null, false);
		}
		super.setValue(itemId);
	}
}
