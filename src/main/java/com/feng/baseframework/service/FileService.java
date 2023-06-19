package com.feng.baseframework.service;

import jakarta.servlet.http.HttpServletResponse;

public interface FileService {

    /**
     *
     * 2023/3/19 20:41
     * 系统文件在线预览接口
     *
     * @author lanhaifeng
     **/
    void onlinePreview(String url, HttpServletResponse response) throws Exception;
}
