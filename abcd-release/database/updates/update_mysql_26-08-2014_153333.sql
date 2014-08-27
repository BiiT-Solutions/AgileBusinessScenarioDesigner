
    create table generic_tree_object_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        scope varchar(255),
        primary key (ID)
    );

    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);

    alter table generic_tree_object_variable 
        drop constraint UK_jlb19chyeg3avh09uf95beaj8;

    alter table generic_tree_object_variable 
        add constraint UK_jlb19chyeg3avh09uf95beaj8  unique (ID);

    alter table generic_tree_object_variable 
        drop constraint UK_424js11diw8j55w2dds9f7793;

    alter table generic_tree_object_variable 
        add constraint UK_424js11diw8j55w2dds9f7793  unique (comparationId);
