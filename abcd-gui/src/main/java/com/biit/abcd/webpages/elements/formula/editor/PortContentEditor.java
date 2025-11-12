package com.biit.abcd.webpages.elements.formula.editor;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.util.Set;

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

public class PortContentEditor extends AcceptCancelWindow {
	private static final long serialVersionUID = 4460942893259168920L;

	private TabSheet rootTabSheet;
	private Tab comparisonExpressionTab;
	private Tab logicExpressionTab;
	private Tab operationExpressionTab;
	private Tab assignationExpressionTab;

	public PortContentEditor(Set<Type> contentTypesToGenerate) {
		super();
		setModal(true);
		setResizable(false);
		setWidth("50%");
		setHeight("50%");
		center();

		Component content = generateContent(contentTypesToGenerate);
		setContent(content);
	}

	private Component generateContent(Set<Type> contentTypesToGenerate) {
		rootTabSheet = new TabSheet();
		rootTabSheet.setSizeFull();

		comparisonExpressionTab = rootTabSheet.addTab(new ComparisonExpressionComponent("Comparison Expression"));
		logicExpressionTab = rootTabSheet.addTab(new LogicExpressionComponent("Logic Expression"));
		operationExpressionTab = rootTabSheet.addTab(new OperationExpressionComponent("Calculation Expression"));
		assignationExpressionTab = rootTabSheet.addTab(new AsssignationExpressionComponent("Assignation Expression"));
		comparisonExpressionTab.setEnabled(false);
		logicExpressionTab.setEnabled(false);
		operationExpressionTab.setEnabled(false);
		assignationExpressionTab.setEnabled(false);

		for (Type type : contentTypesToGenerate) {
			switch (type) {
			case VOID:
				// Empty value, do nothing
				break;
			case COMPARISON:
				comparisonExpressionTab.setEnabled(true);
				break;
			case LOGIC:
				logicExpressionTab.setEnabled(true);
				break;
			case ASSIGNATION:
				assignationExpressionTab.setEnabled(true);
				break;
			case CALCULATION:
				operationExpressionTab.setEnabled(true);
				break;
			case TREE_OBJECT_REFERENCE:
				// Get or create TreeObject reference tab
				break;
			case EXPRESSION:
				// Get or create expression tab
				break;
			default:
				break;
			}
		}
		return rootTabSheet;
	}

	protected interface IValueFormulaExpression {
		public FormulaExpressionComponent getValue();
	}

	private class ExpressionComponent extends CustomComponent implements IValueFormulaExpression {
		private static final long serialVersionUID = 542080054076192679L;
		protected ListSelect expressions;

		public ExpressionComponent(String caption) {
			super();
			setCaption(caption);
			VerticalLayout rootLayout = new VerticalLayout();
			expressions = new ListSelect();
			expressions.setMultiSelect(false);

			rootLayout.addComponent(expressions);
			setCompositionRoot(rootLayout);
			setSizeUndefined();
		}

		public void addExpressions(Set<FormulaExpresion> expressionSet) {
			for (FormulaExpresion expression : expressionSet) {
				expressions.addItem(expression);
				expressions.setItemCaption(expression, expression.getExpresionClass().getSimpleName());
			}
		}

		@Override
		public FormulaExpressionComponent getValue() {
			return ((FormulaExpresion) expressions.getValue()).getNewFormulaExpressionComponent();
		}
	}

	private class ComparisonExpressionComponent extends ExpressionComponent {
		private static final long serialVersionUID = 751749241918623310L;

		public ComparisonExpressionComponent(String caption) {
			super(caption);
			addExpressions(FormulaExpresion.getComparationExpression());
		}
	}

	private class OperationExpressionComponent extends ExpressionComponent {
		private static final long serialVersionUID = -1741785614444240259L;

		public OperationExpressionComponent(String caption) {
			super(caption);
			addExpressions(FormulaExpresion.getOperationExpression());
		}
	}

	private class LogicExpressionComponent extends ExpressionComponent {
		private static final long serialVersionUID = 9015785082300605997L;

		public LogicExpressionComponent(String caption) {
			super(caption);
			addExpressions(FormulaExpresion.getLogicExpression());
		}
	}

	private class AsssignationExpressionComponent extends ExpressionComponent {
		private static final long serialVersionUID = 654486198531451386L;

		public AsssignationExpressionComponent(String caption) {
			super(caption);
			addExpressions(FormulaExpresion.getAssignationExpression());
		}
	}

	public FormulaExpressionComponent getValue() {
		Component selectedComponent = rootTabSheet.getSelectedTab();
		
		if(selectedComponent instanceof ExpressionComponent){
			return ((ExpressionComponent)selectedComponent).getValue();
		}
		// return ((FormulaExpresion)
		// comboBox.getValue()).getNewFormulaExpressionComponent();
		return null;
	}
}
