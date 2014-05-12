package com.biit.abcd.webpages.components;

import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalButtonGroup extends CustomComponent {

	private static final long serialVersionUID = 4862986305501412362L;
	private static String CLASSNAME = "v-horizontal-button-group";
	private HorizontalLayout rootLayout;
	private String size;
	private boolean contractIcons;

	public HorizontalButtonGroup() {
		setStyleName(CLASSNAME);

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(false);
		setCompositionRoot(rootLayout);
		setSizeFull();

		contractIcons = false;

		addAttachListener(new AttachListener() {
			private static final long serialVersionUID = -2513076537414804598L;

			@Override
			public void attach(AttachEvent event) {
				setIconSize();
			}
		});
	}

	public void addIconButton(IconButton button) {
		rootLayout.addComponent(button);
		button.setSizeFull();
	}

	public void setContractIcons(boolean contractIcons) {
		this.contractIcons = contractIcons;
		this.size = null;
	}
	
	public void setContractIcons(boolean contractIcons, String size) {
		this.contractIcons = contractIcons;
		this.size = size;
	}

	private void setIconSize() {
		Iterator<Component> itr = rootLayout.iterator();
		if(contractIcons){
			rootLayout.setWidth(null);
		}

		while (itr.hasNext()) {
			Component component = itr.next();
			rootLayout.setExpandRatio(component, 0.0f);
			if (contractIcons) {
				component.setWidth(size);
			} else {
				component.setWidth("100%");
			}
		}
		
		markAsDirtyRecursive();
	}

}
