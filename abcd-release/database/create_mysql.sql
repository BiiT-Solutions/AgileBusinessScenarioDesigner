
    create table TestAnswerMultiCheckBox_multiCheckBoxValue (
        TestAnswerMultiCheckBox_ID bigint not null,
        multiCheckBoxValue varchar(255)
    );

    create table diagram (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        primary key (ID)
    );

    create table diagram_biit_text (
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

    create table diagram_calculation (
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

    create table diagram_child (
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

    create table diagram_fork (
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

    create table diagram_fork_expression_value_tree_object_reference (
        diagram_fork_ID bigint not null,
        reference_ID bigint not null
    );

    create table diagram_links (
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

    create table diagram_nodes (
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

    create table diagram_points (
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

    create table diagram_repeat (
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

    create table diagram_rule (
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

    create table diagram_sink (
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

    create table diagram_sizes (
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

    create table diagram_source (
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

    create table diagram_table (
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

    create table elements_of_diagram (
        diagram_ID bigint not null,
        diagramElements_ID bigint not null
    );

    create table expression_function (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value varchar(255),
        primary key (ID)
    );

    create table expression_operator_logic (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        currentValue varchar(255),
        primary key (ID)
    );

    create table expression_operator_math (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        currentValue varchar(255),
        primary key (ID)
    );

    create table expression_symbol (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value varchar(255),
        primary key (ID)
    );

    create table expression_value_boolean (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value bit not null,
        primary key (ID)
    );

    create table expression_value_custom_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        unit varchar(255),
        reference_ID bigint,
        variable_ID bigint,
        primary key (ID)
    );

    create table expression_value_expression_reference (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value_ID bigint,
        primary key (ID)
    );

    create table expression_value_generic_custom_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255),
        variable_ID bigint,
        primary key (ID)
    );

    create table expression_value_generic_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255),
        primary key (ID)
    );

    create table expression_value_global_variable (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        constant_ID bigint,
        primary key (ID)
    );

    create table expression_value_number (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value double precision not null,
        primary key (ID)
    );

    create table expression_value_postal_code (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value TEXT,
        primary key (ID)
    );

    create table expression_value_string (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value TEXT,
        primary key (ID)
    );

    create table expression_value_systemdate (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value datetime,
        primary key (ID)
    );

    create table expression_value_timestamp (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        value datetime,
        primary key (ID)
    );

    create table expression_value_tree_object_reference (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        unit varchar(255),
        reference_ID bigint,
        primary key (ID)
    );

    create table expressions_chain (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        name varchar(255),
        primary key (ID)
    );

    create table expressions_chain_expression_basic (
        expressions_chain_ID bigint not null,
        expressions_ID bigint not null
    );

    create table form_custom_variables (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(190),
        scope varchar(190),
        type varchar(255),
        form bigint,
        primary key (ID)
    );

    create table global_variable_data_date (
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

    create table global_variable_data_number (
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

    create table global_variable_data_postalcode (
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

    create table global_variable_data_set (
        global_variables_ID bigint not null,
        data_ID bigint not null
    );

    create table global_variable_data_text (
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

    create table global_variables (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        format integer,
        name varchar(190),
        primary key (ID)
    );

    create table rule (
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

    create table rule_decision_table (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        name varchar(255),
        primary key (ID)
    );

    create table rule_decision_table_row (
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

    create table rule_decision_table_rule_decision_table_row (
        rule_decision_table_ID bigint not null,
        rules_ID bigint not null
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

    create table tree_answers (
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

    create table tree_categories (
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

    create table tree_forms (
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
        version integer,
        availableFrom datetime not null,
        availableTo datetime,
        organizationId DOUBLE not null,
        primary key (ID)
    );

    create table tree_forms_diagram (
        tree_forms_ID bigint not null,
        diagrams_ID bigint not null,
        primary key (tree_forms_ID, diagrams_ID)
    );

    create table tree_forms_expressions_chain (
        tree_forms_ID bigint not null,
        expressionChain_ID bigint not null,
        primary key (tree_forms_ID, expressionChain_ID)
    );

    create table tree_forms_form_custom_variables (
        tree_forms_ID bigint not null,
        customVariables_ID bigint not null,
        primary key (tree_forms_ID, customVariables_ID)
    );

    create table tree_forms_rule (
        tree_forms_ID bigint not null,
        rules_ID bigint not null,
        primary key (tree_forms_ID, rules_ID)
    );

    create table tree_forms_rule_decision_table (
        tree_forms_ID bigint not null,
        tableRules_ID bigint not null,
        primary key (tree_forms_ID, tableRules_ID)
    );

    create table tree_forms_test_scenario (
        tree_forms_ID bigint not null,
        testScenarios_ID bigint not null,
        primary key (tree_forms_ID, testScenarios_ID)
    );

    create table tree_groups (
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
        repeatable bit not null,
        primary key (ID)
    );

    create table tree_questions (
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
        answerFormat varchar(255),
        answerType varchar(255),
        primary key (ID)
    );

    alter table diagram 
        add constraint UK_cqcepkojqmp1r8a42yb1hh1c4  unique (ID);

    alter table diagram 
        add constraint UK_i991a76ub6cc04w67skutnlft  unique (comparationId);

    alter table diagram_biit_text 
        add constraint UK_jcm4u3t3e37kkjb5mltbs5mha  unique (ID);

    alter table diagram_biit_text 
        add constraint UK_26e5ksw8ie3l7k0gtpdghwxr8  unique (comparationId);

    alter table diagram_calculation 
        add constraint UK_telsgfj7x5yb9vr3tyes22dfo  unique (ID);

    alter table diagram_calculation 
        add constraint UK_skg33psaqtjwx1oevy1wp7tfa  unique (comparationId);

    alter table diagram_child 
        add constraint UK_g7xdiesweik5op9e9onvswkbc  unique (ID);

    alter table diagram_child 
        add constraint UK_r7oc8jgmhkeagu3emxy90ttof  unique (comparationId);

    alter table diagram_fork 
        add constraint UK_hsf0g6nuli7nv6ypx60d3n4dg  unique (ID);

    alter table diagram_fork 
        add constraint UK_efh14aqc52m2bt7suefmq12qn  unique (comparationId);

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint UK_qjdox15ikgx1yrjmmwmi60s5o  unique (reference_ID);

    alter table diagram_links 
        add constraint UK_h604hokhqdqbq8jnjxeupxokf  unique (ID);

    alter table diagram_links 
        add constraint UK_ph2m4o4wkkd1ls8xxffxf0q4  unique (comparationId);

    alter table diagram_nodes 
        add constraint UK_8a8ejpu1o9334lnd8qaxmngtc  unique (ID);

    alter table diagram_nodes 
        add constraint UK_r73mc67afakc2crvohrek8qk4  unique (comparationId);

    alter table diagram_points 
        add constraint UK_hlburchqkc9q4ahhvbtm8r9p9  unique (ID);

    alter table diagram_points 
        add constraint UK_nmcm8lwfgn9lwd2kg6e77mlpx  unique (comparationId);

    alter table diagram_repeat 
        add constraint UK_cmfc1l3o05vfhv0pt6mgqm3nd  unique (ID);

    alter table diagram_repeat 
        add constraint UK_5jgfcctfdg2s0qbkuaekf6m0g  unique (comparationId);

    alter table diagram_rule 
        add constraint UK_o4hrkbic99uuo11mqks328fv9  unique (ID);

    alter table diagram_rule 
        add constraint UK_l4rl73pva0mblx0df1ntrb3m7  unique (comparationId);

    alter table diagram_sink 
        add constraint UK_cjl3d4py1hd5d5tvo23yd780y  unique (ID);

    alter table diagram_sink 
        add constraint UK_bo43xa2d2veyl1w7o8xfjse1m  unique (comparationId);

    alter table diagram_sizes 
        add constraint UK_ipg7ga5eq6253uw0hwppg86ub  unique (ID);

    alter table diagram_sizes 
        add constraint UK_lpwfoljk42ognggvtk6m0w75j  unique (comparationId);

    alter table diagram_source 
        add constraint UK_ff46qqbboep27fpldjifb16ou  unique (ID);

    alter table diagram_source 
        add constraint UK_2skl3aj5ivhq9i7j7vun9eak3  unique (comparationId);

    alter table diagram_table 
        add constraint UK_rdrkegb0jykxqg5jxtb2fbhc5  unique (ID);

    alter table diagram_table 
        add constraint UK_8f7iwv857341khhqhi12jharn  unique (comparationId);

    alter table elements_of_diagram 
        add constraint UK_a91dlb8ggcm3dkxl70tcjspe  unique (diagramElements_ID);

    alter table expression_function 
        add constraint UK_7hl0l4oq4hokig8i3g30hv3s4  unique (ID);

    alter table expression_function 
        add constraint UK_lbyra8yeo77q9okjte7yh5mck  unique (comparationId);

    alter table expression_operator_logic 
        add constraint UK_5nv4rm0dcwpx72pidk99uqdcv  unique (ID);

    alter table expression_operator_logic 
        add constraint UK_joflju3ccpgq1r4y7ro8egx3g  unique (comparationId);

    alter table expression_operator_math 
        add constraint UK_blgg3hvajwkh0url70tu2l3nc  unique (ID);

    alter table expression_operator_math 
        add constraint UK_8thp00x8evlgl3w2guass30ql  unique (comparationId);

    alter table expression_symbol 
        add constraint UK_d5pgsndfldjl77mxwesg47oyy  unique (ID);

    alter table expression_symbol 
        add constraint UK_sw4rg5w1mhkjtuhmshj7jeh5w  unique (comparationId);

    alter table expression_value_boolean 
        add constraint UK_8ji8c5v2rtu64hu7fd6w16wds  unique (ID);

    alter table expression_value_boolean 
        add constraint UK_9ji3f3ooll872c0bitchknl29  unique (comparationId);

    alter table expression_value_custom_variable 
        add constraint UK_g4ftxu9duvw2yoonn0mjb5sku  unique (ID);

    alter table expression_value_custom_variable 
        add constraint UK_6be9j8k8bp0uudvgtk61e4tu4  unique (comparationId);

    alter table expression_value_expression_reference 
        add constraint UK_egetkbs1hrvgqajn7qplpf4yn  unique (ID);

    alter table expression_value_expression_reference 
        add constraint UK_irf75tqpf7ywr50qxdg3fqwge  unique (comparationId);

    alter table expression_value_generic_custom_variable 
        add constraint UK_1ilcykdnhd0ixr9iaomtrog3h  unique (ID);

    alter table expression_value_generic_custom_variable 
        add constraint UK_72ucvxvkp3dgy4ci55hda72p5  unique (comparationId);

    alter table expression_value_generic_variable 
        add constraint UK_cqsa891g9cu7egmcxep74o4gh  unique (ID);

    alter table expression_value_generic_variable 
        add constraint UK_1qdyf7b9pdbtg7ugajlcbuy99  unique (comparationId);

    alter table expression_value_global_variable 
        add constraint UK_gbcgljgnhew0ftkmyyjo9ef0k  unique (ID);

    alter table expression_value_global_variable 
        add constraint UK_thug7lch6aqrx4iwq0dtwabrr  unique (comparationId);

    alter table expression_value_number 
        add constraint UK_4iv29s41m049yhnm25ykovg2j  unique (ID);

    alter table expression_value_number 
        add constraint UK_5pr6kfc8ssvjg8bcoxs7iaxo6  unique (comparationId);

    alter table expression_value_postal_code 
        add constraint UK_jnl1ff1sox87eqvsa3w1nugx9  unique (ID);

    alter table expression_value_postal_code 
        add constraint UK_65y7nqlko6hgjo6b3go7lwa82  unique (comparationId);

    alter table expression_value_string 
        add constraint UK_ssjam0lpu4ptut6q7uiyinb46  unique (ID);

    alter table expression_value_string 
        add constraint UK_imteh9r9im4ctld1b9b5kkacw  unique (comparationId);

    alter table expression_value_systemdate 
        add constraint UK_mj1pnbvm8gv3gngq2yh15502f  unique (ID);

    alter table expression_value_systemdate 
        add constraint UK_co43pbqqg7civhr3g8p752mt8  unique (comparationId);

    alter table expression_value_timestamp 
        add constraint UK_kju72l70wu9w63min4j3y59jw  unique (ID);

    alter table expression_value_timestamp 
        add constraint UK_ser5ax80v1g2co9dbdqstv81l  unique (comparationId);

    alter table expression_value_tree_object_reference 
        add constraint UK_57lm65prgqk32q1w0rd3jptux  unique (ID);

    alter table expression_value_tree_object_reference 
        add constraint UK_an3mu4deq3muvltuwdawkfbhc  unique (comparationId);

    alter table expressions_chain 
        add constraint UK_l2k0vcoohq76m4cl2k8g3y9hc  unique (ID);

    alter table expressions_chain 
        add constraint UK_qageeu0ehecelhxmn5qdv93am  unique (comparationId);

    alter table expressions_chain_expression_basic 
        add constraint UK_6n86noaf7rpgu2qagq951m5da  unique (expressions_ID);

    alter table form_custom_variables 
        add constraint UK_2pj0qoh0ntvs9laf9sh42rqap  unique (form, name, scope);

    alter table form_custom_variables 
        add constraint UK_891b13251e3pkyk1k5vccpmnh  unique (ID);

    alter table form_custom_variables 
        add constraint UK_sq1dj9kjhkv951lr0o3pu42fc  unique (comparationId);

    alter table global_variable_data_date 
        add constraint UK_mylp5k1001col2n4rpdshrea7  unique (ID);

    alter table global_variable_data_date 
        add constraint UK_sptr6kn7psib0u6dqt8r7stkq  unique (comparationId);

    alter table global_variable_data_number 
        add constraint UK_ksal0k0qkvv2v95wvysufcap3  unique (ID);

    alter table global_variable_data_number 
        add constraint UK_jbkxahvrsxikil188orj2bion  unique (comparationId);

    alter table global_variable_data_postalcode 
        add constraint UK_o8e2he19y5wsa4bxopiih4ewp  unique (ID);

    alter table global_variable_data_postalcode 
        add constraint UK_1g8ysiehft4rrd3aiheq1cktx  unique (comparationId);

    alter table global_variable_data_set 
        add constraint UK_pttoh2ouj3te5iqnhvym6l2g1  unique (data_ID);

    alter table global_variable_data_text 
        add constraint UK_t91aqfdhy6wnrrg749k2een9u  unique (ID);

    alter table global_variable_data_text 
        add constraint UK_mowohsw3lf7j5hh676yr34en8  unique (comparationId);

    alter table global_variables 
        add constraint UK_nggea8kl5cb894e5du3cwsrd0  unique (ID);

    alter table global_variables 
        add constraint UK_kr7p6k3u1po5mbamq95rvh6gj  unique (comparationId);

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);

    alter table rule 
        add constraint UK_8rqluiaunf9galin639sd894c  unique (ID);

    alter table rule 
        add constraint UK_bmfhvbjf4kaugtlg6wom9crg9  unique (comparationId);

    alter table rule_decision_table 
        add constraint UK_bumgbl5omimxtvvhvthstr882  unique (ID);

    alter table rule_decision_table 
        add constraint UK_20los6ndm6d9errmf4erx9f12  unique (comparationId);

    alter table rule_decision_table_row 
        add constraint UK_ondmt5ex56yathpd70q11lwgg  unique (ID);

    alter table rule_decision_table_row 
        add constraint UK_r7njhoe2s46aht07exgq5hp5f  unique (comparationId);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint UK_9357dg3c4e5y9gkaggcdxrfnb  unique (rules_ID);

    alter table test_answer_input_date 
        add constraint UK_cctx6br59kggymqx6ltv65iv9  unique (ID);

    alter table test_answer_input_date 
        add constraint UK_8r0jv8nwetnhcx3mwixg6uhn6  unique (comparationId);

    alter table test_answer_input_number 
        add constraint UK_rrf27v5jka8vvm7pvp3846qac  unique (ID);

    alter table test_answer_input_number 
        add constraint UK_aljs5lkmjtgb0ys9oxac2jo5m  unique (comparationId);

    alter table test_answer_input_postalcode 
        add constraint UK_9n169jx3xa3jv40g097bb6k4g  unique (ID);

    alter table test_answer_input_postalcode 
        add constraint UK_gmbj48islivqbak8uhqn4acra  unique (comparationId);

    alter table test_answer_input_text 
        add constraint UK_hdccu2j9fjpphcxxt6wsuqo1v  unique (ID);

    alter table test_answer_input_text 
        add constraint UK_fm38doewevroqbh9w9yl6uy45  unique (comparationId);

    alter table test_answer_multi_checkbox 
        add constraint UK_u2axqvpcnrfbj8tflcs5v8qu  unique (ID);

    alter table test_answer_multi_checkbox 
        add constraint UK_neljyd1uorqx4rh8nwh89pofm  unique (comparationId);

    alter table test_answer_radio_button 
        add constraint UK_eakcce3ybd0073kdajy217yju  unique (ID);

    alter table test_answer_radio_button 
        add constraint UK_rrv3rw7jasepphc1943fqgis5  unique (comparationId);

    alter table test_scenario 
        add constraint UK_j17qvfqb5wcp4c3bgknvdii31  unique (ID);

    alter table test_scenario 
        add constraint UK_eh6es7t34ldoxns3sswdj6vku  unique (comparationId);

    alter table test_scenario 
        add constraint UK_jshq05r5jh9kw6obudq99vsee  unique (name);

    alter table test_scenario_test_answer_basic 
        add constraint UK_a50es1q5iamqr0xaqhnym6a9w  unique (questionTestAnswerRelationship_ID);

    alter table tree_answers 
        add constraint UK_413vxa542h86uqy4uvcnv6y2x  unique (ID);

    alter table tree_answers 
        add constraint UK_5xuj3de6ide6evpo4sijpqa4o  unique (comparationId);

    alter table tree_categories 
        add constraint UK_ec3bvy7lletc6jmyvyfwuroqv  unique (ID);

    alter table tree_categories 
        add constraint UK_gtcyh8mle277igwtb5dvhjkr1  unique (comparationId);

    alter table tree_forms 
        add constraint UK_iwgivd7sy9sfbjyj0hlyccrxt  unique (label, version);

    alter table tree_forms 
        add constraint UK_plkq2e2pj19uak2ncrgf1ft6v  unique (ID);

    alter table tree_forms 
        add constraint UK_k9mhkly9g8lqwf1m9esm50y6m  unique (comparationId);

    alter table tree_forms_diagram 
        add constraint UK_otbxhecixo9rbriamr8v44nik  unique (diagrams_ID);

    alter table tree_forms_expressions_chain 
        add constraint UK_o92uqjo7rspntjcyeohxrlktl  unique (expressionChain_ID);

    alter table tree_forms_form_custom_variables 
        add constraint UK_8javwlxk4w7hc1g5g52yecvtt  unique (customVariables_ID);

    alter table tree_forms_rule 
        add constraint UK_t8v6e3oyk0k56toxk97afkpvc  unique (rules_ID);

    alter table tree_forms_rule_decision_table 
        add constraint UK_b274bmp72bu1n40rl4k5kvhas  unique (tableRules_ID);

    alter table tree_forms_test_scenario 
        add constraint UK_c0jjkkgcgpgx4o9h315avv5ev  unique (testScenarios_ID);

    alter table tree_groups 
        add constraint UK_sfdvxxi1k3p9pqsjl5nhmgdp  unique (ID);

    alter table tree_groups 
        add constraint UK_sno2xl7o9nxmt3xh48ywus36u  unique (comparationId);

    alter table tree_questions 
        add constraint UK_9lkt55st6up2vyh38lrmu0dc5  unique (ID);

    alter table tree_questions 
        add constraint UK_nu1epukynjltak450rhyp6eu0  unique (comparationId);

    alter table TestAnswerMultiCheckBox_multiCheckBoxValue 
        add constraint FK_9hig9ck1w4ry8gscespat7k7i 
        foreign key (TestAnswerMultiCheckBox_ID) 
        references test_answer_multi_checkbox (ID);

    alter table diagram_calculation 
        add constraint FK_hsl3um574sjl43jv8ne0qyq1 
        foreign key (formExpression_ID) 
        references expressions_chain (ID);

    alter table diagram_calculation 
        add constraint FK_1uyl1hy99cv9fsblw0vty8nva 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_calculation 
        add constraint FK_g827oda16qin30e31hhplvhyl 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_calculation 
        add constraint FK_5km22on0tjdp4ni437u0cqvx1 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_calculation 
        add constraint FK_4hddxi4jsy1lyvul13uh1im7i 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_child 
        add constraint FK_6vxec3l99gphfdbj8b4u9gcas 
        foreign key (childDiagram_ID) 
        references diagram (ID);

    alter table diagram_child 
        add constraint FK_s4ju47278gra0mv0udmlfm30p 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_child 
        add constraint FK_5eniu52wmx99g8daejx2ea7wy 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_child 
        add constraint FK_mk7mxfcjnf05307b1wgoj9tbr 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_child 
        add constraint FK_qs6gusu4xfg3ntskiiw6i8nlk 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_fork 
        add constraint FK_7yh670hkt93t0pcttv0bq1uuc 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_fork 
        add constraint FK_eii7oliuam2g30sl5dtq1pjle 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_fork 
        add constraint FK_hsgneuh3yuqhqv4kogkhp91u8 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_fork 
        add constraint FK_635sdu3m0ivan4vb6f86e6ws0 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_fork_expression_value_tree_object_reference 
        add constraint FK_k4ysmnavvafierl7bdith4l1j 
        foreign key (diagram_fork_ID) 
        references diagram_fork (ID);

    alter table diagram_links 
        add constraint FK_f9u34y5vofhcg8gcr88wm8ox1 
        foreign key (expressionChain_ID) 
        references expressions_chain (ID);

    alter table diagram_links 
        add constraint FK_4uc5itybcysh76lvtc9ty3469 
        foreign key (source_ID) 
        references diagram_nodes (ID);

    alter table diagram_links 
        add constraint FK_7jd18fn2cgk256ih0iyoorfti 
        foreign key (target_ID) 
        references diagram_nodes (ID);

    alter table diagram_links 
        add constraint FK_82fd22e255jkopdyoep9xmk2y 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_repeat 
        add constraint FK_aqu17m49nfn0yojvr0wndem3j 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_repeat 
        add constraint FK_f4cg4oy7tlbbcheb3ec7045yo 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_repeat 
        add constraint FK_qu2ktgi18pqa3t86hk44xksfx 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_repeat 
        add constraint FK_erdsxlq0j84okd36bay5dt6q9 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_rule 
        add constraint FK_fxsyldssewd7iifwrr4f985d5 
        foreign key (rule_ID) 
        references rule (ID);

    alter table diagram_rule 
        add constraint FK_nbxgk4j5653j1qa0bw4y70w65 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_rule 
        add constraint FK_aglllff4oo9xy4r8knqyu8v3k 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_rule 
        add constraint FK_7dwjxlkwog3pq4dvcmhx9sssw 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_rule 
        add constraint FK_h1iqc4v2ldablgwfng2000fcv 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_sink 
        add constraint FK_itbnin1nehkac81mo4qx0msvy 
        foreign key (formExpression_ID) 
        references expressions_chain (ID);

    alter table diagram_sink 
        add constraint FK_rektcafnj1qj3khm8w8sxyx8i 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_sink 
        add constraint FK_abyk770ndq91g7yecoi8h3pjt 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_sink 
        add constraint FK_ff3w8r2n4ux3vmd2tr1lc00sd 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_sink 
        add constraint FK_g13n7i3embg63j1pkswrlhvbb 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_source 
        add constraint FK_d1l2nioib46nwd01bhbofv6ny 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_source 
        add constraint FK_17lhs8umd0536jewa7ht9hbs2 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_source 
        add constraint FK_3h8kcg1497rr1y30r9kb4b90m 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_source 
        add constraint FK_a2hmqaospl98o7hmxdvuh093f 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table diagram_table 
        add constraint FK_m64qi6jykhdxghdvv760geh30 
        foreign key (table_ID) 
        references rule_decision_table (ID);

    alter table diagram_table 
        add constraint FK_7iejtd4odckryjuy5lu68ebm5 
        foreign key (biitText_ID) 
        references diagram_biit_text (ID);

    alter table diagram_table 
        add constraint FK_m0co9nwuhqbwjf59ha3ltisma 
        foreign key (position_ID) 
        references diagram_points (ID);

    alter table diagram_table 
        add constraint FK_odmfi3t11xmwe0hnevh2ymxek 
        foreign key (size_ID) 
        references diagram_sizes (ID);

    alter table diagram_table 
        add constraint FK_n4ug9duflxy9px6g0fb8uoq6w 
        foreign key (parent_ID) 
        references diagram (ID);

    alter table elements_of_diagram 
        add constraint FK_i75jp9q1ac01gmqrc3ur49f0g 
        foreign key (diagram_ID) 
        references diagram (ID);

    alter table expression_value_custom_variable 
        add constraint FK_nfs9s2t4plx74r3n105o09sjx 
        foreign key (variable_ID) 
        references form_custom_variables (ID);

    alter table expression_value_generic_custom_variable 
        add constraint FK_4u49ngxh67i7rqxx3xb7379yi 
        foreign key (variable_ID) 
        references form_custom_variables (ID);

    alter table expression_value_global_variable 
        add constraint FK_42woqe4atagm0r4oxgcnk6qwo 
        foreign key (constant_ID) 
        references global_variables (ID);

    alter table expressions_chain_expression_basic 
        add constraint FK_5u04lt24nd5qdcqdc9s5htsy8 
        foreign key (expressions_chain_ID) 
        references expressions_chain (ID);

    alter table form_custom_variables 
        add constraint FK_ev3h2dj07tfxm6xw6d5v03fb 
        foreign key (form) 
        references tree_forms (ID);

    alter table global_variable_data_set 
        add constraint FK_lwed03o1lmh4t449lt1lk8k3b 
        foreign key (global_variables_ID) 
        references global_variables (ID);

    alter table rule 
        add constraint FK_3327iu3vsaiyi6xxv47du4nte 
        foreign key (actions_ID) 
        references expressions_chain (ID);

    alter table rule 
        add constraint FK_9617cunyhxob1svrhrvs3xc1u 
        foreign key (condition_ID) 
        references expressions_chain (ID);

    alter table rule_decision_table_row 
        add constraint FK_mqhpiw889x6dvrni1kbdr6n8u 
        foreign key (action_ID) 
        references expressions_chain (ID);

    alter table rule_decision_table_row 
        add constraint FK_85ewg5m9454k3316wo7ypnxq3 
        foreign key (conditions_ID) 
        references expressions_chain (ID);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint FK_9357dg3c4e5y9gkaggcdxrfnb 
        foreign key (rules_ID) 
        references rule_decision_table_row (ID);

    alter table rule_decision_table_rule_decision_table_row 
        add constraint FK_dk0yu4ajw581l5dpebmouwuce 
        foreign key (rule_decision_table_ID) 
        references rule_decision_table (ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_jy3bdbqvowh4tjeta3t231nbl 
        foreign key (questionTestAnswerRelationship_KEY) 
        references tree_questions (ID);

    alter table test_scenario_test_answer_basic 
        add constraint FK_a1lgwvamyrw44r3i3kfbvn0ti 
        foreign key (test_scenario_ID) 
        references test_scenario (ID);

    alter table tree_forms_diagram 
        add constraint FK_otbxhecixo9rbriamr8v44nik 
        foreign key (diagrams_ID) 
        references diagram (ID);

    alter table tree_forms_diagram 
        add constraint FK_h95kubmo9q1w4k54fxnk74kyb 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);

    alter table tree_forms_expressions_chain 
        add constraint FK_o92uqjo7rspntjcyeohxrlktl 
        foreign key (expressionChain_ID) 
        references expressions_chain (ID);

    alter table tree_forms_expressions_chain 
        add constraint FK_5wekloqjwnu88bfqpn292su2w 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);

    alter table tree_forms_form_custom_variables 
        add constraint FK_8javwlxk4w7hc1g5g52yecvtt 
        foreign key (customVariables_ID) 
        references form_custom_variables (ID);

    alter table tree_forms_form_custom_variables 
        add constraint FK_jprngt3ueexabb6la0oy6jv3o 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);

    alter table tree_forms_rule 
        add constraint FK_t8v6e3oyk0k56toxk97afkpvc 
        foreign key (rules_ID) 
        references rule (ID);

    alter table tree_forms_rule 
        add constraint FK_o7o97qsjclwv4fu4n6dajk1on 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);

    alter table tree_forms_rule_decision_table 
        add constraint FK_b274bmp72bu1n40rl4k5kvhas 
        foreign key (tableRules_ID) 
        references rule_decision_table (ID);

    alter table tree_forms_rule_decision_table 
        add constraint FK_mb6o16g0xsjlo3nx5xoc0i0g9 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);

    alter table tree_forms_test_scenario 
        add constraint FK_c0jjkkgcgpgx4o9h315avv5ev 
        foreign key (testScenarios_ID) 
        references test_scenario (ID);

    alter table tree_forms_test_scenario 
        add constraint FK_fqeidj7da9e5kofvo7xc6970x 
        foreign key (tree_forms_ID) 
        references tree_forms (ID);
