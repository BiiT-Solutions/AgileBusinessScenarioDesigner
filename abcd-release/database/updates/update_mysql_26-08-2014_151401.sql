
    create table expression_value_generic_tree_object_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        isEditable bit not null,
        unit varchar(255),
        reference_ID bigint,
        variable_ID bigint,
        primary key (ID)
    );

    create table generic_tree_objec_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        scope varchar(255),
        primary key (ID)
    );

    alter table expression_value_generic_tree_object_variable 
        drop constraint UK_7n8ku4ak0rdfunmtaxaujcam5;

    alter table expression_value_generic_tree_object_variable 
        add constraint UK_7n8ku4ak0rdfunmtaxaujcam5  unique (ID);

    alter table expression_value_generic_tree_object_variable 
        drop constraint UK_7e0lf1yib64j0snwa63051rsp;

    alter table expression_value_generic_tree_object_variable 
        add constraint UK_7e0lf1yib64j0snwa63051rsp  unique (comparationId);

    alter table generic_tree_objec_variable 
        drop constraint UK_syp6avkb1p0vie24hjnc2c2lc;

    alter table generic_tree_objec_variable 
        add constraint UK_syp6avkb1p0vie24hjnc2c2lc  unique (ID);

    alter table generic_tree_objec_variable 
        drop constraint UK_4el3985122iyitgocq5lk2tqu;

    alter table generic_tree_objec_variable 
        add constraint UK_4el3985122iyitgocq5lk2tqu  unique (comparationId);

    alter table expression_value_generic_tree_object_variable 
        add constraint FK_ed3ebxjhwouw5wi3wt1rhkmyi 
        foreign key (variable_ID) 
        references generic_tree_objec_variable (ID);
