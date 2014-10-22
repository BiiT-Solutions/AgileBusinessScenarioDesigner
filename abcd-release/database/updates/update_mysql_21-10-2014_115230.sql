
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

    alter table test_scenario 
        add column formLabel varchar(255) not null;

    alter table test_scenario 
        add column formOrganizationId DOUBLE not null;

    alter table test_scenario 
        add column formVersion integer not null;

    alter table test_scenario 
        add column testScenarioForm_ID bigint;

    create table test_scenario_object (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label longtext,
        name varchar(190),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table test_scenario_question_answer (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label longtext,
        name varchar(190),
        sortSeq bigint not null,
        parent_ID bigint,
        originalReferenceId bigint not null,
        testAnswer_ID bigint,
        primary key (ID)
    );

    alter table diagram_fork_expression_value_tree_object_reference 
        drop constraint UK_41wpgkw0p4oj65dmnb34e4lqj;

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint UK_41wpgkw0p4oj65dmnb34e4lqj  unique (references_ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_1ilcykdnhd0ixr9iaomtrog3h;

    alter table expression_value_generic_custom_variable 
        add constraint UK_1ilcykdnhd0ixr9iaomtrog3h  unique (ID);

    alter table expression_value_generic_custom_variable 
        drop constraint UK_72ucvxvkp3dgy4ci55hda72p5;

    alter table expression_value_generic_custom_variable 
        add constraint UK_72ucvxvkp3dgy4ci55hda72p5  unique (comparationId);

    alter table global_variable_data_set 
        drop constraint UK_2o1wa9axcmlhv1c8knoh3b7bw;

    alter table global_variable_data_set 
        add constraint UK_2o1wa9axcmlhv1c8knoh3b7bw  unique (variableData_ID);

    alter table global_variables 
        drop constraint UK_ba2w3ms6v9agn6ac5ois703u2;

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);

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

    alter table test_scenario_object 
        drop constraint UK_ni4qkyh4avtnx2xxgj98xbnf0;

    alter table test_scenario_object 
        add constraint UK_ni4qkyh4avtnx2xxgj98xbnf0  unique (ID);

    alter table test_scenario_object 
        drop constraint UK_s2jcrs64woh6taqwno31p9qbq;

    alter table test_scenario_object 
        add constraint UK_s2jcrs64woh6taqwno31p9qbq  unique (comparationId);

    alter table test_scenario_question_answer 
        drop constraint UK_kwt6r5hqjxegc137o8h9emnln;

    alter table test_scenario_question_answer 
        add constraint UK_kwt6r5hqjxegc137o8h9emnln  unique (ID);

    alter table test_scenario_question_answer 
        drop constraint UK_nykl3acgrh43614ohdvjw18so;

    alter table test_scenario_question_answer 
        add constraint UK_nykl3acgrh43614ohdvjw18so  unique (comparationId);

    alter table tree_forms_expressions_chain 
        drop constraint UK_melu1cfpayuydi8fv6gxnoufq;

    alter table tree_forms_expressions_chain 
        add constraint UK_melu1cfpayuydi8fv6gxnoufq  unique (expressionChains_ID);

    alter table test_answer_list_test_answer_basic 
        add constraint FK_13u55f485qtvswc62ojpdrr1n 
        foreign key (test_answer_list_ID) 
        references test_answer_list (ID);
