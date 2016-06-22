package com.biit.abcd.core;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.utils.GraphvizApp;
import com.biit.abcd.utils.GraphvizApp.ImgType;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Test(groups = { "graphviz" })
public class GraphvizTests {
	private Form form;

	@Test
	public void createDiagramImage() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly, IOException, InterruptedException {
		form = FormUtils.createCompleteForm();
		Assert.assertNotNull(form);

		for (Diagram diagram : form.getDiagrams()) {
			Assert.assertNotNull(GraphvizApp.generateImage(form, diagram, ImgType.SVG));
		}

	}

}
