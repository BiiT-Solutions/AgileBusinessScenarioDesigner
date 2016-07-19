package com.biit.abcd.core;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.utils.GraphvizApp;
import com.biit.abcd.utils.GraphvizApp.ImgType;
import com.biit.abcd.utils.ImageManipulator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Test(groups = { "graphviz" })
public class GraphvizTests {

	@Test
	public void createDiagramImage() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException,
			ElementIsReadOnly, IOException, InterruptedException, TranscoderException {
		Form form = FormUtils.createCompleteForm();
		Assert.assertNotNull(form);

		for (Diagram diagram : form.getDiagrams()) {
			byte[] imageSVG = GraphvizApp.generateImage(form, diagram, ImgType.SVG);
			Assert.assertNotNull(imageSVG);
			byte[] imagePNG = ImageManipulator.svgToPng(imageSVG, 2500, 2600);
			Assert.assertNotNull(imagePNG);

			// convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream("/tmp/testSvgToPng.png");
			fileOuputStream.write(imagePNG);
			fileOuputStream.close();
		}
	}
}
