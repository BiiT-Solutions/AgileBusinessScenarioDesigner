
    create table test_answer_list (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        primary key (ID)
    );

    create table test_answer_list_test_answer_basic (
        test_answer_list_ID bigint not null,
        testAnswerList_ID bigint not null
    );

    create table test_scenario_test_answer_list (
        test_scenario_ID bigint not null,
        questionTestAnswerListRelationship_ID bigint not null,
        questionTestAnswerListRelationship_KEY bigint not null,
        primary key (test_scenario_ID, questionTestAnswerListRelationship_KEY)
    );

    alter table test_answer_list 
        drop constraint UK_4rh2n0h8kut89dnrp1p6yxqao;

    alter table test_answer_list 
        add constraint UK_4rh2n0h8kut89dnrp1p6yxqao  unique (ID);

    alter table test_answer_list 
        drop constraint UK_1h1jnby3gascn5q6ahgkelilq;

    alter table test_answer_list 
        add constraint UK_1h1jnby3gascn5q6ahgkelilq  unique (comparationId);

    alter table test_answer_list_test_answer_basic 
        drop constraint UK_70eapkpukk1o7veso7p8hhl0k;

    alter table test_answer_list_test_answer_basic 
        add constraint UK_70eapkpukk1o7veso7p8hhl0k  unique (testAnswerList_ID);

    alter table test_scenario_test_answer_list 
        drop constraint UK_sailkaoi0cqrpr6wtvhvwhqly;

    alter table test_scenario_test_answer_list 
        add constraint UK_sailkaoi0cqrpr6wtvhvwhqly  unique (questionTestAnswerListRelationship_ID);

    alter table test_answer_list_test_answer_basic 
        add constraint FK_13u55f485qtvswc62ojpdrr1n 
        foreign key (test_answer_list_ID) 
        references test_answer_list (ID);

    alter table test_scenario_test_answer_list 
        add constraint FK_sailkaoi0cqrpr6wtvhvwhqly 
        foreign key (questionTestAnswerListRelationship_ID) 
        references test_answer_list (ID);

    alter table test_scenario_test_answer_list 
        add constraint FK_ehqahrp9e5cnsc66wfe0l0hfj 
        foreign key (test_scenario_ID) 
        references test_scenario (ID);
