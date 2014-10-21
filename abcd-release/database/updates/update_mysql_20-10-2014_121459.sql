
    alter table test_scenario 
        add column formLabel varchar(255);
	
	ALTER TABLE `abcd`.`test_scenario_object` 
CHANGE COLUMN `label` `label` VARCHAR(255) NULL DEFAULT NULL ;

    alter table test_scenario 
        add column formOrganizationId double precision;

    alter table test_scenario 
        add column formVersion integer;

    create table test_scenario_object (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
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

    create table test_scenario_test_scenario_object (
        test_scenario_ID bigint not null,
        testScenarioObjects_ID bigint not null
    );

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

    alter table test_scenario_test_scenario_object 
        drop constraint UK_328cqlt1wm00trxs4fq2x6o7l;

    alter table test_scenario_test_scenario_object 
        add constraint UK_328cqlt1wm00trxs4fq2x6o7l  unique (testScenarioObjects_ID);

    alter table test_scenario_test_scenario_object 
        add constraint FK_b2sqpl48648pq604dx1n2k38v 
        foreign key (test_scenario_ID) 
        references test_scenario (ID);
