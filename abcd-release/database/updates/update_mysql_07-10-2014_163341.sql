
    alter table tree_forms 
        drop constraint UK_plkq2e2pj19uak2ncrgf1ft6v;

    alter table tree_forms 
        add constraint UK_plkq2e2pj19uak2ncrgf1ft6v  unique (ID);

    alter table tree_forms 
        drop constraint UK_k9mhkly9g8lqwf1m9esm50y6m;

    alter table tree_forms 
        add constraint UK_k9mhkly9g8lqwf1m9esm50y6m  unique (comparationId);
