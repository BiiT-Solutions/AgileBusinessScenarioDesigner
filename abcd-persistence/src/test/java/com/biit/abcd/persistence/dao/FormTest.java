package com.biit.abcd.persistence.dao;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
@Test(groups = {"formDao"})
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
    private final static String DUMMY_FORM = "Dummy Form";
    private final static String FULL_FORM = "Complete Form";
    private final static String OTHER_FORM = "Other Form";
    private final static String CATEGORY_LABEL = "Category1";
    private Category formCategoryUp;
    private Category formCategoryDown;
    private Question formQuestion;

    @Autowired
    private IFormDao formDao;

    @Test
    public void storeDummyForm() throws FieldTooLongException, ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);
        form.setLabel(DUMMY_FORM);
        formDao.makePersistent(form);
        Assert.assertEquals(formDao.getRowCount(), 1);
        Assert.assertEquals(formDao.get(form.getId()).getLabel(), DUMMY_FORM);
        formDao.makeTransient(form);
        Assert.assertNull(formDao.get(form.getId()));
    }

    @Test
    public void storeFormWithCategory() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException,
            ElementIsReadOnly, ElementCannotBeRemovedException {
        Form form = new Form();
        form.setOrganizationId(0l);
        form.setLabel(FULL_FORM);
        Category category = new Category();
        category.setName(CATEGORY_LABEL);
        form.addChild(category);
        formDao.makePersistent(form);
        Form retrievedForm = formDao.get(form.getId());
        Assert.assertEquals(retrievedForm.getId(), form.getId());
        Assert.assertEquals(retrievedForm.getChildren().size(), 1);
        formDao.makeTransient(form);
    }

    @Test
    public void storeOtherFormWithSameLabelCategory() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException, ElementCannotBeRemovedException, ElementIsReadOnly {
        Form form = new Form();
        form.setOrganizationId(0l);
        form.setLabel(OTHER_FORM);
        Category category = new Category();
        category.setName(CATEGORY_LABEL);
        form.addChild(category);
        formDao.makePersistent(form);
        Form retrievedForm = formDao.get(form.getId());

        Assert.assertEquals(retrievedForm.getId(), form.getId());
        Assert.assertEquals(retrievedForm.getChildren().size(), 1);
        formDao.makeTransient(form);
    }

    private Form createCompleteForm() throws FieldTooLongException, CharacterNotAllowedException,
            NotValidChildException, ElementIsReadOnly {
        Form form = new Form();
        form.setOrganizationId(0l);
        form.setLabel(FULL_FORM);

        Category category = new Category();
        category.setName("Category1");
        form.addChild(category);
        setFormCategoryUp(category);

        Category category2 = new Category();
        category2.setName("Category2");
        form.addChild(category2);

        Category category3 = new Category();
        category3.setName("Category3");
        form.addChild(category3);
        setFormCategoryDown(category3);

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
        setFormQuestion(question3);

        Answer answer1 = new Answer();
        answer1.setName("Answer1");
        question2.addChild(answer1);

        Answer answer2 = new Answer();
        answer2.setName("Answer2");
        question2.addChild(answer2);

        Answer answer3 = new Answer();
        answer3.setName("Answer3");
        question2.addChild(answer3);

        return form;
    }

    private void setFormQuestion(Question formQuestion) {
        this.formQuestion = formQuestion;
    }

    private void setFormCategoryUp(Category formCategoryUp) {
        this.formCategoryUp = formCategoryUp;
    }

    private void setFormCategoryDown(Category formCategoryDown) {
        this.formCategoryDown = formCategoryDown;
    }

    private Question getFormQuestion() {
        return formQuestion;
    }

    private Category getFormCategoryUp() {
        return formCategoryUp;
    }

    private Category getFormCategoryDown() {
        return formCategoryDown;
    }

    @Test
    @Rollback(value = false)
    @Transactional(value = TxType.NEVER)
    public void moveElementsInHierarchyDown() throws NotValidChildException, ChildrenNotFoundException,
            FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {

        Form form = createCompleteForm();

        // Update form with new elements
        formDao.makePersistent(form);

        Form.move(getFormQuestion(), getFormCategoryDown());

        // Update form with this changes
        formDao.merge(form);

        Form storedForm = formDao.get(form.getId());
        Assert.assertNotNull(storedForm);

        // Compare order is the same.
        Assert.assertTrue(compare(form, storedForm));
        formDao.makeTransient(storedForm);
    }

    @Test
    @Rollback(value = false)
    @Transactional(value = TxType.NEVER)
    public void moveElementsInHierarchyUp() throws NotValidChildException, ChildrenNotFoundException,
            FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            ElementCannotBeRemovedException {
        Form form = createCompleteForm();

        // Update form with new elements
        formDao.makePersistent(form);

        Form.move(getFormQuestion(), getFormCategoryUp());

        // Update form with this changes
        formDao.merge(form);

        Form storedForm = formDao.get(form.getId());
        Assert.assertNotNull(storedForm);

        // Compare order is the same.
        Assert.assertTrue(compare(form, storedForm));
        formDao.makeTransient(storedForm);
    }

    @Test
    @Rollback(value = false)
    @Transactional(value = TxType.NEVER)
    public void copyForm() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly,
            NotValidStorableObjectException, ElementCannotBeRemovedException {

        Form form = createCompleteForm();

        // Update form with new elements
        form = formDao.makePersistent(form);

        Assert.assertEquals(formDao.getRowCount(), 1);

        Form copiedForm = form.copy(null, "Copied Form");

        copiedForm = formDao.makePersistent(copiedForm);

        Assert.assertEquals(formDao.getRowCount(), 2);

        formDao.makeTransient(form);
        formDao.makeTransient(copiedForm);
        Assert.assertEquals(formDao.getRowCount(), 0);
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
