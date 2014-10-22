
    alter table diagram_fork_expression_value_tree_object_reference 
        drop constraint UK_41wpgkw0p4oj65dmnb34e4lqj;

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint UK_41wpgkw0p4oj65dmnb34e4lqj  unique (references_ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_1ilcykdnhd0ixr9iaomtrog3h;

    alter table expression_value_generic_custom_variable 
        add constraint UK_1ilcykdnhd0ixr9iaomtrog3h  unique (ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_72ucvxvkp3dgy4ci55hda72p5;

    alter table expression_value_generic_custom_variable 
        add constraint UK_72ucvxvkp3dgy4ci55hda72p5  unique (comparationId);

    alter table global_variable_data_set 
        drop constraint UK_2o1wa9axcmlhv1c8knoh3b7bw;

    alter table global_variable_data_set 
        add constraint UK_2o1wa9axcmlhv1c8knoh3b7bw  unique (variableData_ID);

    alter table tree_forms_expressions_chain 
        drop constraint UK_melu1cfpayuydi8fv6gxnoufq;

    alter table tree_forms_expressions_chain 
        add constraint UK_melu1cfpayuydi8fv6gxnoufq  unique (expressionChains_ID);
