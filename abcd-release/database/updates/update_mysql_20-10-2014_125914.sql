
    create table test_scenario_question_answer_test_answer_basic (
        test_scenario_question_answer_ID bigint not null,
        testAnswerList_ID bigint not null
    );

    alter table test_scenario_question_answer_test_answer_basic 
        drop constraint UK_fgnceq91bmcis4hwd4bjy2ba;

    alter table test_scenario_question_answer_test_answer_basic 
        add constraint UK_fgnceq91bmcis4hwd4bjy2ba  unique (testAnswerList_ID);

    alter table test_scenario_question_answer_test_answer_basic 
        add constraint FK_s0q6961c0iqfbd8i65us49mlj 
        foreign key (test_scenario_question_answer_ID) 
        references test_scenario_question_answer (ID);
