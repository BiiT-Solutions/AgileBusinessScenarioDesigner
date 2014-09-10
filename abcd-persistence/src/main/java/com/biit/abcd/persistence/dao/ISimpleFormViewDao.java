package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;

public interface ISimpleFormViewDao {

	int getRowCount();

	List<SimpleFormView> getAll();

}
