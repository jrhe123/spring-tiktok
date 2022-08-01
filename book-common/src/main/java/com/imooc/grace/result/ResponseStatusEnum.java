package com.imooc.grace.result;

public enum ResponseStatusEnum {

    SUCCESS(200, true, "success"),
    FAILED(500, false, "fail"),

    // 50x
    UN_LOGIN(501,false,"un login error"),
    TICKET_INVALID(502,false,"ticket invalid"),
    NO_AUTH(503,false,"no auth"),
    MOBILE_ERROR(504,false,"mobile error"),
    SMS_NEED_WAIT_ERROR(505,false,"sms need wait error"),
    SMS_CODE_ERROR(506,false,"sms code error"),
    USER_FROZEN(507,false,"account invalid"),
    USER_UPDATE_ERROR(508,false,"user update error"),
    USER_INACTIVE_ERROR(509,false,"user inactive error"),
    USER_INFO_UPDATED_ERROR(5091,false,"user info update error"),
    USER_INFO_UPDATED_NICKNAME_EXIST_ERROR(5092,false,"nickname already exists"),
    USER_INFO_UPDATED_IMOOCNUM_EXIST_ERROR(5092,false,"imooc number already exists"),
    USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR(5092,false,"imooc number cannot update error"),
    FILE_UPLOAD_NULL_ERROR(510,false,"upload file is required"),
    FILE_UPLOAD_FAILD(511,false,"upload file fail"),
    FILE_FORMATTER_FAILD(512,false,"file format invalid"),
    FILE_MAX_SIZE_500KB_ERROR(5131,false,"max file size is 500KB"),
    FILE_MAX_SIZE_2MB_ERROR(5132,false,"max file size is 2MB"),
    FILE_NOT_EXIST_ERROR(514,false,"file not exists"),
    USER_STATUS_ERROR(515,false,"user status error"),
    USER_NOT_EXIST_ERROR(516,false,"user not exist error"),

    // 自定义系统级别异常 54x
    SYSTEM_INDEX_OUT_OF_BOUNDS(541, false, "system index of bounds"),
    SYSTEM_ARITHMETIC_BY_ZERO(542, false, "system error arithmetic by zero"),
    SYSTEM_NULL_POINTER(543, false, "system null pointer"),
    SYSTEM_NUMBER_FORMAT(544, false, "sytem number format"),
    SYSTEM_PARSE(545, false, "system parse error"),
    SYSTEM_IO(546, false, "system io erroor"),
    SYSTEM_FILE_NOT_FOUND(547, false, "system file not found"),
    SYSTEM_CLASS_CAST(548, false, "system cast error"),
    SYSTEM_PARSER_ERROR(549, false, "system parse error"),
    SYSTEM_DATE_PARSER_ERROR(550, false, "system date parse error"),

    // admin 管理系统 56x
    ADMIN_USERNAME_NULL_ERROR(561, false, "admin username null error"),
    ADMIN_USERNAME_EXIST_ERROR(562, false, "admin username exists error"),
    ADMIN_NAME_NULL_ERROR(563, false, "admin name null error"),
    ADMIN_PASSWORD_ERROR(564, false, "admin password not match"),
    ADMIN_CREATE_ERROR(565, false, "admin create error"),
    ADMIN_PASSWORD_NULL_ERROR(566, false, "admiin password null error"),
    ADMIN_NOT_EXIT_ERROR(567, false, "admin not exist or password not match"),
    ADMIN_FACE_NULL_ERROR(568, false, "admin face null error"),
    ADMIN_FACE_LOGIN_ERROR(569, false, "admin face login error"),
    CATEGORY_EXIST_ERROR(570, false, "category already exists"),

    // 媒体中心 相关错误 58x
    ARTICLE_COVER_NOT_EXIST_ERROR(580, false, "article cover not exists"),
    ARTICLE_CATEGORY_NOT_EXIST_ERROR(581, false, "article category not exists"),
    ARTICLE_CREATE_ERROR(582, false, "article create error"),
    ARTICLE_QUERY_PARAMS_ERROR(583, false, "article query params error"),
    ARTICLE_DELETE_ERROR(584, false, "article delete error"),
    ARTICLE_WITHDRAW_ERROR(585, false, "article withdraw error"),
    ARTICLE_REVIEW_ERROR(585, false, "article review error"),
    ARTICLE_ALREADY_READ_ERROR(586, false, "article already read error"),

    // 人脸识别错误代码
    FACE_VERIFY_TYPE_ERROR(600, false, "face verify type error"),
    FACE_VERIFY_LOGIN_ERROR(601, false, "face verify login error"),

    // 系统错误，未预期的错误 555
    SYSTEM_ERROR(555, false, "system error"),
    SYSTEM_OPERATION_ERROR(556, false, "system operation error"),
    SYSTEM_RESPONSE_NO_INFO(557, false, ""),
    SYSTEM_ERROR_GLOBAL(558, false, "system degrade"),
    SYSTEM_ERROR_FEIGN(559, false, "system feign degrade"),
    SYSTEM_ERROR_ZUUL(560, false, "request busy");


    // 响应业务状态
    private Integer status;
    // 调用是否成功
    private Boolean success;
    // 响应消息，可以为成功或者失败的消息
    private String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }
    public Boolean success() {
        return success;
    }
    public String msg() {
        return msg;
    }
}
