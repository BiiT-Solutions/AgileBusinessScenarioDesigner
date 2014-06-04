package com.biit.abcd.webpages.elements.ruleeditor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.elements.decisiontable.CellRowSelector;
import com.biit.abcd.webpages.elements.decisiontable.CellRowSelector.Cell;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class RuleConditionTable extends Table {
	private static final long serialVersionUID = 6488012179627542320L;
	private CellRowSelector cellRowSelector;

	public RuleConditionTable() {
		setImmediate(true);
		setSizeFull();
		setSelectable(true);

		addContainerProperty("Question", String.class, null);
		addContainerProperty("Condition", String.class, null);
	}

	public Collection<Question> getSelectedQuestions() {
		Set<Question> questions = new HashSet<Question>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			questions.add((Question) cell.getCol());
		}
		return questions;
	}

	public Collection<TableRule> getSelectedRules() {
		Set<TableRule> rules = new HashSet<TableRule>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			rules.add((TableRule) cell.getRow());
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
					row1.getItemProperty("Condition").setValue("= " + question.getChild(0));
				} catch (com.vaadin.data.Property.ReadOnlyException | ChildrenNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
