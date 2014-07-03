package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectTableWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesTable extends PropertiesForClassComponent<DiagramTable> {
	private static final long serialVersionUID = -4698686607497943878L;
	private DiagramTable instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesTable() {
		super(DiagramTable.class);
	}

	@Override
	protected void setElementAbstract(DiagramTable element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton("Table");
		fieldWithSearchButton.setNullCaption("Table");
		fieldWithSearchButton.setValue(null);
		if (instance.getTable() != null) {
			fieldWithSearchButton.setValue(instance.getTable(), instance.getTable().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2075223046207815648L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectTableWindow tableWindow = new SelectTableWindow();
				tableWindow.addAcceptAcctionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (tableWindow.getSelectedTableRule() != null) {
							fieldWithSearchButton.setValue(tableWindow.getSelectedTableRule(), tableWindow
									.getSelectedTableRule().getName());
							instance.setTable(tableWindow.getSelectedTableRule());
							firePropertyUpdateListener(instance);
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
						}
					}
				});
				tableWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -3314196233359245226L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setTable(null);
				firePropertyUpdateListener(instance);
			}
		});

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(fieldWithSearchButton);

		addTab(categoryForm, "TODO - JsonDiagramProperties Table", true, 0);
	}

	@Override
	protected void updateElement() {
		//All the updates are done in the field action directly.
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}
