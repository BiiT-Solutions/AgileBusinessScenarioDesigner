package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
@Test(groups = {"diagramDao"})
public class DiagramTest extends AbstractTransactionalTestNGSpringContextTests {
    private final static String DIAGRAM_IN_JSON = "{\"cells\":[{\"type\":\"biit.SourceNode\",\"tooltip\":\"Source Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":328,\"y\":470},\"angle\":0,\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"embeds\":\"\",\"z\":1,\"attrs\":{}},{\"type\":\"biit.RuleNode\",\"tooltip\":\"Rule Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":490,\"y\":418},\"angle\":0,\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"embeds\":\"\",\"z\":2,\"attrs\":{}},{\"type\":\"biit.SinkNode\",\"tooltip\":\"Sink Tooltip\",\"size\":{\"width\":30,\"height\":30},\"position\":{\"x\":644,\"y\":472},\"angle\":0,\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"embeds\":\"\",\"z\":3,\"attrs\":{}},{\"type\":\"link\",\"id\":\"aa228ac0-a038-4072-9ea2-45c8a39af388\",\"embeds\":\"\",\"source\":{\"id\":\"a052d3a6-007c-4057-a789-c7aa19008b0f\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"out\"},\"target\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":4,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}},{\"type\":\"link\",\"id\":\"5a5dfab6-3682-4d1e-b85c-c6d24de1a555\",\"embeds\":\"\",\"source\":{\"id\":\"31db56a7-53de-4e38-acf8-98ee300a20a1\",\"selector\":\"g:nth-child(1) circle:nth-child(3)   \",\"port\":\"out\"},\"target\":{\"id\":\"62958d22-23ff-467f-9ad5-f034ac24ad1d\",\"selector\":\"g:nth-child(1) circle:nth-child(2)   \",\"port\":\"in\"},\"z\":5,\"attrs\":{\".marker-source\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\",\"transform\":\"scale(0.001)\"},\".marker-target\":{\"d\":\"M 10 0 L 0 5 L 10 10 z\"},\".connection\":{\"stroke\":\"black\"}}}]}";
    private final static String DIAGRAM_FORM = "Diagram Form";
    private final static String DUMMY_DIAGRAM = "Dummy Diagram";

    @Autowired
    private IDiagramDao diagramDao;

    @Autowired
    private IFormDao formDao;

    private Diagram diagram;

    @Test
    public void testEmptyDatabase() throws UnexpectedDatabaseException {
        // Read
        Assert.assertEquals(diagramDao.getRowCount(), 0);
    }

    @Test
    public void convertJsonToDiagram() {
        diagram = Diagram.fromJson(DIAGRAM_IN_JSON);
        Assert.assertNotNull(diagram);
        Assert.assertEquals(diagram.getDiagramObjects().size(), 5);
        // Test source child.
        Iterator<DiagramObject> iterator = diagram.getDiagramObjects().iterator();
        Assert.assertTrue(iterator.hasNext());
        DiagramObject diagramObject = null;
        while (iterator.hasNext()) {
            DiagramObject iteratedObject = iterator.next();
            if (iteratedObject.getType().equals(DiagramObjectType.SOURCE)) {
                diagramObject = iteratedObject;
            }
        }

        Assert.assertNotNull(diagramObject);
        Assert.assertEquals(diagramObject.getType(), DiagramObjectType.SOURCE);
        Assert.assertEquals(((DiagramElement) diagramObject).getTooltip(), "Source Tooltip");
        Assert.assertEquals(((DiagramElement) diagramObject).getSize().getWidth(), 30);
        Assert.assertEquals(((DiagramElement) diagramObject).getSize().getHeight(), 30);
        Assert.assertEquals(((DiagramElement) diagramObject).getPosition().getX(), 328);
        Assert.assertEquals(((DiagramElement) diagramObject).getPosition().getY(), 470);
        Assert.assertEquals(((DiagramElement) diagramObject).getAngle(), 0f);
        Assert.assertEquals(diagramObject.getJointjsId(), "a052d3a6-007c-4057-a789-c7aa19008b0f");
    }

    @Test(dependsOnMethods = "convertJsonToDiagram")
    public void storeDiagramObjects() throws ElementCannotBeRemovedException {
        diagramDao.makePersistent(diagram);
        Diagram diagram2 = diagramDao.get(diagram.getId());
        Assert.assertEquals(diagram2.getDiagramObjects().size(), 5);
        for (Diagram diagram : diagramDao.getAll()) {
            diagramDao.makeTransient(diagram);
        }
        Assert.assertEquals(diagramDao.getRowCount(), 0);
    }

    @Test
    public void storeFormDiagram() throws FieldTooLongException, ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);
        form.setLabel(DIAGRAM_FORM);

        Diagram diagram = new Diagram(DUMMY_DIAGRAM);
        form.getDiagrams().add(diagram);

        formDao.makePersistent(form);
        Form retrievedForm = formDao.get(form.getId());

        Assert.assertEquals(retrievedForm.getId(), form.getId());
        Assert.assertEquals(diagramDao.getRowCount(), 1);
        Iterator<Diagram> iterator = retrievedForm.getDiagrams().iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next().getName(), DUMMY_DIAGRAM);

        formDao.makeTransient(form);
    }

    @Test
    public void diagramChildLinkNull() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException, ElementIsReadOnly, ElementCannotBeRemovedException, DependencyExistException {
        Form form = new Form();
        form.setOrganizationId(0l);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);

        Question question1 = new Question();
        question1.setName("Question1");
        category.addChild(question1);

        Question question2 = new Question();
        question2.setName("Question2");
        category.addChild(question2);

        // Create a diagram
        Diagram diagram = new Diagram("diagram1");

        DiagramSource startNode = new DiagramSource();
        startNode.setJointjsId(IdGenerator.createId());
        startNode.setType(DiagramObjectType.SOURCE);
        startNode.setSize(new Size(1, 1));
        startNode.setPosition(new Point(1, 1));
        Node nodeSource = new Node(startNode.getJointjsId());
        diagram.addDiagramObject(startNode);

        DiagramChild diagramChild = new DiagramChild();
        diagramChild.setDiagram(null);
        diagramChild.setJointjsId(IdGenerator.createId());
        diagramChild.setSize(new Size(2, 2));
        diagramChild.setPosition(new Point(1, 1));
        diagramChild.setType(DiagramObjectType.DIAGRAM_CHILD);

        Node nodeChild = new Node(diagramChild.getJointjsId());
        diagram.addDiagramObject(diagramChild);

        DiagramLink startLink = new DiagramLink();
        startLink.setSource(nodeSource);
        startLink.setJointjsId(IdGenerator.createId());
        startLink.setType(DiagramObjectType.LINK);
        startLink.setTarget(nodeChild);
        diagram.addDiagramObject(startLink);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        diagramEndNode.setSize(new Size(3, 3));
        diagramEndNode.setPosition(new Point(1, 1));
        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagram.addDiagramObject(diagramEndNode);

        DiagramLink endLink = new DiagramLink();
        endLink.setSource(nodeChild);
        endLink.setJointjsId(IdGenerator.createId());
        endLink.setType(DiagramObjectType.LINK);
        endLink.setTarget(nodeSink);

        form.addDiagram(diagram);

        formDao.makePersistent(form);

        question1.remove();

        formDao.makePersistent(form);

        formDao.makeTransient(form);
    }

    @Test
    public void diagramForkNullExpression() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);

        Question question1 = new Question();
        question1.setName("Question1");
        category.addChild(question1);

        Question question2 = new Question();
        question2.setName("Question2");
        category.addChild(question2);

        // Create a diagram
        Diagram diagram = new Diagram("diagram1");

        DiagramSource startNode = new DiagramSource();
        startNode.setJointjsId(IdGenerator.createId());
        startNode.setType(DiagramObjectType.SOURCE);
        startNode.setSize(new Size(1, 1));
        startNode.setPosition(new Point(1, 1));
        Node nodeSource = new Node(startNode.getJointjsId());
        diagram.addDiagramObject(startNode);

        DiagramFork diagramFork = new DiagramFork();
        diagramFork.setReference(null);
        diagramFork.setJointjsId(IdGenerator.createId());
        diagramFork.setSize(new Size(2, 2));
        diagramFork.setPosition(new Point(1, 1));
        diagramFork.setType(DiagramObjectType.FORK);

        Node nodeChild = new Node(diagramFork.getJointjsId());
        diagram.addDiagramObject(diagramFork);

        DiagramLink startLink = new DiagramLink();
        startLink.setSource(nodeSource);
        startLink.setJointjsId(IdGenerator.createId());
        startLink.setType(DiagramObjectType.LINK);
        startLink.setTarget(nodeChild);
        diagram.addDiagramObject(startLink);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        diagramEndNode.setSize(new Size(3, 3));
        diagramEndNode.setPosition(new Point(1, 1));
        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagram.addDiagramObject(diagramEndNode);

        DiagramLink endLink = new DiagramLink();
        endLink.setSource(nodeChild);
        endLink.setJointjsId(IdGenerator.createId());
        endLink.setType(DiagramObjectType.LINK);
        endLink.setTarget(nodeSink);

        form.addDiagram(diagram);

        formDao.makePersistent(form);

        formDao.makeTransient(form);
    }

    @Test
    public void diagramExpressionNullExpression() throws NotValidChildException,
            FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);

        Question question1 = new Question();
        question1.setName("Question1");
        category.addChild(question1);

        Question question2 = new Question();
        question2.setName("Question2");
        category.addChild(question2);

        // Create a diagram
        Diagram diagram = new Diagram("diagram1");

        DiagramSource startNode = new DiagramSource();
        startNode.setJointjsId(IdGenerator.createId());
        startNode.setType(DiagramObjectType.SOURCE);
        startNode.setSize(new Size(1, 1));
        startNode.setPosition(new Point(1, 1));
        Node nodeSource = new Node(startNode.getJointjsId());
        diagram.addDiagramObject(startNode);

        DiagramExpression diagramExpression = new DiagramExpression();
        diagramExpression.setExpression(null);
        diagramExpression.setJointjsId(IdGenerator.createId());
        diagramExpression.setSize(new Size(2, 2));
        diagramExpression.setPosition(new Point(1, 1));
        diagramExpression.setType(DiagramObjectType.FORK);

        Node nodeChild = new Node(diagramExpression.getJointjsId());
        diagram.addDiagramObject(diagramExpression);

        DiagramLink startLink = new DiagramLink();
        startLink.setSource(nodeSource);
        startLink.setJointjsId(IdGenerator.createId());
        startLink.setType(DiagramObjectType.LINK);
        startLink.setTarget(nodeChild);
        diagram.addDiagramObject(startLink);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        diagramEndNode.setSize(new Size(3, 3));
        diagramEndNode.setPosition(new Point(1, 1));
        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagram.addDiagramObject(diagramEndNode);

        DiagramLink endLink = new DiagramLink();
        endLink.setSource(nodeChild);
        endLink.setJointjsId(IdGenerator.createId());
        endLink.setType(DiagramObjectType.LINK);
        endLink.setTarget(nodeSink);

        form.addDiagram(diagram);

        formDao.makePersistent(form);

        formDao.makeTransient(form);
    }

    @Test
    public void diagramRuleNullExpression() throws NotValidChildException,
            FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);

        Question question1 = new Question();
        question1.setName("Question1");
        category.addChild(question1);

        Question question2 = new Question();
        question2.setName("Question2");
        category.addChild(question2);

        // Create a diagram
        Diagram diagram = new Diagram("diagram1");

        DiagramSource startNode = new DiagramSource();
        startNode.setJointjsId(IdGenerator.createId());
        startNode.setType(DiagramObjectType.SOURCE);
        startNode.setSize(new Size(1, 1));
        startNode.setPosition(new Point(1, 1));
        Node nodeSource = new Node(startNode.getJointjsId());
        diagram.addDiagramObject(startNode);

        DiagramRule diagramRule = new DiagramRule();
        diagramRule.setRule(null);
        diagramRule.setJointjsId(IdGenerator.createId());
        diagramRule.setSize(new Size(2, 2));
        diagramRule.setPosition(new Point(1, 1));
        diagramRule.setType(DiagramObjectType.FORK);

        Node nodeChild = new Node(diagramRule.getJointjsId());
        diagram.addDiagramObject(diagramRule);

        DiagramLink startLink = new DiagramLink();
        startLink.setSource(nodeSource);
        startLink.setJointjsId(IdGenerator.createId());
        startLink.setType(DiagramObjectType.LINK);
        startLink.setTarget(nodeChild);
        diagram.addDiagramObject(startLink);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        diagramEndNode.setSize(new Size(3, 3));
        diagramEndNode.setPosition(new Point(1, 1));
        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagram.addDiagramObject(diagramEndNode);

        DiagramLink endLink = new DiagramLink();
        endLink.setSource(nodeChild);
        endLink.setJointjsId(IdGenerator.createId());
        endLink.setType(DiagramObjectType.LINK);
        endLink.setTarget(nodeSink);

        form.addDiagram(diagram);

        formDao.makePersistent(form);

        formDao.makeTransient(form);
    }

    @Test
    public void diagramTableNullExpression() throws NotValidChildException,
            FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);

        Question question1 = new Question();
        question1.setName("Question1");
        category.addChild(question1);

        Question question2 = new Question();
        question2.setName("Question2");
        category.addChild(question2);

        // Create a diagram
        Diagram diagram = new Diagram("diagram1");

        DiagramSource startNode = new DiagramSource();
        startNode.setJointjsId(IdGenerator.createId());
        startNode.setType(DiagramObjectType.SOURCE);
        startNode.setSize(new Size(1, 1));
        startNode.setPosition(new Point(1, 1));
        Node nodeSource = new Node(startNode.getJointjsId());
        diagram.addDiagramObject(startNode);

        DiagramTable diagramTable = new DiagramTable();
        diagramTable.setTable(null);
        diagramTable.setJointjsId(IdGenerator.createId());
        diagramTable.setSize(new Size(2, 2));
        diagramTable.setPosition(new Point(1, 1));
        diagramTable.setType(DiagramObjectType.FORK);

        Node nodeChild = new Node(diagramTable.getJointjsId());
        diagram.addDiagramObject(diagramTable);

        DiagramLink startLink = new DiagramLink();
        startLink.setSource(nodeSource);
        startLink.setJointjsId(IdGenerator.createId());
        startLink.setType(DiagramObjectType.LINK);
        startLink.setTarget(nodeChild);
        diagram.addDiagramObject(startLink);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        diagramEndNode.setSize(new Size(3, 3));
        diagramEndNode.setPosition(new Point(1, 1));
        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagram.addDiagramObject(diagramEndNode);

        DiagramLink endLink = new DiagramLink();
        endLink.setSource(nodeChild);
        endLink.setJointjsId(IdGenerator.createId());
        endLink.setType(DiagramObjectType.LINK);
        endLink.setTarget(nodeSink);

        form.addDiagram(diagram);

        formDao.makePersistent(form);

        formDao.makeTransient(form);
    }

}
