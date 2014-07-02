package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class JsonDiagramPropertiesLink extends PropertiesForClassComponent<DiagramLink> {
	private static final long serialVersionUID = 6308407654774598230L;
	private DiagramLink instance;
	private TextField diagramElementLabel;

	public JsonDiagramPropertiesLink() {
		super(DiagramLink.class);
	}

	@Override
	public void setElementAbstract(DiagramLink element) {
		instance = element;

		FormLayout linkForm = new FormLayout();
		linkForm.setWidth(null);

		if (instance.getSourceElement() instanceof DiagramFork) {
			DiagramFork fork = (DiagramFork) element.getSourceElement();
			if(fork.getQuestion()!=null){
				if(fork.getQuestion().getAnswerType() == AnswerType.INPUT ){
					//Fill input field value (Expression?)
					
				}else{
					//Select question/others
					
				}
			}
		} else {
			diagramElementLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
			if (instance.getText() == null) {
				diagramElementLabel.setValue("");
			} else {
				diagramElementLabel.setValue(instance.getText());
			}
			linkForm.addComponent(diagramElementLabel);
		}

		addTab(linkForm, "TODO - diagramLinkExprProperties", true, 0);
	}

	@Override
	public void updateElement() {
		if (instance.getSourceElement() instanceof DiagramFork) {
			
		}else{
			instance.setText(diagramElementLabel.getValue());
		}
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}