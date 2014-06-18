package com.biit.abcd.webpages.elements.expressiontree.expression;

import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.abcd.webpages.elements.decisiontable.FormAnswerTable;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LogicValueWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -343361729638805865L;
	private static final String LAYOUT_WIDTH = "300px";
	private static final String BUTTON_WIDTH = "150px";

	private Button equals, notEquals, in, between, lt, gt, le, ge;
	private VerticalLayout leftLayout, rightLayout, buttonLayout;
	private FormQuestionTable formQuestionTable;
	
	public String value;

	private Form form;

	public LogicValueWindow(Form form) {
		this.form = form;
		setContent(generateContent());
		setWidth("50%");
		setHeight("50%");
		setResizable(false);
		setModal(true);
		setDraggable(false);
	}

	public Component generateContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		leftLayout = new VerticalLayout();
		leftLayout.setHeight("100%");
		setLayoutCommonProperties(leftLayout);
		leftLayout.addComponent(initializeFormSelect());

		rightLayout = new VerticalLayout();
		setLayoutCommonProperties(rightLayout);

		rootLayout.addComponent(leftLayout);
		rootLayout.addComponent(generateButtonControlLayout());
		rootLayout.addComponent(rightLayout);

		rootLayout.setExpandRatio(leftLayout, 0.5f);
		rootLayout.setExpandRatio(rootLayout.getComponent(1), 0.0f);
		rootLayout.setExpandRatio(rightLayout, 0.5f);

		return rootLayout;
	}

	private Component initializeFormSelect() {
		System.out.println("initialization-form-select: " + form);
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setRootElement(form);
		formQuestionTable.setSizeFull();
		formQuestionTable.setSelectable(true);
		formQuestionTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5136968822484230250L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				rightLayout.removeAllComponents();
				if(formQuestionTable.isElementFiltered(event.getProperty().getValue())){
					buttonLayout.setEnabled(false);
					value = new String()+event.getProperty().getValue();
				}else{
					buttonLayout.setEnabled(true);
					value = null;
				}
			}
		});

		return formQuestionTable;
	}

	private void setLayoutCommonProperties(AbstractLayout layout) {
		layout.setHeight("100%");
		layout.setWidth(LAYOUT_WIDTH);
	}

	// public Component generate

	public Component generateButtonControlLayout() {
		buttonLayout = new VerticalLayout();
		buttonLayout.setSizeUndefined();
		buttonLayout.setImmediate(true);

		equals = new Button("==", new ClickListener() {
			private static final long serialVersionUID = -5416348199481011369L;

			@Override
			public void buttonClick(ClickEvent event) {
				rightLayout.removeAllComponents();
				rightLayout.addComponent(generateAssignComponent());
				value+="==";
			}
		});
		equals.setWidth(BUTTON_WIDTH);
		notEquals = new Button("!=", new ClickListener() {
			private static final long serialVersionUID = -8694935914521670453L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		notEquals.setWidth(BUTTON_WIDTH);
		in = new Button("IN", new ClickListener() {
			private static final long serialVersionUID = -424144236022517724L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		in.setWidth(BUTTON_WIDTH);
		between = new Button("BETWEEN", new ClickListener() {
			private static final long serialVersionUID = 5019566205212092519L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		between.setWidth(BUTTON_WIDTH);
		lt = new Button("<", new ClickListener() {
			private static final long serialVersionUID = 6363491202148612210L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		gt = new Button(">", new ClickListener() {
			private static final long serialVersionUID = -1501775483554899749L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		le = new Button("<=", new ClickListener() {
			private static final long serialVersionUID = -7829456771123932402L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		ge = new Button(">=", new ClickListener() {
			private static final long serialVersionUID = -7823269914618304196L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

		buttonLayout.addComponent(equals);
		buttonLayout.addComponent(notEquals);
		buttonLayout.addComponent(lt);
		buttonLayout.addComponent(gt);
		buttonLayout.addComponent(le);
		buttonLayout.addComponent(ge);
		buttonLayout.addComponent(in);
		buttonLayout.addComponent(between);

		return buttonLayout;
	}

	private Component generateAssignComponent() {
		Question question = (Question) formQuestionTable.getValue();
		if (question.getAnswerType() == AnswerType.RADIO || question.getAnswerType() == AnswerType.MULTI_CHECKBOX) {
			FormAnswerTable answerTable = new FormAnswerTable();
			answerTable.setSizeFull();
			answerTable.setSelectable(true);
			return answerTable;
		}

		TextField textField = new TextField("Value");
		textField.setWidth("100%");
		textField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8473885385116301183L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				value += event.getProperty().getValue();
			}
		});
		return textField;

	}

}
