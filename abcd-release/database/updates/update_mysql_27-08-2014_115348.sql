
    alter table expression_value_generic_custom_variable 
        add column genericReference_ID bigint;

    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);

    alter table generic_tree_object_variable 
        drop constraint UK_8p2ila1hcx266jhxtkygthqvb;

    alter table generic_tree_object_variable 
        add constraint UK_8p2ila1hcx266jhxtkygthqvb  unique (type);

    alter table expression_value_generic_custom_variable 
        add constraint FK_do2932v3e8frampt2fuyw8dds 
        foreign key (genericReference_ID) 
        references generic_tree_object_variable (ID);
