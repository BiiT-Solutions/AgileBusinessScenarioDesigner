package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.QuestionAndAnswerValue;
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

		addContainerProperty(getContainerPropertyIds().size(), QuestionValueEditCell.class, null, "TODO-QUESTION",
				null, Align.CENTER);
		addContainerProperty(getContainerPropertyIds().size(), AnswerValueEditCell.class, null, "TODO-ANSWER", null,
				Align.CENTER);

		for (Object itemId : getItemIds()) {
			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
			updateItem((TableRuleRow) itemId);
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
				if (((Integer) propertyId) % 2 == 0) {
					if (((Integer) propertyId / 2) >= ((TableRuleRow) itemId).getConditions().size()) {
						((TableRuleRow) itemId).getConditions().add(new QuestionAndAnswerValue());
					}
					editCellComponent = new QuestionValueEditCell();
					editCellComponent.addEditButtonClickListener(new CellEditButtonQuestionClickListener(
							getQuestionAndAnswerValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonQuestionClickListener(
							getQuestionAndAnswerValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
				} else {
					editCellComponent = new AnswerValueEditCell();
					editCellComponent.addEditButtonClickListener(new CellEditButtonAnswerClickListener(
							getQuestionAndAnswerValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonAnswerClickListener(
							getQuestionAndAnswerValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
				}
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
		int i = 0;
		for (QuestionAndAnswerValue questionAndAnswerValue : rule.getConditions()) {
			QuestionValueEditCell questionValue = ((QuestionValueEditCell) row.getItemProperty(i).getValue());
			AnswerValueEditCell answerValue = ((AnswerValueEditCell) row.getItemProperty(i + 1).getValue());

			if (questionValue != null) {
				questionValue.setLabel(questionAndAnswerValue.getQuestion());
				row.getItemProperty(i).setValue(questionValue);
			}
			if (answerValue != null) {
				answerValue.setLabel(questionAndAnswerValue.getAnswer());
				row.getItemProperty(i + 1).setValue(answerValue);
			}

			i += 2;
		}
	}

	public QuestionAndAnswerValue getQuestionAndAnswerValue(TableRuleRow row, Object propertyId) {
		if (((Integer) propertyId % 2) == 0) {
			return row.getConditions().get(((Integer) propertyId) / 2);
		} else {
			return row.getConditions().get(((Integer) propertyId - 1) / 2);
		}
	}

	private class CellEditButtonQuestionClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private QuestionAndAnswerValue questionAnswer;
		private TableRuleRow rule;

		public CellEditButtonQuestionClickListener(QuestionAndAnswerValue questionAndAnswerValue, TableRuleRow rule) {
			this.questionAnswer = questionAndAnswerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
					.getFormController().getForm(), false);

			if (questionAnswer.getQuestion() != null) {
				newConditionWindow.setTreeObjectSelected(questionAnswer.getQuestion());
			}
			newConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
					if (selectedQuestion == null) {
						questionAnswer.setAnswer(null);
					}
					questionAnswer.setQuestion(selectedQuestion);
					updateItem(rule);
					newConditionWindow.close();
				}
			});

			newConditionWindow.showCentered();
		}
	}

	private class CellEditButtonAnswerClickListener implements ClickListener {
		private static final long serialVersionUID = -1802531580937378464L;
		private QuestionAndAnswerValue questionAnswer;
		private TableRuleRow rule;

		public CellEditButtonAnswerClickListener(QuestionAndAnswerValue questionAndAnswerValue, TableRuleRow rule) {
			this.questionAnswer = questionAndAnswerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			if (questionAnswer.getQuestion() != null) {
				final AddNewAnswerValue newAnswerValueWindow = new AddNewAnswerValue(questionAnswer.getQuestion());
				if (questionAnswer.getAnswer() != null) {
					newAnswerValueWindow.setTreeObjectSelected(questionAnswer.getAnswer());
				}
				newAnswerValueWindow.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Answer selectedanswer = ((AddNewAnswerValue) window).getSelectedTableValue();
						questionAnswer.setAnswer(selectedanswer);
						updateItem(rule);
						newAnswerValueWindow.close();
					}
				});
				newAnswerValueWindow.showCentered();
			} else {
				// TODO show message
			}

		}
	}

	private class CellDeleteButtonQuestionClickListener implements ClickListener {
		private static final long serialVersionUID = -4967974394553397046L;
		private QuestionAndAnswerValue questionAnswer;
		private TableRuleRow rule;

		public CellDeleteButtonQuestionClickListener(QuestionAndAnswerValue questionAndAnswerValue, TableRuleRow rule) {
			this.questionAnswer = questionAndAnswerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			questionAnswer.setQuestion(null);
			questionAnswer.setAnswer(null);
			updateItem(rule);
		}
	}

	private class CellDeleteButtonAnswerClickListener implements ClickListener {
		private static final long serialVersionUID = 6594787287245555367L;
		private QuestionAndAnswerValue questionAnswer;
		private TableRuleRow rule;

		public CellDeleteButtonAnswerClickListener(QuestionAndAnswerValue questionAndAnswerValue, TableRuleRow rule) {
			this.questionAnswer = questionAndAnswerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			questionAnswer.setAnswer(null);
			updateItem(rule);
		}
	}

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
