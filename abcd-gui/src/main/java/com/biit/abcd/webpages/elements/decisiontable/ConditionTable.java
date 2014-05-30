package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.AnswerCondition;
import com.biit.abcd.persistence.entity.rules.TableRule;
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

	private CellRowSelector cellRowSelector;

	public ConditionTable() {
		setImmediate(true);
		setSizeFull();
		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
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
		for (Object object : cellRowSelector.getSelectedPropertiesId()) {
			questions.add((Question) object);
		}
		return questions;
	}

	public Collection<TableRule> getSelectedRules() {
		Set<TableRule> rules = new HashSet<TableRule>();
		for (Object object : cellRowSelector.getSelectedItemsId()) {
			rules.add((TableRule) object);
		}
		return rules;
	}

	public void addItem(TableRule rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
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
						(TableRule) itemId));
				editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonClickListener((Question) propertyId,
						(TableRule) itemId));
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateItem(TableRule rule) {
		Item row = getItem(rule);
		for (Question question : rule.getConditions().keySet()) {
			QuestionValueEditCell questionValue = ((QuestionValueEditCell) row.getItemProperty(question).getValue());
			questionValue.setLabel(rule.getConditions().get(question).toString());
			row.getItemProperty(question).setValue(questionValue);
		}
	}

	public class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private Question question;
		private TableRule rule;

		public CellEditButtonClickListener(Question question, TableRule rule) {
			this.question = question;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			AddNewAnswerValue newAnswerValue = new AddNewAnswerValue(question);
			newAnswerValue.showCentered();
			newAnswerValue.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					Answer selectedAnswer = ((AddNewAnswerValue) window).getSelectedTableValue();
					if (selectedAnswer != null) {
						rule.putCondition(question, new AnswerCondition(selectedAnswer));
						updateItem(rule);
					}
				}
			});
		}
	}

	public class CellDeleteButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -7125934888135148456L;
		private Question question;
		private TableRule rule;

		public CellDeleteButtonClickListener(Question question, TableRule rule) {
			this.question = question;
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			rule.removeCondition(question);
		}
	}
}
