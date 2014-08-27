
    create table expression_value_generic_custom_variable (
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

    alter table expression_value_generic_custom_variable 
        drop constraint UK_1ilcykdnhd0ixr9iaomtrog3h;

    alter table expression_value_generic_custom_variable 
        add constraint UK_1ilcykdnhd0ixr9iaomtrog3h  unique (ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_72ucvxvkp3dgy4ci55hda72p5;

    alter table expression_value_generic_custom_variable 
        add constraint UK_72ucvxvkp3dgy4ci55hda72p5  unique (comparationId);

    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);

    alter table generic_tree_object_variable 
        drop constraint UK_8p2ila1hcx266jhxtkygthqvb;

    alter table generic_tree_object_variable 
        add constraint UK_8p2ila1hcx266jhxtkygthqvb  unique (type);

    alter table expression_value_generic_custom_variable 
        add constraint FK_mur266gqhqw4alh8k227cv1hd 
        foreign key (reference_ID) 
        references generic_tree_object_variable (ID);

    alter table expression_value_generic_custom_variable 
        add constraint FK_4u49ngxh67i7rqxx3xb7379yi 
        foreign key (variable_ID) 
        references form_custom_variables (ID);
