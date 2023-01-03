package com.feng.baseframework.io;

import io.jsonwebtoken.lang.Assert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ByteBufTest {

    private ByteBuf byteBuf;
    private String resultByte;
    private String result;
    private String sourceResult;

    @Before
    public void setUp() throws Exception {
        // 1.创建一个非池化的ByteBuf，大小为10个字节
        byteBuf = Unpooled.buffer(10);
    }

    @Test
    public void initTest() {
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());

        // 1.创建一个非池化的ByteBuf，大小为10个字节
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 0, cap: 10)".equals(result));
        Assert.state("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]".equals(resultByte));

        // 2.写入一段内容
        byte[] bytes = {1,2,3,4,5};
        sourceResult = Arrays.toString(bytes);
        byteBuf.writeBytes(bytes);
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("[1, 2, 3, 4, 5]".equals(sourceResult));
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 5, cap: 10)".equals(result));
        Assert.state("[1, 2, 3, 4, 5, 0, 0, 0, 0, 0]".equals(resultByte));

        // 3.读取一段内容
        byte b1 = byteBuf.readByte();
        byte b2 = byteBuf.readByte();
        sourceResult = Arrays.toString(new byte[]{b1,b2});
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("[1, 2]".equals(sourceResult));
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 2, widx: 5, cap: 10)".equals(result));
        Assert.state("[1, 2, 3, 4, 5, 0, 0, 0, 0, 0]".equals(resultByte));

        // 4.将读取的内容丢弃
        byteBuf.discardReadBytes();
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 3, cap: 10)".equals(result));
        Assert.state("[3, 4, 5, 4, 5, 0, 0, 0, 0, 0]".equals(resultByte));

        // 5.清空读写指针
        byteBuf.clear();
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 0, cap: 10)".equals(result));
        Assert.state("[3, 4, 5, 4, 5, 0, 0, 0, 0, 0]".equals(resultByte));

        // 6.再次写入一段内容，比第一段内容少
        byte[] bytes2 = {1,2,3};
        byteBuf.writeBytes(bytes2);
        sourceResult = Arrays.toString(bytes2);
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("[1, 2, 3]".equals(sourceResult));
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 3, cap: 10)".equals(result));
        Assert.state("[1, 2, 3, 4, 5, 0, 0, 0, 0, 0]".equals(resultByte));

        // 7.将ByteBuf清零
        byteBuf.setZero(0,byteBuf.capacity());
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 3, cap: 10)".equals(result));
        Assert.state("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]".equals(resultByte));

        // 8.再次写入一段超过容量的内容
        byte[] bytes3 = {1,2,3,4,5,6,7,8,9,10,11};
        byteBuf.writeBytes(bytes3);
        sourceResult = Arrays.toString(bytes3);
        result = byteBuf.toString();
        resultByte = Arrays.toString(byteBuf.array());
        Assert.state("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]".equals(sourceResult));
        Assert.state("UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 14, cap: 64)".equals(result));
        Assert.state("[0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]".equals(resultByte));
    }
}