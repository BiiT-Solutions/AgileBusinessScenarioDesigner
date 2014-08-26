
    create table expression_value_function_all_variable (
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

    alter table expression_value_function_all_variable 
        drop constraint UK_byouygbe8ycwptp7xv2gu6hg1;

    alter table expression_value_function_all_variable 
        add constraint UK_byouygbe8ycwptp7xv2gu6hg1  unique (ID);

    alter table expression_value_function_all_variable 
        drop constraint UK_od7h6by5nusxwf71whlumpnyx;

    alter table expression_value_function_all_variable 
        add constraint UK_od7h6by5nusxwf71whlumpnyx  unique (comparationId);

    alter table expression_value_function_all_variable 
        add constraint FK_ctvvsq1omvk7r50ih2aarl36w 
        foreign key (variable_ID) 
        references form_scope_all_variables (ID);
