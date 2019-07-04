package com.biit.abcd.webpages.components;

/**
 * Callback class to do the action
 */
public interface SaveAction {

	/**
	 * Returns if is valid to save or not. This serves to implement a validation
	 * condition previous to a download.
	 * 
	 * @return true if is valid.
	 */
	boolean isValid();

	byte[] getInformationData();

	/**
	 * Extension of the file to generate. Also must be the type of file in
	 * graphviz.
	 * 
	 * @return the extension of the file.S
	 */
	String getExtension();

	/**
	 * Mimetype of the generated file ("application/pdf", "image/png", ...)
	 * 
	 * @return the mimetype.
	 */
	String getMimeType();

	/**
	 * Gets the file name.
	 * 
	 * @return the file name.
	 */
	String getFileName();

}
