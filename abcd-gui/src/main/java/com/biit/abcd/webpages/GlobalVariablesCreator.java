package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
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
				VariableWindow window = new VariableWindow(ServerTranslate
						.translate(LanguageCodes.GLOBAL_VARIABLE_ADD_WINDOW_TITLE));
				window.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						GlobalVariable value = ((VariableWindow) window).getValue();
						if (value != null) {
							variableTable.addItem(value);
							variableTable.setValue(value);
						}
						window.close();
					}
				});
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = -1957065660286348445L;
					@Override
					public void windowClose(CloseEvent e) {
						createDataWindow();						
					}
				});
				
				window.showCentered();
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
		upperMenu.addAddValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2087675518126425145L;

			@Override
			public void buttonClick(ClickEvent event) {
				final GlobalVariable variable = variableTable.getSelectedGlobalVariable();

				if (variable != null) {
					createDataWindow();
				}
			}
		});
		upperMenu.addRemoveValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9099439032797611211L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object selectedVariable = variableDataTable.getValue();
				if (selectedVariable != null) {
					variableDataTable.removeItem(selectedVariable);
					((GlobalVariable) variableTable.getValue()).getData().remove(selectedVariable);
				} else {
					MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
							LanguageCodes.WARNING_SELECT_VARIABLE_DATA_TO_DELETE);
				}
			}
		});
		return upperMenu;
	}

	private void createDataWindow(){
		final GlobalVariable variable = variableTable.getSelectedGlobalVariable();

		if (variable != null) {
			VariableDataWindow variableDataWindow = new VariableDataWindow(variable.getFormat(),
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE_ADD_WINDOW_TITLE));
			variableDataWindow.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					VariableData variableData = ((VariableDataWindow) window).getValue();
					if (variableData != null) {
						// Add item.
						variableDataTable.addItem((VariableData) variableData);
						((GlobalVariable) variableTable.getValue()).getData().add(variableData);
					}
					window.close();
				}
			});
			variableDataWindow.showCentered();
		}
	}
	
	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	private void save() {
		UserSessionHandler.getGlobalVariablesController().update(variableTable.getGlobalVariables());
	}

}
