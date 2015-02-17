package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class AddNewConditionExpressionWindow extends AcceptCancelWindow{

	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.AddNewConditionExpressionWindow";

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}
	
	public TreeTableElement getTable(){
		return $(TreeTableElement.class).first();
	}
	
	public void selectAnswer(int row){
		getTable().getCell(row, 0).click();
		getTable().getCell(row, 0).waitForVaadin();
		clickAccept();
	}
	
	public CssLayoutElement getToken(int pos){
		return getWindow().$(HorizontalLayoutElement.class).$$(CssLayoutElement.class).get(pos);
	}
	
	public boolean isTokenDisabled(int pos){
		return getToken(pos).getAttribute("class").contains("v-csslayout-expression-disabled");
	}

}
