package com.biit.abcd.persistence.entity;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.jackson.serialization.BaseRepeatableGroupDeserializer;
import com.biit.form.jackson.serialization.BaseRepeatableGroupSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tree_groups")
@JsonDeserialize(using = BaseRepeatableGroupDeserializer.class)
@JsonSerialize(using = BaseRepeatableGroupSerializer.class)
@Cacheable(true)
public class Group extends BaseRepeatableGroup {
    private static final long serialVersionUID = -7455213859023593111L;

    public Group() {
        super();
    }

    public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super(name);
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        super.copyData(object);
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        CheckDependencies.checkTreeObjectDependencies(this);
    }
}
