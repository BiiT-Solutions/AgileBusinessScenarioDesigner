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
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class DiagramTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DIAGRAM_IN_JSON = "{\"cells\":[{\"type\":\"biit.SourceNode\",\"tooltip\":\"Source Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":328,\"y\":470},\"angle\":0,\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"embeds\":\"\",\"z\":1,\"attrs\":{}},{\"type\":\"biit.RuleNode\",\"tooltip\":\"Rule Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":490,\"y\":418},\"angle\":0,\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"embeds\":\"\",\"z\":2,\"attrs\":{}},{\"type\":\"biit.SinkNode\",\"tooltip\":\"Sink Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":644,\"y\":472},\"angle\":0,\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"embeds\":\"\",\"z\":3,\"attrs\":{}},{\"type\":\"link\",\"id\":\"aa228ac0-a038-4072-9ea2-45c8a39af388\",\"embeds\":\"\",\"source\":{\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"out\"},\"target\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":4,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}},{\"type\":\"link\",\"id\":\"5a5dfab6-3682-4d1e-b85c-c6d24de1a555\",\"embeds\":\"\",\"source\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(3)   \",\"port\":\"out\"},\"target\":{\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":5,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}}]}";
	private final static String DUMMY_FORM = "Dummy Form";

	@Autowired
	private IDiagramDao diagramDao;

	@Autowired
	private IFormDao formDao;

	private Diagram diagram;
	private Form form;

	private String storedJson;

	@Test(groups = { "diagramDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(diagramDao.getRowCount(), 0);
	}

	@Test(groups = { "diagramDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyDiagram() throws NotValidFormException {
		form = new Form();
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

	@Test(groups = { "jsonParser" })
	public void convertJsonToDiagram() {
		diagram = Diagram.fromJson(DIAGRAM_IN_JSON);
		Assert.assertNotNull(diagram);
		Assert.assertEquals(diagram.getDiagramObjects().size(), 5);
		// Test first child.
		Assert.assertEquals(diagram.getDiagramObjects().get(0).getType(), "biit.SourceNode");
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getTooltip(), "Source Tooltip");
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getSize().getWidth(), 30);
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getSize().getHeight(), 30);
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getPosition().getX(), 328);
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getPosition().getY(), 470);
		Assert.assertEquals(((DiagramElement) diagram.getDiagramObjects().get(0)).getAngle(), 0f);
		Assert.assertEquals(diagram.getDiagramObjects().get(0).getJointjsId(), "a052d3a6-007c-4057-a789-c7aa19008b0f");
	}

	@Test(groups = { "jsonParser" }, dependsOnMethods = { "convertJsonToDiagram" })
	public void convertDiagramToJson() {
		storedJson = diagram.toJson();
	}

	@Test(groups = { "diagramDao", "jsonParser" }, dependsOnMethods = { "convertDiagramToJson", "storeDummyDiagram" })
	public void storeDiagramObjects() {
		diagram.setForm(form);
		diagramDao.makePersistent(diagram);
		Diagram diagram2 = diagramDao.read(diagram.getId());
		Assert.assertEquals(diagram2.getDiagramObjects().size(), 5);
		Assert.assertEquals(diagram2.toJson(), storedJson);
	}

	@Test(groups = { "diagramDao" }, dependsOnMethods = { "getDummyDiagram", "storeDiagramObjects" })
	public void removeDummyDiagram() {
		List<Diagram> diagrams = diagramDao.getAll();
		for (Diagram diagram : diagrams) {
			diagramDao.makeTransient(diagram);
		}
		Assert.assertEquals(diagramDao.getRowCount(), 0);

		List<Form> forms = formDao.getAll();
		for (Form form : forms) {
			formDao.makeTransient(form);
		}
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

}
