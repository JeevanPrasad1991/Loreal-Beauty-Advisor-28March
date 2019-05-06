package ba.cpm.com.lorealba.constant;

import android.os.Environment;

/**
 * Created by jeevanp on 14-12-2017.
 */

public class CommonString {
    //preference
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_QUESTION_CD = "question_cd";
    public static final String KEY_MY_LIBRARY_URL = "http://lorealba.parinaam.in/knowledge/index.html";
    public static final String KEY_MY_KNOWLEDGE_URL = "http://lorealba.parinaam.in/knowledge/index.html";
    public static final String KEY_ANSWER_CD = "answer_cd";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_REPORT_TYPE = "REPORT_TYPE";
    public static final String KEY_STOCK_TYPE = "STOCK_TYPE";
    public static final String KEY_YYYYMMDD_DATE = "yyyymmddDate";
    public static final String KEY_STOREVISITED_STATUS = "STOREVISITED_STATUS";
    public static String URL = "http://lba.parinaam.in/webservice/Loginservice.svc/";
    public static String URLGORIMAG = "http://lba.parinaam.in/webservice/Imageupload.asmx/";
    public static final int CAPTURE_MEDIA = 131;
    public static final String KEY_PATH = "PATH";
    public static final String KEY_VERSION = "APP_VERSION";
    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_FAILURE = "Failure";
    public static final String MESSAGE_INTERNET_NOT_AVALABLE = "No Internet Connection.Please Check Your Network Connection";
    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Please Check Your Network Connection";
    public static final String MESSAGE_NO_RESPONSE_SERVER = "Server Not Responding.Please try again.";
    public static final String MESSAGE_INVALID_JSON = "Problem Occured while parsing Json : invalid json data";
    public static final String MESSAGE_NUMBER_FORMATE_EXEP = "Invailid Mid";

    public static final String KEY_P = "P";
    public static final String KEY_D = "D";
    public static final String KEY_U = "U";
    public static final String KEY_C = "C";
    public static final String KEY_Y = "Y";
    public static final String KEY_N = "N";
    public static final String STORE_STATUS_LEAVE = "L";
    public static final String KEY_CHECK_IN = "I";
    ///all service key

    public static final String KEY_LOGIN_DETAILS = "Login";
    public static final String KEY_DOWNLOAD_INDEX = "download_Index";
    public static final int TAG_FROM_CURRENT = 1;
    public static final int DOWNLOAD_ALL_SERVICE = 2;
    public static final int COVERAGE_DETAIL = 3;
    public static final int UPLOADJsonDetail = 5;
    //File Path
    public static final String BACKUP_FILE_PATH = Environment.getExternalStorageDirectory() + "/Lorealba_backup/";
    public static final String Promotion_ReF_File_Path = Environment.getExternalStorageDirectory() + "/LorealBaPromoImages/";
    ////for insert data key
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/.Lorealba_Images/";
    public static final String ONBACK_ALERT_MESSAGE = "Unsaved data will be lost - Do you want to continue?";
    public static final String KEY_USER_TYPE = "RIGHTNAME";
    public static final String KEY_IS_QUIZ_DONE = "is_quiz_done";

    //jeevan
    public static final String DATA_DELETE_ALERT_MESSAGE = "Saved data will be lost - Do you want to continue?";
    public static final String KEY_CHECKOUT_IMAGE = "CHECKOUT_IMAGE";
    public static final String KEY_STORE_NAME = "STORE_NAME";
    public static final String KEY_STORE_ADDRESS = "STORE_ADDEESS";
    public static final String KEY_STORE_ID = "STORE_ID";
    public static final String KEY_VISIT_DATE = "VISIT_DATE";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_REASON_ID = "REASON_ID";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_IMAGE = "STORE_IMAGE";
    public static final String KEY_COVERAGE_REMARK = "REMARK";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_ID = "ID";

    public static final String TABLE_COVERAGE_DATA = "COVERAGE_DATA";
    public static final String CREATE_TABLE_COVERAGE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_COVERAGE_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER,USER_ID VARCHAR, "
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_LATITUDE + " VARCHAR," + KEY_LONGITUDE + " VARCHAR,"
            + KEY_IMAGE + " VARCHAR,"
            + KEY_CHECKOUT_IMAGE + " VARCHAR,"
            + KEY_REASON_ID + " INTEGER," + KEY_COVERAGE_REMARK
            + " VARCHAR," + KEY_REASON + " VARCHAR)";


    public static final String TABLE_INSERT_AUDIT_OPENINGHEADER_DATA = "AUDIT_OPENINGHEADER_DATA";

    public static final String CREATE_TABLE_AUDIT_OPENINGHEADER_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_INSERT_AUDIT_OPENINGHEADER_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER, "
            + "QUESTION_CATEGORY_CD" + " INTEGER,"
            + " QUESTION_CATEGORY" + " VARCHAR)";


    public static final String TABLE_STORE_AUDIT_DATA = "STORE_AUDIT_DATA";

    public static final String CREATE_TABLE_STORE_AUDIT_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_STORE_AUDIT_DATA + " (" + "Common_Id"
            + " INTEGER  ," + KEY_STORE_ID
            + " INTEGER, "
            + "QUESTION_CATEGORY_CD" + " INTEGER, "

            + "QUESTION" + " VARCHAR, "
            + "QUESTION_CD" + " INTEGER, "
            + "CURRECT_ANSWER" + " VARCHAR, "
            + "ANSWER_CD" + " INTEGER, "
            + "IMAGE_ALLOW" + " VARCHAR, "
            + "IMAGEALLOW_ANS" + " VARCHAR, "
            + "AUDIT_IMG" + " VARCHAR, "
            + " QUESTION_CATEGORY" + " VARCHAR)";


    public static final String KEY_FOR_SPINNER_DROP_DOWN = "Please select dropDown answer";
    public static final String KEY_FOR_CAMERA_C = "Please click camera";

    public static final String TAG_FROM_NONWORKING = "from_NonWorking";
    public static final String TAG_OBJECT = "OBJECT";
    public static final String TAG_OTP = "OTP";
    public static final String Total_Amount = "Total_Amount";
    public static final String TABLE_STORE_GEOTAGGING = "STORE_GEOTAGGING";
    public static final String CREATE_TABLE_STORE_GEOTAGGING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STORE_GEOTAGGING
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "STORE_ID" + " INTEGER,"
            + "LATITUDE" + " VARCHAR,"
            + "LONGITUDE" + " VARCHAR,"
            + "GEO_TAG" + " VARCHAR,"
            + "STATUS" + " VARCHAR,"
            + "FRONT_IMAGE" + " VARCHAR)";

    public static final String MESSAGE_NO_JCP = "NO JCP FOR THIS DATE";

    public static final String KEY_ATTENDENCE_STATUS = "ATTENDENCE_STATUS";
    public static final String MESSAGE_CHANGED = "Invalid UserId Or Password / Password Has Been Changed.";
    public static final String MESSAGE_LOGIN_NO_DATA = "Data mapping error.";
    public static final String KEY_NOTICE_BOARD_LINK = "NOTICE_BOARD_LINK";

    public static final String Table_Sale_Tracking = "SALE_TRACKING_TABLE";


    public static final String Create_Table_Sale_Tracking = "CREATE TABLE  IF NOT EXISTS "
            + Table_Sale_Tracking + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER, "
            + KEY_VISIT_DATE + " VARCHAR, USER_ID VARCHAR, "
            + "STORE_NAME" + " VARCHAR, "
            + "STORE_ADDRESS" + " VARCHAR, "
            + "CONTACT_NUMBER" + " VARCHAR, "
            + "PRODUCT_ID" + " INTEGER, "
            + "PRODUCT" + " VARCHAR, "
            + "QUANTITY" + " INTEGER, "
            + "PRODUCT_RATE" + " INTEGER, "
            + "TOTAL_AMOUNT" + " INTEGER, "
            + "BUYER_NAME" + " VARCHAR, "
            + "POS_SALE_FLAG" + " VARCHAR, "
            + "GENDER" + " VARCHAR, "
            + "EAN_CODE" + " VARCHAR)";

    public static final String TABLE_ATTENDENCE_TABLE = "ATTENDENCE_TABLE";

    public static final String CREATE_TABLE_ATTENDENCE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ATTENDENCE_TABLE
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_USER_ID
            + " VARCHAR,"
            + KEY_VISIT_DATE
            + " VARCHAR,"
            + KEY_IMAGE
            + " VARCHAR,"
            + KEY_REASON + " VARCHAR)";


    public static final String Table_promotion = "PROMOTION_TABLE";

    public static final String Create_Table_promotion = "CREATE TABLE  IF NOT EXISTS "
            + Table_promotion + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_ID + " INTEGER, "
            + KEY_VISIT_DATE + " VARCHAR, "
            + "PROMOTION_NAME" + " VARCHAR, "
            + "PROMOTION_ID" + " INTEGER, "
            + "PROMOTION_TYPE" + " VARCHAR, "
            + "PROMOTION_TYPE_ID" + " INTEGER, "
            + "PRODUCT_CLUSTER" + " VARCHAR, "
            + "PRODUCT_CLUSTER_ID" + " INTEGER, "
            + "START_DATE" + " VARCHAR, "
            + "END_DATE" + " VARCHAR, "
            + "PROMOTION_EXIST" + " VARCHAR, "
            + "PROMO_IMG" + " VARCHAR, "
            + "REASON" + " VARCHAR, "
            + "REASON_ID" + " INTEGER)";
}
