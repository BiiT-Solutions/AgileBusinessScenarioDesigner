package com.biit.abcd.core.providers;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.hibernate.FormDao;
import com.biit.abcd.persistence.dao.hibernate.SimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public class FormProvider {

    private final FormDao formDao;
    private final SimpleFormViewDao simpleFormViewDao;

    public FormProvider(FormDao formDao, SimpleFormViewDao simpleFormViewDao) {
        this.formDao = formDao;
        this.simpleFormViewDao = simpleFormViewDao;
    }


    public Form get(Long formId) {
        return get(simpleFormViewDao.get(formId));
    }

    public Form saveForm(Form form) {
        form.setJson(form.getJson());
        return formDao.makePersistent(form);
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
        if (simpleFormView.getJson() != null && !simpleFormView.getJson().isEmpty()) {
            try {
                return Form.fromJson(simpleFormView.getJson());
            } catch (JsonProcessingException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
                return formDao.get(simpleFormView.getId());
            }
        }
        return formDao.get(simpleFormView.getId());
    }
}
