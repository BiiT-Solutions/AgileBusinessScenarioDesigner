package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.persistence.entity.rules.QuestionAndAnswerValue;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Table;

public class QuestionAnswerPairTable extends Table {
	private static final long serialVersionUID = -963052429591605697L;
	private static final int rowHeaderWidth = 32;
	
	private Table thisTable;
	private CellRowSelector cellRowSelector;
	
	
	public QuestionAnswerPairTable() {
		thisTable = this;
		
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

	/**
	 * Removes all columns and rows.
	 */
	public void removeAll() {
		removeAllItems();
		removeAllColumns();
	}

	private void removeAllColumns() {
		List<Object> objectList = new ArrayList<>(getContainerPropertyIds());
		for (Object propertyId : objectList) {
			removeContainerProperty(propertyId);
		}
	}

	public void addColumnPair() {

		addContainerProperty(getContainerPropertyIds().size(), QuestionValueEditCell.class, null, "TODO-QUESTION", null,
				Align.CENTER);
		addContainerProperty(getContainerPropertyIds().size(), AnswerValueEditCell.class, null, "TODO-ANSWER", null, Align.CENTER);

		for (Object itemId : getItemIds()) {
			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
		}
	}

	// TODO
	/*
	 * public Collection<Question> getSelectedColumns() { Set<Question>
	 * questions = new HashSet<Question>(); for (Cell cell :
	 * cellRowSelector.getSelectedCells()) { questions.add((Question)
	 * cell.getCol()); } return questions; }
	 */

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
		for (final Object propertyId : getContainerPropertyIds()) {
			if (item.getItemProperty(propertyId).getValue() == null) {
				EditCellComponent editCellComponent = null;
				if(item.getItemProperty(propertyId).getClass().isInstance(QuestionValueEditCell.class) ){
					editCellComponent = new QuestionValueEditCell();
				}else{
					editCellComponent = new AnswerValueEditCell();
				}
				//TO DO
//				editCellComponent.addEditButtonClickListener(new CellEditButtonQuestionAnswerPairClickListener(
//						(Question) propertyId, (TableRuleRow) itemId));
//				editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonQuestionAnswerPairClickListener(
//						(Question) propertyId, (TableRuleRow) itemId));
				// Propagate element click.
				editCellComponent.addLayoutClickListener(new LayoutClickPropagator(item, itemId, propertyId));
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
	 * Updates row content.
	 * 
	 * @param rule
	 */
	@SuppressWarnings("unchecked")
	private void updateItem(TableRuleRow rule) {
		Item row = getItem(rule);
		int i=0;
		for (QuestionAndAnswerValue questionAndAnswerValue : rule.getConditions()) {
			QuestionValueEditCell questionValue = ((QuestionValueEditCell) row.getItemProperty(i).getValue());
			AnswerValueEditCell answerValue = ((AnswerValueEditCell) row.getItemProperty(i+1).getValue());
			
			if (questionValue != null) {
				questionValue.setLabel(questionAndAnswerValue.getQuestion().toString());
				row.getItemProperty(i).setValue(questionValue);
			}
			if (answerValue != null) {
				questionValue.setLabel(questionAndAnswerValue.getAnswer().toString());
				row.getItemProperty(i+1).setValue(answerValue);
			}
			
			i+=2;
		}
	}
	
	// private class CellEditButtonClickListener implements ClickListener {
	// private static final long serialVersionUID = -4186477224806988479L;
	// private Question question;
	// private TableRuleRow rule;
	//
	// public CellEditButtonClickListener(Question question, TableRuleRow rule)
	// {
	// this.question = question;
	// this.rule = rule;
	// }
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// final AddNewAnswerValue newAnswerValue = new AddNewAnswerValue(question);
	// newAnswerValue.showCentered();
	// if (rule.getConditions() != null && rule.getConditions().get(question) !=
	// null) {
	// newAnswerValue
	// .setTreeObjectSelected(((AnswerCondition)
	// rule.getConditions().get(question)).getAnswer());
	// }
	// newAnswerValue.addAcceptAcctionListener(new AcceptActionListener() {
	// @Override
	// public void acceptAction(AcceptCancelWindow window) {
	// Answer selectedAnswer = ((AddNewAnswerValue)
	// window).getSelectedTableValue();
	// if (selectedAnswer != null) {
	// rule.putCondition(question, new AnswerCondition(selectedAnswer));
	// updateItem(rule);
	// newAnswerValue.close();
	// }
	// }
	// });
	// }
	// }
	//
	// private class CellDeleteButtonClickListener implements ClickListener {
	// private static final long serialVersionUID = -7125934888135148456L;
	// private Question question;
	// private TableRuleRow rule;
	//
	// public CellDeleteButtonClickListener(Question question, TableRuleRow
	// rule) {
	// this.question = question;
	// this.rule = rule;
	// }
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// rule.removeCondition(question);
	// updateItem(rule);
	// }
	// }
	
	public void addCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.addCellSelectionListener(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.removeCellSelectionListener(listener);
	}
	
	private class LayoutClickPropagator implements LayoutClickListener {
		private static final long serialVersionUID = 5504698883691497113L;
		private Item item;
		private Object itemId;
		private Object propertyId;
		
		public LayoutClickPropagator(Item item, Object itemId, Object propertyId) {
			this.item = item;
			this.itemId = itemId;
			this.propertyId = propertyId;
		}
		
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
	}
}
