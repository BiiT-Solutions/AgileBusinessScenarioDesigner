package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class DiagramTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";

	@Autowired
	private IDiagramDao diagramDao;

	@Autowired
	private IFormDao formDao;

	@Test(groups = { "diagramDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(diagramDao.getRowCount(), 0);
	}

	@Test(groups = { "diagramDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyDiagram() throws NotValidFormException {
		Form form = new Form();
		form.setName(DUMMY_FORM);
		formDao.makePersistent(form);
		
		Diagram diagram = new Diagram(form);
		diagramDao.makePersistent(diagram);
		
		Assert.assertEquals(diagramDao.getRowCount(), 1);
	}

	@Test(groups = { "diagramDao" }, dependsOnMethods = "storeDummyDiagram")
	public void getDummyDiagram() {
		List<Diagram> diagrams = diagramDao.getAll();
		Assert.assertEquals(diagrams.get(0).getForm().getName(), DUMMY_FORM);
	}

	@Test(groups = { "diagramDao" }, dependsOnMethods = "getDummyDiagram")
	public void removeDummyDiagram() {
		List<Diagram> diagrams = diagramDao.getAll();
		diagramDao.makeTransient(diagrams.get(0));
		Assert.assertEquals(diagramDao.getRowCount(), 0);

		List<Form> forms = formDao.getAll();
		formDao.makeTransient(forms.get(0));
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

}
