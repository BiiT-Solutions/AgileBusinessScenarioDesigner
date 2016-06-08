package com.biit.abcd.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;

/**
 * This class generates PdfTableBlock from different kinds of elements in the
 * form
 *
 */
public class PdfBlockGenerator {

	public static final int ANNEX_COLS = 5;
	public static final int ANNEX_QUESTION_ROWS = 1;

	public static PdfTableBlock generateAnnexQuestionTableBlock(Question question) {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(ANNEX_QUESTION_ROWS + question.getAllChildrenInHierarchy(BaseAnswer.class).size(),
					ANNEX_COLS);

			block.insertRow(PdfRowGenerator.generateAnnexQuestion(question));

			if (!question.getChildren().isEmpty()) {
				block.insertCol(
						PdfCol.generateWhiteCol(question.getAllChildrenInHierarchy(BaseAnswer.class).size(), 1));
				for (TreeObject child : question.getChildren()) {
					// They are all answers
					block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) child));
					for (TreeObject subChild : child.getChildren()) {
						block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) subChild));
					}
				}
			}
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<TreeObject> treeObjects = new ArrayList<>(form.getAll(BaseQuestion.class));

		for (TreeObject object : treeObjects) {
			if (!object.isHiddenElement()) {
				blocks.add(generateAnnexQuestionTableBlock((Question) object));
			}
		}

		return blocks;
	}
}
