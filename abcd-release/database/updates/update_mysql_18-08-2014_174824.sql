
    alter table expression_function 
        add column isEditable bit not null;

    alter table expression_operator_logic 
        add column isEditable bit not null;

    alter table expression_operator_math 
        add column isEditable bit not null;

    alter table expression_symbol 
        add column isEditable bit not null;

    alter table expression_value_boolean 
        add column isEditable bit not null;

    alter table expression_value_custom_variable 
        add column isEditable bit not null;

    alter table expression_value_expression_reference 
        add column isEditable bit not null;

    alter table expression_value_global_variable 
        add column isEditable bit not null;

    alter table expression_value_number 
        add column isEditable bit not null;

    alter table expression_value_string 
        add column isEditable bit not null;

    alter table expression_value_timestamp 
        add column isEditable bit not null;

    alter table expression_value_tree_object_reference 
        add column isEditable bit not null;

    alter table expressions_chain 
        add column isEditable bit not null;
