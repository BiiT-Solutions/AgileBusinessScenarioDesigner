
    create table TestAnswerMultiCheckBox_multiCheckBoxValue (
        TestAnswerMultiCheckBox_ID bigint not null,
        multiCheckBoxValue varchar(255)
    );

    create table test_answer_input_date (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        dateValue datetime,
        primary key (ID)
    );

    create table test_answer_input_number (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        inputValue double precision,
        primary key (ID)
    );

    create table test_answer_input_postalcode (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        inputValue varchar(255),
        primary key (ID)
    );

    create table test_answer_input_text (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        inputValue varchar(255),
        primary key (ID)
    );

    create table test_answer_multi_checkbox (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        primary key (ID)
    );

    create table test_answer_radio_button (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        radioButtonValue varchar(255),
        primary key (ID)
    );

    create table test_scenario (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(190),
        primary key (ID)
    );

    create table test_scenario_test_answer_basic (
        test_scenario_ID bigint not null,
        questionTestAnswerRelationship_ID bigint not null,
        questionTestAnswerRelationship_KEY bigint not null,
        primary key (test_scenario_ID, questionTestAnswerRelationship_KEY)
    );

    alter table test_answer_input_date 
        drop constraint UK_cctx6br59kggymqx6ltv65iv9;

    alter table test_answer_input_date 
        add constraint UK_cctx6br59kggymqx6ltv65iv9  unique (ID);

    alter table test_answer_input_date 
        drop constraint UK_8r0jv8nwetnhcx3mwixg6uhn6;

    alter table test_answer_input_date 
        add constraint UK_8r0jv8nwetnhcx3mwixg6uhn6  unique (comparationId);

    alter table test_answer_input_number 
        drop constraint UK_rrf27v5jka8vvm7pvp3846qac;

    alter table test_answer_input_number 
        add constraint UK_rrf27v5jka8vvm7pvp3846qac  unique (ID);

    alter table test_answer_input_number 
        drop constraint UK_aljs5lkmjtgb0ys9oxac2jo5m;

    alter table test_answer_input_number 
        add constraint UK_aljs5lkmjtgb0ys9oxac2jo5m  unique (comparationId);

    alter table test_answer_input_postalcode 
        drop constraint UK_9n169jx3xa3jv40g097bb6k4g;

    alter table test_answer_input_postalcode 
        add constraint UK_9n169jx3xa3jv40g097bb6k4g  unique (ID);

    alter table test_answer_input_postalcode 
        drop constraint UK_gmbj48islivqbak8uhqn4acra;

    alter table test_answer_input_postalcode 
        add constraint UK_gmbj48islivqbak8uhqn4acra  unique (comparationId);

    alter table test_answer_input_text 
        drop constraint UK_hdccu2j9fjpphcxxt6wsuqo1v;

    alter table test_answer_input_text 
        add constraint UK_hdccu2j9fjpphcxxt6wsuqo1v  unique (ID);

    alter table test_answer_input_text 
        drop constraint UK_fm38doewevroqbh9w9yl6uy45;

    alter table test_answer_input_text 
        add constraint UK_fm38doewevroqbh9w9yl6uy45  unique (comparationId);

    alter table test_answer_multi_checkbox 
        drop constraint UK_u2axqvpcnrfbj8tflcs5v8qu;

    alter table test_answer_multi_checkbox 
        add constraint UK_u2axqvpcnrfbj8tflcs5v8qu  unique (ID);

    alter table test_answer_multi_checkbox 
        drop constraint UK_neljyd1uorqx4rh8nwh89pofm;

    alter table test_answer_multi_checkbox 
        add constraint UK_neljyd1uorqx4rh8nwh89pofm  unique (comparationId);

    alter table test_answer_radio_button 
        drop constraint UK_eakcce3ybd0073kdajy217yju;

    alter table test_answer_radio_button 
        add constraint UK_eakcce3ybd0073kdajy217yju  unique (ID);

    alter table test_answer_radio_button 
        drop constraint UK_rrv3rw7jasepphc1943fqgis5;

    alter table test_answer_radio_button 
        add constraint UK_rrv3rw7jasepphc1943fqgis5  unique (comparationId);

    alter table test_scenario 
        drop constraint UK_j17qvfqb5wcp4c3bgknvdii31;

    alter table test_scenario 
        add constraint UK_j17qvfqb5wcp4c3bgknvdii31  unique (ID);

    alter table test_scenario 
        drop constraint UK_eh6es7t34ldoxns3sswdj6vku;

    alter table test_scenario 
        add constraint UK_eh6es7t34ldoxns3sswdj6vku  unique (comparationId);

    alter table test_scenario 
        drop constraint UK_jshq05r5jh9kw6obudq99vsee;

    alter table test_scenario 
        add constraint UK_jshq05r5jh9kw6obudq99vsee  unique (name);

    alter table test_scenario_test_answer_basic 
        drop constraint UK_a50es1q5iamqr0xaqhnym6a9w;

    alter table test_scenario_test_answer_basic 
        add constraint UK_a50es1q5iamqr0xaqhnym6a9w  unique (questionTestAnswerRelationship_ID);

    alter table TestAnswerMultiCheckBox_multiCheckBoxValue 
        add constraint FK_9hig9ck1w4ry8gscespat7k7i 
        foreign key (TestAnswerMultiCheckBox_ID) 
        references test_answer_multi_checkbox (ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_jy3bdbqvowh4tjeta3t231nbl 
        foreign key (questionTestAnswerRelationship_KEY) 
        references tree_questions (ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_a1lgwvamyrw44r3i3kfbvn0ti 
        foreign key (test_scenario_ID) 
        references test_scenario (ID);
