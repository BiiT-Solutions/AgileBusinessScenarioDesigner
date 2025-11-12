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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Open a window with a download button and allows the user to download a file
 * with some data obtained from database.
 *
 */
public class SaveAsButton extends IconButton {
	private static final long serialVersionUID = 972763993558067752L;
	private SaveButtonWindow newSaveWindow;
	private SaveAction saveAction;
	private boolean launchAction = true;
	private byte[] data;
	private List<SaveActionListener> acceptListeners;

	public SaveAsButton(final LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip, IconSize size,
			SaveAction saveActionCall) {
		super(caption, icon, tooltip, size);
		acceptListeners = new ArrayList<>();
		createAction(caption, saveActionCall);
	}

	public SaveAsButton(final LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip, SaveAction saveActionCall) {
		super(caption, icon, tooltip);
		acceptListeners = new ArrayList<>();
		createAction(caption, saveActionCall);
	}

	private void createAction(final LanguageCodes caption, SaveAction saveActionCall) {
		this.saveAction = saveActionCall;
		setImmediate(true);
		this.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 186068333738000082L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (saveAction != null && launchAction) {
					if (saveAction.isValid()) {
						newSaveWindow = new SaveButtonWindow("Download file...");
						UI.getCurrent().addWindow(newSaveWindow);
						// We only start to load the resource when the windows
						// is created. As Vaadin does not control it well, I use
						// a focus listener to ensure that the windows is
						// displayed with the message.
						newSaveWindow.addFocusListener(new FocusListener() {

							@Override
							public void focus(FocusEvent event) {
								if (data == null) {
									fireSaveActionListeners();
									data = saveAction.getInformationData();

									// Show download data window.
									if (data != null) {
										try {
											AbcdLogger.info(this.getClass().getName(), "User '"
													+ UserSessionHandler.getUser().getEmailAddress()
													+ "' has created the data file in '" + caption.toString() + "'.");
										} catch (Exception e) {
											AbcdLogger.errorMessage(this.getClass().getName(), e);
										}
										newSaveWindow.loadResource();
									} else {
										AbcdLogger.info(this.getClass().getName(),
												"User '" + UserSessionHandler.getUser().getEmailAddress()
														+ "' has an error when trying to created the data in '"
														+ caption.toString() + "'.");
										newSaveWindow.showError();
									}

								}
							}
						});
						// Force the focus to the window to trigger the
						// getResource method.
						newSaveWindow.setFocus();
					}
				}
			}
		});
	}

	/**
	 * Window with download button.
	 */
	private class SaveButtonWindow extends Window {
		private Label labelTextField;
		private static final long serialVersionUID = 8215514233447669989L;
		private VerticalLayout layout;

		public SaveButtonWindow(String title) {
			setCaption(title);
			setModal(true);
			setResizable(false);
			center();
			addCloseAction();

			layout = new VerticalLayout();
			layout.setSizeUndefined();
			layout.setSpacing(true);
			layout.setMargin(true);

			labelTextField = new Label(ServerTranslate.translate(LanguageCodes.SAVE_AS_FILE_CREATING));
			layout.addComponent(labelTextField);
			this.setContent(layout);
		}

		private void addCloseAction() {
			addCloseListener((CloseListener) e -> {
                // Reset data to allow recreation.
                data = null;
            });
		}

		public void loadResource() {
			StreamResource streamResource = getResource();

			if (streamResource != null) {
				labelTextField.setValue(ServerTranslate.translate(LanguageCodes.SAVE_AS_FILE_CREATED));
				center();

				Button downloadButton = new IconButton(LanguageCodes.BUTTON_DOWNLOAD, ThemeIcon.DOWNLOAD,
						LanguageCodes.BUTTON_DOWNLOAD, IconSize.SMALL, new ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								try {
									AbcdLogger.info(this.getClass().getName(), "User '"
											+ UserSessionHandler.getUser().getEmailAddress()
											+ "' has pressed the download button.");
								} catch (Exception e) {
									AbcdLogger.errorMessage(this.getClass().getName(), e);
								}
							}
						});

				FileDownloader fileDownloader = new FileDownloader(streamResource);
				fileDownloader.extend(downloadButton);

				layout.addComponent(downloadButton);
			} else {
				showError();
			}

		}

		public void showError() {
			labelTextField.setValue(ServerTranslate.translate(LanguageCodes.SAVE_AS_FILE_FAILED));
			center();
		}

		public void setFocus() {
			focus();
		}

		private StreamResource getResource() {
			StreamResource resource = null;
			try {
				// Vaadin manage it like a file to download.
				StreamSource streamSource = () -> new ByteArrayInputStream(data);

				// Allow user to download it.
				resource = new StreamResource(streamSource, saveAction.getFileName());

				resource.setMIMEType(saveAction.getMimeType());
				resource.setCacheTime(-1);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
			return resource;
		}
	}

	public SaveAction getSaveActionCallback() {
		return saveAction;
	}

	public void setSaveActionCallback(SaveAction saveActionCallback) {
		this.saveAction = saveActionCallback;
	}

	public void addSaveActionListener(SaveActionListener listener) {
		acceptListeners.add(listener);
	}

	public void removeSaveActionListener(SaveActionListener listener) {
		acceptListeners.remove(listener);
	}

	protected void fireSaveActionListeners() {
		for (SaveActionListener listener : acceptListeners) {
			listener.saveAction();
		}
	}

	public void setLaunchAction(boolean launchAction) {
		this.launchAction = launchAction;
	}

}
