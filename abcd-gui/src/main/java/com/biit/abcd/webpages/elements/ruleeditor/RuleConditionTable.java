package com.biit.abcd.webpages.elements.ruleeditor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.elements.decisiontable.Cell;
import com.biit.abcd.webpages.elements.decisiontable.CellRowSelector;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class RuleConditionTable extends Table {
	private static final long serialVersionUID = 6488012179627542320L;
	private CellRowSelector cellRowSelector;

	public RuleConditionTable() {
		setImmediate(true);
		setSizeFull();
		setSelectable(true);

		addContainerProperty("Question", String.class, null);
		addContainerProperty("Condition", TextField.class, null);
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

	@SuppressWarnings("unchecked")
	public void addItem(Question question) {
		if (question != null) {
			Object newItemId = addItem();
			Item row1 = getItem(newItemId);
			row1.getItemProperty("Question").setValue(question.getName());
			if (question.getChildren().size() > 0) {
				try {
					TextField condition = new TextField();
					condition.setValue("= " + question.getChild(0));
					row1.getItemProperty("Condition").setValue(condition);
				} catch (com.vaadin.data.Property.ReadOnlyException | ChildrenNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
