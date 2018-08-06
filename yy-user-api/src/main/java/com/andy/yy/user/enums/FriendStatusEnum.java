package com.andy.yy.user.enums;

/**
 * @author richard
 * @since 2018/2/8 16:43
 */
public enum FriendStatusEnum {
	PASS("通过", "PASS"),
	AUDIT("审核", "AUDIT"),
	REFUSE("拒绝", "REFUSE"),
	BLACK("拉黑", "BLACK");

	private String desc;
	private String value;

	FriendStatusEnum(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}
	public String getValue() {
		return value;
	}

	public static FriendStatusEnum getEnum(String value) {
		for (FriendStatusEnum obj : FriendStatusEnum.values()) {
			if (obj.getValue().equals(value)) {
				return obj;
			}
		}
		return null;
	}

	public static String getEnumDesc(String value) {
		for (FriendStatusEnum obj : FriendStatusEnum.values()) {
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
