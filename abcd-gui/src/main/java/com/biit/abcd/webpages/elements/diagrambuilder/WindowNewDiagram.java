package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.webpages.FormDiagramBuilder;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindowNewDiagram extends WindowCreateNewObject {
	private static final long serialVersionUID = -733085811793240174L;

	public WindowNewDiagram(FormDiagramBuilder parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		if ((inputTextField.getValue() != null) && !inputTextField.getValue().isEmpty()) {
			List<Diagram> diagrams = UserSessionHandler.getFormController().getForm().getDiagrams();
			for (Diagram diagram : diagrams) {
				if (diagram.getName().equals(inputTextField.getValue())) {
					MessageManager.showError(LanguageCodes.ERROR_DIAGRAM_REPEATED_NAME);
					return;
				}
			}

			// If is a valid string and there is no other diagram with the same name, then add it.
			Diagram newDiagram = new Diagram(inputTextField.getValue());
			UserSessionHandler.getFormController().getForm().addDiagram(newDiagram);
			((FormDiagramBuilder) getParentWindow()).addDiagram(newDiagram);
			((FormDiagramBuilder) getParentWindow()).sortTableMenu();
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has created a " + newDiagram.getClass() + " with 'Name: " + newDiagram.getName() + "'.");
			close();
		} else {
			MessageManager.showError(LanguageCodes.ERROR_NAME_NOT_VALID);
		}
	}

}
