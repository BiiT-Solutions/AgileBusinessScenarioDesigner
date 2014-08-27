
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

    alter table expression_value_generic_variable 
        drop constraint UK_cqsa891g9cu7egmcxep74o4gh;

    alter table expression_value_generic_variable 
        add constraint UK_cqsa891g9cu7egmcxep74o4gh  unique (ID);

    alter table expression_value_generic_variable 
        drop constraint UK_1qdyf7b9pdbtg7ugajlcbuy99;

    alter table expression_value_generic_variable 
        add constraint UK_1qdyf7b9pdbtg7ugajlcbuy99  unique (comparationId);

    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);
