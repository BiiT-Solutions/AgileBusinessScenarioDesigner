package com.biit.abcd.webpages;

import java.util.Date;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.utils.DateManager;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.abcd.webpages.components.VariableDataWindow;
import com.biit.abcd.webpages.components.VariableWindow;
import com.biit.abcd.webpages.elements.globalvariables.GlobalVariablesTable;
import com.biit.abcd.webpages.elements.globalvariables.GlobalVariablesUpperMenu;
import com.biit.abcd.webpages.elements.globalvariables.VariableDataTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class GlobalVariablesCreator extends FormWebPageComponent {
	private static final long serialVersionUID = 6042328256995069412L;

	private HorizontalLayout rootLayout;
	private GlobalVariablesTable variableTable;
	private VariableDataTable variableDataTable;

	public GlobalVariablesCreator() {
		super();
	}

	@Override
	protected void initContent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		variableTable = new GlobalVariablesTable();
		variableDataTable = new VariableDataTable();

		variableTable.setSizeFull();
		variableDataTable.setSizeFull();

		rootLayout.addComponent(variableTable);
		rootLayout.addComponent(variableDataTable);

		getWorkingAreaLayout().addComponent(rootLayout);
		setUpperMenu(createUpperMenu());

		variableTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8155028206136931686L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				variableDataTable.removeAllItems();
				variableDataTable.setVariable((GlobalVariable) variableTable.getValue());
			}
		});

		// Add already existing GlobalVariables.
		for (GlobalVariable globalVariable : UserSessionHandler.getGlobalVariablesController().getGlobalVariables()) {
			variableTable.addItem(globalVariable);
		}
	}

	private UpperMenu createUpperMenu() {
		GlobalVariablesUpperMenu upperMenu = new GlobalVariablesUpperMenu();
		upperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3692380302089994511L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});
		upperMenu.addAddVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4843889679428803021L;

			@Override
			public void buttonClick(ClickEvent event) {
				createVariableWindow();
			}
		});
		upperMenu.addRemoveVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -6942516382918522958L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object selectedVariable = variableTable.getValue();
				if (selectedVariable != null) {
					variableTable.removeItem(selectedVariable);
				} else {
					MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
							LanguageCodes.WARNING_SELECT_VARIABLE_TO_DELETE);
				}
			}
		});

		upperMenu.addEditVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6386920185257203923L;

			@Override
			public void buttonClick(ClickEvent event) {
				createEditVariableWindow();
			}
		});

		upperMenu.addAddValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2087675518126425145L;

			@Override
			public void buttonClick(ClickEvent event) {
				createValueDataWindow();
			}
		});
		upperMenu.addRemoveValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9099439032797611211L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object selectedVariable = variableDataTable.getValue();
				if (selectedVariable != null) {
					// Check if the value is the last of the list, if not, it
					// can't be deleted
					if (isLastValueOfVariableList(((GlobalVariable) variableTable.getValue()).getData(),
							selectedVariable)) {
						variableDataTable.removeItem(selectedVariable);
						((GlobalVariable) variableTable.getValue()).getData().remove(selectedVariable);
					} else {
						MessageManager.showWarning(LanguageCodes.WARNING_SELECT_VARIABLE_DATA_INVALID_TITLE,
								LanguageCodes.WARNING_SELECT_VARIABLE_DATA_INVALID);
					}
				} else {
					MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
							LanguageCodes.WARNING_SELECT_VARIABLE_DATA_TO_DELETE);
				}
			}
		});

		upperMenu.addEditValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -589339616429194896L;

			@Override
			public void buttonClick(ClickEvent event) {
				createEditValueDataWindow();
			}
		});

		return upperMenu;
	}

	private void createVariableWindow() {
		VariableWindow window = new VariableWindow(
				ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_ADD_WINDOW_TITLE));
		window.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				GlobalVariable value = ((VariableWindow) window).getValue();
				if (value != null) {
					variableTable.addItem(value);
					variableTable.setValue(value);
				}
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has created a " + value.getClass() + " with 'Name: " + value.getName() + " - Type: "
						+ value.getFormat() + "'.");
				window.close();
			}
		});
		window.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = -1957065660286348445L;

			@Override
			public void windowClose(CloseEvent e) {
				createValueDataWindow();
			}
		});

		window.showCentered();
	}

	private void createEditVariableWindow() {
		final GlobalVariable variable = variableTable.getSelectedGlobalVariable();

		if (variable != null) {
			VariableWindow window = new VariableWindow(
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_EDIT_WINDOW_TITLE));
			window.setValue(variable);
			window.disableTypeEdition();

			window.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					GlobalVariable editedVariable = ((VariableWindow) window).getValue();
					if (editedVariable != null) {
						// Update the internal value
						variable.updateValues(editedVariable);
						// Update the view of the value
						variableTable.updateItem(variable);
					}
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has edited a " + variable.getClass()
							+ " with 'Name: " + variable.getName() + " - Type: " + variable.getFormat() + "'.");
					window.close();
				}
			});
			window.showCentered();
		}
	}

	private void createValueDataWindow() {
		final GlobalVariable variable = variableTable.getSelectedGlobalVariable();

		if (variable != null) {
			// Check if the previous value has "value to" set to infinite
			if (isLastValidToInfinite(variable)) {
				MessageManager.showWarning(LanguageCodes.WARNING_SELECT_VARIABLE_DATA_INFINITE_VALUE_TITLE,
						LanguageCodes.WARNING_SELECT_VARIABLE_DATA_INFINITE_VALUE);
			} else {
				VariableDataWindow variableDataWindow = new VariableDataWindow(variable.getFormat(),
						ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE_ADD_WINDOW_TITLE));

				// If the global variable has values, we manage the
				// "value from"/"value to" properties
				List<VariableData> auxVariableList = ((GlobalVariable) variableTable.getValue()).getData();
				if (auxVariableList.size() > 0) {
					// Set the "value from" value related to the previous one
					VariableData auxVariableData = auxVariableList.get(auxVariableList.size() - 1);
					Date auxDate = new Date(auxVariableData.getValidTo().getTime());
					variableDataWindow.setValidFromValue(DateManager.incrementDateOneDay(auxDate));
					// Disable the "value from" data field
					variableDataWindow.setValidFromEditable(false);
				}

				variableDataWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						VariableData variableData = ((VariableDataWindow) window).getValue();
						if (variableData != null) {
							// Add item.
							variableDataTable.addItem(variableData);
							((GlobalVariable) variableTable.getValue()).getData().add(variableData);
						}
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has created a "
										+ variableData.getClass() + " with 'Value: " + variableData.getValue()
										+ " - Valid from: " + variableData.getValidFrom() + " - Valid to: "
										+ variableData.getValidTo() + "'.");
						window.close();
					}
				});
				variableDataWindow.showCentered();
			}
		}
	}

	private void createEditValueDataWindow() {
		final GlobalVariable variable = variableTable.getSelectedGlobalVariable();

		if (variable != null) {
			final VariableData selectedValue = variableDataTable.getSelectedVariableData();
			if (selectedValue != null) {

				VariableDataWindow variableDataWindow = new VariableDataWindow(variable.getFormat(),
						ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE_EDIT_WINDOW_TITLE));

				variableDataWindow.setValue(selectedValue);
				variableDataWindow.disableValueFromTo();

				variableDataWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						VariableData editedVariable = ((VariableDataWindow) window).getValue();
						if (editedVariable != null) {
							try {
								// Update the internal value
								if (selectedValue.updateValues(editedVariable)) {
									// Update the view of the value
									variableDataTable.updateItem(selectedValue);
								}
							} catch (NotValidTypeInVariableData e) {
								MessageManager.showError(e.getMessage());
							}
						}
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has edited a "
										+ selectedValue.getClass() + " with 'Value: " + selectedValue.getValue()
										+ " - Valid from: " + selectedValue.getValidFrom() + " - Valid to: "
										+ selectedValue.getValidTo() + "'.");
						window.close();
					}
				});
				variableDataWindow.showCentered();
			}
		}
	}

	private boolean isLastValueOfVariableList(List<VariableData> variableDataList, Object selectedVariable) {
		int variableListSize = variableDataList.size();
		int valuePosition = variableDataList.indexOf(selectedVariable);
		if (variableListSize == (valuePosition + 1)) {
			return true;
		}
		return false;
	}

	private boolean isLastValidToInfinite(GlobalVariable variable) {
		List<VariableData> auxVariableList = ((GlobalVariable) variableTable.getValue()).getData();
		if (auxVariableList.size() > 0) {
			VariableData auxVariableData = auxVariableList.get(auxVariableList.size() - 1);
			if (auxVariableData.getValidTo() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	private void save() {
		UserSessionHandler.getGlobalVariablesController().update(variableTable.getGlobalVariables());
		MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
	}
}
