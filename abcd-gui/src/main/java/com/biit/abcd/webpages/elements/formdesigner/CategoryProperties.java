package com.biit.abcd.webpages.elements.formdesigner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.DocumentException;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.DroolsSubmittedFormResultWindow;
import com.biit.abcd.webpages.elements.testscenario.WindowLaunchTestScenarioCategory;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class CategoryProperties extends SecuredFormElementProperties<Category> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Category instance;
	private TextField categoryLabel;
	private final String TECHNICAL_NAME_VALIDATOR_REGEX = "([A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff_])([0-9A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff\\_\\xb7]){2,}";

	public CategoryProperties() {
		super(Category.class);
	}

	@Override
	public void setElementForProperties(Category element) {
		instance = element;
		categoryLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		categoryLabel.setValue(instance.getName());
		categoryLabel.addValidator(new RegexpValidator(TECHNICAL_NAME_VALIDATOR_REGEX, ServerTranslate
				.translate(LanguageCodes.TECHNICAL_NAME_ERROR)));

		FormLayout categoryFormLayout = new FormLayout();
		categoryFormLayout.setWidth(null);
		categoryFormLayout.addComponent(categoryLabel);

		addTab(categoryFormLayout,
				ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION), true, 0);

		// TODO finish functionality to allow test individual categories
//		addTab(createLaunchCategoryTestButton(), "TEST", true);
	}

	@Override
	protected void updateConcreteFormElement() {
		String instanceName = instance.getName();
		try {
			instance.setName(categoryLabel.getValue());
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has modified the Category '" + instanceName + "' property 'Name' to '" + instance.getName()
					+ "'.");
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				try {
					instance.setName(categoryLabel.getValue().substring(0, 185));
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Category '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "' (Name too long).");
				} catch (CharacterNotAllowedException e1) {
					MessageManager.showWarning(
							ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate
									.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
											instance.getName(), instance.getSimpleAsciiName() }));
					try {
						instance.setName(instance.getSimpleAsciiName());
					} catch (CharacterNotAllowedException e2) {
						// Impossible.
					}
				}
			} catch (FieldTooLongException e1) {
				// Impossible.
			}
		} catch (CharacterNotAllowedException e) {
			MessageManager.showWarning(
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS),
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
							instance.getName(), instance.getSimpleAsciiName() }));
			try {
				instance.setName(instance.getSimpleAsciiName());
			} catch (FieldTooLongException | CharacterNotAllowedException e1) {
				// Impossible.
			}
		}
		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(categoryLabel));
	}

	private Button createLaunchCategoryTestButton() {
		Button launchCategoryTest = new Button("Test");
		launchCategoryTest.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1430183207737163347L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					final WindowLaunchTestScenarioCategory testCategoryWindow = new WindowLaunchTestScenarioCategory(
							instance);
					testCategoryWindow.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							TestScenario categoryTestScenario = testCategoryWindow.getTestScenario();
							Form form = testCategoryWindow.getForm();
							if ((form != null) && (categoryTestScenario != null)) {
								try {
									FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
									ISubmittedForm submittedForm;
									submittedForm = droolsExporter.processForm(form, UserSessionHandler
											.getGlobalVariablesController().getGlobalVariables(), categoryTestScenario);
									if (submittedForm instanceof DroolsForm) {
										final DroolsSubmittedFormResultWindow droolsResultWindow = new DroolsSubmittedFormResultWindow(
												((DroolsForm) submittedForm).getSubmittedForm(), form);
										droolsResultWindow.addAcceptActionListener(new AcceptActionListener() {
											@Override
											public void acceptAction(AcceptCancelWindow window) {
												droolsResultWindow.close();
											}
										});
										droolsResultWindow.showCentered();
									}
								} catch (ExpressionInvalidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (RuleInvalidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (RuleNotImplementedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (DocumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CategoryNameWithoutTranslation e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ActionNotImplementedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CategoryDoesNotExistException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (GroupDoesNotExistException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								testCategoryWindow.close();
							}
						}
					});
					testCategoryWindow.showCentered();
				} catch (NotValidChildException | FieldTooLongException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return launchCategoryTest;
	}
}