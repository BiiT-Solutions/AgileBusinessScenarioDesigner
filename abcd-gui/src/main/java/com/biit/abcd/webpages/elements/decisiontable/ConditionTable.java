package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.AnswerCondition;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class ConditionTable extends Table {

	private static final long serialVersionUID = -8109315235459994799L;
	private static final int rowHeaderWidth = 32;

	private CellRowSelector cellRowSelector;

	public ConditionTable() {
		setRowHeaderMode(RowHeaderMode.INDEX);
		setColumnWidth(null, rowHeaderWidth);

		setImmediate(true);
		setSizeFull();

		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		addActionHandler(cellRowSelector);
		setSelectable(false);
	}

	public void addColumn(Question question) {
		addContainerProperty(question, Component.class, null, question.getName(), null, Align.CENTER);
		for (Object itemId : getItemIds()) {
			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
		}
	}

	public Collection<Question> getSelectedQuestions() {
		Set<Question> questions = new HashSet<Question>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			questions.add((Question) cell.getCol());
		}
		return questions;
	}

	public Collection<TableRuleRow> getSelectedRules() {
		Set<TableRuleRow> rules = new HashSet<TableRuleRow>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			rules.add((TableRuleRow) cell.getRow());
		}
		return rules;
	}

	public void addItem(TableRuleRow rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
			updateItem(rule);
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		final Table thisTable = this;
		for (final Object propertyId : getContainerPropertyIds()) {
			if (item.getItemProperty(propertyId).getValue() == null) {
				EditCellComponent editCellComponent = new QuestionValueEditCell();
				// Propagate element click.
				editCellComponent.addLayoutClickListener(new LayoutClickListener() {
					private static final long serialVersionUID = -8606373054437936380L;

					@Override
					public void layoutClick(LayoutClickEvent event) {
						MouseEventDetails mouseEvent = new MouseEventDetails();
						mouseEvent.setAltKey(event.isAltKey());
						mouseEvent.setButton(event.getButton());
						mouseEvent.setClientX(event.getClientX());
						mouseEvent.setClientY(event.getClientY());
						mouseEvent.setCtrlKey(event.isCtrlKey());
						mouseEvent.setMetaKey(event.isMetaKey());
						mouseEvent.setRelativeX(event.getRelativeX());
						mouseEvent.setRelativeY(event.getRelativeY());
						mouseEvent.setShiftKey(event.isShiftKey());
						if (event.isDoubleClick()) {
							// Double click
							mouseEvent.setType(0x00002);
						} else {
							mouseEvent.setType(0x00001);
						}
						cellRowSelector.itemClick(new ItemClickEvent(thisTable, item, itemId, propertyId, mouseEvent));
					}
				});
				editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((Question) propertyId,
						(TableRuleRow) itemId));
				editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonClickListener((Question) propertyId,
						(TableRuleRow) itemId));
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
		setCurrentSelectedCells(new HashSet<Cell>(), null, false);
		return super.removeContainerProperty(propertyId);
	}

	@Override
	public boolean removeItem(Object itemId) {
		setCurrentSelectedCells(new HashSet<Cell>(), null, true);
		return super.removeItem(itemId);
	}

	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell, boolean propagate) {
		cellRowSelector.setCurrentSelectedCells(this, cells, cursorCell, propagate);
	}

	public void selectRows(Set<Object> rowIds, boolean propagate) {
		Set<Cell> rows = new HashSet<Cell>();
		for (Object rowId : rowIds) {
			for (Object colId : getContainerPropertyIds()) {
				Cell tempCell = new Cell(rowId, colId);
				rows.add(tempCell);
			}
		}
		setCurrentSelectedCells(rows, null, propagate);
	}

	/**
	 * Updates a row of the table.
	 * 
	 * @param rule
	 */
	@SuppressWarnings("unchecked")
	private void updateItem(TableRuleRow rule) {
		Item row = getItem(rule);
		for (Question question : rule.getConditions().keySet()) {
			QuestionValueEditCell questionValue = ((QuestionValueEditCell) row.getItemProperty(question).getValue());
			questionValue.setLabel(rule.getConditions().get(question).toString());
			row.getItemProperty(question).setValue(questionValue);
		}
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private Question question;
		private TableRuleRow rule;

		public CellEditButtonClickListener(Question question, TableRuleRow rule) {
			this.question = question;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final AddNewAnswerValue newAnswerValue = new AddNewAnswerValue(question);
			newAnswerValue.showCentered();
			if (rule.getConditions() != null && rule.getConditions().get(question) != null) {
				newAnswerValue
						.setTreeObjectSelected(((AnswerCondition) rule.getConditions().get(question)).getAnswer());
			}
			newAnswerValue.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					Answer selectedAnswer = ((AddNewAnswerValue) window).getSelectedTableValue();
					if (selectedAnswer != null) {
						rule.putCondition(question, new AnswerCondition(selectedAnswer));
						updateItem(rule);
						newAnswerValue.close();
					}
				}
			});
		}
	}

	private class CellDeleteButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -7125934888135148456L;
		private Question question;
		private TableRuleRow rule;

		public CellDeleteButtonClickListener(Question question, TableRuleRow rule) {
			this.question = question;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			rule.removeCondition(question);
			updateItem(rule);
		}
	}

	public void addCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.addCellSelectionListener(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.removeCellSelectionListener(listener);
	}
}
