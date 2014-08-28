
    alter table form_custom_variables 
        drop constraint UK_2pj0qoh0ntvs9laf9sh42rqap;

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);
