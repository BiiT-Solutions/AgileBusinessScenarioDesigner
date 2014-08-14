
    create table BaseAnswer (
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
        primary key (ID)
    );

    create table BaseCategory (
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
        primary key (ID)
    );

    create table BaseForm (
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
        version integer,
        primary key (ID)
    );

    create table BaseGroup (
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

    create table BaseQuestion (
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
        primary key (ID)
    );

    alter table PARENT_OF_CHILDREN 
        add column TreeObject_ID bigint not null;

    alter table TREE_ANSWERS 
        add column label varchar(255);

    alter table TREE_CATEGORIES 
        add column label varchar(255);

    alter table TREE_FORMS 
        add column label varchar(255);

    alter table TREE_GROUPS 
        add column label varchar(255);

    alter table TREE_QUESTIONS 
        add column label varchar(255);

    alter table BaseAnswer 
        drop constraint UK_76lq0gmnjriqwvt0axvjt4h8s;

    alter table BaseAnswer 
        add constraint UK_76lq0gmnjriqwvt0axvjt4h8s  unique (ID);

    alter table BaseAnswer 
        drop constraint UK_djol5d7vg4wvgutb2l6cj5p3y;

    alter table BaseAnswer 
        add constraint UK_djol5d7vg4wvgutb2l6cj5p3y  unique (comparationId);

    alter table BaseCategory 
        drop constraint UK_ey647aoo30r3uqxirrm3v7jwa;

    alter table BaseCategory 
        add constraint UK_ey647aoo30r3uqxirrm3v7jwa  unique (ID);

    alter table BaseCategory 
        drop constraint UK_l6dm3qlnaolwpi2k56utdasqr;

    alter table BaseCategory 
        add constraint UK_l6dm3qlnaolwpi2k56utdasqr  unique (comparationId);

    alter table BaseForm 
        drop constraint UK_3n8x6f9irekyp7of2b7gibpio;

    alter table BaseForm 
        add constraint UK_3n8x6f9irekyp7of2b7gibpio  unique (name, version);

    alter table BaseForm 
        drop constraint UK_9mtlfju71ni1foihstunmc72e;

    alter table BaseForm 
        add constraint UK_9mtlfju71ni1foihstunmc72e  unique (ID);

    alter table BaseForm 
        drop constraint UK_g7segt6l5tegemkbp78vk02bc;

    alter table BaseForm 
        add constraint UK_g7segt6l5tegemkbp78vk02bc  unique (comparationId);

    alter table BaseGroup 
        drop constraint UK_2jv9tjr1cl5ijdaueliw7ye25;

    alter table BaseGroup 
        add constraint UK_2jv9tjr1cl5ijdaueliw7ye25  unique (ID);

    alter table BaseGroup 
        drop constraint UK_2ps7c6cq37l6c3672phkxkhc1;

    alter table BaseGroup 
        add constraint UK_2ps7c6cq37l6c3672phkxkhc1  unique (comparationId);

    alter table BaseQuestion 
        drop constraint UK_45n10petyj45qbfliy3nruklb;

    alter table BaseQuestion 
        add constraint UK_45n10petyj45qbfliy3nruklb  unique (ID);

    alter table BaseQuestion 
        drop constraint UK_3m9ci7rb0u3owmafk0xh5w8bp;

    alter table BaseQuestion 
        add constraint UK_3m9ci7rb0u3owmafk0xh5w8bp  unique (comparationId);
