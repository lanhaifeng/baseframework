package com.feng.memory.domain;

import java.io.Serializable;

/**
 * baseframework
 * 2020/8/4 17:52
 * 内存实体类
 *
 * @author lanhaifeng
 * @since
 **/
public class MemoryInfo implements Serializable {

	private Integer id;
	private Long memory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMemory() {
		return memory;
	}

	public void setMemory(Long memory) {
		this.memory = memory;
	}
}
