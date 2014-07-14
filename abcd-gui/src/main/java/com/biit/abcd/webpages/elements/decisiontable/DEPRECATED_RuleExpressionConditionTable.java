package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.SelectAnswerWindow;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;

public class DEPRECATED_RuleExpressionConditionTable extends Table {
	private static final long serialVersionUID = -963052429591605697L;
	private static final int rowHeaderWidth = 32;

	private Table thisTable;
	private CellRowSelector cellRowSelector;

	public DEPRECATED_RuleExpressionConditionTable() {
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
		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_QUESTION_CONDITION), null, Align.CENTER);
		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_ANSWER_CONDITION), null, Align.CENTER);

		for (Object itemId : getItemIds()) {
			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
			updateItem((TableRuleRow) itemId);
		}
	}

	public Collection<Integer> getSelectedColumns() {
		Set<Integer> columns = new HashSet<Integer>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			columns.add((Integer) cell.getCol());
		}
		return columns;
	}

	public void removeColumns(Collection<Integer> columnIds) {
		// To remove the expression related
		if((columnIds.size() == 1)){
			if((columnIds.iterator().next() % 2) == 0){
				columnIds.add(columnIds.iterator().next()+1);
			}
			else{
				columnIds.add(columnIds.iterator().next()-1);
			}
		}
		for (Object object : getItemIds()) {
			TableRuleRow row = (TableRuleRow) object;
			Set<Expression> values = new HashSet<Expression>();
			for (Integer columnId : columnIds) {
				values.add(getExpressionValue(row, columnId));
			}
			for (Expression value : values) {
				row.getConditions().remove(value);
			}
		}

		Set<Integer> filteredColumnIds = new HashSet<>();
		for (Integer columnId : columnIds) {
			if ((columnId % 2) == 0) {
				filteredColumnIds.add(columnId);
			} else {
				filteredColumnIds.add(columnId - 1);
			}
		}
		for (int i = 0; i < filteredColumnIds.size(); i++) {
			removeContainerProperty(getContainerPropertyIds().size() - 1);
			removeContainerProperty(getContainerPropertyIds().size() - 1);
		}

		// Update
		for (Object object : getItemIds()) {
			TableRuleRow row = (TableRuleRow) object;
			updateItem(row);
		}
	}

	public Collection<TableRuleRow> getSelectedRules() {
		Set<TableRuleRow> rules = new HashSet<TableRuleRow>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			rules.add((TableRuleRow) cell.getRow());
		}
		return rules;
	}

	public void addItem(TableRuleRow rule) {
		System.out.println("addItem");
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
			System.out.println("rule not null");
			System.out.println("FIRST EXPRESSION: " + rule.getConditions().get(0)
					.getExpression());
			updateItem(rule);
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		System.out.println("CONT_PROP_SIZE: " + getContainerPropertyIds().size());
		for (final Object propertyId : getContainerPropertyIds()) {
			System.out.println("PROP_ID: " + propertyId);
			if (item.getItemProperty(propertyId).getValue() == null) {
				ExpressionEditCell editCellComponent = new ExpressionEditCell();
				((TableRuleRow) itemId).getConditions().add(new ExpressionValueTreeObjectReference());
				if ((((Integer) propertyId) % 2) == 0) {
					editCellComponent.addEditButtonClickListener(new CellEditButtonQuestionClickListener(
							getExpressionValue((TableRuleRow) itemId, propertyId),
							getNextExpressionValue((TableRuleRow) itemId, propertyId),
							(TableRuleRow) itemId));
					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonQuestionClickListener(
							getExpressionValue((TableRuleRow) itemId, propertyId),
							(TableRuleRow) itemId));
				} else {
					editCellComponent.addEditButtonClickListener(new CellEditButtonAnswerClickListener(
							getPreviousExpressionValue((TableRuleRow) itemId, propertyId),
							getExpressionValue((TableRuleRow) itemId, propertyId),
							(TableRuleRow) itemId));
					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonAnswerClickListener(
							getExpressionValue((TableRuleRow) itemId, propertyId),
							(TableRuleRow) itemId));
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

	// TODO

	@SuppressWarnings("unchecked")
	public void updateItem(TableRuleRow rule) {
		Item row = getItem(rule);
		int i = 0;
		for (Expression expressionValue : rule.getConditions()) {
			System.out.println("Expression class: " + expressionValue.getClass());
			System.out.println("Expression reference: " + expressionValue.getExpression());
			ExpressionEditCell cellValue = ((ExpressionEditCell) row.getItemProperty(i).getValue());
			System.out.println("kiwi-cellValue " + cellValue);
			if (cellValue != null) {
				System.out.println(expressionValue.getExpression());
				cellValue.setLabel(expressionValue.getExpression());
				row.getItemProperty(i).setValue(cellValue);
			}
			i++;
		}
	}


	public Expression getExpressionValue(TableRuleRow row, Object propertyId) {
		return row.getConditions().get((Integer) propertyId);
	}

	public Expression getNextExpressionValue(TableRuleRow row, Object propertyId) {
		int index = ((Integer) propertyId) + 1;
		if(index>=row.getConditions().size()){
			return null;
		} else {
			return row.getConditions().get(index);
		}
	}

	public Expression getPreviousExpressionValue(TableRuleRow row, Object propertyId) {
		return row.getConditions().get(((Integer) propertyId) -1);
	}

	private class CellEditButtonQuestionClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private ExpressionValueTreeObjectReference questionValue;
		//		private ExpressionValueTreeObjectReference answerValue;
		private TableRuleRow rule;

		/**
		 * @param questionValue
		 * @param answerValue
		 * @param rule
		 */
		public CellEditButtonQuestionClickListener(Expression questionValue, Expression answerValue, TableRuleRow rule) {
			this.questionValue = (ExpressionValueTreeObjectReference) questionValue;
			//			this.answerValue = (ExpressionValueTreeObjectReference) answerValue;
			this.rule = rule;
		}
		// TODO
		@Override
		public void buttonClick(ClickEvent event) {
			final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
					.getFormController().getForm(), false);

			if (questionValue.getReference() != null) {
				newConditionWindow.setTreeObjectSelected(questionValue.getReference());
			}
			newConditionWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
					//					ExpressionValueTreeObjectReference answerValue = searchAnswer(rule, selectedQuestion);
					//					Answer answerToQuestion = (Answer) answerValue.getReference();
					//					if ((selectedQuestion == null) || (!selectedQuestion.contains(answerToQuestion))){
					//						answerValue.setReference(null);
					//					}
					questionValue.setReference(selectedQuestion);
					updateItem(rule);
					newConditionWindow.close();
				}
			});
			newConditionWindow.showCentered();
		}
	}

	private class CellEditButtonAnswerClickListener implements ClickListener {
		private static final long serialVersionUID = -1802531580937378464L;
		private ExpressionValueTreeObjectReference questionValue;
		private ExpressionValueTreeObjectReference answerValue;
		private TableRuleRow rule;

		/**
		 * @param questionValue
		 * @param answerValue
		 * @param rule
		 */
		public CellEditButtonAnswerClickListener(Expression questionValue, Expression answerValue, TableRuleRow rule) {
			this.questionValue = (ExpressionValueTreeObjectReference) questionValue;
			this.answerValue = (ExpressionValueTreeObjectReference) answerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			if (questionValue.getReference() != null) {
				final SelectAnswerWindow newAnswerValueWindow = new SelectAnswerWindow((Question) questionValue.getReference());
				if (answerValue.getReference() != null) {
					newAnswerValueWindow.setTreeObjectSelected(answerValue.getReference());
				}
				newAnswerValueWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Answer selectedanswer = ((SelectAnswerWindow) window).getSelectedTableValue();
						answerValue.setReference(selectedanswer);
						updateItem(rule);
						newAnswerValueWindow.close();
					}
				});
				newAnswerValueWindow.showCentered();
			} else {
				MessageManager.showError(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
						LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
			}
		}
	}

	private class CellDeleteButtonQuestionClickListener implements ClickListener {
		private static final long serialVersionUID = -4967974394553397046L;
		private ExpressionValueTreeObjectReference questionValue;
		private TableRuleRow rule;

		public CellDeleteButtonQuestionClickListener(Expression questionValue, TableRuleRow rule) {
			this.questionValue = (ExpressionValueTreeObjectReference) questionValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			questionValue.setReference(null);
			//			answerValue.setReference(null);
			updateItem(rule);
		}
	}

	private class CellDeleteButtonAnswerClickListener implements ClickListener {
		private static final long serialVersionUID = 6594787287245555367L;
		private ExpressionValueTreeObjectReference answerValue;
		private TableRuleRow rule;

		public CellDeleteButtonAnswerClickListener(Expression answerValue, TableRuleRow rule) {
			this.answerValue = (ExpressionValueTreeObjectReference) answerValue;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			answerValue.setReference(null);
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
	//	public RuleExpressionConditionTable() {
	//		thisTable = this;
	//
	//		setRowHeaderMode(RowHeaderMode.INDEX);
	//		setColumnWidth(null, rowHeaderWidth);
	//
	//		setImmediate(true);
	//		setSizeFull();
	//
	//		cellRowSelector = new CellRowSelector();
	//
	//		addItemClickListener(cellRowSelector);
	//		setCellStyleGenerator(cellRowSelector);
	//		addActionHandler(cellRowSelector);
	//		setSelectable(false);
	//	}
	//
	//	/**
	//	 * Removes all columns and rows.
	//	 */
	//	public void removeAll() {
	//		removeAllItems();
	//		removeAllColumns();
	//	}
	//
	//	private void removeAllColumns() {
	//		List<Object> objectList = new ArrayList<>(getContainerPropertyIds());
	//		for (Object propertyId : objectList) {
	//			removeContainerProperty(propertyId);
	//		}
	//	}
	//
	//	public void addColumnPair() {
	//
	//		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
	//				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_QUESTION_CONDITION), null, Align.CENTER);
	//		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
	//				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_ANSWER_CONDITION), null, Align.CENTER);
	//
	//		for (Object itemId : getItemIds()) {
	//			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
	//			updateItem((TableRuleRow) itemId);
	//		}
	//	}
	//
	//	public Collection<Integer> getSelectedColumns() {
	//		Set<Integer> columns = new HashSet<Integer>();
	//		for (Cell cell : cellRowSelector.getSelectedCells()) {
	//			columns.add((Integer) cell.getCol());
	//		}
	//		return columns;
	//	}
	//
	//	public void removeColumns(Collection<Integer> columnIds) {
	//		for (Object object : getItemIds()) {
	//			TableRuleRow row = (TableRuleRow) object;
	//			Set<ExpressionValueTreeObjectReference> values = new HashSet<ExpressionValueTreeObjectReference>();
	//			for (Integer columnId : columnIds) {
	//				values.add(getExpressionValue(row, columnId));
	//			}
	//			for (ExpressionValueTreeObjectReference value : values) {
	//				row.getConditions().remove(value);
	//			}
	//		}
	//
	//		Set<Integer> filteredColumnIds = new HashSet<>();
	//		for (Integer columnId : columnIds) {
	//			if ((columnId % 2) == 0) {
	//				filteredColumnIds.add(columnId);
	//			} else {
	//				filteredColumnIds.add(columnId - 1);
	//			}
	//		}
	//		for (int i = 0; i < filteredColumnIds.size(); i++) {
	//			removeContainerProperty(getContainerPropertyIds().size() - 1);
	//			removeContainerProperty(getContainerPropertyIds().size() - 1);
	//		}
	//
	//		// Update
	//		for (Object object : getItemIds()) {
	//			TableRuleRow row = (TableRuleRow) object;
	//			updateItem(row);
	//		}
	//	}
	//
	//	public Collection<TableRuleRow> getSelectedRules() {
	//		Set<TableRuleRow> rules = new HashSet<TableRuleRow>();
	//		for (Cell cell : cellRowSelector.getSelectedCells()) {
	//			rules.add((TableRuleRow) cell.getRow());
	//		}
	//		return rules;
	//	}
	//
	//	public void addItem(TableRuleRow rule) {
	//		if (rule != null) {
	//			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
	//			updateItem(rule);
	//		}
	//	}
	//
	//	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
	//		for (final Object propertyId : getContainerPropertyIds()) {
	//			if (item.getItemProperty(propertyId).getValue() == null) {
	//				EditCellComponent editCellComponent = new ExpressionEditCell();
	//				((TableRuleRow) itemId).getConditions().add(new ExpressionValueTreeObjectReference());
	//				if (((Integer)propertyId % 2) == 0) {
	//					editCellComponent.addEditButtonClickListener(new CellEditButtonQuestionClickListener(
	//							getExpressionValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//					//					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonQuestionClickListener(
	//					//							getExpressionValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//				} else {
	//					//					editCellComponent.addEditButtonClickListener(new CellEditButtonAnswerClickListener(
	//					//							getExpressionValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//					//					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonAnswerClickListener(
	//					//							getExpressionValue((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//				}
	//				// Propagate element click.
	//				editCellComponent.addLayoutClickListener(new LayoutClickPropagator(item, itemId, propertyId));
	//				item.getItemProperty(propertyId).setValue(editCellComponent);
	//			}
	//		}
	//	}
	//
	//	@Override
	//	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
	//		setCurrentSelectedCells(new HashSet<Cell>(), null, false);
	//		return super.removeContainerProperty(propertyId);
	//	}
	//
	//	@Override
	//	public boolean removeItem(Object itemId) {
	//		setCurrentSelectedCells(new HashSet<Cell>(), null, true);
	//		return super.removeItem(itemId);
	//	}
	//
	//	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell, boolean propagate) {
	//		cellRowSelector.setCurrentSelectedCells(this, cells, cursorCell, propagate);
	//	}
	//
	//	public void selectRows(Set<Object> rowIds, boolean propagate) {
	//		Set<Cell> rows = new HashSet<Cell>();
	//		for (Object rowId : rowIds) {
	//			for (Object colId : getContainerPropertyIds()) {
	//				Cell tempCell = new Cell(rowId, colId);
	//				rows.add(tempCell);
	//			}
	//		}
	//		setCurrentSelectedCells(rows, null, propagate);
	//	}
	//
	//	/**
	//	 * Updates row content.
	//	 *
	//	 * @param rule
	//	 */
	//	@SuppressWarnings("unchecked")
	//	private void updateItem(TableRuleRow rule) {
	//		Item row = getItem(rule);
	//		int i = 0;
	//		for (Expression expressionValue : rule.getConditions()) {
	//			ExpressionEditCell cellValue = ((ExpressionEditCell) row.getItemProperty(i).getValue());
	//			if (cellValue != null) {
	//				cellValue.setLabel(expressionValue.getExpressionTableString());
	//				row.getItemProperty(i).setValue(cellValue);
	//			}
	//			i++;
	//		}
	//	}
	//
	//	public ExpressionValueTreeObjectReference getExpressionValue(TableRuleRow row, Object propertyId) {
	//		if (((Integer) propertyId % 2) == 0) {
	//			return (ExpressionValueTreeObjectReference) row.getConditions().get(((Integer) propertyId) / 2);
	//		} else {
	//			return (ExpressionValueTreeObjectReference) row.getConditions().get(((Integer) propertyId - 1) / 2);
	//		}
	//	}
	//
	//	private class CellEditButtonQuestionClickListener implements ClickListener {
	//		private static final long serialVersionUID = -4186477224806988479L;
	//		private ExpressionValueTreeObjectReference questionValue;
	//		private TableRuleRow rule;
	//
	//		public CellEditButtonQuestionClickListener(ExpressionValueTreeObjectReference questionValue, TableRuleRow rule) {
	//			this.questionValue = questionValue;
	//			this.rule = rule;
	//		}
	//
	//		@Override
	//		public void buttonClick(ClickEvent event) {
	//			final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
	//					.getFormController().getForm(), false);
	//
	//			if (questionValue.getReference() != null) {
	//				newConditionWindow.setTreeObjectSelected(questionValue.getReference());
	//			}
	//			newConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
	//				@Override
	//				public void acceptAction(AcceptCancelWindow window) {
	//					Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
	//					ExpressionValueTreeObjectReference answerValue = searchAnswer(rule, selectedQuestion);
	//					Answer answerToQuestion = (Answer) answerValue.getReference();
	//					if ((selectedQuestion == null) || (!selectedQuestion.contains(answerToQuestion))){
	//						answerValue.setReference(null);
	//					}
	//					questionValue.setReference(selectedQuestion);
	//					updateItem(rule);
	//					newConditionWindow.close();
	//				}
	//			});
	//			newConditionWindow.showCentered();
	//		}
	//	}
	//
	//	//	private class CellEditButtonAnswerClickListener implements ClickListener {
	//	//		private static final long serialVersionUID = -1802531580937378464L;
	//	//		private QuestionAndAnswerCondition questionAnswer;
	//	//		private TableRuleRow rule;
	//	//
	//	//		public CellEditButtonAnswerClickListener(ExpressionValueTreeObjectReference questionAndAnswerValue, TableRuleRow rule) {
	//	//			this.questionAnswer = questionAndAnswerValue;
	//	//			this.rule = rule;
	//	//		}
	//	//
	//	//		@Override
	//	//		public void buttonClick(ClickEvent event) {
	//	//			if (questionAnswer.getQuestion() != null) {
	//	//				final SelectAnswerWindow newAnswerValueWindow = new SelectAnswerWindow(questionAnswer.getQuestion());
	//	//				if (questionAnswer.getAnswer() != null) {
	//	//					newAnswerValueWindow.setTreeObjectSelected(questionAnswer.getAnswer());
	//	//				}
	//	//				newAnswerValueWindow.addAcceptAcctionListener(new AcceptActionListener() {
	//	//					@Override
	//	//					public void acceptAction(AcceptCancelWindow window) {
	//	//						Answer selectedanswer = ((SelectAnswerWindow) window).getSelectedTableValue();
	//	//						questionAnswer.setAnswer(selectedanswer);
	//	//						updateItem(rule);
	//	//						newAnswerValueWindow.close();
	//	//					}
	//	//				});
	//	//				newAnswerValueWindow.showCentered();
	//	//			} else {
	//	//				MessageManager.showError(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
	//	//						LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
	//	//			}
	//	//		}
	//	//	}
	//
	//	//	private class CellDeleteButtonQuestionClickListener implements ClickListener {
	//	//		private static final long serialVersionUID = -4967974394553397046L;
	//	//		private QuestionAndAnswerCondition questionAnswer;
	//	//		private TableRuleRow rule;
	//	//
	//	//		public CellDeleteButtonQuestionClickListener(ExpressionValueTreeObjectReference questionAndAnswerValue,
	//	//				TableRuleRow rule) {
	//	//			this.questionAnswer = questionAndAnswerValue;
	//	//			this.rule = rule;
	//	//		}
	//	//
	//	//		@Override
	//	//		public void buttonClick(ClickEvent event) {
	//	//			questionAnswer.setQuestion(null);
	//	//			questionAnswer.setAnswer(null);
	//	//			updateItem(rule);
	//	//		}
	//	//	}
	//
	//	//	private class CellDeleteButtonAnswerClickListener implements ClickListener {
	//	//		private static final long serialVersionUID = 6594787287245555367L;
	//	//		private QuestionAndAnswerCondition questionAnswer;
	//	//		private TableRuleRow rule;
	//	//
	//	//		public CellDeleteButtonAnswerClickListener(ExpressionValueTreeObjectReference questionAndAnswerValue, TableRuleRow rule) {
	//	//			this.questionAnswer = questionAndAnswerValue;
	//	//			this.rule = rule;
	//	//		}
	//	//
	//	//		@Override
	//	//		public void buttonClick(ClickEvent event) {
	//	//			questionAnswer.setAnswer(null);
	//	//			updateItem(rule);
	//	//		}
	//	//	}
	//
	//	public void addCellSelectionListener(CellSelectionListener listener) {
	//		cellRowSelector.addCellSelectionListener(listener);
	//	}
	//
	//	public void removeCellSelectionListener(CellSelectionListener listener) {
	//		cellRowSelector.removeCellSelectionListener(listener);
	//	}
	//
	//	private class LayoutClickPropagator implements LayoutClickListener {
	//		private static final long serialVersionUID = 5504698883691497113L;
	//		private Item item;
	//		private Object itemId;
	//		private Object propertyId;
	//
	//		public LayoutClickPropagator(Item item, Object itemId, Object propertyId) {
	//			this.item = item;
	//			this.itemId = itemId;
	//			this.propertyId = propertyId;
	//		}
	//
	//		@Override
	//		public void layoutClick(LayoutClickEvent event) {
	//			MouseEventDetails mouseEvent = new MouseEventDetails();
	//			mouseEvent.setAltKey(event.isAltKey());
	//			mouseEvent.setButton(event.getButton());
	//			mouseEvent.setClientX(event.getClientX());
	//			mouseEvent.setClientY(event.getClientY());
	//			mouseEvent.setCtrlKey(event.isCtrlKey());
	//			mouseEvent.setMetaKey(event.isMetaKey());
	//			mouseEvent.setRelativeX(event.getRelativeX());
	//			mouseEvent.setRelativeY(event.getRelativeY());
	//			mouseEvent.setShiftKey(event.isShiftKey());
	//			if (event.isDoubleClick()) {
	//				// Double click
	//				mouseEvent.setType(0x00002);
	//			} else {
	//				mouseEvent.setType(0x00001);
	//			}
	//			cellRowSelector.itemClick(new ItemClickEvent(thisTable, item, itemId, propertyId, mouseEvent));
	//		}
	//	}
	//
	//	public ExpressionValueTreeObjectReference searchAnswer(TableRuleRow rule, Question selectedQuestion){
	//		int questionIndex = rule.getConditions().indexOf(selectedQuestion);
	//		return (ExpressionValueTreeObjectReference) rule.getConditions().get(questionIndex+1);
	//	}

	//	private static final long serialVersionUID = -963052429591605697L;
	//	private static final int rowHeaderWidth = 32;
	//
	//	private Table thisTable;
	//	private CellRowSelector cellRowSelector;
	//
	//	public RuleExpressionConditionTable() {
	//		thisTable = this;
	//
	//		setRowHeaderMode(RowHeaderMode.INDEX);
	//		setColumnWidth(null, rowHeaderWidth);
	//
	//		setImmediate(true);
	//		setSizeFull();
	//
	//		cellRowSelector = new CellRowSelector();
	//
	//		addItemClickListener(cellRowSelector);
	//		setCellStyleGenerator(cellRowSelector);
	//		addActionHandler(cellRowSelector);
	//		setSelectable(false);
	//	}
	//
	//	/**
	//	 * Removes all columns and rows.
	//	 */
	//	public void removeAll() {
	//		removeAllItems();
	//		removeAllColumns();
	//	}
	//
	//	private void removeAllColumns() {
	//		List<Object> objectList = new ArrayList<>(getContainerPropertyIds());
	//		for (Object propertyId : objectList) {
	//			removeContainerProperty(propertyId);
	//		}
	//	}
	//
	//	public void addColumnPair() {
	//
	//		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
	//				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_QUESTION_CONDITION), null, Align.CENTER);
	//		addContainerProperty(getContainerPropertyIds().size(), ExpressionEditCell.class, null,
	//				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_ANSWER_CONDITION), null, Align.CENTER);
	//
	//		for (Object itemId : getItemIds()) {
	//			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
	//			updateItem((TableRuleRow) itemId);
	//		}
	//	}
	//
	//	public Collection<Integer> getSelectedColumns() {
	//		Set<Integer> columns = new HashSet<Integer>();
	//		for (Cell cell : cellRowSelector.getSelectedCells()) {
	//			columns.add((Integer) cell.getCol());
	//		}
	//		return columns;
	//	}
	//
	//	public void removeColumns(Collection<Integer> columnIds) {
	//		for (Object object : getItemIds()) {
	//			TableRuleRow row = (TableRuleRow) object;
	//			Set<Expression> values = new HashSet<Expression>();
	//			for (Integer columnId : columnIds) {
	//				values.add(getExpression(row, columnId));
	//			}
	//			for (Expression value : values) {
	//				row.removeCondition(value);
	//			}
	//		}
	//
	//		Set<Integer> filteredColumnIds = new HashSet<>();
	//		for (Integer columnId : columnIds) {
	//			if ((columnId % 2) == 0) {
	//				filteredColumnIds.add(columnId);
	//			} else {
	//				filteredColumnIds.add(columnId - 1);
	//			}
	//		}
	//		for (int i = 0; i < filteredColumnIds.size(); i++) {
	//			removeContainerProperty(getContainerPropertyIds().size() - 1);
	//			removeContainerProperty(getContainerPropertyIds().size() - 1);
	//		}
	//
	//		// Update
	//		for (Object object : getItemIds()) {
	//			TableRuleRow row = (TableRuleRow) object;
	//			updateItem(row);
	//		}
	//	}
	//
	//	public Collection<TableRuleRow> getSelectedRules() {
	//		Set<TableRuleRow> rules = new HashSet<TableRuleRow>();
	//		for (Cell cell : cellRowSelector.getSelectedCells()) {
	//			rules.add((TableRuleRow) cell.getRow());
	//		}
	//		return rules;
	//	}
	//
	//	public void addItem(TableRuleRow rule) {
	//		if (rule != null) {
	//			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
	//			updateItem(rule);
	//		}
	//	}
	//
	//	@SuppressWarnings("unchecked")
	//	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
	//		for (final Object propertyId : getContainerPropertyIds()) {
	//			if (item.getItemProperty(propertyId).getValue() == null) {
	//				EditCellComponent editCellComponent = null;
	//				((TableRuleRow) itemId).getConditions().add(new ExpressionValueTreeObjectReference());
	//
	//				if ((((Integer) propertyId) % 2) == 0) {
	//					editCellComponent = new ExpressionEditCell();
	//					editCellComponent.addEditButtonClickListener(new CellEditButtonQuestionClickListener(
	//							getExpression((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonQuestionClickListener(
	//							getExpression((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//				} else {
	//					editCellComponent = new ExpressionEditCell();
	//					editCellComponent.addEditButtonClickListener(new CellEditButtonAnswerClickListener(
	//							getExpression((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//					editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonAnswerClickListener(
	//							getExpression((TableRuleRow) itemId, propertyId), (TableRuleRow) itemId));
	//				}
	//				// Propagate element click.
	//				editCellComponent.addLayoutClickListener(new LayoutClickPropagator(item, itemId, propertyId));
	//				item.getItemProperty(propertyId).setValue(editCellComponent);
	//			}
	//		}
	//	}
	//
	//	@Override
	//	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
	//		setCurrentSelectedCells(new HashSet<Cell>(), null, false);
	//		return super.removeContainerProperty(propertyId);
	//	}
	//
	//	@Override
	//	public boolean removeItem(Object itemId) {
	//		setCurrentSelectedCells(new HashSet<Cell>(), null, true);
	//		return super.removeItem(itemId);
	//	}
	//
	//	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell, boolean propagate) {
	//		cellRowSelector.setCurrentSelectedCells(this, cells, cursorCell, propagate);
	//	}
	//
	//	public void selectRows(Set<Object> rowIds, boolean propagate) {
	//		Set<Cell> rows = new HashSet<Cell>();
	//		for (Object rowId : rowIds) {
	//			for (Object colId : getContainerPropertyIds()) {
	//				Cell tempCell = new Cell(rowId, colId);
	//				rows.add(tempCell);
	//			}
	//		}
	//		setCurrentSelectedCells(rows, null, propagate);
	//	}
	//
	//	/**
	//	 * Updates row content.
	//	 *
	//	 * @param rule
	//	 */
	//	@SuppressWarnings("unchecked")
	//	private void updateItem(TableRuleRow rule) {
	//		Item row = getItem(rule);
	//		List<Expression> expressions = rule.getConditions();
	//		for(int i= 0; i<rule.getConditions().size(); i+=2){
	//			ExpressionEditCell questionValue = ((ExpressionEditCell) row.getItemProperty(i).getValue());
	//			ExpressionEditCell answerValue = ((ExpressionEditCell) row.getItemProperty(i + 1).getValue());
	//			Expression questionExpression = expressions.get(i);
	//			Expression valueExpression = expressions.get(i);
	//
	//			if (questionValue != null) {
	//				questionValue.setLabel(questionExpression.getExpressionTableString());
	//				row.getItemProperty(i).setValue(questionValue);
	//			}
	//			if (answerValue != null) {
	//				answerValue.setLabel(valueExpression.getExpressionTableString());
	//				row.getItemProperty(i + 1).setValue(answerValue);
	//			}
	//		}
	//	}
	//
	//	public ExpressionValueTreeObjectReference getExpression(TableRuleRow row, Object propertyId) {
	//		if (((Integer) propertyId % 2) == 0) {
	//			return (ExpressionValueTreeObjectReference)row.getConditions().get(((Integer) propertyId) / 2);
	//		} else {
	//			return (ExpressionValueTreeObjectReference)row.getConditions().get(((Integer) propertyId - 1) / 2);
	//		}
	//	}
	//
	//	private class CellEditButtonQuestionClickListener implements ClickListener {
	//		private static final long serialVersionUID = -4186477224806988479L;
	//		private ExpressionValueTreeObjectReference questionExpression;
	//		private ExpressionValueTreeObjectReference answerExpression;
	//		private TableRuleRow rule;
	//
	//		public CellEditButtonQuestionClickListener(ExpressionValueTreeObjectReference questionExpression,
	//				TableRuleRow rule) {
	//			this.questionExpression = questionExpression;
	//			this.answerExpression = rule.getAnswerToQuestion(questionExpression);
	//			this.rule = rule;
	//		}
	//
	//		@Override
	//		public void buttonClick(ClickEvent event) {
	//			final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
	//					.getFormController().getForm(), false);
	//
	//			if (questionExpression.getReference() != null) {
	//				newConditionWindow.setTreeObjectSelected(questionExpression.getReference());
	//			}
	//			newConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
	//				@Override
	//				public void acceptAction(AcceptCancelWindow window) {
	//					Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
	//					if ((selectedQuestion == null) || !selectedQuestion.contains(answerExpression.getReference())) {
	//						answerExpression.setReference(null);
	//					}
	//					questionExpression.setReference(selectedQuestion);
	//					updateItem(rule);
	//					newConditionWindow.close();
	//				}
	//			});
	//
	//			newConditionWindow.showCentered();
	//		}
	//	}
	//
	//	private class CellEditButtonAnswerClickListener implements ClickListener {
	//		private static final long serialVersionUID = -1802531580937378464L;
	//		private ExpressionValueTreeObjectReference questionExpression;
	//		private ExpressionValueTreeObjectReference answerExpression;
	//		private TableRuleRow rule;
	//
	//		public CellEditButtonAnswerClickListener(ExpressionValueTreeObjectReference questionExpression,
	//				TableRuleRow rule) {
	//			this.questionExpression = questionExpression;
	//			this.answerExpression = rule.getAnswerToQuestion(questionExpression);
	//			this.rule = rule;
	//		}
	//
	//		@Override
	//		public void buttonClick(ClickEvent event) {
	//			if (questionExpression.getReference() != null) {
	//				final SelectAnswerWindow newAnswerValueWindow = new SelectAnswerWindow((Question) questionExpression.getReference());
	//				if (answerExpression.getReference() != null) {
	//					newAnswerValueWindow.setTreeObjectSelected(answerExpression.getReference());
	//				}
	//				newAnswerValueWindow.addAcceptAcctionListener(new AcceptActionListener() {
	//					@Override
	//					public void acceptAction(AcceptCancelWindow window) {
	//						Answer selectedanswer = ((SelectAnswerWindow) window).getSelectedTableValue();
	//						answerExpression.setReference(selectedanswer);
	//						updateItem(rule);
	//						newAnswerValueWindow.close();
	//					}
	//				});
	//				newAnswerValueWindow.showCentered();
	//			} else {
	//				MessageManager.showError(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
	//						LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
	//			}
	//		}
	//	}
	//
	//	private class CellDeleteButtonQuestionClickListener implements ClickListener {
	//		private static final long serialVersionUID = -4967974394553397046L;
	//		private ExpressionValueTreeObjectReference questionExpression;
	//		private ExpressionValueTreeObjectReference answerExpression;
	//		private TableRuleRow rule;
	//
	//		public CellDeleteButtonQuestionClickListener(ExpressionValueTreeObjectReference questionExpression,
	//				TableRuleRow rule) {
	//			this.questionExpression = questionExpression;
	//			this.answerExpression = rule.getAnswerToQuestion(questionExpression);
	//			this.rule = rule;
	//		}
	//
	//		@Override
	//		public void buttonClick(ClickEvent event) {
	//			questionExpression.setReference(null);
	//			answerExpression.setReference(null);
	//			updateItem(rule);
	//		}
	//	}
	//
	//	private class CellDeleteButtonAnswerClickListener implements ClickListener {
	//		private static final long serialVersionUID = 3353249673597591243L;
	//		private ExpressionValueTreeObjectReference answerExpression;
	//		private TableRuleRow rule;
	//
	//		public CellDeleteButtonAnswerClickListener(ExpressionValueTreeObjectReference questionExpression,
	//				TableRuleRow rule) {
	//			this.answerExpression = rule.getAnswerToQuestion(questionExpression);
	//			this.rule = rule;
	//		}
	//
	//		@Override
	//		public void buttonClick(ClickEvent event) {
	//			answerExpression.setReference(null);
	//			updateItem(rule);
	//		}
	//	}
	//
	//	public void addCellSelectionListener(CellSelectionListener listener) {
	//		cellRowSelector.addCellSelectionListener(listener);
	//	}
	//
	//	public void removeCellSelectionListener(CellSelectionListener listener) {
	//		cellRowSelector.removeCellSelectionListener(listener);
	//	}
	//
	//	private class LayoutClickPropagator implements LayoutClickListener {
	//		private static final long serialVersionUID = 5504698883691497113L;
	//		private Item item;
	//		private Object itemId;
	//		private Object propertyId;
	//
	//		public LayoutClickPropagator(Item item, Object itemId, Object propertyId) {
	//			this.item = item;
	//			this.itemId = itemId;
	//			this.propertyId = propertyId;
	//		}
	//
	//		@Override
	//		public void layoutClick(LayoutClickEvent event) {
	//			MouseEventDetails mouseEvent = new MouseEventDetails();
	//			mouseEvent.setAltKey(event.isAltKey());
	//			mouseEvent.setButton(event.getButton());
	//			mouseEvent.setClientX(event.getClientX());
	//			mouseEvent.setClientY(event.getClientY());
	//			mouseEvent.setCtrlKey(event.isCtrlKey());
	//			mouseEvent.setMetaKey(event.isMetaKey());
	//			mouseEvent.setRelativeX(event.getRelativeX());
	//			mouseEvent.setRelativeY(event.getRelativeY());
	//			mouseEvent.setShiftKey(event.isShiftKey());
	//			if (event.isDoubleClick()) {
	//				// Double click
	//				mouseEvent.setType(0x00002);
	//			} else {
	//				mouseEvent.setType(0x00001);
	//			}
	//			cellRowSelector.itemClick(new ItemClickEvent(thisTable, item, itemId, propertyId, mouseEvent));
	//		}
	//	}
}