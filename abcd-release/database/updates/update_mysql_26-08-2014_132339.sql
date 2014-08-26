
    create table expression_value_tree_object_set_variable (
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

    create table tree_object_set_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        scope varchar(255),
        primary key (ID)
    );

    alter table expression_value_tree_object_set_variable 
        drop constraint UK_qc2p8bk2r5wuujqt606lnc3nl;

    alter table expression_value_tree_object_set_variable 
        add constraint UK_qc2p8bk2r5wuujqt606lnc3nl  unique (ID);

    alter table expression_value_tree_object_set_variable 
        drop constraint UK_sjn8dfp11bdbhhysiahbdqa7y;

    alter table expression_value_tree_object_set_variable 
        add constraint UK_sjn8dfp11bdbhhysiahbdqa7y  unique (comparationId);

    alter table tree_object_set_variable 
        drop constraint UK_fya96xniqcwe4wk9fexdv6cmr;

    alter table tree_object_set_variable 
        add constraint UK_fya96xniqcwe4wk9fexdv6cmr  unique (ID);

    alter table tree_object_set_variable 
        drop constraint UK_h3e6wxhnbp225xvwkm4thm1j4;

    alter table tree_object_set_variable 
        add constraint UK_h3e6wxhnbp225xvwkm4thm1j4  unique (comparationId);

    alter table expression_value_tree_object_set_variable 
        add constraint FK_ikd8gvkiviy5ria4cnh69lmwr 
        foreign key (variable_ID) 
        references tree_object_set_variable (ID);
