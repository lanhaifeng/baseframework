package com.feng.baseframework.service.impl;

import com.feng.baseframework.service.FileService;
import com.feng.baseframework.util.FileConvertUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * baseframework
 * 2023/3/19 20:41
 * 文件预览
 *
 * @author lanhaifeng
 * @since 2.0.0
 **/
@Service
public class FileServiceImpl implements FileService {

    private static final List<String> SUPPORT_TYPES = Arrays.asList("txt", "doc", "docx",
            "xls", "xlsx", "ppt", "pptx");

    private static final List<String> REMOTE_TYPES = Arrays.asList("http://", "https://");

    @Override
    public void onlinePreview(String url, HttpServletResponse response) throws Exception {
        //获取文件类型
        String[] str = url.split("\\.");

        if (str.length == 0) {
            throw new Exception("文件格式不正确");
        }
        String suffix = str[str.length - 1];
        if (!SUPPORT_TYPES.contains(suffix)) {
            throw new Exception("文件格式不支持预览");
        }
        InputStream in;
        if (url.startsWith(REMOTE_TYPES.get(0)) || url.startsWith(REMOTE_TYPES.get(1))) {
            in = FileConvertUtil.convertNetFile(url, suffix);
        } else {
            in = FileConvertUtil.convertLocaleFile(url, suffix);
        }
        response.setContentType("application/pdf");
        OutputStream outputStream = response.getOutputStream();
        //创建存放文件内容的数组
        byte[] buff = new byte[1024];
        //所读取的内容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while ((n = in.read(buff)) != -1) {
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff, 0, n);
        }
        //强制将缓存区的数据进行输出
        outputStream.flush();
        //关流
        outputStream.close();
        in.close();
    }
}
