package com.feng.baseframework.model;


import java.io.Serializable;

/**
 * baseframework
 * 2018/9/30 16:34
 * 通用drools规则模块实体
 *
 * @author lanhaifeng
 * @since
 **/
public class RuleTp implements Serializable {


	private static final long	serialVersionUID	= -6714320945610590040L;
	private String				condition;
	private String				content;
	private Integer				seq;

	

	/**
	 * 构造
	 * 
	 * @param condition 
	 * @param content
	 * @param seq 
	 */
	public RuleTp(String condition, String content, Integer seq) {
		this.condition = condition;
		this.content = content;
		this.seq = seq;
	}

	/**
	 * 获取seq
	 * 
	 * @return seq
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 设置seq
	 * 
	 * @param seq
	 *
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 获取condition
	 * 
	 * @return condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * 设置condition
	 * 
	 * @param condition
	 *
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * 获取content
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置content
	 * 
	 * @param content
	 *
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
