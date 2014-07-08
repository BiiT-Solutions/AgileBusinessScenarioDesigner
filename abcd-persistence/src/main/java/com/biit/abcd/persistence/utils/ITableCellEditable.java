package com.biit.abcd.persistence.utils;

/**
 * Interface created to allow the name edition in the vaadin tables
 * All the classes that appear in an independent editable cell must implement this interface
 * @author Alfonso
 *
 */
public interface ITableCellEditable {
	
	public String getName();

	public void setName(String name);

	public String toString();
}
