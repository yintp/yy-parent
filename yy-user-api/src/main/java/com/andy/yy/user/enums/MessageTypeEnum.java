package com.andy.yy.user.enums;

/**
 * 消息类型
 * @author richard
 * @since 2018/2/28 18:43
 */
public enum MessageTypeEnum {
	EXCEPTION("异常", "EXCEPTION"),
	MSG("消息", "MSG"),
	ADD_FRIEND("添加好友", "ADD_FRIEND"),
	PASS_FRIEND("添加好友通过", "PASS_FRIEND"),
	REFUSE_FRIEND("添加好友拒绝", "REFUSE_FRIEND");

	private String desc;
	private String value;

	MessageTypeEnum(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}
	public String getValue() {
		return value;
	}

	public static MessageTypeEnum getEnum(String value) {
		for (MessageTypeEnum obj : MessageTypeEnum.values()) {
			if (obj.getValue().equals(value)) {
				return obj;
			}
		}
		return null;
	}

	public static String getEnumDesc(String value) {
		for (MessageTypeEnum obj : MessageTypeEnum.values()) {
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
