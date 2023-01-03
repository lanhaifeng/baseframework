package com.feng.baseframework.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hzmc.common.protobuf.ProtoActiveMQ;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * baseframework
 * 2018/8/16 17:17
 * Protobuf工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class ProtobufUtil {

    public static void main(String[] args) {
        sendProtobufBytesTOMq(1);
    }

    public static String translateBytesTOString(byte[] sources){
        return new String(org.bouncycastle.util.encoders.Base64.encode(sources));
    }

    public static String buildAccessId(){
        return  UUID.randomUUID().toString();
    }

    private static List<ProtoActiveMQ.CapaaAccess> buildCountAuditAccess(Integer count){
        List<ProtoActiveMQ.CapaaAccess> data = new ArrayList<>();
        for (int i=0;i<count;i++){
            data.add(buildAuditAccess(i));
        }

        return data;
    }

    private static ProtoActiveMQ.CapaaAccess buildAuditAccess(Integer suffix){
        if(suffix == null){
            suffix = 0;
        }
        ProtoActiveMQ.CapaaAccess.Builder accessBuilder = ProtoActiveMQ.CapaaAccess.newBuilder();

        accessBuilder.setAccessID(ByteString.copyFromUtf8(buildAccessId()));
        accessBuilder.setCmdType(ProtoActiveMQ.MsgCmdType.CAPAAAccess);
//        accessBuilder.setSessionID(ByteString.copyFromUtf8(buildAccessId()));
        accessBuilder.setSessionID(ByteString.copyFromUtf8("900120180816075512007780"));
        accessBuilder.setAppUser(ByteString.copyFromUtf8("test"+suffix));
        accessBuilder.setEndIP(ByteString.copyFromUtf8("192.168.60.31"));
        accessBuilder.setRuleName(ByteString.copyFromUtf8("PUBLIC"));
        accessBuilder.setOpTime(new Date().getTime());
        accessBuilder.setStrCmdType(ByteString.copyFromUtf8("SELECT"));
        accessBuilder.setObjectName(ByteString.copyFromUtf8("test"+suffix));
        accessBuilder.setObjectOwner(ByteString.copyFromUtf8("test"+suffix));
        accessBuilder.setObjectType(ByteString.copyFromUtf8("test"+suffix));
        accessBuilder.setActionLevel(1);
        accessBuilder.setDataSrcValue(ProtoActiveMQ.DataSrcType.DATASRC_RESERVED.getNumber());
        accessBuilder.setCliIp(ByteString.copyFromUtf8("192.168.60.31"));
        accessBuilder.setCliPort(18004);
        accessBuilder.setSvrIp(ByteString.copyFromUtf8("192.168.230.64"));
        accessBuilder.setSvrPort(1521);
        accessBuilder.setDbStatus(ProtoActiveMQ.DBStatusType.DB_ACTIVE);
        accessBuilder.setAuditLevel(1);
        accessBuilder.setTxID(ByteString.copyFromUtf8("null"));
        accessBuilder.setScn(ByteString.copyFromUtf8("0"));
        accessBuilder.setCscn(ByteString.copyFromUtf8("0"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("1"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("'PUBLIC'"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("1"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("PUBLIC"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("2"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("'USERENV'"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("'CURRENT_SCHEMA'"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("3"));
        accessBuilder.addBindValue(ByteString.copyFromUtf8("4"));
        accessBuilder.setSrcMac(ByteString.copyFromUtf8("7C:1C:F1:D4:BE:D8"));
        accessBuilder.setSqlParserTypeValue(ProtoActiveMQ.SqlParserType.SQL_PROTOCOL_PARSER_VALUE);
        accessBuilder.setSqlText(ByteString.copyFromUtf8("SELECT ID, NAME FROM ( SELECT USER_ID AS ID, USERNAME AS NAME FROM SYS.ALL_USERS UNION ALL SELECT ? AS ID, ? AS NAME FROM SYS.DUAL ) ORDER BY DECODE(NAME, USER, ?, ?, ?, SYS_CONTEXT(?, ?), ?, ?), ID"));
        accessBuilder.setOriginalSqlText(ByteString.copyFromUtf8("SELECT ID, NAME FROM ( SELECT USER_ID AS ID, USERNAME AS NAME FROM SYS.ALL_USERS UNION ALL SELECT 1 AS ID, 'PUBLIC' AS NAME FROM SYS.DUAL ) ORDER BY DECODE(NAME, USER, 1, 'PUBLIC', 2, SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA'), 3, 4), ID"));
        accessBuilder.setDataType(ProtoActiveMQ.DataType.TRAFFIC_FLOW_VALUE);

        ProtoActiveMQ.CapaaAccess capaaAccess = accessBuilder.build();
        System.out.println("capaaAccess length1:"+capaaAccess.toByteArray().length);
        accessBuilder.setCmdLen(capaaAccess.toByteArray().length+5);
        capaaAccess = accessBuilder.build();
        System.out.println("capaaAccess length2:"+capaaAccess.toByteArray().length);

        System.out.println("capaaAccess base64:"+translateBytesTOString(capaaAccess.toByteArray()));
        System.out.println("capaaAccess json:\n"+capaaAccess.toString());
        ProtoActiveMQ.CapaaAccess.Builder accessBuilder2 = ProtoActiveMQ.CapaaAccess.newBuilder();
        System.out.println("capaaAccess length:"+accessBuilder2.build().toByteArray().length);
        try {
            ProtoActiveMQ.CapaaAccess capaaAccessTarget = ProtoActiveMQ.CapaaAccess.parseFrom(capaaAccess.toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println("capaaAccess json:\n"+capaaAccess.toString());
        return capaaAccess;
    }

    private static void sendProtobufBytesTOMq(Integer count){
        if(count == null || count < 1){
            count = 1;
        }
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session = null;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory("hzmcmq",
                "hzmcmq@2017", "tcp://192.168.230.64:61615?wireFormat.maxInactivityDuration=0&wireFormat.maxInactivityDurationInitalDelay=30000");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createTopic("access1");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session, buildCountAuditAccess(count) ,producer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(session != null){
                    session.close();
                }
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, List<ProtoActiveMQ.CapaaAccess> datas, MessageProducer producer)
            throws Exception {
        BytesMessage message = null;
        for(ProtoActiveMQ.CapaaAccess capaaAccess : datas){
            message = session.createBytesMessage();
            message.writeBytes(capaaAccess.toByteArray());
            producer.send(message);
        }
    }
}
