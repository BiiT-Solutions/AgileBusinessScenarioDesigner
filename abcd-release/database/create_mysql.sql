
    create table custom_variables (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        default_value varchar(255),
        name varchar(190),
        scope varchar(190),
        type varchar(255),
        form bigint not null,
        primary key (id)
    );

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
        position bigint,
        size bigint,
        text bigint,
        diagram bigint,
        primary key (id)
    );

    create table diagram_expression (
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
        position bigint,
        size bigint,
        text bigint,
        expression bigint,
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
        position bigint,
        size bigint,
        text bigint,
        primary key (id)
    );

    create table diagram_fork_references (
        diagram_fork bigint not null,
        reference bigint not null
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
        expression_chain bigint,
        source bigint,
        target bigint,
        primary key (id)
    );

    create table diagram_nodes (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        jointjs_id varchar(255),
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
        position bigint,
        size bigint,
        text bigint,
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
        position bigint,
        size bigint,
        text bigint,
        rule bigint,
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
        position bigint,
        size bigint,
        text bigint,
        expression bigint,
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
        position bigint,
        size bigint,
        text bigint,
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
        position bigint,
        size bigint,
        text bigint,
        table_rule bigint,
        primary key (id)
    );

    create table diagram_text (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        fill varchar(255),
        font_size varchar(255),
        stroke varchar(255),
        stroke_width varchar(255),
        text varchar(255),
        primary key (id)
    );

    create table elements_of_diagram (
        diagram bigint not null,
        diagram_object bigint not null,
        primary key (diagram, diagram_object)
    );

    create table expression_function (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
        current_value varchar(255),
        primary key (id)
    );

    create table expression_operator_math (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
        current_value varchar(255),
        primary key (id)
    );

    create table expression_plugin_method (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
        plugin_interface varchar(255),
        plugin_method_name varchar(255),
        plugin_name varchar(255),
        primary key (id)
    );

    create table expression_symbol (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
        unit varchar(255),
        reference bigint,
        variable bigint,
        primary key (id)
    );

    create table expression_value_generic_custom_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255),
        variable bigint,
        primary key (id)
    );

    create table expression_value_generic_variable (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
        global_variable bigint,
        primary key (id)
    );

    create table expression_value_number (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
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
        sort_sequence bigint not null,
        unit varchar(255),
        reference bigint,
        primary key (id)
    );

    create table expressions_chain (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_sequence bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table expressions_chain_expressions (
        expressions_chain bigint not null,
        expressions bigint not null
    );

    create table global_variable_data_date (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        valid_from datetime,
        valid_to datetime,
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
        valid_from datetime,
        valid_to datetime,
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
        valid_from datetime,
        valid_to datetime,
        postalcode varchar(255),
        primary key (id)
    );

    create table global_variable_data_text (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        valid_from datetime,
        valid_to datetime,
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

    create table global_variables_variable_data (
        global_variable bigint not null,
        variable_data bigint not null
    );

    create table rule (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        name varchar(255),
        actions bigint,
        conditions bigint,
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
        actions bigint,
        conditions bigint,
        primary key (id)
    );

    create table rule_decision_table_row_rules (
        rule_decision_table bigint not null,
        rule bigint not null
    );

    create table test_answer_input_date (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        date_value datetime,
        primary key (id)
    );

    create table test_answer_input_number (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        input_value double precision,
        primary key (id)
    );

    create table test_answer_input_postalcode (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        input_value varchar(255),
        primary key (id)
    );

    create table test_answer_input_text (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        input_value varchar(255),
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
        test_answer_multi_checkbox bigint not null,
        multi_check_box_value varchar(255)
    );

    create table test_answer_radio_button (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        radio_button_value varchar(255),
        primary key (id)
    );

    create table test_scenario (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        form_id bigint not null,
        form_label varchar(190) not null,
        form_organization DOUBLE not null,
        name varchar(190),
        test_scenario_form bigint,
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
        add_enabled bit,
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
        test_answer bigint,
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
        available_from datetime not null,
        available_to datetime,
        status varchar(255),
        primary key (id)
    );

    create table tree_forms_diagram (
        form bigint not null,
        diagram bigint not null,
        primary key (form, diagram)
    );

    create table tree_forms_expressions_chain (
        form bigint not null,
        expression_chain bigint not null,
        primary key (form, expression_chain)
    );

    create table tree_forms_rule (
        form bigint not null,
        rule bigint not null,
        primary key (form, rule)
    );

    create table tree_forms_rule_decision_table (
        form bigint not null,
        table_rule bigint not null,
        primary key (form, table_rule)
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
        answer_format varchar(255),
        answer_type varchar(255),
        primary key (id)
    );

    alter table custom_variables 
        add constraint UK_m5lcv2lfe2ndoj4jbynogd5wh  unique (comparation_id);

    alter table diagram 
        add constraint UK_fw7vqsrf5p8j89vc6ivxw0x4l  unique (comparation_id);

    alter table diagram_child 
        add constraint UK_72flappx1vyc05wxvoaugnpbj  unique (id);

    alter table diagram_child 
        add constraint UK_4hhdypcpnc80foag5dcpkes  unique (comparation_id);

    alter table diagram_expression 
        add constraint UK_foovwuuqivj3kw3k1kxcnv3oy  unique (id);

    alter table diagram_expression 
        add constraint UK_3jc1yfkq09pwapfu2s93unfqf  unique (comparation_id);

    alter table diagram_fork 
        add constraint UK_bxxdhr1ueq0hiw2ri4fyl7okt  unique (id);

    alter table diagram_fork 
        add constraint UK_owctn38n79mnt1ujbow3qrj6a  unique (comparation_id);

    alter table diagram_fork_references 
        add constraint UK_fbxwothu21m4to5t92cxr15c8  unique (reference);

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

    alter table diagram_text 
        add constraint UK_irny259eb8d9k80q5wf7ng6dy  unique (comparation_id);

    alter table elements_of_diagram 
        add constraint UK_96p4qycb4aqbkmv8vwhhal4vq  unique (diagram_object);

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

    alter table expressions_chain_expressions 
        add constraint UK_gfkvidp1dtdj24nbdx49uhhd7  unique (expressions);

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

    alter table global_variable_data_text 
        add constraint UK_8eoxdjy9bqti61l6bklvwg21g  unique (id);

    alter table global_variable_data_text 
        add constraint UK_jr5t4hfjxv9e4465ngdsl8x7f  unique (comparation_id);

    alter table global_variables 
        add constraint UK_8hxc6j1i2cfa44g7wypkkjxvo  unique (comparation_id);

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);

    alter table global_variables_variable_data 
        add constraint UK_eg5ow0st4g9jhpus91x58i5s2  unique (variable_data);

    alter table rule 
        add constraint UK_kynfmryi32dudhf5d4ff1d248  unique (comparation_id);

    alter table rule_decision_table 
        add constraint UK_n9wggwt5vxd0c181iar53br72  unique (comparation_id);

    alter table rule_decision_table_row 
        add constraint UK_k9s0wi274elpijmqjof3avgy2  unique (comparation_id);

    alter table rule_decision_table_row_rules 
        add constraint UK_a74s342wqaat8f3d02tcs5fbo  unique (rule);

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
        add constraint UK_r9xm5nx5hucs0inuegkqf6v5e  unique (name, form_id);

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
        add constraint UK_goqe055h5r63fj0txmtyqlww7  unique (diagram);

    alter table tree_forms_expressions_chain 
        add constraint UK_9ci7n44y8tluqrc4pikjcitr0  unique (expression_chain);

    alter table tree_forms_rule 
        add constraint UK_gyqbf18go6la4iwmtq5leojl9  unique (rule);

    alter table tree_forms_rule_decision_table 
        add constraint UK_saklv952rv0ykbxy559pmv874  unique (table_rule);

    alter table tree_groups 
        add constraint UK_lkx000598o8fu6o45gc6j8j6h  unique (id);

    alter table tree_groups 
        add constraint UK_544k9wyac47tkk3mh7itf8h9f  unique (comparation_id);

    alter table tree_questions 
        add constraint UK_qmnu5ia8n490ok4d7obj7khk6  unique (id);

    alter table tree_questions 
        add constraint UK_589h63s3jthrsckwd8a4dn3xq  unique (comparation_id);

    alter table custom_variables 
        add constraint FK_nbvp3rs0mkj01q3u3y7d1smam 
        foreign key (form) 
        references tree_forms (id);

    alter table diagram_child 
        add constraint FK_ncuhyfx5qmiftgbj9s5cjx9d5 
        foreign key (diagram) 
        references diagram (id);

    alter table diagram_child 
        add constraint FK_4cf674wpibbe0qu7b5aqqce66 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_child 
        add constraint FK_qs6x2dafrt5yv63srkvtwnamk 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_child 
        add constraint FK_skm8tgo9scls3xtuk2axws6np 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_child 
        add constraint FK_95r5dam5y3ox7eij844g9b9fw 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_expression 
        add constraint FK_9dv9j4umqakxay1f33wnn4cya 
        foreign key (expression) 
        references expressions_chain (id);

    alter table diagram_expression 
        add constraint FK_lm4fpjlcpv4wwq59u3se1a5su 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_expression 
        add constraint FK_hlwupp5q0996noony4cvl8ur9 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_expression 
        add constraint FK_5dlx0utrhid02c2xqh042mah0 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_expression 
        add constraint FK_p6s1bxgi0gj5j291tuu3uycw5 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_fork 
        add constraint FK_6yel8f5hil7li38f282q74ux6 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_fork 
        add constraint FK_69dorhc0t6fwodxxi3c7knqi0 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_fork 
        add constraint FK_a85qld4o8y27mwwnh1s4yj8j3 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_fork 
        add constraint FK_5ma5sm4ekcu4nnsm9kgybi6cu 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_fork_references 
        add constraint FK_71e1x030rjp93nhsgou9anq7g 
        foreign key (diagram_fork) 
        references diagram_fork (id);

    alter table diagram_links 
        add constraint FK_i6e0lumbe85ontcmb1cqkxkam 
        foreign key (expression_chain) 
        references expressions_chain (id);

    alter table diagram_links 
        add constraint FK_2r961ciiuuj63mhql6o2ctaxs 
        foreign key (source) 
        references diagram_nodes (id);

    alter table diagram_links 
        add constraint FK_kuc1tjl52tgboyjr3djrk98k5 
        foreign key (target) 
        references diagram_nodes (id);

    alter table diagram_links 
        add constraint FK_tcs4xaphicr6lvh57b1pvfu8u 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_repeat 
        add constraint FK_fnf79gbmkjall8waihy9mae54 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_repeat 
        add constraint FK_9yimxc3m7tsn4gucch86y5v40 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_repeat 
        add constraint FK_hscgju2pqwi8nfjqtr1amk353 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_repeat 
        add constraint FK_3j5tdbdtvvmigv6fmxc3myd0f 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_rule 
        add constraint FK_dw83eb92r6ro4fs6whcf5ofsr 
        foreign key (rule) 
        references rule (id);

    alter table diagram_rule 
        add constraint FK_6w9f3lrcc88ingbnowsj1lbje 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_rule 
        add constraint FK_8d6ptx2e4p0urr7mu55xbw58g 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_rule 
        add constraint FK_j7x9iisgdh2gpt91petq39yah 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_rule 
        add constraint FK_7hrh2jygbpgha4ifygjelmjso 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_sink 
        add constraint FK_kcm35bx2c41ge2d732lj23dkh 
        foreign key (expression) 
        references expressions_chain (id);

    alter table diagram_sink 
        add constraint FK_tirw3mqnn4jwq7xstei8lnno2 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_sink 
        add constraint FK_mnax2jig9jg5qpisjqn6yk9ho 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_sink 
        add constraint FK_5oxjhkxs4egr30q75h8p4ugnh 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_sink 
        add constraint FK_42tevycbgnk1db7dcp3pdbie4 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_source 
        add constraint FK_7axo1txo4e85jyv3sfaurgxot 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_source 
        add constraint FK_ayjqsgwofi5v9by4y7y62lkd 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_source 
        add constraint FK_tkfmffmi58urjickhy2g34wai 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_source 
        add constraint FK_n32ufbyplqnyssc8oxtef7y4 
        foreign key (parent) 
        references diagram (id);

    alter table diagram_table 
        add constraint FK_e9i92wb61ka6q57bcdltq2mqm 
        foreign key (table_rule) 
        references rule_decision_table (id);

    alter table diagram_table 
        add constraint FK_dk9dv2v3cq6qk08xisr46ct15 
        foreign key (position) 
        references diagram_points (id);

    alter table diagram_table 
        add constraint FK_lsdigutf9mdou7sj3sh06hvsf 
        foreign key (size) 
        references diagram_sizes (id);

    alter table diagram_table 
        add constraint FK_p47g5xgk2o3qs416rt4ic1i61 
        foreign key (text) 
        references diagram_text (id);

    alter table diagram_table 
        add constraint FK_2xlmgbabun5yxio8y4ph3caay 
        foreign key (parent) 
        references diagram (id);

    alter table elements_of_diagram 
        add constraint FK_srdhwrnjmtakxrktmdn6qs5r9 
        foreign key (diagram) 
        references diagram (id);

    alter table expression_value_custom_variable 
        add constraint FK_2nswd0obxh9dycb44n2uynrji 
        foreign key (variable) 
        references custom_variables (id);

    alter table expression_value_generic_custom_variable 
        add constraint FK_8n70184rsj92524498jmsgq84 
        foreign key (variable) 
        references custom_variables (id);

    alter table expression_value_global_variable 
        add constraint FK_oteo9jes2oxo2ylaqmia00gb 
        foreign key (global_variable) 
        references global_variables (id);

    alter table expressions_chain_expressions 
        add constraint FK_8s9codiage9cy7foa0s37oyge 
        foreign key (expressions_chain) 
        references expressions_chain (id);

    alter table global_variables_variable_data 
        add constraint FK_3jqe1nvcx8elyt4g5unqca2bk 
        foreign key (global_variable) 
        references global_variables (id);

    alter table rule 
        add constraint FK_4drb97blr8qkukiypobjl5kxt 
        foreign key (actions) 
        references expressions_chain (id);

    alter table rule 
        add constraint FK_oy2233as9agrnkpf9eken12xd 
        foreign key (conditions) 
        references expressions_chain (id);

    alter table rule_decision_table_row 
        add constraint FK_ai05fqtcj4rykt9erhpx6ka8x 
        foreign key (actions) 
        references expressions_chain (id);

    alter table rule_decision_table_row 
        add constraint FK_2fbqms309rxw6y3qnq570dybm 
        foreign key (conditions) 
        references expressions_chain (id);

    alter table rule_decision_table_row_rules 
        add constraint FK_a74s342wqaat8f3d02tcs5fbo 
        foreign key (rule) 
        references rule_decision_table_row (id);

    alter table rule_decision_table_row_rules 
        add constraint FK_lq0unaa7qahk87qi3mtb6dlo8 
        foreign key (rule_decision_table) 
        references rule_decision_table (id);

    alter table test_answer_multi_checkbox_values 
        add constraint FK_t537k9ec1xkdxxscrte6kdcpv 
        foreign key (test_answer_multi_checkbox) 
        references test_answer_multi_checkbox (id);

    alter table test_scenario 
        add constraint FK_j6113fnb4bvtl953ym924i0w4 
        foreign key (test_scenario_form) 
        references test_scenario_form (id);

    alter table tree_forms_diagram 
        add constraint FK_goqe055h5r63fj0txmtyqlww7 
        foreign key (diagram) 
        references diagram (id);

    alter table tree_forms_diagram 
        add constraint FK_evycbl1p33a1cm7et4hkb83q6 
        foreign key (form) 
        references tree_forms (id);

    alter table tree_forms_expressions_chain 
        add constraint FK_9ci7n44y8tluqrc4pikjcitr0 
        foreign key (expression_chain) 
        references expressions_chain (id);

    alter table tree_forms_expressions_chain 
        add constraint FK_grick71aaipvnvsyup1xxl7ud 
        foreign key (form) 
        references tree_forms (id);

    alter table tree_forms_rule 
        add constraint FK_gyqbf18go6la4iwmtq5leojl9 
        foreign key (rule) 
        references rule (id);

    alter table tree_forms_rule 
        add constraint FK_9rs2f818ipriw69olvgv1rp5j 
        foreign key (form) 
        references tree_forms (id);

    alter table tree_forms_rule_decision_table 
        add constraint FK_saklv952rv0ykbxy559pmv874 
        foreign key (table_rule) 
        references rule_decision_table (id);

    alter table tree_forms_rule_decision_table 
        add constraint FK_dr80lmno4djoqidma1h9khh1h 
        foreign key (form) 
        references tree_forms (id);

	CREATE TABLE `hibernate_sequence` (
		`next_val` bigint(20) DEFAULT NULL
	);

	LOCK TABLES `hibernate_sequence` WRITE;
	INSERT INTO `hibernate_sequence` VALUES (1);
	UNLOCK TABLES;


