
    create table diagram (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        name varchar(255),
        primary key (id)
    );

    create table diagram_biit_text (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        fill varchar(255),
        fontSize varchar(255),
        stroke varchar(255),
        strokeWidth varchar(255),
        text varchar(255),
        primary key (id)
    );

    create table diagram_calculation (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        expression_id bigint,
        primary key (id)
    );

    create table diagram_child (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        diagram_id bigint,
        primary key (id)
    );

    create table diagram_fork (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        primary key (id)
    );

    create table diagram_fork_expression_value_tree_object_reference (
        diagram_fork_id bigint not null,
        references_id bigint not null
    );

    create table diagram_links (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        attrs longtext,
        manhattan bit not null,
        smooth bit not null,
        text varchar(255),
        vertices longtext,
        expressionChain_id bigint,
        source_id bigint,
        target_id bigint,
        primary key (id)
    );

    create table diagram_nodes (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        jointjsId varchar(255),
        port varchar(255),
        selector varchar(255),
        primary key (id)
    );

    create table diagram_points (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        x integer not null,
        y integer not null,
        primary key (id)
    );

    create table diagram_repeat (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        primary key (id)
    );

    create table diagram_rule (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        rule_id bigint,
        primary key (id)
    );

    create table diagram_sink (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        expression_id bigint,
        primary key (id)
    );

    create table diagram_sizes (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        height integer not null,
        width integer not null,
        primary key (id)
    );

    create table diagram_source (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        primary key (id)
    );

    create table diagram_table (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        embeds varchar(255),
        jointjs_id varchar(255),
        type varchar(255),
        z integer not null,
        parent bigint,
        angle float not null,
        tooltip varchar(255),
        biitText_id bigint,
        position_id bigint,
        size_id bigint,
        table_id bigint,
        primary key (id)
    );

    create table elements_of_diagram (
        diagram_id bigint not null,
        diagramObjects_id bigint not null,
        primary key (diagram_id, diagramObjects_id)
    );

    create table expression_function (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value varchar(255),
        primary key (id)
    );

    create table expression_operator_logic (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        currentValue varchar(255),
        primary key (id)
    );

    create table expression_operator_math (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        currentValue varchar(255),
        primary key (id)
    );

    create table expression_plugin_method (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        pluginInterface varchar(255),
        pluginMethodName varchar(255),
        pluginName varchar(255),
        primary key (id)
    );

    create table expression_symbol (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value varchar(255),
        primary key (id)
    );

    create table expression_value_boolean (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value bit,
        primary key (id)
    );

    create table expression_value_custom_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        unit varchar(255),
        reference_id bigint,
        variable_id bigint,
        primary key (id)
    );

    create table expression_value_generic_custom_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        type varchar(255),
        variable_id bigint,
        primary key (id)
    );

    create table expression_value_generic_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        type varchar(255),
        primary key (id)
    );

    create table expression_value_global_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        globalVariable_id bigint,
        primary key (id)
    );

    create table expression_value_number (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value double precision,
        primary key (id)
    );

    create table expression_value_postal_code (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        text TEXT,
        primary key (id)
    );

    create table expression_value_string (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        text TEXT,
        primary key (id)
    );

    create table expression_value_systemdate (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value datetime,
        primary key (id)
    );

    create table expression_value_timestamp (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        value datetime,
        primary key (id)
    );

    create table expression_value_tree_object_reference (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        unit varchar(255),
        reference_id bigint,
        primary key (id)
    );

    create table expressions_chain (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sortSeq bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table expressions_chain_expression_basic (
        expressions_chain_id bigint not null,
        expressions_id bigint not null
    );

    create table form_custom_variables (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        defaultValue varchar(255),
        name varchar(190),
        scope varchar(190),
        type varchar(255),
        form bigint not null,
        primary key (id)
    );

    create table global_variable_data_date (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        validFrom datetime,
        validTo datetime,
        value datetime,
        primary key (id)
    );

    create table global_variable_data_number (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        validFrom datetime,
        validTo datetime,
        value double precision,
        primary key (id)
    );

    create table global_variable_data_postalcode (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        validFrom datetime,
        validTo datetime,
        postalCode varchar(255),
        primary key (id)
    );

    create table global_variable_data_set (
        global_variables_id bigint not null,
        variableData_id bigint not null
    );

    create table global_variable_data_text (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        validFrom datetime,
        validTo datetime,
        value varchar(255),
        primary key (id)
    );

    create table global_variables (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        format integer,
        name varchar(190),
        primary key (id)
    );

    create table rule (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        name varchar(255),
        actions_id bigint,
        conditions_id bigint,
        primary key (id)
    );

    create table rule_decision_table (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        name varchar(255),
        primary key (id)
    );

    create table rule_decision_table_row (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        action_id bigint,
        conditions_id bigint,
        primary key (id)
    );

    create table rule_decision_table_rule_decision_table_row (
        rule_decision_table_id bigint not null,
        rules_id bigint not null
    );

    create table test_answer_input_date (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        dateValue datetime,
        primary key (id)
    );

    create table test_answer_input_number (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        inputValue double precision,
        primary key (id)
    );

    create table test_answer_input_postalcode (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        inputValue varchar(255),
        primary key (id)
    );

    create table test_answer_input_text (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        inputValue varchar(255),
        primary key (id)
    );

    create table test_answer_multi_checkbox (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        primary key (id)
    );

    create table test_answer_multi_checkbox_values (
        TestAnswerMultiCheckBox_id bigint not null,
        multiCheckBoxValue varchar(255)
    );

    create table test_answer_radio_button (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        radioButtonValue varchar(255),
        primary key (id)
    );

    create table test_scenario (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        formId bigint not null,
        formLabel varchar(190) not null,
        formOrganization DOUBLE not null,
        name varchar(190),
        testScenarioForm_id bigint,
        primary key (id)
    );

    create table test_scenario_category (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    );

    create table test_scenario_form (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        primary key (id)
    );

    create table test_scenario_group (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        repeatable bit not null,
        addEnabled bit not null,
        primary key (id)
    );

    create table test_scenario_question (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        testAnswer_id bigint,
        primary key (id)
    );

    create table tree_answers (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    );

    create table tree_categories (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    );

    create table tree_forms (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        availableFrom datetime not null,
        availableTo datetime,
        status varchar(255),
        primary key (id)
    );

    create table tree_forms_diagram (
        tree_forms_id bigint not null,
        diagrams_id bigint not null,
        primary key (tree_forms_id, diagrams_id)
    );

    create table tree_forms_expressions_chain (
        tree_forms_id bigint not null,
        expressionChains_id bigint not null,
        primary key (tree_forms_id, expressionChains_id)
    );

    create table tree_forms_rule (
        tree_forms_id bigint not null,
        rules_id bigint not null,
        primary key (tree_forms_id, rules_id)
    );

    create table tree_forms_rule_decision_table (
        tree_forms_id bigint not null,
        tableRules_id bigint not null,
        primary key (tree_forms_id, tableRules_id)
    );

    create table tree_groups (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        repeatable bit not null,
        primary key (id)
    );

    create table tree_questions (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        answerFormat varchar(255),
        answerType varchar(255),
        primary key (id)
    );

    alter table diagram 
        add constraint UK_fw7vqsrf5p8j89vc6ivxw0x4l  unique (comparation_id);

    alter table diagram_biit_text 
        add constraint UK_49t5md0mmh4pocfqnvy9ilnat  unique (comparation_id);

    alter table diagram_calculation 
        add constraint UK_1j7d4tym3l6bbatilo3wb122v  unique (id);

    alter table diagram_calculation 
        add constraint UK_gqwu3mfvrb58rdq8959neypo4  unique (comparation_id);

    alter table diagram_child 
        add constraint UK_72flappx1vyc05wxvoaugnpbj  unique (id);

    alter table diagram_child 
        add constraint UK_4hhdypcpnc80foag5dcpkes  unique (comparation_id);

    alter table diagram_fork 
        add constraint UK_bxxdhr1ueq0hiw2ri4fyl7okt  unique (id);

    alter table diagram_fork 
        add constraint UK_owctn38n79mnt1ujbow3qrj6a  unique (comparation_id);

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint UK_65ni4svjxtkp69xhtrpslw9fc  unique (references_id);

    alter table diagram_links 
        add constraint UK_ie9xqy3i3400jjj5iadjljigu  unique (id);

    alter table diagram_links 
        add constraint UK_cwiej3pcj8wvjfxf3ova9i4eg  unique (comparation_id);

    alter table diagram_nodes 
        add constraint UK_9pj0bfa6d9ooo68wroe3112mo  unique (comparation_id);

    alter table diagram_points 
        add constraint UK_8iiwsxhg4dk96ebvielf6uqld  unique (comparation_id);

    alter table diagram_repeat 
        add constraint UK_klalqt3rsoso3104e89wqbumq  unique (id);

    alter table diagram_repeat 
        add constraint UK_jfquyt4s511sanyi3ee83cyij  unique (comparation_id);

    alter table diagram_rule 
        add constraint UK_gopil1dui1rbsxmk5q6vc65s3  unique (id);

    alter table diagram_rule 
        add constraint UK_8h7dmg9qne2rtgslk2ka6lxpt  unique (comparation_id);

    alter table diagram_sink 
        add constraint UK_749ch3ctvs3luil2q7qbs9l1s  unique (id);

    alter table diagram_sink 
        add constraint UK_3pu3yriyc4h5i23644h1eh5rl  unique (comparation_id);

    alter table diagram_sizes 
        add constraint UK_5mr2jxui5p9uddxkt21909ufm  unique (comparation_id);

    alter table diagram_source 
        add constraint UK_dyj2vgl165o2ifbqcyg19e525  unique (id);

    alter table diagram_source 
        add constraint UK_1hx8jx1f7bv1r0mi5yk1l0u1j  unique (comparation_id);

    alter table diagram_table 
        add constraint UK_agvdl2gvo6c0v3tynk1787tp2  unique (id);

    alter table diagram_table 
        add constraint UK_rw9myjse63o9xg9mb2otswpky  unique (comparation_id);

    alter table elements_of_diagram 
        add constraint UK_t0p95y98xmoiwbwwjj4agjvv0  unique (diagramObjects_id);

    alter table expression_function 
        add constraint UK_mscuoauoec01ombvya4tvjkhu  unique (id);

    alter table expression_function 
        add constraint UK_8toyx6ij5060ogygcclb8i4hy  unique (comparation_id);

    alter table expression_operator_logic 
        add constraint UK_5461iwwa1ahnrl8ggvjrkwknd  unique (id);

    alter table expression_operator_logic 
        add constraint UK_dcphv7j72tajhnwh1kegikwtw  unique (comparation_id);

    alter table expression_operator_math 
        add constraint UK_i9c5vsetkj1j0te6hwvhtcg91  unique (id);

    alter table expression_operator_math 
        add constraint UK_92wxs3ah2kayf1b9c9shr90yi  unique (comparation_id);

    alter table expression_plugin_method 
        add constraint UK_gi2q7w3u470u0ifgsfqsbk984  unique (id);

    alter table expression_plugin_method 
        add constraint UK_3fmtwuesyav37dot8gv0ko71u  unique (comparation_id);

    alter table expression_symbol 
        add constraint UK_my15yxokgqbuxpe9wmf3wquk9  unique (id);

    alter table expression_symbol 
        add constraint UK_r9e4peno4cx2wp1uvgtllwom6  unique (comparation_id);

    alter table expression_value_boolean 
        add constraint UK_qm0e2g6b1m31espuk8il01kjl  unique (id);

    alter table expression_value_boolean 
        add constraint UK_ctkfkgplxtpwou1686f2ynem2  unique (comparation_id);

    alter table expression_value_custom_variable 
        add constraint UK_7fymux6ua2d13a65jqgk9kud4  unique (id);

    alter table expression_value_custom_variable 
        add constraint UK_87m8f5n9hr4pnr1802mu2m642  unique (comparation_id);

    alter table expression_value_generic_custom_variable 
        add constraint UK_32mi467xj5pnire64ia600ntd  unique (id);

    alter table expression_value_generic_custom_variable 
        add constraint UK_7apgbq1ytk3hfm8gpwlyucks5  unique (comparation_id);

    alter table expression_value_generic_variable 
        add constraint UK_7207b6tju5ili01ltsd02lw14  unique (id);

    alter table expression_value_generic_variable 
        add constraint UK_3l564k5m9xvoe6iu1jn6ydhi5  unique (comparation_id);

    alter table expression_value_global_variable 
        add constraint UK_do6e0tyohuxhvqk5yxqlx2xro  unique (id);

    alter table expression_value_global_variable 
        add constraint UK_fe1mr92e7jkx5upcu1wcoybx0  unique (comparation_id);

    alter table expression_value_number 
        add constraint UK_7ryi3secm30knk5egj9qi21qp  unique (id);

    alter table expression_value_number 
        add constraint UK_dlovh9kyhhypjulupfnck0c9p  unique (comparation_id);

    alter table expression_value_postal_code 
        add constraint UK_gdykqwgewfnsybb4fm2awrkjt  unique (id);

    alter table expression_value_postal_code 
        add constraint UK_fm19ywy0qh753x71om8pg01dk  unique (comparation_id);

    alter table expression_value_string 
        add constraint UK_etccy65hkgu38c7rbs4pcwdsy  unique (id);

    alter table expression_value_string 
        add constraint UK_2fnjjbbastbptodx28hoiv083  unique (comparation_id);

    alter table expression_value_systemdate 
        add constraint UK_knwd66yl8khks9n2ifr4gqunv  unique (id);

    alter table expression_value_systemdate 
        add constraint UK_2e79190fql3tc4t0ruwx0wvw3  unique (comparation_id);

    alter table expression_value_timestamp 
        add constraint UK_8fbaeameopwc01d48p77etip1  unique (id);

    alter table expression_value_timestamp 
        add constraint UK_9mqlu8vq1vwpmqsnoewoh5y4a  unique (comparation_id);

    alter table expression_value_tree_object_reference 
        add constraint UK_73aosv17a4nig5pr4kex85y1w  unique (id);

    alter table expression_value_tree_object_reference 
        add constraint UK_px1xks2ug1enl6dvmgkiotadq  unique (comparation_id);

    alter table expressions_chain 
        add constraint UK_p5okqcbctfvi2ndwxqjw6o8c6  unique (id);

    alter table expressions_chain 
        add constraint UK_by4epobjwm4ye7l11tql8uqsg  unique (comparation_id);

    alter table expressions_chain_expression_basic 
        add constraint UK_41n882737ioltrw54yk3b7p64  unique (expressions_id);

    alter table form_custom_variables 
        add constraint UK_4gau1o2x6o63kn4impw9cihqc  unique (comparation_id);

    alter table global_variable_data_date 
        add constraint UK_jjcyaeadrlsle9w6ui03fxyts  unique (id);

    alter table global_variable_data_date 
        add constraint UK_msqrr3xst8ku9g7kokb6rb7en  unique (comparation_id);

    alter table global_variable_data_number 
        add constraint UK_bj7tyc2ha6qe0116cq9vp1noi  unique (id);

    alter table global_variable_data_number 
        add constraint UK_74ua5ydhfxowubdgkjv9vqfj0  unique (comparation_id);

    alter table global_variable_data_postalcode 
        add constraint UK_im5todr6m202sjon36n213mxo  unique (id);

    alter table global_variable_data_postalcode 
        add constraint UK_a0mcoc1isf9pufl9p5dighu8f  unique (comparation_id);

    alter table global_variable_data_set 
        add constraint UK_sxrvly4ppc6mq4wpdblvxbo11  unique (variableData_id);

    alter table global_variable_data_text 
        add constraint UK_8eoxdjy9bqti61l6bklvwg21g  unique (id);

    alter table global_variable_data_text 
        add constraint UK_jr5t4hfjxv9e4465ngdsl8x7f  unique (comparation_id);

    alter table global_variables 
        add constraint UK_8hxc6j1i2cfa44g7wypkkjxvo  unique (comparation_id);

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);

    alter table rule 
        add constraint UK_kynfmryi32dudhf5d4ff1d248  unique (comparation_id);

    alter table rule_decision_table 
        add constraint UK_n9wggwt5vxd0c181iar53br72  unique (comparation_id);

    alter table rule_decision_table_row 
        add constraint UK_k9s0wi274elpijmqjof3avgy2  unique (comparation_id);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint UK_j9j9wq9fnivf6m5exoi9fwfj2  unique (rules_id);

    alter table test_answer_input_date 
        add constraint UK_cphw2941utrn4uyhgdph14s40  unique (id);

    alter table test_answer_input_date 
        add constraint UK_d2p884tmai0cnqk95q3ltinqh  unique (comparation_id);

    alter table test_answer_input_number 
        add constraint UK_4ofxlpel2ni73niiaxccvlxvt  unique (id);

    alter table test_answer_input_number 
        add constraint UK_6fs71o9i7xtrmyajxaj80lr8a  unique (comparation_id);

    alter table test_answer_input_postalcode 
        add constraint UK_drkl5pqjuprararsrjm2lqbcv  unique (id);

    alter table test_answer_input_postalcode 
        add constraint UK_c834o3x0nvgen6hfkghysq74g  unique (comparation_id);

    alter table test_answer_input_text 
        add constraint UK_bsj5308s1ar59oiu7bwom0ti6  unique (id);

    alter table test_answer_input_text 
        add constraint UK_oqjrd81kc9i6kdufrk20so96y  unique (comparation_id);

    alter table test_answer_multi_checkbox 
        add constraint UK_h2tok76e6tgdgr1ee9x5ubha1  unique (id);

    alter table test_answer_multi_checkbox 
        add constraint UK_nreg34q5x6fe5ydrmdeadx8wd  unique (comparation_id);

    alter table test_answer_radio_button 
        add constraint UK_hnf9v1jayrt252x6e3nvtt35m  unique (id);

    alter table test_answer_radio_button 
        add constraint UK_8tusgqyww1kn79kq4nfq817lu  unique (comparation_id);

    alter table test_scenario 
        add constraint UK_49e2vwe0pem0fb31dxr3ed6b0  unique (name, formId);

    alter table test_scenario 
        add constraint UK_amdun27lw5uwuo7bg48ehvu1h  unique (comparation_id);

    alter table test_scenario_category 
        add constraint UK_b8ms1d56c8qcaldu7s4si7rw9  unique (id);

    alter table test_scenario_category 
        add constraint UK_82ukiq0ro6ba04fsaor0oq5bi  unique (comparation_id);

    alter table test_scenario_form 
        add constraint UK_9sq5ml2sp3rpcu3nybqhbpa3x  unique (id);

    alter table test_scenario_form 
        add constraint UK_83jqkelp07q0wtmggqmi96j3o  unique (comparation_id);

    alter table test_scenario_group 
        add constraint UK_14f7skh7n0utwpmqqq2fpq2q8  unique (id);

    alter table test_scenario_group 
        add constraint UK_p7j9r4tahrxlkq1yyfi68af4l  unique (comparation_id);

    alter table test_scenario_question 
        add constraint UK_5bbbb419f91pnm261shci71dw  unique (id);

    alter table test_scenario_question 
        add constraint UK_7bmmeh5wqxore150ue340ry2b  unique (comparation_id);

    alter table tree_answers 
        add constraint UK_dktggosx7ohcksfob4k94bfmb  unique (id);

    alter table tree_answers 
        add constraint UK_novq229qj7ibt96gyqw251biu  unique (comparation_id);

    alter table tree_categories 
        add constraint UK_t6nmk0eqg7yvg78lxqlrtdr66  unique (id);

    alter table tree_categories 
        add constraint UK_1xpeifv5qxo29x7yhpfmjtbyo  unique (comparation_id);

    alter table tree_forms 
        add constraint UK_89ul4cb5nvq3aaiyni9w6dqaq  unique (label, version, organization_id);

    alter table tree_forms 
        add constraint UK_765yjtcx0oa8unb588ngimaml  unique (id);

    alter table tree_forms 
        add constraint UK_t05hap53xy8005w0etx1tm0yx  unique (comparation_id);

    alter table tree_forms_diagram 
        add constraint UK_kupriscdg8vbaueghjbub50wn  unique (diagrams_id);

    alter table tree_forms_expressions_chain 
        add constraint UK_odixr0cg2sffi97kgn1twedbq  unique (expressionChains_id);

    alter table tree_forms_rule 
        add constraint UK_qywcba5hk1dbarg1hdttwxef6  unique (rules_id);

    alter table tree_forms_rule_decision_table 
        add constraint UK_e0ilwwjcfr73m08kid8l8vqth  unique (tableRules_id);

    alter table tree_groups 
        add constraint UK_lkx000598o8fu6o45gc6j8j6h  unique (id);

    alter table tree_groups 
        add constraint UK_544k9wyac47tkk3mh7itf8h9f  unique (comparation_id);

    alter table tree_questions 
        add constraint UK_qmnu5ia8n490ok4d7obj7khk6  unique (id);

    alter table tree_questions 
        add constraint UK_589h63s3jthrsckwd8a4dn3xq  unique (comparation_id);

    alter table diagram_calculation 
        add constraint FK_o48kw0ris957rafejrjdb9qhf 
        foreign key (expression_id) 
        references expressions_chain (id);

    alter table diagram_calculation 
        add constraint FK_852gnfq8vxmn9hkywoecobtn6 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_calculation 
        add constraint FK_6gvwnekjmacsy0i4hduqxn8o8 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_calculation 
        add constraint FK_c864ws12t8a39qaeklw89wf6e 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_calculation 
        add constraint FK_e18740pms15g0xatw1vs88wuv 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_child 
        add constraint FK_372y4rvd7o3v11eeqx1taswsb 
        foreign key (diagram_id) 
        references diagram (id);

    alter table diagram_child 
        add constraint FK_brc1vc1rak0u4aqy01lt4ak1g 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_child 
        add constraint FK_7mwsew8svdmmv56m8w0rhn36c 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_child 
        add constraint FK_bijy2w9fvk0pxdy5hqxjn2q92 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_child 
        add constraint FK_95r5dam5y3ox7eij844g9b9fw 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_fork 
        add constraint FK_kolfllp3dvo0jyqc2tynvlno8 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_fork 
        add constraint FK_sleka7dxx5x1wkqeca7wh4xuk 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_fork 
        add constraint FK_2jst1ltfa2vfa91xcjnv6s5wc 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_fork 
        add constraint FK_5ma5sm4ekcu4nnsm9kgybi6cu 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint FK_qispgfcvla8gk8fyhefj0vjoa 
        foreign key (diagram_fork_id) 
        references diagram_fork (id);

    alter table diagram_links 
        add constraint FK_stuif50dkuu0aofgdw5osbr60 
        foreign key (expressionChain_id) 
        references expressions_chain (id);

    alter table diagram_links 
        add constraint FK_lpworf73vveqyeb1woyewpedp 
        foreign key (source_id) 
        references diagram_nodes (id);

    alter table diagram_links 
        add constraint FK_fttc9baj2wxqq9oyfg15rb9vy 
        foreign key (target_id) 
        references diagram_nodes (id);

    alter table diagram_links 
        add constraint FK_tcs4xaphicr6lvh57b1pvfu8u 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_repeat 
        add constraint FK_i5qfwfwrqif1a6ervtx7k3b23 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_repeat 
        add constraint FK_byn6g8r9a09o9td4ulip61p3e 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_repeat 
        add constraint FK_qm92epa6njuvf47fptx2baqh3 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_repeat 
        add constraint FK_3j5tdbdtvvmigv6fmxc3myd0f 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_rule 
        add constraint FK_k832erg1q7skten4dnhgc6plu 
        foreign key (rule_id) 
        references rule (id);

    alter table diagram_rule 
        add constraint FK_byd7j1dmqdj6an0efh37n7ptg 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_rule 
        add constraint FK_6lskcjmbl9xlj3fp0rh2qdvag 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_rule 
        add constraint FK_6u2ay4hkyl9covhsw5h5pe8bg 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_rule 
        add constraint FK_7hrh2jygbpgha4ifygjelmjso 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_sink 
        add constraint FK_d6i3eu1y3dhg1aqi297gl5hc7 
        foreign key (expression_id) 
        references expressions_chain (id);

    alter table diagram_sink 
        add constraint FK_j1bq9cxe96nes01g1sj8uu6yu 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_sink 
        add constraint FK_ccaxk6bfmwcfpms10yxx8yalb 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_sink 
        add constraint FK_h92qa8de78mabk8p936leo54i 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_sink 
        add constraint FK_42tevycbgnk1db7dcp3pdbie4 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_source 
        add constraint FK_6pfm1s0mv6lrckpl8kir908eg 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_source 
        add constraint FK_nkdq9q9sbfty5ha444iileysg 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_source 
        add constraint FK_qalf6e6t0nqdvpqysu7j6aytd 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_source 
        add constraint FK_n32ufbyplqnyssc8oxtef7y4 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_table 
        add constraint FK_ews6b37yjyobhivu09sacwcou 
        foreign key (table_id) 
        references rule_decision_table (id);

    alter table diagram_table 
        add constraint FK_9ih9i9jga6lnggldt4qwdf6ml 
        foreign key (biitText_id) 
        references diagram_biit_text (id);

    alter table diagram_table 
        add constraint FK_cfqkxwg3fqpy4dx1fi3kj4o49 
        foreign key (position_id) 
        references diagram_points (id);

    alter table diagram_table 
        add constraint FK_77ecqjnrkysvsoo2xgre6xuoi 
        foreign key (size_id) 
        references diagram_sizes (id);

    alter table diagram_table 
        add constraint FK_2xlmgbabun5yxio8y4ph3caay 
        foreign key (parent) 
        references diagram (id);

    alter table elements_of_diagram 
        add constraint FK_nkcto9r1kt3lshu9xu3lvvew2 
        foreign key (diagram_id) 
        references diagram (id);

    alter table expression_value_custom_variable 
        add constraint FK_6t3dlq7qrjolbhuio0eya8rqq 
        foreign key (variable_id) 
        references form_custom_variables (id);

    alter table expression_value_generic_custom_variable 
        add constraint FK_mxxy1tfwjpa53ej8a578gl6s1 
        foreign key (variable_id) 
        references form_custom_variables (id);

    alter table expression_value_global_variable 
        add constraint FK_3qe2k1hlyg75b5xuwlwt8rhx4 
        foreign key (globalVariable_id) 
        references global_variables (id);

    alter table expressions_chain_expression_basic 
        add constraint FK_qhk31snb756cd02e1wykshmbg 
        foreign key (expressions_chain_id) 
        references expressions_chain (id);

    alter table form_custom_variables 
        add constraint FK_ev3h2dj07tfxm6xw6d5v03fb 
        foreign key (form) 
        references tree_forms (id);

    alter table global_variable_data_set 
        add constraint FK_3a6w2ktwy9ppq1ftw2cprnq7u 
        foreign key (global_variables_id) 
        references global_variables (id);

    alter table rule 
        add constraint FK_ly8wgmgy3428yj8l4s95p6jv9 
        foreign key (actions_id) 
        references expressions_chain (id);

    alter table rule 
        add constraint FK_3794ko8ce1i5of4vysh7pkj0o 
        foreign key (conditions_id) 
        references expressions_chain (id);

    alter table rule_decision_table_row 
        add constraint FK_jy4vg5whyrriu3k8wc156a6xi 
        foreign key (action_id) 
        references expressions_chain (id);

    alter table rule_decision_table_row 
        add constraint FK_ctfbyk16e845mqjmmyramewu2 
        foreign key (conditions_id) 
        references expressions_chain (id);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint FK_j9j9wq9fnivf6m5exoi9fwfj2 
        foreign key (rules_id) 
        references rule_decision_table_row (id);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint FK_3b21bread2lyauj75i58tehxi 
        foreign key (rule_decision_table_id) 
        references rule_decision_table (id);

    alter table test_answer_multi_checkbox_values 
        add constraint FK_pwr2c4mmyjta3qcj4i3a831wc 
        foreign key (TestAnswerMultiCheckBox_id) 
        references test_answer_multi_checkbox (id);

    alter table test_scenario 
        add constraint FK_ifr684ejunas5qla9q691iigh 
        foreign key (testScenarioForm_id) 
        references test_scenario_form (id);

    alter table tree_forms_diagram 
        add constraint FK_kupriscdg8vbaueghjbub50wn 
        foreign key (diagrams_id) 
        references diagram (id);

    alter table tree_forms_diagram 
        add constraint FK_5q539qvus88s3p9tfpj8b1clh 
        foreign key (tree_forms_id) 
        references tree_forms (id);

    alter table tree_forms_expressions_chain 
        add constraint FK_odixr0cg2sffi97kgn1twedbq 
        foreign key (expressionChains_id) 
        references expressions_chain (id);

    alter table tree_forms_expressions_chain 
        add constraint FK_gk1nyr8acjbvubqut6coox9nq 
        foreign key (tree_forms_id) 
        references tree_forms (id);

    alter table tree_forms_rule 
        add constraint FK_qywcba5hk1dbarg1hdttwxef6 
        foreign key (rules_id) 
        references rule (id);

    alter table tree_forms_rule 
        add constraint FK_s5ey41ep7k9cp45hr16fv2eoc 
        foreign key (tree_forms_id) 
        references tree_forms (id);

    alter table tree_forms_rule_decision_table 
        add constraint FK_e0ilwwjcfr73m08kid8l8vqth 
        foreign key (tableRules_id) 
        references rule_decision_table (id);

    alter table tree_forms_rule_decision_table 
        add constraint FK_66thky5y9pgts1o4rmohiq8sh 
        foreign key (tree_forms_id) 
        references tree_forms (id);

	CREATE TABLE `hibernate_sequence` (
		`next_val` bigint(20) DEFAULT NULL
	);

	LOCK TABLES `hibernate_sequence` WRITE;
	INSERT INTO `hibernate_sequence` VALUES (1);
	UNLOCK TABLES;


