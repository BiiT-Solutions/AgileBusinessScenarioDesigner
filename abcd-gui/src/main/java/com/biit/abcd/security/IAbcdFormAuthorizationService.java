package com.biit.abcd.security;

import com.biit.abcd.core.security.ISecurityService;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.usermanager.entity.IUser;

public interface IAbcdFormAuthorizationService extends ISecurityService {

	boolean isFormReadOnly(Form form, IUser<Long> user);

	boolean isFormReadOnly(SimpleFormView form, IUser<Long> user);

	boolean isFormAlreadyInUse(Long formId, IUser<Long> user);

}
