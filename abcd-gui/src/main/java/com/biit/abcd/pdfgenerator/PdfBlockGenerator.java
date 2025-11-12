package com.biit.abcd.pdfgenerator;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfRow;
import com.biit.abcd.pdfgenerator.utils.PdfTableBlock;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;

/**
 * This class generates PdfTableBlock from different kinds of elements in the
 * form
 *
 */
public class PdfBlockGenerator {

	public static final int STRUCTURE_COLS = 4;
	public static final int GROUP_ROWS = 1;
	public static final int QUESTION_ROW = 1;
	public static final int EMPTY_BLOCK_ROWS = 1;
	public static final int MIN_GROUP_ROWS = 1;
	public static final int MIN_QUESTION_ROWS = 1;
	public static final int MIN_ANSWER_ROWS = 1;
	private static final int MIN_VARIABLE_ROWS = 1;
	private static final int VARIABLE_COLS = 4;
	private static final int MIN_EXPRESSION_ROWS = 4;
	private static final int MIN_EXPRESSION_COLS = 1;
	private static final int MIN_EMPTY_ROW = 2;
	private static final int MIN_RULE_CONDITION_ROWS = 3;
	private static final int MIN_RULE_ACTION_ROWS = 4;
	private static final int MIN_RULE_COLS = 1;

	private static PdfTableBlock generateEmptyBlock() {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(EMPTY_BLOCK_ROWS, STRUCTURE_COLS);
			block.insertRow(PdfRowGenerator.generateEmptyRow(EMPTY_BLOCK_ROWS, STRUCTURE_COLS));
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	private static PdfTableBlock generateAnnexGroupTableBlock(BaseGroup group) {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(MIN_GROUP_ROWS + getNumberOfChildsAndAnswers(group), STRUCTURE_COLS);

			block.insertRow(PdfRowGenerator.generateStructureGroupRoot(group));

			if (!group.getChildren().isEmpty()) {
				for (TreeObject child : group.getChildren()) {
					if (child instanceof Question) {
						generateAndAddQuestion(block, (Question) child);
					} else {
						generateAndAddGroup(block, (BaseGroup) child);
					}
				}
			}
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	private static int getNumberOfChildsAndAnswers(BaseGroup group) {
		int total = group.getChildren().size();
		for (TreeObject question : group.getChildren(BaseQuestion.class)) {
			total += question.getAllChildrenInHierarchy(BaseAnswer.class).size();
		}
		return total;
	}

	private static void generateAndAddGroup(PdfTableBlock block, BaseGroup child) throws BadBlockException {
		block.insertRow(PdfRowGenerator.generateStructureGroup(child));
	}

	private static void generateAndAddQuestion(PdfTableBlock block, Question question) throws BadBlockException {
		PdfRow row = PdfRowGenerator.generateQuestion(question);
		block.insertRow(row);

		if (!question.getChildren().isEmpty()) {
			for (TreeObject child : question.getChildren()) {
				// They are all answers
				block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) child));
				for (TreeObject subChild : child.getChildren()) {
					block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) subChild));
				}
			}
		}
	}

	public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<BaseGroup> treeObjects = new ArrayList<>(form.getAll(BaseGroup.class));

		for (TreeObject object : treeObjects) {
			if (!object.isHiddenElement()) {
				if (!generateAnnexGroupTableBlock((BaseGroup) object).isWellFormatted())
					throw new BadBlockException();
				if (!generateEmptyBlock().isWellFormatted()) {
					throw new BadBlockException();
				}

				blocks.add(generateAnnexGroupTableBlock((BaseGroup) object));
				blocks.add(generateEmptyBlock());
			}
		}

		return blocks;
	}

	public static List<PdfTableBlock> generateFormVariableTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();
		PdfTableBlock block = new PdfTableBlock(MIN_VARIABLE_ROWS + form.getCustomVariables().size(), VARIABLE_COLS);
		block.insertRow(PdfRowGenerator.generateTitleRow("Name", "Type", "Scope", "Default value"));
		for (CustomVariable variable : form.getCustomVariables()) {
			block.insertRow(PdfRowGenerator.generateVariableRow(variable));
		}
		blocks.add(block);
		return blocks;
	}

	public static List<PdfTableBlock> generateExpressionTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		for (ExpressionChain expression : form.getExpressionChains()) {
			PdfTableBlock block = new PdfTableBlock(MIN_EXPRESSION_ROWS, MIN_EXPRESSION_COLS);
			block.insertRow(PdfRowGenerator.generateBorderlessTitleRow(expression.getName()));
			block.insertRow(PdfRowGenerator.generateExpressionsRow(expression));
			block.insertRow(PdfRowGenerator.generateEmptyRow(MIN_EMPTY_ROW, MIN_EXPRESSION_COLS));
			blocks.add(block);
		}
		return blocks;
	}

	public static List<PdfTableBlock> generateRuleTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		for (Rule rule : form.getRules()) {
			PdfTableBlock blockCondition = new PdfTableBlock(MIN_RULE_CONDITION_ROWS, MIN_RULE_COLS);
			blockCondition.insertRow(PdfRowGenerator.generateBorderlessTitleRow(rule.getName()));
			blockCondition.insertRow(PdfRowGenerator.generateTitleRow("Conditions"));
			blockCondition.insertRow(PdfRowGenerator.generateExpressionsRow(rule.getConditions()));
			blocks.add(blockCondition);
			PdfTableBlock blockAction = new PdfTableBlock(MIN_RULE_ACTION_ROWS, MIN_RULE_COLS);
			blockAction.insertRow(PdfRowGenerator.generateTitleRow("Actions"));
			blockAction.insertRow(PdfRowGenerator.generateExpressionsRow(rule.getActions()));
			blockAction.insertRow(PdfRowGenerator.generateEmptyRow(MIN_EMPTY_ROW, MIN_EXPRESSION_COLS));
			blocks.add(blockAction);
		}
		return blocks;
	}

	public static List<PdfTableBlock> generateTableBlocks(TableRule table) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();
		PdfTableBlock tableBlock = new PdfTableBlock(table.getRules().size(), 2);
		for (TableRuleRow ruleRow : table.getRules()) {
			tableBlock.insertRow(PdfRowGenerator.generateRuleRow(ruleRow));
		}
		blocks.add(tableBlock);
		return blocks;
	}

}
