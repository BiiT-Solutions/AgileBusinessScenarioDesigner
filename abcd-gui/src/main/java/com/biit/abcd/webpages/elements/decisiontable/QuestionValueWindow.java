package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;

public class QuestionValueWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -8664839720728561970L;
	
//	public QuestionValueWindow(DecisionRule) {
//		super();
//		
//		setModal(true);
//		setResizable(false);
//		setWidth("50%");
//		setHeight("50%");
//		
//	}
	
	private class TextQuestionEditor extends CustomComponent{
		
		private TextField textField;
		
		public TextQuestionEditor() {
			setCompositionRoot(textField);
		}
		
	}
	
	private class SingleAnswerQuestionEditor extends CustomComponent{
		
	}
	
	private class MultipleAnswerQuestionEditor extends CustomComponent{
		
	}

}
