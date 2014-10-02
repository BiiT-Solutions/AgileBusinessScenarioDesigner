
    alter table tree_forms 
        drop constraint UK_iwgivd7sy9sfbjyj0hlyccrxt;

    alter table tree_forms 
        add constraint UK_iwgivd7sy9sfbjyj0hlyccrxt  unique (label, version);
