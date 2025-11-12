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

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.abcd.persistence.utils.FormVersionComparator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
@Test(groups = {"newVersion"})
public class NewVersionTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private IFormDao formDao;

    private Form form;
    private Form newVersionForm;

    @Test
    public void newVersion() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
            InvalidAnswerFormatException, NotValidStorableObjectException, TreeObjectNotEqualsException,
            StorableObjectNotEqualsException, FormNotEqualsException, GroupNotEqualsException,
            QuestionNotEqualsException, CustomVariableNotEqualsException, ExpressionNotEqualsException,
            GlobalVariableNotEqualsException, VariableDataNotEqualsException, TableRuleNotEqualsException,
            RuleNotEqualsException, DiagramNotEqualsException, DiagramObjectNotEqualsException, NodeNotEqualsException,
            SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException,
            ElementCannotBeRemovedException, ElementIsReadOnly {
        form = FormUtils.createCompleteForm();
        Assert.assertNotNull(form);
        formDao.makePersistent(form);

        newVersionForm = form.createNewVersion(null);
        Assert.assertNotNull(newVersionForm);
        Assert.assertEquals((int) form.getVersion() + 1, (int) newVersionForm.getVersion());

        new FormVersionComparator(true).compare(form, newVersionForm);
        formDao.makePersistent(newVersionForm);

        formDao.makeTransient(form);
        formDao.makeTransient(newVersionForm);
    }

}
