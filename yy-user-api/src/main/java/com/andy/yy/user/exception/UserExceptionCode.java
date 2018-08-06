package com.andy.yy.user.exception;

import com.andy.yy.base.core.ExceptionCode;

/**
 * @author richard
 * @since 2018/1/30 17:15
 */
public class UserExceptionCode extends ExceptionCode {

	private static final long serialVersionUID = 1L;

	public static ExceptionCode PARAMS_NOT_NULL = getCode(20001, "参数不能为空");
	public static ExceptionCode ACCOUNT_NOT_NULL = getCode(20002, "请输入账号");
	public static ExceptionCode PWD_NOT_NULL = getCode(20003, "请输入密码");
	public static ExceptionCode ACCOUNT_NOT_MATCH = getCode(20004, "账号请输入最多16位数字、英文");
	public static ExceptionCode REGISTER_FAIL = getCode(20005, "注册失败");
	public static ExceptionCode USER_NOT_EXSIT = getCode(20006, "该账户不存在");
	public static ExceptionCode PWD_ERROR = getCode(20007, "密码错误");
	public static ExceptionCode ACCOUNT_EXIST = getCode(20008, "该账户已存在");
	public static ExceptionCode ACCOUNT_DISABLED = getCode(20009, "该账户已禁用");
	public static ExceptionCode APPLY_ADD_FRIEND_FAIL = getCode(20010, "申请添加好友失败");
	public static ExceptionCode ALREADY_FRIEND = getCode(20011, "该用户已经是你的好友");
	public static ExceptionCode YOUSELF = getCode(20012, "不能添加自己");
	public static ExceptionCode NOT_ADD_FRIEND = getCode(20013, "请先添加好友");
	public static ExceptionCode PARAMS_ERROR = getCode(20014, "参数异常");
	public static ExceptionCode USER_NOT_AUDIT = getCode(20015, "未申请添加好友");
	public static ExceptionCode HEAD_IMAGE_URL_NULL = getCode(20016, "请上传头像");
	public static ExceptionCode HEAD_IMAGE_URL_TOO_LANG = getCode(20017, "上传头像链接太长");
	public static ExceptionCode NICK_NAME_NULL = getCode(20018, "请填写昵称");
	public static ExceptionCode NICK_NAME_TOO_LANG = getCode(20019, "昵称不能超过24个字符");
	public static ExceptionCode REMARK_TOO_LANG = getCode(20020, "个性签名不能超过255个字符");

	public UserExceptionCode(int code, String message) {
		super(code, message);
	}

	public static ExceptionCode getCode(int index, String message) {
		return ExceptionCode.getCode(index, message);
	}
}
