package com.biit.abcd.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfCol;
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
		System.out.println("Group " + group);
		try {
			block = new PdfTableBlock(MIN_GROUP_ROWS + getNumberOfChildsAndAnswers(group), STRUCTURE_COLS);

			block.insertRow(PdfRowGenerator.generateStructureGroupRoot(group));

			if (!group.getChildren().isEmpty()) {
				for (TreeObject child : group.getChildren()) {
					System.out.println(child);
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
		System.out.println("block size: " + block.getNumberCols() + " " + block.getNumberRows());
		PdfRow row = PdfRowGenerator.generateQuestion(question);
		System.out.println("row size: " + row.getCurrentCols() + " " + row.getNumberCols() + " " + row.getNumberRows());
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

	private static PdfTableBlock generateAnnexQuestionTableBlock(Question question) throws BadBlockException {
		PdfTableBlock block = null;
		block = new PdfTableBlock(MIN_QUESTION_ROWS + question.getAllChildrenInHierarchy(BaseAnswer.class).size(),
				STRUCTURE_COLS);

		block.insertRow(PdfRowGenerator.generateQuestion(question));

		if (!question.getChildren().isEmpty()) {
			block.insertCol(PdfCol.generateWhiteCol(question.getAllChildrenInHierarchy(BaseAnswer.class).size(), 1));
			for (TreeObject child : question.getChildren()) {
				// They are all answers
				block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) child));
				for (TreeObject subChild : child.getChildren()) {
					block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) subChild));
				}
			}
		}
		return block;
	}

	public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<TreeObject> treeObjects = new ArrayList<>(form.getAll(BaseGroup.class));

		for (TreeObject object : treeObjects) {
			if (!object.isHiddenElement()) {
				System.out.println(object);
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
			block.insertRow(PdfRowGenerator.generateBorderlessTitleRow("Expression name: " + expression.getName()));
			block.insertRow(PdfRowGenerator.generateTitleRow(expression.getRepresentation()));
			block.insertRow(PdfRowGenerator.generateEmptyRow(MIN_EMPTY_ROW, MIN_EXPRESSION_COLS));
			blocks.add(block);
		}
		return blocks;
	}

	public static List<PdfTableBlock> generateRuleTableBlocks(Form form) throws BadBlockException {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		for (Rule rule : form.getRules()) {
			PdfTableBlock blockCondition = new PdfTableBlock(MIN_RULE_CONDITION_ROWS, MIN_RULE_COLS);
			blockCondition.insertRow(PdfRowGenerator.generateBorderlessTitleRow("Rule name: " + rule.getName()));
			blockCondition.insertRow(PdfRowGenerator.generateTitleRow("Conditions"));
			blockCondition.insertRow(PdfRowGenerator.generateTitleRow(rule.getConditions().getRepresentation()));
			blocks.add(blockCondition);
			PdfTableBlock blockAction = new PdfTableBlock(MIN_RULE_ACTION_ROWS, MIN_RULE_COLS);
			blockAction.insertRow(PdfRowGenerator.generateTitleRow("Actions"));
			blockAction.insertRow(PdfRowGenerator.generateTitleRow(rule.getActions().getRepresentation()));
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
