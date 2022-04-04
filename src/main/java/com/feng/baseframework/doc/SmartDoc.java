package com.feng.baseframework.doc;

import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.HtmlApiDocBuilder;
import com.power.doc.constants.DocGlobalConstants;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.SourceCodePath;

/**
 * 类名:SmartDoc <br/>
 * 描述:smart-doc生成api文档 <br/>
 * 时间:2022/4/3 21:20 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class SmartDoc {

    public static void main(String[] args) {
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
