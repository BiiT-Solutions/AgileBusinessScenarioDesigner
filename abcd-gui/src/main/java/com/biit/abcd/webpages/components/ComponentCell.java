package com.biit.abcd.webpages.components;

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

import java.util.Iterator;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class ComponentCell extends CustomComponent implements LayoutClickNotifier {
	private static final long serialVersionUID = -3071199876634808049L;
	private static final String CLASSNAME = "v-edit-cell-component";
	private static final String ICON_CLASSNAME = "v-tree-designer-icon";

	private CssLayout rootLayout;
	private TouchCallBack touchCallback;
	private boolean callbackAttached;
	private boolean disabled;

	public ComponentCell() {
		callbackAttached = false;
		disabled = false;

		addStyleName(CLASSNAME);
		setImmediate(true);
		setSizeUndefined();

		rootLayout = new CssLayout();
		rootLayout.setWidth(null);
		rootLayout.setHeight(null);
		rootLayout.setImmediate(true);

		setCompositionRoot(rootLayout);
	}

	@Override
	public void attach() {
		super.attach();
		disabled = false;
		if (callbackAttached) {
			unregisterTouchCallback();
		}
		registerTouchCallback();
	}
	
	@Override
	public void detach(){
		if(disabled){
			return;
		}		
		super.detach();
		disabled = true;
	}

	public void clear() {
		if(disabled){
			return;
		}
		rootLayout.removeAllComponents();
	}

	public void addLabel(String value) {
		if(disabled){
			return;
		}
		Label label = new Label(value, ContentMode.HTML);
		label.setWidth(null);
		label.addStyleName("label-in-table");
		rootLayout.addComponent(label);
	}

	public void addIcon(ThemeIcon themeIcon) {
		if(disabled){
			return;
		}
		IconButton button = new IconButton(themeIcon, IconSize.SMALL, (String) null);
		button.addStyleName(ICON_CLASSNAME);
		rootLayout.addComponent(button);
	}

	public void addIconButton(ThemeIcon themeIcon, Button.ClickListener listener) {
		if(disabled){
			return;
		}
		IconButton button = new IconButton(themeIcon, IconSize.SMALL, (String) null);
		button.addClickListener(listener);
		rootLayout.addComponent(button);
	}

	@Override
	public void addLayoutClickListener(LayoutClickListener listener) {
		rootLayout.addLayoutClickListener(listener);
	}

	@Override
	@Deprecated
	public void addListener(LayoutClickListener listener) {
		addLayoutClickListener(listener);
	}

	@Override
	public void removeLayoutClickListener(LayoutClickListener listener) {
		rootLayout.removeLayoutClickListener(listener);
	}

	@Override
	@Deprecated
	public void removeListener(LayoutClickListener listener) {
		removeLayoutClickListener(listener);
	}

	public void registerTouchCallBack(Table table, Object itemId) {
		unregisterTouchCallback();
		touchCallback = new TouchCallBack(table, itemId);
		if (isAttached()) {
			registerTouchCallback();
		}
	}

	private void registerTouchCallback() {
		if (touchCallback != null) {
			addLayoutClickListener(touchCallback);
			Iterator<Component> componentItr = rootLayout.iterator();
			while (componentItr.hasNext()) {
				Component component = componentItr.next();
				if (component instanceof IconButton) {
					((IconButton) component).addClickListener(touchCallback);
				}
			}
			callbackAttached = true;
		}
	}

	private void unregisterTouchCallback() {
		if (touchCallback != null) {
			removeLayoutClickListener(touchCallback);
			Iterator<Component> componentItr = rootLayout.iterator();
			while (componentItr.hasNext()) {
				Component component = componentItr.next();
				if (component instanceof IconButton) {
					((IconButton) component).removeClickListener(touchCallback);
				}
			}
			callbackAttached = false;
		}
	}

	private class TouchCallBack implements LayoutClickListener, Button.ClickListener {
		private static final long serialVersionUID = -7149798680908270822L;
		private Table table;
		private Object itemId;

		public TouchCallBack(Table table, Object itemId) {
			this.table = table;
			this.itemId = itemId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			table.setValue(itemId);
		}

		@Override
		public void layoutClick(LayoutClickEvent event) {
			table.setValue(itemId);
		}
	}
}
