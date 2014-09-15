
    alter table global_variables 
        drop constraint UK_ba2w3ms6v9agn6ac5ois703u2;

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);
