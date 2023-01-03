package com.feng.baseframework.constant;

/**
 * baseframework
 * 2019/8/8 17:00
 * 加密解密枚举类
 *
 * @author lanhaifeng
 * @since
 **/
public enum CipherType {
	ENCRYPT(1, "加密"),
	DECRYPT(2, "解密"),
	;
	private int value;
	private String label;

	private CipherType(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	public static CipherType get(Integer value){
		if(value == null){
			return null;
		}
		for (CipherType cipherType : values()) {
			if(cipherType.value == value){
				return cipherType;
			}
		}

		return null;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
}
