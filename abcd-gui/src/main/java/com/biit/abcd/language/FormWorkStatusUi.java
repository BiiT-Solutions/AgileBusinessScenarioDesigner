package com.biit.abcd.language;

import com.biit.abcd.persistence.entity.FormWorkStatus;

public enum FormWorkStatusUi {

	DESIGN(FormWorkStatus.DESIGN, LanguageCodes.CAPTION_FORM_WORK_STATUS_DESIGN),

	FINAL_DESIGN(FormWorkStatus.FINAL_DESIGN, LanguageCodes.CAPTION_FORM_WORK_STATUS_FINAL_DESIGN);

	private FormWorkStatus formStatus;
	private LanguageCodes languageCode;

	private FormWorkStatusUi(FormWorkStatus formStatus, LanguageCodes languageCode) {
		this.formStatus = formStatus;
		this.languageCode = languageCode;
	}

	public FormWorkStatus getFormWorkStatus() {
		return formStatus;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}
}
