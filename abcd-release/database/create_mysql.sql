
    create table BaseAnswer (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table BaseCategory (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table BaseForm (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        version integer,
        primary key (ID)
    );

    create table BaseGroup (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        repetable bit not null,
        primary key (ID)
    );

    create table BaseQuestion (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        primary key (ID)
    );

    create table DIAGRAM_BIIT_TEXT (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        fill varchar(255),
        fontSize varchar(255),
        stroke varchar(255),
        strokeWidth varchar(255),
        text varchar(255),
        primary key (ID)
    );

    create table DIAGRAM_CALCULATION (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        formExpression_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_CHILD (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        childDiagram_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_FORK (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_FORK_EXPRESSION_VALUE_TREE_OBJECT_REFERENCE (
        DIAGRAM_FORK_ID bigint not null,
        reference_ID bigint not null
    );

    create table DIAGRAM_LINKS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        attrs longtext,
        manhattan bit not null,
        smooth bit not null,
        text varchar(255),
        vertices longtext,
        expressionChain_ID bigint,
        source_ID bigint,
        target_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_NODES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        jointjsId varchar(255),
        port varchar(255),
        selector varchar(255),
        primary key (ID)
    );

    create table DIAGRAM_POINTS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        x integer not null,
        y integer not null,
        primary key (ID)
    );

    create table DIAGRAM_REPEAT (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_RULE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        rule_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_SINK (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        formExpression_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_SIZES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        height integer not null,
        width integer not null,
        primary key (ID)
    );

    create table DIAGRAM_SOURCE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        primary key (ID)
    );

    create table DIAGRAM_TABLE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        embeds varchar(255),
        jointjsId varchar(255),
        type varchar(255),
        z integer not null,
        parent_ID bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_ID bigint,
        position_ID bigint,
        size_ID bigint,
        table_ID bigint,
        primary key (ID)
    );

    create table ELEMENTS_OF_DIAGRAM (
        DIAGRAM_ID bigint not null,
        diagramElements_ID bigint not null
    );

    create table EXPRESSIONS_CHAIN (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        primary key (ID)
    );

    create table EXPRESSIONS_CHAIN_EXPRESSION_BASIC (
        EXPRESSIONS_CHAIN_ID bigint not null,
        expressions_ID bigint not null,
        expression_index integer not null,
        primary key (EXPRESSIONS_CHAIN_ID, expression_index)
    );

    create table EXPRESSION_FUNCTION (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value varchar(255),
        primary key (ID)
    );

    create table EXPRESSION_OPERATOR_LOGIC (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        currentValue varchar(255),
        primary key (ID)
    );

    create table EXPRESSION_OPERATOR_MATH (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        currentValue varchar(255),
        primary key (ID)
    );

    create table EXPRESSION_SYMBOL (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value varchar(255),
        primary key (ID)
    );

    create table EXPRESSION_VALUE_BOOLEAN (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value bit not null,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_CUSTOM_VARIABLE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        unit varchar(255),
        reference_ID bigint,
        variable_ID bigint,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_EXPRESSION_REFERENCE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value_ID bigint,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_GLOBAL_VARIABLE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        constant_ID bigint,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_NUMBER (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value double precision not null,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_STRING (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value varchar(255),
        primary key (ID)
    );

    create table EXPRESSION_VALUE_TIMESTAMP (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        value datetime,
        primary key (ID)
    );

    create table EXPRESSION_VALUE_TREE_OBJECT_REFERENCE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        unit varchar(255),
        reference_ID bigint,
        primary key (ID)
    );

    create table FORM_CUSTOM_VARIABLES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        scope varchar(255),
        type varchar(255),
        form bigint,
        primary key (ID)
    );

    create table GLOBAL_VARIABLES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        format integer,
        name varchar(255),
        primary key (ID)
    );

    create table GLOBAL_VARIABLE_DATA_DATE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        validFrom datetime,
        validTo datetime,
        value datetime,
        primary key (ID)
    );

    create table GLOBAL_VARIABLE_DATA_NUMBER (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        validFrom datetime,
        validTo datetime,
        value double precision,
        primary key (ID)
    );

    create table GLOBAL_VARIABLE_DATA_POSTALCODE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        validFrom datetime,
        validTo datetime,
        postalCode varchar(255),
        primary key (ID)
    );

    create table GLOBAL_VARIABLE_DATA_SET (
        GLOBAL_VARIABLES_ID bigint not null,
        data_ID bigint not null
    );

    create table GLOBAL_VARIABLE_DATA_TEXT (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        validFrom datetime,
        validTo datetime,
        value varchar(255),
        primary key (ID)
    );

    create table PARENT_OF_CHILDREN (
        TreeObject_ID bigint not null,
        children_ID bigint not null
    );

    create table RULE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        actions_ID bigint,
        condition_ID bigint,
        primary key (ID)
    );

    create table RULE_DECISION_TABLE (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        primary key (ID)
    );

    create table RULE_DECISION_TABLE_ROW (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        action_ID bigint,
        conditions_ID bigint,
        primary key (ID)
    );

    create table RULE_DECISION_TABLE_RULE_DECISION_TABLE_ROW (
        RULE_DECISION_TABLE_ID bigint not null,
        rules_ID bigint not null
    );

    create table TREE_ANSWERS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table TREE_CATEGORIES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table TREE_FORMS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        version integer,
        availableFrom datetime not null,
        availableTo datetime,
        primary key (ID)
    );

    create table TREE_FORMS_DIAGRAM (
        TREE_FORMS_ID bigint not null,
        diagrams_ID bigint not null
    );

    create table TREE_FORMS_EXPRESSIONS_CHAIN (
        TREE_FORMS_ID bigint not null,
        expressionChain_ID bigint not null
    );

    create table TREE_FORMS_FORM_CUSTOM_VARIABLES (
        TREE_FORMS_ID bigint not null,
        customVariables_ID bigint not null
    );

    create table TREE_FORMS_RULE (
        TREE_FORMS_ID bigint not null,
        rules_ID bigint not null
    );

    create table TREE_FORMS_RULE_DECISION_TABLE (
        TREE_FORMS_ID bigint not null,
        tableRules_ID bigint not null
    );

    create table TREE_GROUPS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        repetable bit not null,
        primary key (ID)
    );

    create table TREE_QUESTIONS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        answerFormat varchar(255),
        answerType varchar(255),
        primary key (ID)
    );

    alter table BaseAnswer 
        add constraint UK_76lq0gmnjriqwvt0axvjt4h8s  unique (ID);

    alter table BaseAnswer 
        add constraint UK_djol5d7vg4wvgutb2l6cj5p3y  unique (comparationId);

    alter table BaseCategory 
        add constraint UK_ey647aoo30r3uqxirrm3v7jwa  unique (ID);

    alter table BaseCategory 
        add constraint UK_l6dm3qlnaolwpi2k56utdasqr  unique (comparationId);

    alter table BaseForm 
        add constraint UK_3n8x6f9irekyp7of2b7gibpio  unique (name, version);

    alter table BaseForm 
        add constraint UK_9mtlfju71ni1foihstunmc72e  unique (ID);

    alter table BaseForm 
        add constraint UK_g7segt6l5tegemkbp78vk02bc  unique (comparationId);

    alter table BaseGroup 
        add constraint UK_2jv9tjr1cl5ijdaueliw7ye25  unique (ID);

    alter table BaseGroup 
        add constraint UK_2ps7c6cq37l6c3672phkxkhc1  unique (comparationId);

    alter table BaseQuestion 
        add constraint UK_45n10petyj45qbfliy3nruklb  unique (ID);

    alter table BaseQuestion 
        add constraint UK_3m9ci7rb0u3owmafk0xh5w8bp  unique (comparationId);

    alter table DIAGRAM 
        add constraint UK_ay7p6glr0lgh40gyms8advd6y  unique (ID);

    alter table DIAGRAM 
        add constraint UK_e50y73itxrc3fxven31a3jgqa  unique (comparationId);

    alter table DIAGRAM_BIIT_TEXT 
        add constraint UK_3ie03mlss5tigvo8dlmflmxs4  unique (ID);

    alter table DIAGRAM_BIIT_TEXT 
        add constraint UK_npp5ax1h906xvqw0vyj0ej2f6  unique (comparationId);

    alter table DIAGRAM_CALCULATION 
        add constraint UK_awql0acltfchm618r3hlh805l  unique (ID);

    alter table DIAGRAM_CALCULATION 
        add constraint UK_ovy0cafs47g66udw7elnw6ebd  unique (comparationId);

    alter table DIAGRAM_CHILD 
        add constraint UK_t3k6bsfhcuy0qs7k0o84u6v78  unique (ID);

    alter table DIAGRAM_CHILD 
        add constraint UK_78ityk7j3oksl37socy2jc6k  unique (comparationId);

    alter table DIAGRAM_FORK 
        add constraint UK_bmrhpcmpsidih440jug0fmy3p  unique (ID);

    alter table DIAGRAM_FORK 
        add constraint UK_s8fce3gdw3yhk11ykin017rju  unique (comparationId);

    alter table DIAGRAM_FORK_EXPRESSION_VALUE_TREE_OBJECT_REFERENCE 
        add constraint UK_ok0p3g77ch6vnyyl6ju2mep8m  unique (reference_ID);

    alter table DIAGRAM_LINKS 
        add constraint UK_9iraan40ophndylhf7rl9tdtu  unique (ID);

    alter table DIAGRAM_LINKS 
        add constraint UK_e43fba9mfx8j9wu1dgsyitbfw  unique (comparationId);

    alter table DIAGRAM_NODES 
        add constraint UK_9mxb7yttew6bnrymrys1mu8nh  unique (ID);

    alter table DIAGRAM_NODES 
        add constraint UK_cdhtahoisklcb0x3yvl468int  unique (comparationId);

    alter table DIAGRAM_POINTS 
        add constraint UK_5838oohy683a31039ecvd5opy  unique (ID);

    alter table DIAGRAM_POINTS 
        add constraint UK_k9718pygtbgg3ebfgqevkph6d  unique (comparationId);

    alter table DIAGRAM_REPEAT 
        add constraint UK_ntw45kg5blsrohuccmbd2o4na  unique (ID);

    alter table DIAGRAM_REPEAT 
        add constraint UK_siwul99osayup4nlns5nvsmrm  unique (comparationId);

    alter table DIAGRAM_RULE 
        add constraint UK_2dpl35mx8kw7gxft44qpt92k7  unique (ID);

    alter table DIAGRAM_RULE 
        add constraint UK_s5oop6w8g5d3tx0bd0g95jesn  unique (comparationId);

    alter table DIAGRAM_SINK 
        add constraint UK_olk1fvkxl4279bsh7j2msdlvy  unique (ID);

    alter table DIAGRAM_SINK 
        add constraint UK_1wod7h4nrtf3ky5jwoamb3tig  unique (comparationId);

    alter table DIAGRAM_SIZES 
        add constraint UK_85omj3qg918p7v3qa4q3jut6i  unique (ID);

    alter table DIAGRAM_SIZES 
        add constraint UK_hnvqnps2qo1j9ysx2fvv4jkxk  unique (comparationId);

    alter table DIAGRAM_SOURCE 
        add constraint UK_orevm9e3r6etvjb2rbu2m4y0r  unique (ID);

    alter table DIAGRAM_SOURCE 
        add constraint UK_i3t9o27cnq4v7qivwo4asmcmp  unique (comparationId);

    alter table DIAGRAM_TABLE 
        add constraint UK_b6lfr8wfafwciby1u1eenp9b5  unique (ID);

    alter table DIAGRAM_TABLE 
        add constraint UK_jg0rybwvrgmifpup99rkl4p3k  unique (comparationId);

    alter table ELEMENTS_OF_DIAGRAM 
        add constraint UK_qr3pdugi5pesw45bypw4ogsft  unique (diagramElements_ID);

    alter table EXPRESSIONS_CHAIN 
        add constraint UK_r6b3qcunye04y3p594pjfgyjo  unique (ID);

    alter table EXPRESSIONS_CHAIN 
        add constraint UK_i8e4tth0y3fmwib4uv6778uu9  unique (comparationId);

    alter table EXPRESSIONS_CHAIN_EXPRESSION_BASIC 
        add constraint UK_flyjjcyoj8yhemevsg7bno3vg  unique (expressions_ID);

    alter table EXPRESSION_FUNCTION 
        add constraint UK_ndikoxvntmfq63kdkt9k0yqap  unique (ID);

    alter table EXPRESSION_FUNCTION 
        add constraint UK_pi30vw1kuh9fgff40xeehhebq  unique (comparationId);

    alter table EXPRESSION_OPERATOR_LOGIC 
        add constraint UK_l9m29dv18bkcarooi0ph684sy  unique (ID);

    alter table EXPRESSION_OPERATOR_LOGIC 
        add constraint UK_60tdchue2ouaxdrxmg00pxc0w  unique (comparationId);

    alter table EXPRESSION_OPERATOR_MATH 
        add constraint UK_89sjtc9fus2mrc6sa0nlqbbft  unique (ID);

    alter table EXPRESSION_OPERATOR_MATH 
        add constraint UK_2bq9xlg0xg76aj34xd11f1xdu  unique (comparationId);

    alter table EXPRESSION_SYMBOL 
        add constraint UK_a0mj4xeg4yc7lmxhj67pw37m1  unique (ID);

    alter table EXPRESSION_SYMBOL 
        add constraint UK_bvs6hetqyju624kqsqq27mqfq  unique (comparationId);

    alter table EXPRESSION_VALUE_BOOLEAN 
        add constraint UK_1i45jkursyrghwgj6jvvff8qk  unique (ID);

    alter table EXPRESSION_VALUE_BOOLEAN 
        add constraint UK_hnqmmjjs68cg1wn77163l0hjf  unique (comparationId);

    alter table EXPRESSION_VALUE_CUSTOM_VARIABLE 
        add constraint UK_klaj9055hy4rshu4bdq0joi0d  unique (ID);

    alter table EXPRESSION_VALUE_CUSTOM_VARIABLE 
        add constraint UK_3ldw1vben2ynicpi65okfq8wa  unique (comparationId);

    alter table EXPRESSION_VALUE_EXPRESSION_REFERENCE 
        add constraint UK_hqv3c0biopc3mg1eaxjrkjah1  unique (ID);

    alter table EXPRESSION_VALUE_EXPRESSION_REFERENCE 
        add constraint UK_oqbmbnw50w8aubmjr6vewyiiw  unique (comparationId);

    alter table EXPRESSION_VALUE_GLOBAL_VARIABLE 
        add constraint UK_4ksg5vq4sqabbnnv3hp06c75x  unique (ID);

    alter table EXPRESSION_VALUE_GLOBAL_VARIABLE 
        add constraint UK_jpvys7lq0hby41vsyq7sr3ik2  unique (comparationId);

    alter table EXPRESSION_VALUE_NUMBER 
        add constraint UK_pbcy7dd8jhsg8bqk4sngej85v  unique (ID);

    alter table EXPRESSION_VALUE_NUMBER 
        add constraint UK_7dt9bg476kymb6upgx4mghl53  unique (comparationId);

    alter table EXPRESSION_VALUE_STRING 
        add constraint UK_om5sex4vo0560p6vuhgy3n0g9  unique (ID);

    alter table EXPRESSION_VALUE_STRING 
        add constraint UK_cddvmhaqpxrl5mwl3cv18b873  unique (comparationId);

    alter table EXPRESSION_VALUE_TIMESTAMP 
        add constraint UK_3wgtslwnaei3b0wade6rfmam6  unique (ID);

    alter table EXPRESSION_VALUE_TIMESTAMP 
        add constraint UK_qiorsy32gn4bq4q0vlm6kofmq  unique (comparationId);

    alter table EXPRESSION_VALUE_TREE_OBJECT_REFERENCE 
        add constraint UK_n3nwikoqko38xw2wmek718kf7  unique (ID);

    alter table EXPRESSION_VALUE_TREE_OBJECT_REFERENCE 
        add constraint UK_e8o6mmd0o1ife64g64mrtotae  unique (comparationId);

    alter table FORM_CUSTOM_VARIABLES 
        add constraint UK_mck4obo6dyjj549u68slv23r4  unique (form, name, scope);

    alter table FORM_CUSTOM_VARIABLES 
        add constraint UK_3nybes2p4lyy5tswx441g9qho  unique (ID);

    alter table FORM_CUSTOM_VARIABLES 
        add constraint UK_5a4gu30pqa6oss1v5x4vlpxks  unique (comparationId);

    alter table GLOBAL_VARIABLES 
        add constraint UK_fn8ytyklptr3b1dgu8w0htxj8  unique (ID);

    alter table GLOBAL_VARIABLES 
        add constraint UK_hhjablpanffuv49t1r5er22q3  unique (comparationId);

    alter table GLOBAL_VARIABLE_DATA_DATE 
        add constraint UK_54ni94q65e5ewuq9m3re9yymk  unique (ID);

    alter table GLOBAL_VARIABLE_DATA_DATE 
        add constraint UK_9wybbtom82p5t26o3sdpoufwu  unique (comparationId);

    alter table GLOBAL_VARIABLE_DATA_NUMBER 
        add constraint UK_qi4wy0w6h6flclywud4o28cg4  unique (ID);

    alter table GLOBAL_VARIABLE_DATA_NUMBER 
        add constraint UK_4v6f8agpm89pi44irqwpj8b28  unique (comparationId);

    alter table GLOBAL_VARIABLE_DATA_POSTALCODE 
        add constraint UK_antnb66ks0pydw3uotlrgxtc5  unique (ID);

    alter table GLOBAL_VARIABLE_DATA_POSTALCODE 
        add constraint UK_hti05fnbwo7kq6ctpateeutm8  unique (comparationId);

    alter table GLOBAL_VARIABLE_DATA_SET 
        add constraint UK_53jc87bw1iwk6xg497x9s53el  unique (data_ID);

    alter table GLOBAL_VARIABLE_DATA_TEXT 
        add constraint UK_hwrp85qjuwe3ey7fnufpn8lvu  unique (ID);

    alter table GLOBAL_VARIABLE_DATA_TEXT 
        add constraint UK_a3o27bju33mx15midcxu0wcu5  unique (comparationId);

    alter table PARENT_OF_CHILDREN 
        add constraint UK_oj9m32v898q47n973v4c4hmgi  unique (children_ID);

    alter table RULE 
        add constraint UK_tcq4k4akn3tbwxpbvouai1qcr  unique (ID);

    alter table RULE 
        add constraint UK_82r6n7a32psw8u9mqppj27ul1  unique (comparationId);

    alter table RULE_DECISION_TABLE 
        add constraint UK_90wuuasoe4cosghv4hjejk1h  unique (ID);

    alter table RULE_DECISION_TABLE 
        add constraint UK_oln318ud17b40fp5box0lph94  unique (comparationId);

    alter table RULE_DECISION_TABLE_ROW 
        add constraint UK_n1wx98k2hqlt1jhqufcpdqdef  unique (ID);

    alter table RULE_DECISION_TABLE_ROW 
        add constraint UK_l7592h2oxnyq1uut1kfpb40f5  unique (comparationId);

    alter table RULE_DECISION_TABLE_RULE_DECISION_TABLE_ROW 
        add constraint UK_cgp840gw79h21arduudslxhqk  unique (rules_ID);

    alter table TREE_ANSWERS 
        add constraint UK_l02x1dnxdw4gm1tdlnrtxc6h4  unique (ID);

    alter table TREE_ANSWERS 
        add constraint UK_nsvxs27d466w94yohh36oasrb  unique (comparationId);

    alter table TREE_CATEGORIES 
        add constraint UK_qx48maxgp7r9qkh11jalq0794  unique (ID);

    alter table TREE_CATEGORIES 
        add constraint UK_lxhltjmdib7s0qhylpqagu3qm  unique (comparationId);

    alter table TREE_FORMS 
        add constraint UK_sqmslpiklr6yoh5mdndj1xqy4  unique (ID);

    alter table TREE_FORMS 
        add constraint UK_t7shqct5neaftl77sb3o2dw3b  unique (comparationId);

    alter table TREE_FORMS 
        add constraint UK_59cv8u9v3kckrnbt63cdxya75  unique (name, version);

    alter table TREE_FORMS_DIAGRAM 
        add constraint UK_k84u538xoyit1rhl5333c4erl  unique (diagrams_ID);

    alter table TREE_FORMS_EXPRESSIONS_CHAIN 
        add constraint UK_cs4bwpoi96w4exhy4hbvwjq6v  unique (expressionChain_ID);

    alter table TREE_FORMS_FORM_CUSTOM_VARIABLES 
        add constraint UK_loctq36vy2tr6855wichycdo1  unique (customVariables_ID);

    alter table TREE_FORMS_RULE 
        add constraint UK_enxhif8hbbl6a93ec9nih26dj  unique (rules_ID);

    alter table TREE_FORMS_RULE_DECISION_TABLE 
        add constraint UK_cfi9yup6omepie71ogv60vudl  unique (tableRules_ID);

    alter table TREE_GROUPS 
        add constraint UK_l8g7b24ir43unomkfe55fcimo  unique (ID);

    alter table TREE_GROUPS 
        add constraint UK_hwgdl270ks71y5qt3p3gvnqq9  unique (comparationId);

    alter table TREE_QUESTIONS 
        add constraint UK_fkqtw0jhv95ld1t98wsn6ul2u  unique (ID);

    alter table TREE_QUESTIONS 
        add constraint UK_7t4bn32ohjfi0my4tsxuswh3v  unique (comparationId);

    alter table DIAGRAM_CALCULATION 
        add constraint FK_ase9cpjt54pn8kfritr4xuunu 
        foreign key (formExpression_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table DIAGRAM_CALCULATION 
        add constraint FK_edc5lnk4e1pj1cmji62k2105e 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_CALCULATION 
        add constraint FK_7pav5i1q57htdlrqa00yvpg4c 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_CALCULATION 
        add constraint FK_q3sbeb0uqcj270lfiakxfh1ru 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_CALCULATION 
        add constraint FK_1nvvfvi7jf1seo6tdxew4poaa 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_CHILD 
        add constraint FK_tbfvclp8nhuopbecdyswgeu8y 
        foreign key (childDiagram_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_CHILD 
        add constraint FK_hre1s56jb8tdxbsmf0tomx88h 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_CHILD 
        add constraint FK_9y9y4karqql20t1dyjybrb8ep 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_CHILD 
        add constraint FK_78ovljkgyi90ktqjq85ov8gnu 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_CHILD 
        add constraint FK_mv52u65jy5i9kgedocsuctyn 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_FORK 
        add constraint FK_e8iusd3eubreq4fgq26v27wfw 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_FORK 
        add constraint FK_knxqgh4tnpxnrpylqpsf9qr6m 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_FORK 
        add constraint FK_epucngfwc4hm5c5omun7l3ui8 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_FORK 
        add constraint FK_6b767k0bbb67ugxnb8v71d5vd 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_FORK_EXPRESSION_VALUE_TREE_OBJECT_REFERENCE 
        add constraint FK_tfko93r5vxsia8yg97ydxiv6w 
        foreign key (DIAGRAM_FORK_ID) 
        references DIAGRAM_FORK (ID);

    alter table DIAGRAM_LINKS 
        add constraint FK_6i3vnev434fo115ltwsdktqey 
        foreign key (expressionChain_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table DIAGRAM_LINKS 
        add constraint FK_maorcdsi1s0ujq8gdrbe1admb 
        foreign key (source_ID) 
        references DIAGRAM_NODES (ID);

    alter table DIAGRAM_LINKS 
        add constraint FK_999dgb86ex2867j9t7abm2r8r 
        foreign key (target_ID) 
        references DIAGRAM_NODES (ID);

    alter table DIAGRAM_LINKS 
        add constraint FK_oeneew0o1nk807rn2d7kjru00 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_REPEAT 
        add constraint FK_kljrntldancxy70mo8y3rdprj 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_REPEAT 
        add constraint FK_6xiwxfbt6x9wqwha42q4qyoqi 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_REPEAT 
        add constraint FK_aaji21mikpikrfu2ayqrtj5uv 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_REPEAT 
        add constraint FK_nacuf28nnd12cbhgoooahjt2b 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_RULE 
        add constraint FK_8hq8ut821fiig4uf2ufcbq4td 
        foreign key (rule_ID) 
        references RULE (ID);

    alter table DIAGRAM_RULE 
        add constraint FK_klux77stwwt4oygbjlxa6mmoq 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_RULE 
        add constraint FK_kp5ga6fc6fj9vb293iysd60wb 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_RULE 
        add constraint FK_gg9vtkjd8nreh13de98nkmmg4 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_RULE 
        add constraint FK_kb2ehxx1q7nvv5c11diqnm893 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_SINK 
        add constraint FK_n7cmqg98x8dm2q0lh9rdpqs9q 
        foreign key (formExpression_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table DIAGRAM_SINK 
        add constraint FK_6rhprdl3lkwu81ljqiu5hkutu 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_SINK 
        add constraint FK_2evyhkcjv0vcax0una474ii9r 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_SINK 
        add constraint FK_5r557wohp7ptet1wqutp74ddw 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_SINK 
        add constraint FK_6vaqtv3mi27ridngscvhnxlyk 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_SOURCE 
        add constraint FK_gm61xr1a3xmnkdbyju1pcacqk 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_SOURCE 
        add constraint FK_i3p6mwdtml373f74w40sjthw1 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_SOURCE 
        add constraint FK_lw285tg67cvg2bwikkckhnnyy 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_SOURCE 
        add constraint FK_ifj3s7w1uoh8yheeg1k4k8kqb 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table DIAGRAM_TABLE 
        add constraint FK_9vsm7hm5kjdnns64t15drxs1f 
        foreign key (table_ID) 
        references RULE_DECISION_TABLE (ID);

    alter table DIAGRAM_TABLE 
        add constraint FK_8nsphgw722sgdksjgmfxwylwo 
        foreign key (biitText_ID) 
        references DIAGRAM_BIIT_TEXT (ID);

    alter table DIAGRAM_TABLE 
        add constraint FK_sppilf54mbll6koakmfedxtyl 
        foreign key (position_ID) 
        references DIAGRAM_POINTS (ID);

    alter table DIAGRAM_TABLE 
        add constraint FK_ri5pyw5e9s3dssp5msp2ko798 
        foreign key (size_ID) 
        references DIAGRAM_SIZES (ID);

    alter table DIAGRAM_TABLE 
        add constraint FK_aumo1notcjrj4idu5rj8e0org 
        foreign key (parent_ID) 
        references DIAGRAM (ID);

    alter table ELEMENTS_OF_DIAGRAM 
        add constraint FK_9sieo4y8te2rspujp0krgbwlm 
        foreign key (DIAGRAM_ID) 
        references DIAGRAM (ID);

    alter table EXPRESSIONS_CHAIN_EXPRESSION_BASIC 
        add constraint FK_18o9ovkq706j3asddkxhygqbc 
        foreign key (EXPRESSIONS_CHAIN_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table EXPRESSION_VALUE_CUSTOM_VARIABLE 
        add constraint FK_ac6x8h25ffugau8b58flu4cn1 
        foreign key (variable_ID) 
        references FORM_CUSTOM_VARIABLES (ID);

    alter table EXPRESSION_VALUE_GLOBAL_VARIABLE 
        add constraint FK_ip8mqqut8tqvyga97ym2ccugc 
        foreign key (constant_ID) 
        references GLOBAL_VARIABLES (ID);

    alter table FORM_CUSTOM_VARIABLES 
        add constraint FK_m3c6fqkpordvn11jihiyvgx6r 
        foreign key (form) 
        references TREE_FORMS (ID);

    alter table GLOBAL_VARIABLE_DATA_SET 
        add constraint FK_pioqrky7up3mcw6rsouuu102 
        foreign key (GLOBAL_VARIABLES_ID) 
        references GLOBAL_VARIABLES (ID);

    alter table RULE 
        add constraint FK_iodst0nsvtotcpxlieav36ocx 
        foreign key (actions_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table RULE 
        add constraint FK_8imcrv981aypwxbu29g0yc1fl 
        foreign key (condition_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table RULE_DECISION_TABLE_ROW 
        add constraint FK_5ni6jco3ke1lm909jyuthip08 
        foreign key (action_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table RULE_DECISION_TABLE_ROW 
        add constraint FK_saylgeu3tyha9kavj8jrpe6n6 
        foreign key (conditions_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table RULE_DECISION_TABLE_RULE_DECISION_TABLE_ROW 
        add constraint FK_cgp840gw79h21arduudslxhqk 
        foreign key (rules_ID) 
        references RULE_DECISION_TABLE_ROW (ID);

    alter table RULE_DECISION_TABLE_RULE_DECISION_TABLE_ROW 
        add constraint FK_igyyxme5yqcqjouq46qpvj0cs 
        foreign key (RULE_DECISION_TABLE_ID) 
        references RULE_DECISION_TABLE (ID);

    alter table TREE_FORMS_DIAGRAM 
        add constraint FK_k84u538xoyit1rhl5333c4erl 
        foreign key (diagrams_ID) 
        references DIAGRAM (ID);

    alter table TREE_FORMS_DIAGRAM 
        add constraint FK_gfgdv8y6k6u7nmq2aunxl7u2l 
        foreign key (TREE_FORMS_ID) 
        references TREE_FORMS (ID);

    alter table TREE_FORMS_EXPRESSIONS_CHAIN 
        add constraint FK_cs4bwpoi96w4exhy4hbvwjq6v 
        foreign key (expressionChain_ID) 
        references EXPRESSIONS_CHAIN (ID);

    alter table TREE_FORMS_EXPRESSIONS_CHAIN 
        add constraint FK_kycy4dt0kte4p1024664kk1l3 
        foreign key (TREE_FORMS_ID) 
        references TREE_FORMS (ID);

    alter table TREE_FORMS_FORM_CUSTOM_VARIABLES 
        add constraint FK_loctq36vy2tr6855wichycdo1 
        foreign key (customVariables_ID) 
        references FORM_CUSTOM_VARIABLES (ID);

    alter table TREE_FORMS_FORM_CUSTOM_VARIABLES 
        add constraint FK_e66ikdoctiljq606euhg380ji 
        foreign key (TREE_FORMS_ID) 
        references TREE_FORMS (ID);

    alter table TREE_FORMS_RULE 
        add constraint FK_enxhif8hbbl6a93ec9nih26dj 
        foreign key (rules_ID) 
        references RULE (ID);

    alter table TREE_FORMS_RULE 
        add constraint FK_mi629tun2c88r1mr8efdnqd0k 
        foreign key (TREE_FORMS_ID) 
        references TREE_FORMS (ID);

    alter table TREE_FORMS_RULE_DECISION_TABLE 
        add constraint FK_cfi9yup6omepie71ogv60vudl 
        foreign key (tableRules_ID) 
        references RULE_DECISION_TABLE (ID);

    alter table TREE_FORMS_RULE_DECISION_TABLE 
        add constraint FK_1negmxxc3fil3y8i0amm8xmx 
        foreign key (TREE_FORMS_ID) 
        references TREE_FORMS (ID);
