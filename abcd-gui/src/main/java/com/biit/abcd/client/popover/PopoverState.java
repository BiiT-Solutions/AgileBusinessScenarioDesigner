package com.biit.abcd.client.popover;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.window.WindowState;

public class PopoverState extends WindowState {
	private static final long serialVersionUID = 6684264993440891875L;
	private boolean fullscreen;
    private Connector relatedComponent;

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public Connector getRelatedComponent() {
        return relatedComponent;
    }

    public void setRelatedComponent(Connector relativeComponent) {
        this.relatedComponent = relativeComponent;
    }
}
