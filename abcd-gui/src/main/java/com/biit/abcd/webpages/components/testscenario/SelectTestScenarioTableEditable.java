package com.biit.abcd.webpages.components.testscenario;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.EditCellComponent;
import com.biit.abcd.webpages.components.TableCellLabelEdit;
import com.biit.abcd.webpages.components.TableCellLabelEditWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SelectTestScenarioTableEditable extends TableCellLabelEdit {
	private static final long serialVersionUID = 864606847528876831L;

	public SelectTestScenarioTableEditable() {
		super(LanguageCodes.TEST_SCENARIOS_TABLE_COLUMN_NAME, LanguageCodes.TEST_SCENARIOS_TABLE_COLUMN_UPDATE);
	}

	public TestScenario getSelectedTestScenario() {
		return (TestScenario) getValue();
	}

	public void setSelectedTestScenario(TestScenario expression) {
		setValue(expression);
	}

	protected EditCellComponent setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		EditCellComponent editCellComponent = super.setDefaultNewItemPropertyValues(itemId, item);
		if (editCellComponent != null) {
			editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((TestScenario) itemId));
		}
		return null;
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = 2470467428282560338L;
		private TestScenario testScenario;

		public CellEditButtonClickListener(TestScenario formExpression) {
			this.testScenario = formExpression;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final TableCellLabelEditWindow newTableCellEditWindow = new TableCellLabelEditWindow(
					ServerTranslate.translate(LanguageCodes.WINDOW_EDIT_TABLE_CELL_LABEL));

			newTableCellEditWindow.setValue(testScenario.getName());
			newTableCellEditWindow.showCentered();
			newTableCellEditWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					for (TestScenario existingTestScenario : UserSessionHandler.getTestScenariosController()
							.getTestScenarios()) {
						if (existingTestScenario != testScenario
								&& existingTestScenario.getName().equals(newTableCellEditWindow.getValue())) {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_TEST_SCENARIO_NAME);
							return;
						}
					}
					try {
						testScenario.setName(newTableCellEditWindow.getValue());
						testScenario.setUpdateTime();
						updateItemTableRuleInGui(testScenario);
						newTableCellEditWindow.close();
					} catch (FieldTooLongException e) {
						MessageManager.showWarning(ServerTranslate.translate(LanguageCodes.WARNING_TITLE),
								ServerTranslate.translate(LanguageCodes.TEST_SCENARIOS_WARNING_NAME_TOO_LONG));
					}
				}
			});
		}
	}

	public void updateTable(List<TestScenario> testScenarios) {
		this.removeAllItems();
		for (TestScenario value : testScenarios) {
			addRow(value);
		}
	}

	public List<TestScenario> getTestScenarios() {
		List<TestScenario> testScenarios = new ArrayList<>();
		for (Object item : getItemIds()) {
			testScenarios.add((TestScenario) item);
		}
		return testScenarios;
	}
}
