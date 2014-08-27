
    alter table generic_tree_object_variable 
        add column type varchar(255);

    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);

    alter table generic_tree_object_variable 
        drop constraint UK_8p2ila1hcx266jhxtkygthqvb;

    alter table generic_tree_object_variable 
        add constraint UK_8p2ila1hcx266jhxtkygthqvb  unique (type);
