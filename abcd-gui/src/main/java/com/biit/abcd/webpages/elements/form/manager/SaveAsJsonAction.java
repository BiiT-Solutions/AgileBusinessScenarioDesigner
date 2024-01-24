package com.biit.abcd.webpages.elements.form.manager;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.SaveAction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SaveAsJsonAction implements SaveAction {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public byte[] getInformationData() {
        try {
            InputStream is = new ByteArrayInputStream(UserSessionHandler.getFormController().getForm().toJson().getBytes("UTF-8"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int reads = is.read();
            while (reads != -1) {
                baos.write(reads);
                reads = is.read();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            AbcdLogger.errorMessage(SaveAsJsonAction.class.getName(), e);
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
        }
        return null;
    }

    @Override
    public String getExtension() {
        return "json";
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public String getFileName() {
        return UserSessionHandler.getFormController().getForm().getLabel() + "_"
                + UserSessionHandler.getFormController().getForm().getVersion() + "." + getExtension();
    }

}
