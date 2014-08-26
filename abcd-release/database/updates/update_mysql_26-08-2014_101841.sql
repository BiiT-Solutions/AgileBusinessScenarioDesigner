
    create table form_scope_all_variables (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        scope varchar(255),
        primary key (ID)
    );

    alter table form_scope_all_variables 
        drop constraint UK_tgkdge5p37prx8ufvm714vwfe;

    alter table form_scope_all_variables 
        add constraint UK_tgkdge5p37prx8ufvm714vwfe  unique (ID);

    alter table form_scope_all_variables 
        drop constraint UK_gbruwi5al986am17sd2tk3qbh;

    alter table form_scope_all_variables 
        add constraint UK_gbruwi5al986am17sd2tk3qbh  unique (comparationId);
