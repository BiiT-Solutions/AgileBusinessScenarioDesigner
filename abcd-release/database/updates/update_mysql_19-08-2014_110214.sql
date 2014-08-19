
    alter table parent_of_children 
        drop constraint UK_3awgy2uyqhop13ni86af4ufgv;

    alter table parent_of_children 
        add constraint UK_3awgy2uyqhop13ni86af4ufgv  unique (children_ID);
