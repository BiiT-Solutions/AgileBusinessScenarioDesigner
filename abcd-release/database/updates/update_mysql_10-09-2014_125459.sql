
    alter table expression_value_generic_custom_variable 
        drop constraint UK_1ilcykdnhd0ixr9iaomtrog3h;

    alter table expression_value_generic_custom_variable 
        add constraint UK_1ilcykdnhd0ixr9iaomtrog3h  unique (ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_72ucvxvkp3dgy4ci55hda72p5;

    alter table expression_value_generic_custom_variable 
        add constraint UK_72ucvxvkp3dgy4ci55hda72p5  unique (comparationId);
