package com.feng.baseframework.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * baseframework
 * 2019/12/4 9:54
 * 秘钥参数基类
 *
 * @author lanhaifeng
 * @since
 **/
@Setter
@Getter
public class BaseSecretKeyParam implements Serializable{

	//密钥长度,证书大小
	private int keySize;
	//加密算法
	private String algorithm;
}
