
    alter table expression_function 
        add column sortSeq bigint not null;

    alter table expression_operator_logic 
        add column sortSeq bigint not null;

    alter table expression_operator_math 
        add column sortSeq bigint not null;

    alter table expression_symbol 
        add column sortSeq bigint not null;

    alter table expression_value_boolean 
        add column sortSeq bigint not null;

    alter table expression_value_custom_variable 
        add column sortSeq bigint not null;

    alter table expression_value_expression_reference 
        add column sortSeq bigint not null;

    alter table expression_value_generic_custom_variable 
        add column sortSeq bigint not null;

    alter table expression_value_generic_variable 
        add column sortSeq bigint not null;

    alter table expression_value_global_variable 
        add column sortSeq bigint not null;

    alter table expression_value_number 
        add column sortSeq bigint not null;

    alter table expression_value_postal_code 
        add column sortSeq bigint not null;

    alter table expression_value_string 
        add column sortSeq bigint not null;

    alter table expression_value_systemdate 
        add column sortSeq bigint not null;

    alter table expression_value_timestamp 
        add column sortSeq bigint not null;

    alter table expression_value_tree_object_reference 
        add column sortSeq bigint not null;

    alter table expressions_chain 
        add column sortSeq bigint not null;
