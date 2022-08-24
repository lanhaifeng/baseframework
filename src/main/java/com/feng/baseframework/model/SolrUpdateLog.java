package com.feng.baseframework.model;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * baseframework
 * 2018/12/24 15:10
 * solr索引更新实体
 *
 * @author lanhaifeng
 * @since
 **/
public class SolrUpdateLog implements Serializable {

	private static final long serialVersionUID = -2802633470274766552L;
	private Integer id;
	private String auditId;
	private List<UpdateField> fields = new ArrayList<>();
	private String collection;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public List<UpdateField> getFields() {
		return fields;
	}

	public void setFields(List<UpdateField> fields) {
		if (fields != null) this.fields = fields;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public void addFiledAndValues(UpdateField field){
		if(field != null && StringUtils.isNotBlank(field.getField())){
			if(field.getFieldValue() == null){
				field.setFieldValue("");
			}
			fields.add(field);
		}
	}

public static class UpdateField {
		private String field;
		private String fieldValue;

		public UpdateField(String field, String fieldValue) {
			this.field = field;
			this.fieldValue = fieldValue;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}

		public Map getUpdateAtomicVal(){
			Map<String, Object> atomic = new HashMap<>(1);
			atomic.put("set", getFieldValue());
			return atomic;
		}
	}
}


