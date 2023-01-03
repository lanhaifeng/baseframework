package com.feng.baseframework.jdk.lang;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ProjectName: baseframework
 * @Description: 基本类型的测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/26 23:39
 * @UpdateUser:
 * @UpdateDate: 2018/10/26 23:39
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseClassTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 计算机中存储的都是数的补码
     * 原码：二进制定点表示法，最高位为符号位，0表示正，1表示负，其余位数表示数值大小
     * 正数的原码、反码、补码都是相同的
     * 反码：负数反码是除符号位外，其余原码依次取反
     * 补码：负数的补码是反码的末位加1
     *
     * 强制将高精度类型转为低精度类型会丢失精度
     */
    @Test
    public void typeConversionLowerTest(){
        logger.info("130二进制为："+Integer.toBinaryString(130));//00000000 00000000 00000000 10000010
        logger.info("-2二进制为："+Integer.toBinaryString(-2));//11111111 11111111 11111111 11111110
        /**
         * 1. 130为int，int为4个字节32位，故二进制为00000000 00000000 00000000 10000010
         * 2. 强制转为byte，byte为1个字节8位，得到补码10000010
         * 3. 最高一位为1，表示这是一个负数的补码
         * 4. 由补码减去1，得到反码为10000001，由反码除去符号位依次取反得到原码11111110
         * 5. 故结果为-126
         */
        logger.info("(byte)130 is "+(byte)130);//-126
    }

    /**
     * 自动类型转换：数字表示范围小的数据类型可以自动转换成范围大的数据类型
     * 自动转换也要小心数据溢出问题
     *
     * 参与计算的数据本身没有超出数据类型允许的精度，但是其计算结果却很可能超过数据类型允许的精度
     * 一般把第一个数据转换成范围大的数据类型再和其他的数据进行运算
     */
    @Test
    public void typeConversionHighTest(){
        int count = 100000000;
        int price = 1999;
        long totalPrice = count * price;
        logger.info("[int]100000000*[int]1999 is "+totalPrice);//-1963462912
        totalPrice = (long)count * price;
        logger.info("[int]100000000*[int]1999 is "+totalPrice);//199900000000
    }
}
