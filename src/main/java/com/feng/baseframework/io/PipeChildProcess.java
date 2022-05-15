package com.feng.baseframework.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * baseframework
 * 2022/5/15 22:19
 * 管道子进程Child process Parent process
 *
 * @author lanhaifeng
 * @since
 **/
public class PipeChildProcess {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String line ;
        StringBuffer all = new StringBuffer();
        while((line = s.readLine()) != null){
            all.append(line);
        }
        System.out.println(all);
        s.close();
    }
}
