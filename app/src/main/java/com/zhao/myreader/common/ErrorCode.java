package com.zhao.myreader.common;

/**
 * Created by zhao on 2016/10/27.
 */

public class ErrorCode {

    public static final int login_not_user = 1; // 用户不存在
    public static final int login_not_password = 2;// 密码错误
    public static final int internet_waring = 3;// 网络访问错误
    public static final int no_security = 4;// 没有权限(登陆过期)
    public static final int no_login_ = 5;// 没有登录
    public static final int meeting_user_no = 6;// 报名用户不存在
    public static final int old_password = 8;// 原密码错误
    public static final int execute_error = 7;//程序运行异常
    public static final int order_timeout = 9;// 订餐超时
    public static final int email_or_emailPwd_empty = 10;//邮箱或邮箱密码为空
    public static final int email_login_error = 11;//邮箱登陆失败
    public static final int email_params_error = 12;//邮箱接口参数不完整
    public static final int suggestion_params_error = 13;//反馈意见接口参数不完整
    public static final int email_addresses_error = 15;//邮箱地址无效
    public static final int uploadfile_extend_error = 16;//上传附件后缀不支持
    public static final int params_overlength = 17;//接口参数过长
    public static final int leave_error = 18;//请假审批人跟请假人不能相同
    public static final int login_simple_pwd = 19;//简单登录密码


    public static final int more_depart = 1011;//多部门用户
    public static final int Certificate_sys_no_user = 1012;//认证系统内无此用户
    public static final int Certificate_no_bind_user = 1013;//用户没绑定证书在系统内
    public static final int ukey_duplicate_user = 1014;//ukey序列号系统中不存在或存在多笔数据，请联系管理员



}
