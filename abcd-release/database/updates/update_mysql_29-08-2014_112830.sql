
    create table expression_value_generic_custom_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        isEditable bit not null,
        type varchar(255),
        variable_ID bigint,
        primary key (ID)
    );

    create table expression_value_generic_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        isEditable bit not null,
        type varchar(255),
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

    alter table expression_value_generic_variable 
        drop constraint UK_cqsa891g9cu7egmcxep74o4gh;

    alter table expression_value_generic_variable 
        add constraint UK_cqsa891g9cu7egmcxep74o4gh  unique (ID);

    alter table expression_value_generic_variable 
        drop constraint UK_1qdyf7b9pdbtg7ugajlcbuy99;

    alter table expression_value_generic_variable 
        add constraint UK_1qdyf7b9pdbtg7ugajlcbuy99  unique (comparationId);

    alter table expression_value_generic_custom_variable 
        add constraint FK_4u49ngxh67i7rqxx3xb7379yi 
        foreign key (variable_ID) 
        references form_custom_variables (ID);
