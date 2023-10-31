package com.feng.baseframework.lua;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Replica;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.task.RegisterTask;
import org.apache.commons.io.FileUtils;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * baseframework
 * 2023/10/31 22:47
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
public class CacheLuaScriptTest {
    private RedisTemplate<String,String> redisTemplate;
    private AerospikeClient client;
    private AsConfig asConfig;

    private void redisInit() {
        // Redis 连接
        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration("10.100.1.166", 6379);
        config.setPassword("passwd123");
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(config);
        lettuceConnectionFactory.afterPropertiesSet();

        // 手动创建 RedisTemplate
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 设置序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    private void asInit() {
        asConfig = new AsConfig();
        asConfig.setNodes("10.100.1.241:3000");
        asConfig.setNamespace("bsfit");
        asConfig.setSet("dfp-test");
        asConfig.setMaxConn(1000);
        asConfig.setMaxConn(300);
        ClientPolicy policy = new ClientPolicy();
        policy.timeout = 1000;
        policy.connPoolsPerNode = 300 / Runtime.getRuntime().availableProcessors();
        client = new AerospikeClient(policy, Arrays.stream(asConfig.getNodes().split(","))
                .map(host -> {
                    String[] hostPort = host.split(":");
                    return new Host(hostPort[0], Integer.parseInt(hostPort[1]));
                }).toArray(Host[]::new));
    }

    void testRedis() throws IOException {
        redisInit();

        URL url = Thread.currentThread().getContextClassLoader().getResource("lua/redisCacheDevice.lua");
        String redisScriptStr = FileUtils.readFileToString(new File(url.getFile()));

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptText(redisScriptStr);
        List<String> jsonDatas = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        keys.add("redis-script-test");
        jsonDatas.add("ato1|WEB|rule_isVPN|20230917");

        System.out.println(redisTemplate.opsForValue().getOperations().execute(redisScript, keys, jsonDatas.toArray()));

        System.out.println(redisTemplate.opsForValue().get("redis-script-test"));
    }

    void testAs() {
        asInit();
        client.removeUdf(null, "asCacheDevice.lua");
        URL url = Thread.currentThread().getContextClassLoader().getResource("lua/asCacheDevice.lua");
        RegisterTask task = client.register(null, url.getPath(), "asCacheDevice.lua", Language.LUA);
        task.waitTillComplete();
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.expiration = 10; // 设置过期时间（TTL），单位为秒
        writePolicy.sendKey = true;
        writePolicy.replica = Replica.MASTER;
        Key key = new Key(asConfig.getNamespace(), asConfig.getSet(), "redis-script-test");
        Object result = client.execute(writePolicy, key, "asCacheDevice", "getThenUpdate",
                new Value.StringValue("value"), new Value.StringValue("ato1|WEB|rule_isProxy|20230917"));
        System.out.println(result);
    }

    public static void main(String[] args) throws IOException {
        CacheLuaScriptTest cacheLuaScriptTest = new CacheLuaScriptTest();
        cacheLuaScriptTest.testAs();
        cacheLuaScriptTest.testRedis();
    }
}
