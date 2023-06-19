package com.feng.baseframework.controller;

import com.feng.baseframework.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

/**
 * baseframework
 * 2023/3/19 20:38
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
@RestController
public class OfficeController {

    @Resource
    private FileService fileService;

    @GetMapping("/api/file/onlinePreview")
    public void onlinePreview(@RequestParam("url") String url, HttpServletResponse response) throws Exception {
        fileService.onlinePreview(url, response);
    }
}
