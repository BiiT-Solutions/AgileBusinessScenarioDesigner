
    create table expression_value_systemdate (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        isEditable bit not null,
        value datetime,
        primary key (ID)
    );

    alter table expression_value_systemdate 
        drop constraint UK_mj1pnbvm8gv3gngq2yh15502f;

    alter table expression_value_systemdate 
        add constraint UK_mj1pnbvm8gv3gngq2yh15502f  unique (ID);

    alter table expression_value_systemdate 
        drop constraint UK_co43pbqqg7civhr3g8p752mt8;

    alter table expression_value_systemdate 
        add constraint UK_co43pbqqg7civhr3g8p752mt8  unique (comparationId);
