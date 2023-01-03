package com.feng.baseframework.jackson;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * soc
 * 2021/3/29 13:59
 * jackjson 序列化处理
 * 将空字符替换成null
 * 使用：@JsonSerialize(using = CustomStringSerializer.class)
 *
 * @author lanhaifeng
 * @since
 **/
public class CustomStringSerializer extends JsonSerializer<Object> {

	private static Logger logger = LoggerFactory.getLogger(CustomStringSerializer.class);

	@Override
	public void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		writeJdkReflect(obj, jsonGenerator);
//		writeAsmReflect(obj, jsonGenerator);
	}

	private void writeJdkReflect(Object obj, JsonGenerator jsonGenerator) throws IOException {
		if(Objects.isNull(obj)){
			jsonGenerator.writeNull();
		}
		try{
			Object val;
			String property;
			String methodName;
			Method[] getMethods = obj.getClass().getMethods();
			jsonGenerator.writeStartObject();
			for (Method getMethod : getMethods) {
				getMethod.setAccessible(true);
				methodName = getMethod.getName();
				if(methodName.startsWith("get") && getMethod.getParameterCount() == 0 && !methodName.equals("getClass")){
					property = (new StringBuilder()).append(methodName.substring(3, 4).toLowerCase()).append(methodName.substring(4)).toString();
					val = getMethod.invoke(obj);
					jsonGenerator.writeFieldName(property);
					if(getMethod.getGenericReturnType().equals(String.class)){
						if(Objects.nonNull(val) && "".equals(val)){
							jsonGenerator.writeNull();
						}else {
							jsonGenerator.writeString((String)val);
						}
					}else {
						jsonGenerator.writeObject(val);
					}
				}
			}

			jsonGenerator.writeEndObject();
		}catch(Exception e){
			logger.error("[jackson]bean serialize fail, error {}", ExceptionUtils.getFullStackTrace(e));
		}
	}

	private void writeAsmReflect(Object obj, JsonGenerator jsonGenerator) throws IOException {
		if(Objects.isNull(obj)){
			jsonGenerator.writeNull();
			return;
		}
		try{
			MethodAccess access = MethodAccess.get(obj.getClass());
			Object val;
			String property;
			int methodIndex;
			jsonGenerator.writeStartObject();
			String[] methodNames = access.getMethodNames();
			StringBuilder builder = new StringBuilder();
			for (String methodName : methodNames) {
				if((methodIndex = searchGetMethodIndex(access, methodName)) > -1){
					property = builder.append(methodName.substring(3, 4).toLowerCase()).append(methodName.substring(4)).toString();
					val = access.invoke(obj, methodIndex);
					jsonGenerator.writeFieldName(property);
					if(Objects.isNull(val) || "".equals(val)){
						jsonGenerator.writeNull();
					}else {
						if(val instanceof String){
							jsonGenerator.writeString((String)val);
						}else {
							jsonGenerator.writeObject(val);
						}
					}
				}
				builder.setLength(0);
			}

			jsonGenerator.writeEndObject();
		}catch(Exception e){
			logger.error("[jackson]bean serialize fail, error {}", ExceptionUtils.getFullStackTrace(e));
		}
	}

	private int searchGetMethodIndex(MethodAccess access, String methodName){
		if(Objects.isNull(access) || StringUtils.isBlank(methodName)
				|| !methodName.startsWith("get") || methodName.equals("getClass")){
			return -1;
		}
		try{
			return access.getIndex(methodName, 0);
		}catch(Exception e){
			return -1;
		}
	}
}
