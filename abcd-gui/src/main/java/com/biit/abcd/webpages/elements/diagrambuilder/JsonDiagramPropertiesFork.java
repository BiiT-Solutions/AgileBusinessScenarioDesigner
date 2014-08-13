package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionUnit;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.TreeObjectTableSingleSelect;
import com.biit.abcd.webpages.components.WindowSelectDateUnit;
import com.biit.form.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class JsonDiagramPropertiesFork extends PropertiesForClassComponent<DiagramFork> {
	private static final long serialVersionUID = -5767909479835775870L;
	private DiagramFork instance;
	private TreeObjectTableSingleSelect treeObjectTable;
	private ListSelect variableSelection;
	private Button addQuestionButton, addVariableButton;

	public JsonDiagramPropertiesFork() {
		super(DiagramFork.class);
	}

	private void setNewReference(TreeObject treeObjectRefence) {
		instance.setReference(new ExpressionValueTreeObjectReference(treeObjectRefence));
		firePropertyUpdateListener(instance);
	}

	private void setNewDateReference(TreeObject reference, QuestionUnit dateUnit) {
		instance.setReference(new ExpressionValueTreeObjectReference(reference, dateUnit));
		firePropertyUpdateListener(instance);
	}

	private void setNewReferenceCustomVariable(TreeObject treeObjectRefence, CustomVariable variable) {
		instance.setReference(new ExpressionValueCustomVariable(treeObjectRefence, variable));
		firePropertyUpdateListener(instance);
	}

	private void setNewDateReferenceCustomVariable(TreeObject reference, CustomVariable variable, QuestionUnit dateUnit) {
		instance.setReference(new ExpressionValueCustomVariable(reference, variable, dateUnit));
		firePropertyUpdateListener(instance);
	}

	@Override
	public void setElementAbstract(DiagramFork element) {
		instance = element;

		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		initializeFormQuestionTable();
		addQuestionButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_ELEMENT));
		addQuestionButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1427800999596843191L;

			@Override
			public void buttonClick(ClickEvent event) {
				final TreeObject reference = (TreeObject) treeObjectTable.getValue();

				if (reference != null && (reference instanceof Question)) {
					if (((Question) reference).getAnswerFormat() == AnswerFormat.DATE) {
						// Create a window for selecting the unit and assign it
						// to the expression.
						final WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
								.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));
						windowDate.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								QuestionUnit unit = windowDate.getValue();
								setNewDateReference(reference, unit);
								window.close();
							}
						});
						windowDate.showCentered();
					} else {
						setNewReference(reference);
					}
				} else {
					MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
				}
			}
		});
		initializeVariableSelection();
		addVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_VARIABLE));
		addVariableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3314196233359245226L;

			@Override
			public void buttonClick(ClickEvent event) {
				final TreeObject reference = (TreeObject) treeObjectTable.getValue();
				final CustomVariable variable = (CustomVariable) variableSelection.getValue();
				if (variable != null) {
					if (variable.getType() == CustomVariableType.DATE) {
						// Create a window for selecting the unit and assign it
						// to the expression.
						final WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
								.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));

						windowDate.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								QuestionUnit unit = windowDate.getValue();
								setNewDateReferenceCustomVariable(reference, variable, unit);
								window.close();
							}
						});
						windowDate.showCentered();
					} else {
						setNewReferenceCustomVariable(reference, variable);
					}
				} else {
					MessageManager.showError(LanguageCodes.ERROR_SELECT_VARIABLE);
				}
			}
		});

		layout.addComponent(treeObjectTable);
		layout.setExpandRatio(treeObjectTable, 0.5f);
		layout.addComponent(addQuestionButton);
		layout.setComponentAlignment(addQuestionButton, Alignment.TOP_RIGHT);
		layout.addComponent(variableSelection);
		layout.setExpandRatio(variableSelection, 0.5f);
		layout.addComponent(addVariableButton);
		layout.setComponentAlignment(addVariableButton, Alignment.TOP_RIGHT);

		addTab(layout, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORK_NODE_CAPTION), true, 0);
	}

	private void initializeFormQuestionTable() {
		treeObjectTable = new TreeObjectTableSingleSelect();
		treeObjectTable.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_ELEMENTS));
		treeObjectTable.setSizeFull();
		treeObjectTable.setRootElement((Form) UserSessionHandler.getFormController().getForm());
		treeObjectTable.setSelectable(true);
		treeObjectTable.setNullSelectionAllowed(false);
		treeObjectTable.setImmediate(true);
		treeObjectTable.setValue(UserSessionHandler.getFormController().getForm());
		treeObjectTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setFormVariableSelectionValues();
			}
		});
		treeObjectTable.collapseFrom(Category.class);
		treeObjectTable.setPageLength(10);
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection
				.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	private void setFormVariableSelectionValues() {
		if (variableSelection != null) {
			variableSelection.setValue(null);
			variableSelection.removeAllItems();
			if (treeObjectTable.getValue() != null) {
				List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
						.getCustomVariables((TreeObject) treeObjectTable.getValue());
				for (CustomVariable customvariable : customVariables) {
					variableSelection.addItem(customvariable);
					variableSelection.setItemCaption(customvariable, customvariable.getName());
				}
				if (customVariables != null && !customVariables.isEmpty()) {
					variableSelection.setValue(customVariables.get(0));
				}
			}
		}
	}

	@Override
	public void updateElement() {
		// All the updates are done in the field directly.
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}