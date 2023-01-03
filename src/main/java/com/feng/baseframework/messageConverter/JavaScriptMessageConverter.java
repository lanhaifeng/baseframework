package com.feng.baseframework.messageConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.baseframework.model.JsonpProxy;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;

/**
 * 类名:JavaScriptMessageConverter <br/>
 * 描述:处理jsonp，支持response-content为application/javascript <br/>
 * 时间:2022/1/11 17:12 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class JavaScriptMessageConverter extends AbstractJackson2HttpMessageConverter {
    private String jsonPrefix;

    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public JavaScriptMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().build(),new MediaType("application","javascript"));
    }

    protected JavaScriptMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, new MediaType("application","javascript"));
    }

    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }

        String jsonpFunction = object instanceof JsonpProxy  ? ((JsonpProxy)object).getJsonpFunction() : null;
        if(StringUtils.isBlank(jsonpFunction)){
            jsonpFunction = object instanceof MappingJacksonValue  ? ((MappingJacksonValue)object).getJsonpFunction() : null;
        }
        if (jsonpFunction != null) {
            generator.writeRaw("/**/");
            generator.writeRaw(jsonpFunction + "(");
        }

    }

    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction = object instanceof JsonpProxy ? ((JsonpProxy)object).getJsonpFunction() : null;
        if(StringUtils.isBlank(jsonpFunction)){
            jsonpFunction = object instanceof MappingJacksonValue  ? ((MappingJacksonValue)object).getJsonpFunction() : null;
        }
        if (jsonpFunction != null) {
            generator.writeRaw(");");
        }

    }
}
