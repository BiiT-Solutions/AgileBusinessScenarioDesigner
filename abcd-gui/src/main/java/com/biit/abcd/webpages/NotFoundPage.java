package com.biit.abcd.webpages;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.components.ThemeIcon;


public class NotFoundPage extends ErrorPage{
	private static final long serialVersionUID = -5449790417580691336L;

	public NotFoundPage() {
		super();
		
		setLabelContent(ServerTranslate.translate(LanguageCodes.PAGE_NOT_FOUND));
		setImageSource(ThemeIcon.PAGE_NOT_FOUND.getThemeResource());
	}
}