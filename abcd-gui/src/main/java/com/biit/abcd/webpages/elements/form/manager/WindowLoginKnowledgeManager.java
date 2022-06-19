package com.biit.abcd.webpages.elements.form.manager;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.rest.client.KnowledgeManagerService;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class WindowLoginKnowledgeManager extends AcceptCancelWindow {

    private static final String WINDOW_WIDTH = "400px";
    private static final String WINDOW_HEIGHT = "300px";

    private static final String USERNAME_LABEL_CONTENT = LanguageCodes.WINDOW_LOGIN_KNOWLEDGE_MANAGER_USERNAME_LABEL_CONTENT.translate();
    private static final String PASSWORD_LABEL_CONTENT = LanguageCodes.WINDOW_LOGIN_KNOWLEDGE_MANAGER_PASSWORD_LABEL_CONTENT.translate();
    private TextField usernameField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private Label passwordLabel;
    private KnowledgeManagerService knowledgeManagerService;

    public WindowLoginKnowledgeManager() {
        super();
        configure();
        setContent(generate());
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        knowledgeManagerService = (KnowledgeManagerService) helper.getBean("knowledgeManagerService");
        addAcceptActionListener(new AcceptActionListener() {
            @Override
            public void acceptAction(AcceptCancelWindow window) {
                try {
                    CloseableHttpResponse response = knowledgeManagerService.login(usernameField.getValue(), passwordField.getValue(),
                            UserSessionHandler.getUser().getUniqueId());
                    if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 299) {
                        MessageManager.showInfo(LanguageCodes.CAPTION_SUCCESS_LOGIN_KNOWLEDGE_MANAGER);
                    } else {
                        if(response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() <= 404) {
                            MessageManager.showError(LanguageCodes.CAPTION_BAD_LOGIN_KNOWLEDGE_MANAGER);
                        } else {
                            MessageManager.showError(LanguageCodes.CAPTION_ERROR_LOGIN_KNOWLEDGE_MANAGER);
                        }
                    }
                }catch (IOException e) {
                    AbcdLogger.errorMessage(WindowLoginKnowledgeManager.class.toString(), e.toString());
                }
            }
        });
    }

    private Component generate() {
        VerticalLayout rootLayout = new VerticalLayout();
        usernameField = new TextField();
        passwordField = new PasswordField();
        usernameLabel = new Label(USERNAME_LABEL_CONTENT);
        passwordLabel = new Label(PASSWORD_LABEL_CONTENT);

        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setSpacing(true);
        usernameLayout.addComponent(usernameLabel);
        usernameLayout.addComponent(usernameField);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        passwordLayout.setSpacing(true);
        passwordLayout.addComponent(passwordLabel);
        passwordLayout.addComponent(passwordField);

        rootLayout.setSpacing(true);
        rootLayout.setSizeFull();

        rootLayout.addComponent(usernameLayout);
        rootLayout.addComponent(passwordLayout);

        return rootLayout;
    }

    private void configure() {
        setDraggable(true);
        setResizable(false);
        setModal(true);
        setWidth(WINDOW_WIDTH);
        setHeight(WINDOW_HEIGHT);
    }

}
