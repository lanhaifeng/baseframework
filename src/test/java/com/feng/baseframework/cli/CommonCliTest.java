package com.feng.baseframework.cli;

import org.apache.commons.cli.*;

/**
 * 测试common cli
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/4/28 17:53创建:CommonCliTest
 * @since v2.0
 */
public class CommonCliTest {

    public static void main(String[] args) throws ParseException {
        //option的容器
        Options options = new Options();
        //boolean型的option
        options.addOption("help",false,"help information");
        //当第二参数是true时，可以是这样的参数  -Otest
        options.addOption("O",true,"you can set a value after the O");
        Option c = Option.builder("c")  //option的名字,判断的时候要使用这个名字
                .longOpt("filename")           //长名字，可以通过cmd.getOptionValue("filename")获取
                .required(false)               //是否必须有这个选项
                .hasArg()                         //带一个参数
                .argName("filename")     //参数的名字
                .desc("return sum of characters")  //描述
                .build();                             //必须有
        //将c这个option添加进去
        options.addOption(c);

        //parser
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options,args);
        //询问是否有help
        if(cmd.hasOption("help") || cmd.getOptions().length == 0) {
            //调用默认的help函数打印
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "java wordcount [OPTION] <FILENAME>", options );
            return;
        }

        if(cmd.hasOption("c")){
            //获得相应的选项（c）的参数
            String filename = cmd.getOptionValue("c");
            System.out.println(filename);
            //do something
        }
        //将除了选项之外的参数打印出来 otherfilename
        String[] s = cmd.getArgs();
        for(String e : s){
            System.out.println("="+e);
        }
    }
}
