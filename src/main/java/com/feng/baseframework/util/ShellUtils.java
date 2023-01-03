package com.feng.baseframework.util;

import com.feng.baseframework.model.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * 2020/9/3 14:09
 * 执行shell脚本工具类
 *
 *
 * @author lanhaifeng
 * @since
 **/
public class ShellUtils {

    private static final Logger logger = LoggerFactory.getLogger(ShellUtils.class);


    public static final int ERR_GOT_EXCEPTION = 99999;

    /**
     * 数组元素中不为""即为执行结果
     * @param cmd
     * @return com.baseframework.utils.domain.ProcessResult
     */
    public static ProcessResult execShell(String cmd){
        logger.info("execute shell cmd:" + cmd);
        ProcessResult processResult = new ProcessResult();

        String cmds[] = new String[]{"/bin/sh", "-c", cmd};
        if (isWindows()) {
            cmds = new String[]{"cmd", "/c", cmd};
        }
        Process process = null;
        BufferedReader br = null;
        BufferedReader brErr = null;
        StringBuilder result = new StringBuilder();
        StringBuilder resultErr = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(cmds);
            //阻塞，等待执行完成
            process.waitFor();

            int exitValue = process.exitValue();
            logger.info("cmd: " + cmd + ", exitValue: " + exitValue);
            processResult.setExitValue(exitValue);

            Charset charset = Charset.forName("UTF-8");
            if(isWindows()){
                charset = Charset.forName("GBK");
            }
            br = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
            brErr = new BufferedReader(new InputStreamReader(process.getErrorStream(), charset));

            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = brErr.readLine()) != null) {
                resultErr.append(line).append('\n');
            }

            processResult.setResult(result.toString());
            processResult.setErrorMessage(resultErr.toString());
        } catch (Exception e) {
            logger.error("Execute failed due exception:" + e.getMessage());
            processResult.setExitValue(ERR_GOT_EXCEPTION);
            processResult.setErrorMessage(e.getMessage());
            return processResult;
        } finally {
            closeStream(br);
            closeStream(brErr);
            if(process != null){
                process.destroy();
            }
        }
        return processResult;
    }

    private static void closeStream(Closeable stream){
        if(stream != null){
            try {
                stream.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static Boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static Boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
