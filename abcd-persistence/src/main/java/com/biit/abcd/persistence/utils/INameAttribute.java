package com.biit.abcd.persistence.utils;

/**
 * Interface created to allow to retrieve the name of the classes that implement it
 *
 */
public interface INameAttribute {

	public String getName();

	public void setName(String name);

	@Override
	public String toString();
}
