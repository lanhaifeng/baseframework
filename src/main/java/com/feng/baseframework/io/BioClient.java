package com.feng.baseframework.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * baseframework
 * bio客户端
 * 2020/6/21 23:01
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Slf4j
public class BioClient {

    private Socket client;
    private BufferedReader consoleReader;

    public static void main(String[] args) throws IOException {
        BioClient client = new BioClient();
        client.initClient("127.0.0.1", 18080);

        client.listen();
    }

    public void initClient(String ip, int port) throws IOException {
        client = new Socket(ip, port);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void listen()  throws IOException{
        Scanner scan = new Scanner(client.getInputStream());
        scan.useDelimiter("\n");
        PrintStream out = new PrintStream(client.getOutputStream());
        boolean flag = true;
        while (flag){
            String inputData = getConsoleInputData();
            out.println(inputData);
            if (scan.hasNext()){
                String str = scan.next();
                log.info("收到服务端消息：" + str);
            }
            if ("byeBye".equalsIgnoreCase(inputData)){
                flag = false;
            }
        }
        client.close();
    }

    public String getConsoleInputData(){
        //数据接受标记
        boolean flag = true;
        String inputData = null;
        while (flag){
            log.info("请输入数据：");
            try {
                // 读取一行数据
                inputData = consoleReader.readLine();
                if(StringUtils.isBlank(inputData)){
                    log.info("数据输入错误，不允许为空！");
                }else {
                    flag = false;
                }
            } catch (IOException e) {
                log.error("获取控制台输入数据失败：" + ExceptionUtils.getFullStackTrace(e));
            }
        }
        return inputData;
    }
}
