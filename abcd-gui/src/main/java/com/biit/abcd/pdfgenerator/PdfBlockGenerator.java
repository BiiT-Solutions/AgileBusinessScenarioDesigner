package com.biit.abcd.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfCol;
import com.biit.abcd.pdfgenerator.utils.PdfRow;
import com.biit.abcd.pdfgenerator.utils.PdfTableBlock;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseGroup;
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
			block = new PdfTableBlock(MIN_GROUP_ROWS + group.getAllChildrenInHierarchy(TreeObject.class).size(),
					STRUCTURE_COLS);

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

	private static void generateAndAddGroup(PdfTableBlock block, BaseGroup child) throws BadBlockException {
		block.insertRow(PdfRowGenerator.generateStructureGroup(child));
	}

	private static void generateAndAddQuestion(PdfTableBlock block, Question question) throws BadBlockException {
		System.out.println("block size: " + block.getNumberCols() + " " + block.getNumberRows());
		PdfRow row = PdfRowGenerator.generateQuestion(question);
		System.out
				.println("row size: " + row.getCurrentCols() + " " + row.getNumberCols() + " " + row.getNumberRows());
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

	public static PdfTableBlock generateAnnexQuestionTableBlock(Question question) throws BadBlockException {
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

	public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<TreeObject> treeObjects = new ArrayList<>(form.getAll(BaseGroup.class));

		for (TreeObject object : treeObjects) {
			if (!object.isHiddenElement()) {
				blocks.add(generateAnnexGroupTableBlock((BaseGroup) object));
				blocks.add(generateEmptyBlock());
			}
		}

		return blocks;
	}
}
