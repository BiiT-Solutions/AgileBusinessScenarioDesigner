package com.biit.abcd.webpages.entity.comparator;

import java.util.Comparator;

import com.biit.abcd.persistence.entity.StorableObject;

public class StorableObjectCompareByModificationDate implements Comparator<StorableObject> {

	@Override
	public int compare(StorableObject arg0, StorableObject arg1) {
		return arg0.getUpdateTime().compareTo(arg1.getUpdateTime());
	}

}
