
    create table test_scenario_test_answer_basic (
        test_scenario_ID bigint not null,
        questionTestAnswerRelationship_ID bigint not null,
        questionTestAnswerRelationship_KEY bigint not null,
        primary key (test_scenario_ID, questionTestAnswerRelationship_KEY)
    );

    alter table test_scenario_test_answer_basic 
        drop constraint UK_a50es1q5iamqr0xaqhnym6a9w;

    alter table test_scenario_test_answer_basic 
        add constraint UK_a50es1q5iamqr0xaqhnym6a9w  unique (questionTestAnswerRelationship_ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_jy3bdbqvowh4tjeta3t231nbl 
        foreign key (questionTestAnswerRelationship_KEY) 
        references tree_questions (ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_a1lgwvamyrw44r3i3kfbvn0ti 
        foreign key (test_scenario_ID) 
        references test_scenario (ID);
