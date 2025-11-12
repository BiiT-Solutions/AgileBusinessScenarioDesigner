package com.biit.abcd.gui.test.window;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
