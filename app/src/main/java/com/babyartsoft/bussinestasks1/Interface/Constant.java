package com.babyartsoft.bussinestasks1.Interface;

public interface Constant {

    int ZERO = 0;
    long ZERO_L = 0L;
    String ZERO_S = "";
    String TASKS = "Tasks";
    String SLAVES = "Slaves";
    String USERS = "Users";
    String DEVICES = "Devices";
    String BOSS = "Boss";
    int ERROR = -1;

    int MINUS_1 = -1;
    int PLUS_1 = 1;

    int RC_SIGN_IN  = 9000;
//    int RESULT_ON_NEW = 9001;
    int RESULT_ON_EDIT = 9002;
//    int RESULT_ON_REG = 9003;
    int RESULT_ON_ZVON = 9004;
    int RESULT_ON_ADD_SLAVE = 9005;

    String INFO_DATA_INTO = "info_date_into";
    String IFNO_DATA_LAST_EDIT = "info_data_last";
    String INFO_DATE_DELIVERY = "info_date_delivery";
    String INFO_DATE_READ = "info+date_read";
    String INFO_DATE_EDIT_SATUS = "info_date_edit_status";
    String INFO_USER_EDIT_SATATUS = "info_user_edit_status";
    String INFO_USER_ID = "info_user_id";
    String INFO_USER_NAME = "info_user_name";


    String ADAPTER_POSITION = "ADAPTER_POSITION";
    String FROM_NOTIFY = "from_notify";
    String FROM_NOTIFY_ID_TASK = "from_notify_id_task";
    boolean FROM_NOTIFY_YES = true;

    String FILE_USER_SELF = "file_user_self";
    String ID_TOKEN_THIS_DEVICE = "id_token_this_device";
    String IT_IS_NEW_TOKEN = "it_is_new_token";
    String FIRST_RUN = "first_run";
    String KEY_DEVICE = "key_device";
    String NAME_USER_SELF = "name_user_self";
//    String PHONE_USER_SELF = "phone_user_self";
    String KEY_USER_SELF = "key_user_self";
    String USER_SELF_DATE_BOSS = "user_self_date_boss";
    String USER_SELF_STAT_BOSS = "user_self_stat_boss";
    String USER_SELF_PurchaseToken = "user_self_PurchaseToken";
    String POINT_STRING = ".";
    String GMAIL = "gmail";

    String SLAVE_DISPLAY_NAME = "slave_displaq_name";
    String SLAVE_DISPLAY_NAME_EDIT = "slave_displaq_name_edit";
    String SLAVE_PROF = "slave_prof";
    String SLAVE_PHONE = "slave_phone";
    String SLAVE_KEY = "slave_key";
    String NOT_SLAVE_KEY = "_not.slave.key";    // _ в начале нужен для сортировки, что бы быть сверху
    String FOR_MY_KEY = "__for.my.key";          // __ в начале нужен для сортировки, что бы быть сверху

    String FILE_SETTING = "file_setting";
    String POLE_SORT = "pole_sort";
    String STRING_SORT_NAME = "string_sort_name";
    String ARROW_SORT = "arrow";

    String FILTER_SROK_BEGIN = "filter_srok_begin";
    String FILTER_SROK_END = "filter_srok_end";
    String FILTER_SROK_STRING = "filter_srok_string";

    String ID_NOTIFY = "id_notify";

    int SORT_TASK = 101;
    int SORT_INTO = 102;
    int SORT_SROK = 103;
    int SORT_SLAVE = 104;
    int SORT_STATUS = 105;
    int SORT_LAST_EDIT = 106;
    int SORT_DATE_STATUS = 107;

    String FILTER_STATUS = "filter_status";
    int FILTER_STATUS_OFF = 201;
    int FILTER_STATUS_UNLOCK = 202;
    int FILTER_STATUS_LOCK = 203;
    int FILTER_STATUS_GO = 204;
    int FILTER_STATUS_GALKA = 205;
    int FILTER_STATUS_PUSTO = 206;
    int FILTER_STATUS_PAUSE = 207;
    int FILTER_STATUS_CANCEL = 208;

    int FILTER_DATE_NOT = 300;
    int FILTER_DATE_YESTERDAY = 301;
    int FILTER_DATE_TODAY = 302;
    int FILTER_DATE_TOMORROW = 303;
    int FILTER_DATE_WEEK = 304;
    int FILTER_DATE_MONTH = 305;
    int FILTER_DATE_YEAR = 306;
    int FILTER_DATE_YESTERDAY_MORE = 307;
    int FILTER_DATE_TODAY_MORE = 308;

    String FILTER_SLAVE = "filter_slave";
    String FILTER_SLAVE_STRING = "filter_slave_string";
    String FILTER_SLAVE_ALL = "filter_slave_all";
    String FILTER_DATE_PERIOD = "filter_date_period";

    long time30minut    = 1800000L;
    long time60minut    = 3600000L;
    long time2hour      = 7200000L;
    long time3hour      = 10800000L;
    long time4hour      = 14400000L;

    String NOTIFY_FOR_MY = "for_my";
    String NOTIFY_ORDER = "order";

    String IS_DEL = "_.";

    String HELP_VIEW_PAGE_ADAPTER = "help_view_page_adapter";
    int view_about = 1;
    int view_add_slave_url = 2;
    int view_help_new_task = 3;

    String HELP_RUN_ON_START = "help_run_on_start";

}
