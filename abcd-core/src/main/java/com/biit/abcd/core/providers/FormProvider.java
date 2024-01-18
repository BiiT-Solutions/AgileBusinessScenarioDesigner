package com.biit.abcd.core.providers;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormProvider {

    @Autowired
    private IFormDao formDao;

    @Autowired
    private ISimpleFormViewDao simpleFormViewDao;

    public FormProvider() {
        super();
    }


    public Form get(Long formId) {
        return get(simpleFormViewDao.get(formId));
    }

    public Form saveForm(Form form) {
        form.setJson(form.toJson());
        //Save from json.
        try {
            Form mutilatedForm = form.copy(form.getCreatedBy(), form.getLabel());
            mutilatedForm.setId(form.getId());
            mutilatedForm.setJson(form.toJson());
            //Delete all children and rules from form to speed up save (as are stored as Json).
            mutilatedForm.getChildren().clear();
            mutilatedForm.getCustomVariables().clear();
            mutilatedForm.getDiagrams().clear();
            mutilatedForm.getTableRules().clear();
            mutilatedForm.getCustomVariables().clear();
            mutilatedForm.getExpressionChains().clear();
            mutilatedForm.getRules().clear();
            if (mutilatedForm.getId() != null) {
                formDao.merge(mutilatedForm);
                return form;
            } else {
                mutilatedForm = formDao.makePersistent(mutilatedForm);
                form.setId(mutilatedForm.getId());
                //Store id on json
                mutilatedForm.setJson(mutilatedForm.toJson());
                formDao.merge(mutilatedForm);
                return form;
            }
        } catch (CharacterNotAllowedException | NotValidStorableObjectException | FieldTooLongException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        //Save old way
        if (form.getId() != null) {
            return formDao.merge(form);
        } else {
            return formDao.makePersistent(form);
        }
    }

    public void deleteForm(Form form) throws ElementCannotBeRemovedException {
        formDao.makeTransient(form);
    }

    public void updateFormStatus(Long id, FormWorkStatus value) throws UnexpectedDatabaseException {
        formDao.updateFormStatus(id, value);
    }

    public Form get(SimpleFormView simpleFormView) {
        if (simpleFormView == null) {
            return null;
        }
        final SimpleFormViewWithContent simpleFormViewWithContent = simpleFormViewDao.get(simpleFormView.getId());
        if (simpleFormViewWithContent.getJson() != null && !simpleFormViewWithContent.getJson().isEmpty()) {
            try {
                AbcdLogger.debug(this.getClass().getName(), "Obtaining form '" + simpleFormView.getLabel() + "' from json structure.");
                return Form.fromJson(simpleFormViewWithContent.getJson());
            } catch (JsonProcessingException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
                return formDao.get(simpleFormView.getId());
            }
        }
        AbcdLogger.debug(this.getClass().getName(), "Obtaining form '" + simpleFormView.getLabel() + "' from standard database.");
        return formDao.get(simpleFormView.getId());
    }
}
