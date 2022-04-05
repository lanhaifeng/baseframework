package com.feng.baseframework.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.baseframework.util.FileUtils;
import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.HtmlApiDocBuilder;
import com.power.doc.model.*;
import com.power.doc.model.rpc.RpcApiDependency;

import java.io.IOException;

/**
 * 类名:SmartDoc <br/>
 * 描述:smart-doc生成api文档 <br/>
 * 时间:2022/4/3 21:20 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class SmartDoc {
    abstract class MixIn {
        @JsonIgnore
        abstract public void setSourceCodePaths(SourceCodePath... sourcePaths);
        @JsonIgnore
        abstract public void setRequestHeaders(ApiReqParam... requestHeaders);
        @JsonIgnore
        abstract public void setRequestParams(ApiReqParam... requestParams);
        @JsonIgnore
        abstract public void setCustomResponseFields(CustomField... customResponseFields);
        @JsonIgnore
        abstract public void setCustomRequestFields(CustomField... customRequestFields);
        @JsonIgnore
        abstract public void setRevisionLogs(RevisionLog... revisionLogs);
        @JsonIgnore
        abstract public void setDataDictionaries(ApiDataDictionary... dataDictConfigs);
        @JsonIgnore
        abstract public void setErrorCodeDictionaries(ApiErrorCodeDictionary... errorCodeDictConfigs);
        @JsonIgnore
        abstract public void setApiObjectReplacements(ApiObjectReplacement... apiObjectReplaces);
        @JsonIgnore
        abstract public void setRpcApiDependencies(RpcApiDependency... rpcApiDependencies);
        @JsonIgnore
        abstract public void setApiConstants(ApiConstant... apiConstants);
        @JsonIgnore
        abstract public ApiConfig setGroups(ApiGroup... groups);
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.addMixIn(ApiConfig.class, MixIn.class);

        StringBuffer jsonBuffer = new StringBuffer(1024);
        FileUtils.readToBuffer(jsonBuffer, Thread.currentThread().getContextClassLoader().getResourceAsStream("smart-doc.json"));
        ApiConfig config = objectMapper.readValue(jsonBuffer.toString(), ApiConfig.class);

        long start = System.currentTimeMillis();
        HtmlApiDocBuilder.buildApiDoc(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);


    }

    public static void allCodeConfig() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://localhost:8080");
        //设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
        config.setMd5EncryptedHtmlName(true);
        config.setStrict(false);//true会严格要求注释，推荐设置true
//        config.setOutPath(DocGlobalConstants.HTML_DOC_OUT_PATH);//输出到static/doc下
        config.setOutPath("D:\\data\\smart-api");
        //不指定SourcePaths默认加载代码为项目src/main/java下的,若是项目的某一些实体来自外部代码能够一块儿加载
        config.setSourceCodePaths(
                SourceCodePath.builder().setDesc("本项目代码").setPath("src/main/java")
        );
        config.setPackageFilters("com.feng.baseframework.controller");
        long start = System.currentTimeMillis();
        HtmlApiDocBuilder.buildApiDoc(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }
}
