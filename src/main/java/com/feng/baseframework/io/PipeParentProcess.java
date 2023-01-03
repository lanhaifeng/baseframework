package com.feng.baseframework.io;

import java.io.*;

/**
 * baseframework
 * 2022/5/15 22:21
 * 管道父进程
 *
 * @author lanhaifeng
 * @since
 **/
public class PipeParentProcess {

    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String cp = "\"" + System.getProperty("java.class.path");
        cp += File.pathSeparator + ClassLoader.getSystemResource("").getPath() + "\"";
        String cmd = java + " -cp " + cp + " com.feng.baseframework.io.PipeChildProcess";
        Process p = run.exec(cmd);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        bw.write("999999");
        bw.flush();
        bw.close();

        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String s;
        while ((s = br.readLine()) != null)
            System.out.println(s);
    }
}
