
    create table expression_value_postal_code (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        isEditable bit not null,
        value varchar(255),
        primary key (ID)
    );

    alter table expression_value_postal_code 
        drop constraint UK_jnl1ff1sox87eqvsa3w1nugx9;

    alter table expression_value_postal_code 
        add constraint UK_jnl1ff1sox87eqvsa3w1nugx9  unique (ID);

    alter table expression_value_postal_code 
        drop constraint UK_65y7nqlko6hgjo6b3go7lwa82;

    alter table expression_value_postal_code 
        add constraint UK_65y7nqlko6hgjo6b3go7lwa82  unique (comparationId);
