package com.feng.baseframework.constant;

import org.apache.commons.lang.StringUtils;

/**
 * baseframework
 * 2020/8/4 16:04
 * 文件类型枚举
 *
 * @author lanhaifeng
 * @since
 **/
public enum FileTypeEnum {
	CLASS(".class"), JAR(".jar"), PROPERTY(".properties");

	private String fileSuffix;

	public String getFileSuffix() {
		return fileSuffix;
	}

	FileTypeEnum(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public static FileTypeEnum getFileEnum(String fileName){
		if(StringUtils.isBlank(fileName)) return null;
		for (FileTypeEnum fileEnum : values()) {
			if(fileName.toLowerCase().endsWith(fileEnum.fileSuffix.toLowerCase())){
				return fileEnum;
			}
		}

		return null;
	}
}
