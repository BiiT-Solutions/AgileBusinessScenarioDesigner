package com.biit.abcd.client.popover;

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
