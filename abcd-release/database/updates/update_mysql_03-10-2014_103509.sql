
    alter table diagram 
        drop constraint UK_cqcepkojqmp1r8a42yb1hh1c4;

    alter table diagram 
        add constraint UK_cqcepkojqmp1r8a42yb1hh1c4  unique (ID);

    alter table diagram 
        drop constraint UK_i991a76ub6cc04w67skutnlft;

    alter table diagram 
        add constraint UK_i991a76ub6cc04w67skutnlft  unique (comparationId);

    alter table diagram_biit_text 
        drop constraint UK_jcm4u3t3e37kkjb5mltbs5mha;

    alter table diagram_biit_text 
        add constraint UK_jcm4u3t3e37kkjb5mltbs5mha  unique (ID);

    alter table diagram_biit_text 
        drop constraint UK_26e5ksw8ie3l7k0gtpdghwxr8;

    alter table diagram_biit_text 
        add constraint UK_26e5ksw8ie3l7k0gtpdghwxr8  unique (comparationId);

    alter table diagram_calculation 
        drop constraint UK_telsgfj7x5yb9vr3tyes22dfo;

    alter table diagram_calculation 
        add constraint UK_telsgfj7x5yb9vr3tyes22dfo  unique (ID);

    alter table diagram_calculation 
        drop constraint UK_skg33psaqtjwx1oevy1wp7tfa;

    alter table diagram_calculation 
        add constraint UK_skg33psaqtjwx1oevy1wp7tfa  unique (comparationId);

    alter table diagram_child 
        drop constraint UK_g7xdiesweik5op9e9onvswkbc;

    alter table diagram_child 
        add constraint UK_g7xdiesweik5op9e9onvswkbc  unique (ID);

    alter table diagram_child 
        drop constraint UK_r7oc8jgmhkeagu3emxy90ttof;

    alter table diagram_child 
        add constraint UK_r7oc8jgmhkeagu3emxy90ttof  unique (comparationId);

    alter table diagram_fork 
        drop constraint UK_hsf0g6nuli7nv6ypx60d3n4dg;

    alter table diagram_fork 
        add constraint UK_hsf0g6nuli7nv6ypx60d3n4dg  unique (ID);

    alter table diagram_fork 
        drop constraint UK_efh14aqc52m2bt7suefmq12qn;

    alter table diagram_fork 
        add constraint UK_efh14aqc52m2bt7suefmq12qn  unique (comparationId);

    alter table diagram_links 
        drop constraint UK_h604hokhqdqbq8jnjxeupxokf;

    alter table diagram_links 
        add constraint UK_h604hokhqdqbq8jnjxeupxokf  unique (ID);

    alter table diagram_links 
        drop constraint UK_ph2m4o4wkkd1ls8xxffxf0q4;

    alter table diagram_links 
        add constraint UK_ph2m4o4wkkd1ls8xxffxf0q4  unique (comparationId);

    alter table diagram_nodes 
        drop constraint UK_8a8ejpu1o9334lnd8qaxmngtc;

    alter table diagram_nodes 
        add constraint UK_8a8ejpu1o9334lnd8qaxmngtc  unique (ID);

    alter table diagram_nodes 
        drop constraint UK_r73mc67afakc2crvohrek8qk4;

    alter table diagram_nodes 
        add constraint UK_r73mc67afakc2crvohrek8qk4  unique (comparationId);

    alter table diagram_points 
        drop constraint UK_hlburchqkc9q4ahhvbtm8r9p9;

    alter table diagram_points 
        add constraint UK_hlburchqkc9q4ahhvbtm8r9p9  unique (ID);

    alter table diagram_points 
        drop constraint UK_nmcm8lwfgn9lwd2kg6e77mlpx;

    alter table diagram_points 
        add constraint UK_nmcm8lwfgn9lwd2kg6e77mlpx  unique (comparationId);

    alter table diagram_repeat 
        drop constraint UK_cmfc1l3o05vfhv0pt6mgqm3nd;

    alter table diagram_repeat 
        add constraint UK_cmfc1l3o05vfhv0pt6mgqm3nd  unique (ID);

    alter table diagram_repeat 
        drop constraint UK_5jgfcctfdg2s0qbkuaekf6m0g;

    alter table diagram_repeat 
        add constraint UK_5jgfcctfdg2s0qbkuaekf6m0g  unique (comparationId);

    alter table diagram_rule 
        drop constraint UK_o4hrkbic99uuo11mqks328fv9;

    alter table diagram_rule 
        add constraint UK_o4hrkbic99uuo11mqks328fv9  unique (ID);

    alter table diagram_rule 
        drop constraint UK_l4rl73pva0mblx0df1ntrb3m7;

    alter table diagram_rule 
        add constraint UK_l4rl73pva0mblx0df1ntrb3m7  unique (comparationId);

    alter table diagram_sink 
        drop constraint UK_cjl3d4py1hd5d5tvo23yd780y;

    alter table diagram_sink 
        add constraint UK_cjl3d4py1hd5d5tvo23yd780y  unique (ID);

    alter table diagram_sink 
        drop constraint UK_bo43xa2d2veyl1w7o8xfjse1m;

    alter table diagram_sink 
        add constraint UK_bo43xa2d2veyl1w7o8xfjse1m  unique (comparationId);

    alter table diagram_sizes 
        drop constraint UK_ipg7ga5eq6253uw0hwppg86ub;

    alter table diagram_sizes 
        add constraint UK_ipg7ga5eq6253uw0hwppg86ub  unique (ID);

    alter table diagram_sizes 
        drop constraint UK_lpwfoljk42ognggvtk6m0w75j;

    alter table diagram_sizes 
        add constraint UK_lpwfoljk42ognggvtk6m0w75j  unique (comparationId);

    alter table diagram_source 
        drop constraint UK_ff46qqbboep27fpldjifb16ou;

    alter table diagram_source 
        add constraint UK_ff46qqbboep27fpldjifb16ou  unique (ID);

    alter table diagram_source 
        drop constraint UK_2skl3aj5ivhq9i7j7vun9eak3;

    alter table diagram_source 
        add constraint UK_2skl3aj5ivhq9i7j7vun9eak3  unique (comparationId);

    alter table diagram_table 
        drop constraint UK_rdrkegb0jykxqg5jxtb2fbhc5;

    alter table diagram_table 
        add constraint UK_rdrkegb0jykxqg5jxtb2fbhc5  unique (ID);

    alter table diagram_table 
        drop constraint UK_8f7iwv857341khhqhi12jharn;

    alter table diagram_table 
        add constraint UK_8f7iwv857341khhqhi12jharn  unique (comparationId);

    alter table expression_function 
        drop constraint UK_7hl0l4oq4hokig8i3g30hv3s4;

    alter table expression_function 
        add constraint UK_7hl0l4oq4hokig8i3g30hv3s4  unique (ID);

    alter table expression_function 
        drop constraint UK_lbyra8yeo77q9okjte7yh5mck;

    alter table expression_function 
        add constraint UK_lbyra8yeo77q9okjte7yh5mck  unique (comparationId);

    alter table expression_operator_logic 
        drop constraint UK_5nv4rm0dcwpx72pidk99uqdcv;

    alter table expression_operator_logic 
        add constraint UK_5nv4rm0dcwpx72pidk99uqdcv  unique (ID);

    alter table expression_operator_logic 
        drop constraint UK_joflju3ccpgq1r4y7ro8egx3g;

    alter table expression_operator_logic 
        add constraint UK_joflju3ccpgq1r4y7ro8egx3g  unique (comparationId);

    alter table expression_operator_math 
        drop constraint UK_blgg3hvajwkh0url70tu2l3nc;

    alter table expression_operator_math 
        add constraint UK_blgg3hvajwkh0url70tu2l3nc  unique (ID);

    alter table expression_operator_math 
        drop constraint UK_8thp00x8evlgl3w2guass30ql;

    alter table expression_operator_math 
        add constraint UK_8thp00x8evlgl3w2guass30ql  unique (comparationId);

    alter table expression_symbol 
        drop constraint UK_d5pgsndfldjl77mxwesg47oyy;

    alter table expression_symbol 
        add constraint UK_d5pgsndfldjl77mxwesg47oyy  unique (ID);

    alter table expression_symbol 
        drop constraint UK_sw4rg5w1mhkjtuhmshj7jeh5w;

    alter table expression_symbol 
        add constraint UK_sw4rg5w1mhkjtuhmshj7jeh5w  unique (comparationId);

    alter table expression_value_boolean 
        drop constraint UK_8ji8c5v2rtu64hu7fd6w16wds;

    alter table expression_value_boolean 
        add constraint UK_8ji8c5v2rtu64hu7fd6w16wds  unique (ID);

    alter table expression_value_boolean 
        drop constraint UK_9ji3f3ooll872c0bitchknl29;

    alter table expression_value_boolean 
        add constraint UK_9ji3f3ooll872c0bitchknl29  unique (comparationId);

    alter table expression_value_custom_variable 
        drop constraint UK_g4ftxu9duvw2yoonn0mjb5sku;

    alter table expression_value_custom_variable 
        add constraint UK_g4ftxu9duvw2yoonn0mjb5sku  unique (ID);

    alter table expression_value_custom_variable 
        drop constraint UK_6be9j8k8bp0uudvgtk61e4tu4;

    alter table expression_value_custom_variable 
        add constraint UK_6be9j8k8bp0uudvgtk61e4tu4  unique (comparationId);

    alter table expression_value_expression_reference 
        drop constraint UK_egetkbs1hrvgqajn7qplpf4yn;

    alter table expression_value_expression_reference 
        add constraint UK_egetkbs1hrvgqajn7qplpf4yn  unique (ID);

    alter table expression_value_expression_reference 
        drop constraint UK_irf75tqpf7ywr50qxdg3fqwge;

    alter table expression_value_expression_reference 
        add constraint UK_irf75tqpf7ywr50qxdg3fqwge  unique (comparationId);

    alter table expression_value_global_variable 
        drop constraint UK_gbcgljgnhew0ftkmyyjo9ef0k;

    alter table expression_value_global_variable 
        add constraint UK_gbcgljgnhew0ftkmyyjo9ef0k  unique (ID);

    alter table expression_value_global_variable 
        drop constraint UK_thug7lch6aqrx4iwq0dtwabrr;

    alter table expression_value_global_variable 
        add constraint UK_thug7lch6aqrx4iwq0dtwabrr  unique (comparationId);

    alter table expression_value_number 
        drop constraint UK_4iv29s41m049yhnm25ykovg2j;

    alter table expression_value_number 
        add constraint UK_4iv29s41m049yhnm25ykovg2j  unique (ID);

    alter table expression_value_number 
        drop constraint UK_5pr6kfc8ssvjg8bcoxs7iaxo6;

    alter table expression_value_number 
        add constraint UK_5pr6kfc8ssvjg8bcoxs7iaxo6  unique (comparationId);

    alter table expression_value_string 
        drop constraint UK_ssjam0lpu4ptut6q7uiyinb46;

    alter table expression_value_string 
        add constraint UK_ssjam0lpu4ptut6q7uiyinb46  unique (ID);

    alter table expression_value_string 
        drop constraint UK_imteh9r9im4ctld1b9b5kkacw;

    alter table expression_value_string 
        add constraint UK_imteh9r9im4ctld1b9b5kkacw  unique (comparationId);

    alter table expression_value_systemdate 
        drop constraint UK_mj1pnbvm8gv3gngq2yh15502f;

    alter table expression_value_systemdate 
        add constraint UK_mj1pnbvm8gv3gngq2yh15502f  unique (ID);

    alter table expression_value_systemdate 
        drop constraint UK_co43pbqqg7civhr3g8p752mt8;

    alter table expression_value_systemdate 
        add constraint UK_co43pbqqg7civhr3g8p752mt8  unique (comparationId);

    alter table expression_value_timestamp 
        drop constraint UK_kju72l70wu9w63min4j3y59jw;

    alter table expression_value_timestamp 
        add constraint UK_kju72l70wu9w63min4j3y59jw  unique (ID);

    alter table expression_value_timestamp 
        drop constraint UK_ser5ax80v1g2co9dbdqstv81l;

    alter table expression_value_timestamp 
        add constraint UK_ser5ax80v1g2co9dbdqstv81l  unique (comparationId);

    alter table expression_value_tree_object_reference 
        drop constraint UK_57lm65prgqk32q1w0rd3jptux;

    alter table expression_value_tree_object_reference 
        add constraint UK_57lm65prgqk32q1w0rd3jptux  unique (ID);

    alter table expression_value_tree_object_reference 
        drop constraint UK_an3mu4deq3muvltuwdawkfbhc;

    alter table expression_value_tree_object_reference 
        add constraint UK_an3mu4deq3muvltuwdawkfbhc  unique (comparationId);

    alter table expressions_chain 
        drop constraint UK_l2k0vcoohq76m4cl2k8g3y9hc;

    alter table expressions_chain 
        add constraint UK_l2k0vcoohq76m4cl2k8g3y9hc  unique (ID);

    alter table expressions_chain 
        drop constraint UK_qageeu0ehecelhxmn5qdv93am;

    alter table expressions_chain 
        add constraint UK_qageeu0ehecelhxmn5qdv93am  unique (comparationId);

    alter table form_custom_variables 
        drop constraint UK_891b13251e3pkyk1k5vccpmnh;

    alter table form_custom_variables 
        add constraint UK_891b13251e3pkyk1k5vccpmnh  unique (ID);

    alter table form_custom_variables 
        drop constraint UK_sq1dj9kjhkv951lr0o3pu42fc;

    alter table form_custom_variables 
        add constraint UK_sq1dj9kjhkv951lr0o3pu42fc  unique (comparationId);

    alter table global_variable_data_date 
        drop constraint UK_mylp5k1001col2n4rpdshrea7;

    alter table global_variable_data_date 
        add constraint UK_mylp5k1001col2n4rpdshrea7  unique (ID);

    alter table global_variable_data_date 
        drop constraint UK_sptr6kn7psib0u6dqt8r7stkq;

    alter table global_variable_data_date 
        add constraint UK_sptr6kn7psib0u6dqt8r7stkq  unique (comparationId);

    alter table global_variable_data_number 
        drop constraint UK_ksal0k0qkvv2v95wvysufcap3;

    alter table global_variable_data_number 
        add constraint UK_ksal0k0qkvv2v95wvysufcap3  unique (ID);

    alter table global_variable_data_number 
        drop constraint UK_jbkxahvrsxikil188orj2bion;

    alter table global_variable_data_number 
        add constraint UK_jbkxahvrsxikil188orj2bion  unique (comparationId);

    alter table global_variable_data_postalcode 
        drop constraint UK_o8e2he19y5wsa4bxopiih4ewp;

    alter table global_variable_data_postalcode 
        add constraint UK_o8e2he19y5wsa4bxopiih4ewp  unique (ID);

    alter table global_variable_data_postalcode 
        drop constraint UK_1g8ysiehft4rrd3aiheq1cktx;

    alter table global_variable_data_postalcode 
        add constraint UK_1g8ysiehft4rrd3aiheq1cktx  unique (comparationId);

    alter table global_variable_data_text 
        drop constraint UK_t91aqfdhy6wnrrg749k2een9u;

    alter table global_variable_data_text 
        add constraint UK_t91aqfdhy6wnrrg749k2een9u  unique (ID);

    alter table global_variable_data_text 
        drop constraint UK_mowohsw3lf7j5hh676yr34en8;

    alter table global_variable_data_text 
        add constraint UK_mowohsw3lf7j5hh676yr34en8  unique (comparationId);

    alter table global_variables 
        drop constraint UK_nggea8kl5cb894e5du3cwsrd0;

    alter table global_variables 
        add constraint UK_nggea8kl5cb894e5du3cwsrd0  unique (ID);

    alter table global_variables 
        drop constraint UK_kr7p6k3u1po5mbamq95rvh6gj;

    alter table global_variables 
        add constraint UK_kr7p6k3u1po5mbamq95rvh6gj  unique (comparationId);

    alter table global_variables 
        drop constraint UK_ba2w3ms6v9agn6ac5ois703u2;

    alter table global_variables 
        add constraint UK_ba2w3ms6v9agn6ac5ois703u2  unique (name);

    alter table rule 
        drop constraint UK_8rqluiaunf9galin639sd894c;

    alter table rule 
        add constraint UK_8rqluiaunf9galin639sd894c  unique (ID);

    alter table rule 
        drop constraint UK_bmfhvbjf4kaugtlg6wom9crg9;

    alter table rule 
        add constraint UK_bmfhvbjf4kaugtlg6wom9crg9  unique (comparationId);

    alter table rule_decision_table 
        drop constraint UK_bumgbl5omimxtvvhvthstr882;

    alter table rule_decision_table 
        add constraint UK_bumgbl5omimxtvvhvthstr882  unique (ID);

    alter table rule_decision_table 
        drop constraint UK_20los6ndm6d9errmf4erx9f12;

    alter table rule_decision_table 
        add constraint UK_20los6ndm6d9errmf4erx9f12  unique (comparationId);

    alter table rule_decision_table_row 
        drop constraint UK_ondmt5ex56yathpd70q11lwgg;

    alter table rule_decision_table_row 
        add constraint UK_ondmt5ex56yathpd70q11lwgg  unique (ID);

    alter table rule_decision_table_row 
        drop constraint UK_r7njhoe2s46aht07exgq5hp5f;

    alter table rule_decision_table_row 
        add constraint UK_r7njhoe2s46aht07exgq5hp5f  unique (comparationId);
