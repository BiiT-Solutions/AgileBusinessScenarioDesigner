package com.biit.abcd.webpages.elements.decisiontable;

import java.util.List;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.FormTreeTableCellStyleGenerator;
import com.vaadin.ui.Table;

public class FormQuestionTableCellGenerator extends FormTreeTableCellStyleGenerator {
	private static final long serialVersionUID = -5325256862646100074L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		FormQuestionTable formQuestionTable = (FormQuestionTable) source;
		List<Question> disabledQuestions = formQuestionTable.getDisabledQuestions();
		
		if(disabledQuestions.contains(itemId)){
			return "cell-disabled";
		}else{		
			return super.getStyle(source, itemId, propertyId);
		}		
	}
}
