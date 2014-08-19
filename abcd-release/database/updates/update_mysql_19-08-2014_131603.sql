
    create table BaseRepeatableGroup (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        repetable bit not null,
        primary key (ID)
    );

    alter table BaseRepeatableGroup 
        drop constraint UK_ng1m1qsmuu6hbqdtgvcaolu13;

    alter table BaseRepeatableGroup 
        add constraint UK_ng1m1qsmuu6hbqdtgvcaolu13  unique (ID);

    alter table BaseRepeatableGroup 
        drop constraint UK_m1efhcrjm0sx5fx5e7i9xjl0v;

    alter table BaseRepeatableGroup 
        add constraint UK_m1efhcrjm0sx5fx5e7i9xjl0v  unique (comparationId);
