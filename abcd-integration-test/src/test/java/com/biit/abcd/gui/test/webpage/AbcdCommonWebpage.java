package com.biit.abcd.gui.test.webpage;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.gui.test.window.InfoWindow;
import com.biit.abcd.gui.test.window.ProceedWindow;
import com.biit.abcd.gui.test.window.WarningUnsavedData;
import com.biit.gui.tester.TestbenchHelper;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.NotificationElement;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

public class AbcdCommonWebpage extends VaadinGuiWebpage {

    protected static final String CSS_CLASS = "class";
    protected static final String TABLE_ROW_SELECTED = "v-selected";

    private static final String SAVE_BUTTON = "Save";
    private final static String SETTINGS_BUTTON = "settingsButton";
    private final static String LOGOUT_BUTTON = "logoutButton";

    private final InfoWindow infoWindow;
    private final ProceedWindow proceedWindow;
    private final WarningUnsavedData warningUnsavedData;

    public AbcdCommonWebpage() {
        super();
        infoWindow = new InfoWindow();
        proceedWindow = new ProceedWindow();
        warningUnsavedData = new WarningUnsavedData();
        addWindow(infoWindow);
        addWindow(proceedWindow);
        addWindow(warningUnsavedData);
    }

    @Override
    public String getWebpageUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    public void openAndCloseInfoScreen() {
        getInfoButton().click();
        infoWindow.clickCloseButton();
    }

    private ButtonElement getInfoButton() {
        toggleSettings();
        return $(ButtonElement.class).caption("Info").first();
    }

    public void toggleSettings() {
        $(ButtonElement.class).id(SETTINGS_BUTTON).click();
    }

    public void logOut() {
        toggleSettings();
        $(ButtonElement.class).id(LOGOUT_BUTTON).click();
    }

    public ButtonElement getClearCache() {
        toggleSettings();
        ElementQuery<ButtonElement> button = $(ButtonElement.class).caption("Clear Cache");
        if (button.exists()) {
            return button.first();
        }
        return null;
    }

    public void clickClearCache() {
        ButtonElement button = getClearCache();
        try {
            button.waitForVaadin();
        } catch (NullPointerException | NoSuchElementException e) {
            // Already closed.
        }
        Assert.assertTrue(button.isEnabled());
        button.click();
        proceedWindow.clickAccept();
    }

    public void clickAcceptProceed() {
        proceedWindow.clickAccept();
    }

    public ProceedWindow getProceedWindow() {
        return proceedWindow;
    }

    public void clickFormDesigner() {
        $(ButtonElement.class).caption("Form Designer").first().click();
    }

    public void clickFormVariables() {
        $(ButtonElement.class).caption("Form Variables").first().click();
    }

    public void clickDiagramDesigner() {
        $(ButtonElement.class).caption("Diagram Designer").first().click();
    }

    public void clickRuleExpressionEditor() {
        $(ButtonElement.class).caption("Rule Expression Editor").first().click();
    }

    public void clickRuleEditor() {
        $(ButtonElement.class).caption("Rule Editor").first().click();
    }

    public void clickRuleTableEditor() {
        $(ButtonElement.class).caption("Rule Table Editor").first().click();
    }

    public void goToFormManager() {
        $(ButtonElement.class).caption("Forms").first().click();
    }

    public WarningUnsavedData getWarningUnsavedData() {
        return warningUnsavedData;
    }

    public void save() {
        getButtonByCaption(SAVE_BUTTON).click();
        getButtonByCaption(SAVE_BUTTON).waitForVaadin();
        while (true) {

            ElementQuery<NotificationElement> notification = $(NotificationElement.class);
            if (notification.exists()) {
                TestbenchHelper.checkNotificationIsHumanized(notification.first());
                return;
            }
        }
    }
}
