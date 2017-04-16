package tcg_auto.lang;

public abstract class Lang {
	
	// APPLICATION
	public static final String APPLICATION_NAME = "application.name";
	public static final String APPLICATION_TITLE = "application.title";
	
	// MENUS
	public static final String MENU_FILE = "menus.file";
	public static final String MENU_ACTIONS = "menus.actions";
	public static final String MENU_HELP = "menus.help";
	public static final String MENU_OPEN_APPLICATION = "menus.open_application";
	
	// MENU ITEMS
	public static final String MENU_ITEM_FILE_EXIT = "menuitems.menu_file_exit";
	public static final String MENU_ITEM_ACTIONS_SET_LOGIN_PASSWORD = "menuitems.menu_actions_set_login_password";
	public static final String MENU_ITEM_ACTIONS_UPDATE_COURSES = "menuitems.menu_actions_update_courses";
	public static final String MENU_ITEM_ACTIONS_ADD_SUBSCRIPTION = "menuitems.menu_actions_add_subscription";
	public static final String MENU_ITEM_ACTIONS_BOOKING = "menuitems.menu_actions_booking";
	public static final String MENU_ITEM_HELP_SEE_LOG = "menuitems.menu_help_see_logs";
		
	// CLICKABLES
	public static final String CLICKABLES_SUBSCRIPTION_BUTTON_VALIDATION_SUBSCIPTION = "clickables.subscription_button_validate_subscription";

	// LABELS
	public static final String LABELS_SUBSCRIPTION_LABEL_COURSE = "labels.subscription_label_course";
	public static final String LABELS_SUBSCRIPTION_LABEL_DAY = "labels.subscription_label_day";
	public static final String LABELS_SUBSCRIPTION_LABEL_HOUR = "labels.subscription_label_hour";
	public static final String LABELS_SUBSCRIPTION_LABEL_MINUTE = "labels.subscription_label_minute";
	public static final String LABELS_SET_LOGIN_PASSWORD_LABEL_LOGIN = "labels.set_login_password_label_login";
	public static final String LABELS_SET_LOGIN_PASSWORD_LABEL_PASSWORD = "labels.set_login_password_label_password";
	public static final String LABELS_LABEL_ROOM_1 = "labels.label_room_1";
	public static final String LABELS_LABEL_ROOM_2 = "labels.label_room_2";
	public static final String LABELS_BOOKED_COURSE_LIST = "labels.label_booked_course_list";
	public static final String LABELS_SUBSCRIPTION_LIST = "labels.label_subscription_list";
	public static final String LABELS_COURSE_LIST = "labels.label_course_list";
	public static final String LABEL_PREPARING = "labels.preparing";
	
	// ENUMS
	public static final String ENUMS_ENUM_DAY_MONDAY = "enums.enum_day_monday";
	public static final String ENUMS_ENUM_DAY_TUESDAY = "enums.enum_day_tuesday";
	public static final String ENUMS_ENUM_DAY_WEDNESDAY = "enums.enum_day_wednesday";
	public static final String ENUMS_ENUM_DAY_THURSDAY = "enums.enum_day_thursday";
	public static final String ENUMS_ENUM_DAY_FRIDAY = "enums.enum_day_friday";
	
	// TITLES
	public static final String TITLE_TITLE_PANEL_APPLICATION_SESSION_LOGS = "titles.log_panel_application_session_logs";
	public static final String TITLE_TITLE_PANEL_APPLICATION_LOGS = "titles.log_panel_application_logs";
	public static final String TITLE_TITLE_PANEL_SELENIUM_LOGS = "titles.log_panel_selenium_logs";
	public static final String TITLE_SUBSCRIPTION_ADD_SUBSCRIPTION = "titles.subscription_add_subscription";
	public static final String TITLE_COURSE_BOOKING = "titles.course_booking";
	public static final String TITLE_SET_LOGIN = "titles.login_password_set_login";
	public static final String TITLE_SET_PASSWORD = "titles.login_password_set_password";
	public static final String TITLE_EXCEPTION_OCCURRED = "titles.error_occurred";
	
	// MESSAGES
	public static final String MESSAGE_SUBSCRIPTION_ERROR_VALIDATION_HOUR = "messages.subscription_error_validation_hour";
	public static final String MESSAGE_SUBSCRIPTION_ERROR_VALIDATION_MINUTE = "messages.subscription_error_validation_minute";
	public static final String MESSAGE_SUBSCRIPTION_ERROR_NO_COURSE_FOUND = "messages.subscription_error_no_course_found";
	public static final String MESSAGE_EXCEPTION_WITHOUT_CLOSE = "messages.exception_without_close";
	public static final String MESSAGE_EXCEPTION_WITH_CLOSE = "messages.exception_with_close";
	public static final String MESSAGE_TRAY_APP_STARTED = "messages.tray_application_started";
	public static final String MESSAGE_TRAY_INFO_TRY_BOOKING = "messages.tray_info_try_booking";
	public static final String MESSAGE_TRAY_INFO_BOOKING_SUCCESS = "messages.tray_info_booking_success";
	public static final String MESSAGE_TRAY_ERROR_BOOKING = "messages.tray_error_booking";
	public static final String MESSAGE_BOOKING_INFO_BOOKING_SUCCESS = "messages.booking_info_booking_success";
	public static final String MESSAGE_BOOKING_ERROR_BOOKING = "messages.booking_error_booking";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CONNECT = "messages.waiting_dialog_webaction_connect";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_SIGN_IN_LOGIN_PASSWORD = "messages.waiting_dialog_webaction_sign_in_login_password";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLOSE = "messages.waiting_dialog_webaction_close";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_BOOKING = "messages.waiting_dialog_webaction_click_booking";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_ROOM_1 = "messages.waiting_dialog_webaction_click_room_1";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_ROOM_2 = "messages.waiting_dialog_webaction_click_room_2";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_GET_COURSES = "messages.waiting_dialog_webaction_get_courses";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_SELECT_COURSE = "messages.waiting_dialog_webaction_select_course";
	public static final String MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CONFIRM_BOOKING = "messages.waiting_dialog_webaction_confirm_booking";
	
	// LOG CONTENTS
	public static final String LOG_MESSAGE_ERROR_CIPHER_INITIALIZATION_ENCRYPTER = "logs.error_cipher_initialization_encrypter";
	public static final String LOG_MESSAGE_ERROR_CIPHER_INITIALIZATION_DECRYPTER = "logs.error_cipher_initialization_decrypter";
	public static final String LOG_MESSAGE_ERROR_CIPHER_ENCRYPTION = "logs.error_cipher_encryption";
	public static final String LOG_MESSAGE_ERROR_CIPHER_DECRYPTING = "logs.error_cipher_decrypting";
	public static final String LOG_MESSAGE_ERROR_HCI_INITIALIZATION_TRAYICON = "logs.error_hci_initialization_tray";
	public static final String LOG_MESSAGE_ERROR_FILE_WRITE_LOGIN_PASSWORD = "logs.error_file_writing_login_password";
	public static final String LOG_MESSAGE_ERROR_EXIT_APP = "logs.error_exit_application";
	public static final String LOG_MESSAGE_ERROR_EXECUTING_ACTION = "logs.error_executing_action";
	public static final String LOG_MESSAGE_ERROR_EXECUTING_WEB_ACTION = "logs.error_executing_web_action";
	public static final String LOG_MESSAGE_WARN_INITIALIZATION_NO_LOGIN_PASSWORD_FOUND = "logs.warn_initialization_no_login_password_found";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_STARTING_APP = "logs.info_initialization_starting_application";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_APP_STARTED = "logs.info_initialization_application_started";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_LOGIN_PASSWORD = "logs.info_initialization_looking_for_login_password";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_LOGIN_PASSWORD_SUCCESS = "logs.info_initialization_login_password_success";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_CONFIG = "logs.info_initialization_looking_for_config";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_CONFIG_SUCCESS = "logs.info_initialization_config_success";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_WEB_DRIVER_PATH = "logs.info_initialization_looking_for_web_driver_path";
	public static final String LOG_MESSAGE_INFO_INITIALIZATION_WEB_DRIVER_PATH_SUCCESS = "logs.info_initialization_web_driver_path_success";
	public static final String LOG_MESSAGE_INFO_EXECUTING_WEB_ACTION = "logs.info_executin_web_action";
	public static final String LOG_MESSAGE_INFO_WEB_ACTION_EXECUTED_WITH_SUCCESS = "logs.info_web_action_executed_with_success";
	public static final String LOG_MESSAGE_INFO_ACTION_UPDATING_COURSE_LIST = "logs.info_updating_course_list";
	public static final String LOG_MESSAGE_INFO_ACTION_UPDATE_COURSE_LIST_SUCCESS = "logs.info_update_course_list_success";
	public static final String LOG_MESSAGE_INFO_EXIT_APP = "logs.info_exit_application";
	public static final String LOG_MESSAGE_INFO_ACTION_GET_SAVE_LOGIN_PASSWORD = "logs.info_action_get_save_login_password";
	public static final String LOG_MESSAGE_INFO_ACTION_GET_SAVE_WEB_DRIVER_PATH = "logs.info_action_get_save_web_driver_path";
	public static final String LOG_MESSAGE_INFO_CREATE_TASK_SUCCESS = "logs.info_create_task_success";
	
	// TIPS
	public static final String TIPS_TITLE_PANEL_APPLICATION_SESSION_LOGS = "tips.log_panel_application_session_logs";
	public static final String TIPS_TITLE_PANEL_APPLICATION_LOGS = "tips.log_panel_application_logs";
	public static final String TIPS_TITLE_PANEL_SELENIUM_LOGS = "tips.log_panel_selenium_logs";
	
}
