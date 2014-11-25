
    alter table expression_value_generic_custom_variable 
        add column form bigint;

    alter table expression_value_generic_variable 
        add column form bigint;

    alter table expression_value_generic_custom_variable 
        add constraint FK_l5sl3gomg9o31s00rl5s3ypx0 
        foreign key (form) 
        references tree_forms (ID);

    alter table expression_value_generic_variable 
        add constraint FK_bwikattrwwal7q7osrrvygh6 
        foreign key (form) 
        references tree_forms (ID);
