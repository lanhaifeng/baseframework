package com.feng.baseframework.event;

import java.io.Serializable;

/**
 * baseframework
 * 2020/9/27 18:02
 * 用于事件发布
 *
 * @author lanhaifeng
 * @since
 **/
public class CustomizeEvent implements Serializable {
	private static final long serialVersionUID = -7548192922950447775L;

	protected transient Object  source;

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public CustomizeEvent(Object source) {
		if (source == null)
			throw new IllegalArgumentException("null source");

		this.source = source;
	}

	private final long timestamp = System.currentTimeMillis();

	public final long getTimestamp() {
		return this.timestamp;
	}
}
