package com.biit.abcd.persistence.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.persistence.dao.IBaseQuestionDao;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "formDao" })
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";
	private final static String FULL_FORM = "Complete Form";
	private final static String OTHER_FORM = "Other Form";
	private final static String CATEGORY_LABEL = "Category1";
	// private final static String ACTION_EXPRESSION = "Score=3";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IDiagramDao diagramDao;

	@Autowired
	private ITableRuleRowDao tableRuleDao;

	@Autowired
	private IBaseQuestionDao<Question> questionDao;

	@Test
	public void storeDummyForm() throws FieldTooLongException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);
		Assert.assertEquals(formDao.getForm(DUMMY_FORM, 0l).getLabel(), DUMMY_FORM);
		formDao.makeTransient(form);
		Assert.assertNull(formDao.getForm(DUMMY_FORM, 0l));
	}

	@Test
	public void storeFormWithCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementIsReadOnly, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(FULL_FORM);
		Category category = new Category();
		category.setName(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());
		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
		formDao.makeTransient(form);
	}

	@Test
	public void storeOtherFormWithSameLabelCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementCannotBeRemovedException, ElementIsReadOnly {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(OTHER_FORM);
		Category category = new Category();
		category.setName(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
		formDao.makeTransient(form);
	}

	@Test
	public void moveElementsUp() throws NotValidChildException, ChildrenNotFoundException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel("MoveUp");

		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);

		Category category2 = new Category();
		category2.setName("Category2");
		form.addChild(category2);

		Category category3 = new Category();
		category3.setName("Category3");
		form.addChild(category3);

		Group group1 = new Group();
		group1.setName("Group1");
		category2.addChild(group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category2.addChild(group2);

		Group group3 = new Group();
		group3.setName("Group3");
		category2.addChild(group3);

		Question question1 = new Question();
		question1.setName("Question1");
		group2.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		group2.addChild(question2);

		Question question3 = new Question();
		question3.setName("Question3");
		group2.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);

		// Update form with new elements
		formDao.makePersistent(form);

		// Move #2 up
		form.switchChildren(1, 0, null);
		category2.switchChildren(1, 0, null);
		group2.switchChildren(1, 0, null);
		question2.switchChildren(1, 0, null);

		// Update form with this changes
		formDao.makePersistent(form);
		Form storedForm = formDao.read(form.getId());

		// Compare order is the same.
		Assert.assertTrue(compare(form, storedForm));
		formDao.makeTransient(form);
	}
	
	@Test
	public void checkDependencyDiagramLinkNull() throws NotValidChildException, ChildrenNotFoundException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException, DependencyExistException {
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

		//Create a diagram
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
		
		question1.remove();
	}

	private boolean compare(TreeObject object1, TreeObject object2) {
		if (!object1.getComparationId().equals(object2.getComparationId())) {
			return false;
		}
		for (int i = 0; i < object1.getChildren().size(); i++) {
			if (!compare(object1.getChildren().get(i), object2.getChildren().get(i))) {
				return false;
			}
		}
		return true;
	}
}
