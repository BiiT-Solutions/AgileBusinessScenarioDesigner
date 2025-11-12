package com.biit.abcd.core;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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
	public void createDiagramImage() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly, IOException, InterruptedException, TranscoderException {
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
