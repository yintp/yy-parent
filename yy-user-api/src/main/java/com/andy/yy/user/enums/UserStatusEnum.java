package com.andy.yy.user.enums;

/**
 * 用户状态
 * @author richard
 * @since 2018年1月30日 下午4:08:54
 */
public enum UserStatusEnum {
	NORMAL("正常", "NORMAL"),
	DISABLED("禁用", "DISABLED");

	private String desc;
	private String value;

	UserStatusEnum(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}
	public String getValue() {
		return value;
	}

	public static UserStatusEnum getEnum(String value) {
    	for (UserStatusEnum obj : UserStatusEnum.values()) {
    		if (obj.getValue().equals(value)) {
    			return obj;
    		}
    	}
        return null;
    }

	public static String getEnumDesc(String value) {
    	for (UserStatusEnum obj : UserStatusEnum.values()) {
    		if (obj.getValue().equals(value)) {
    			return obj.getDesc();
    		}
    	}
        return null;
    }

	@Override
	public String toString() {
		return value + "," + desc;
	}
}
