package com.biit.abcd.core;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.utils.exporters.dotgraph.ExporterDotForm;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "graphviz" })
public class GraphvizTests extends AbstractTransactionalTestNGSpringContextTests {
	private Form form;

	@Test
	public void createDiagramImage() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly {
		form = FormUtils.createCompleteForm();
		Assert.assertNotNull(form);

		String dotCode = null;
		ExporterDotForm exporter = new ExporterDotForm(form);

		for (Diagram diagram : form.getDiagrams()) {
			dotCode = exporter.export(diagram);
			System.out.println(dotCode);
		}

	}

}
