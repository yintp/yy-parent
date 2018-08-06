package com.andy.yy.user.enums;

/**
 * @author richard
 * @since 2018/2/8 16:43
 */
public enum MessageStatusEnum {
	DO("已处理", "DO"),
	NOT("未处理", "NOT");

	private String desc;
	private String value;

	MessageStatusEnum(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}
	public String getValue() {
		return value;
	}

	public static MessageStatusEnum getEnum(String value) {
		for (MessageStatusEnum obj : MessageStatusEnum.values()) {
			if (obj.getValue().equals(value)) {
				return obj;
			}
		}
		return null;
	}

	public static String getEnumDesc(String value) {
		for (MessageStatusEnum obj : MessageStatusEnum.values()) {
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
