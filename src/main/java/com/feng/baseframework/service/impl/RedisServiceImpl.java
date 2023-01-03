package com.feng.baseframework.service.impl;

import com.feng.baseframework.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * baseframework
 * 2018/8/20 15:23
 * redis缓存业务实现
 *
 * @author lanhaifeng
 * @since
 **/
@Service("redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void changeDb(int index) {
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) stringRedisTemplate.getConnectionFactory();
        jedisConnectionFactory.setDatabase(index);
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, Long timeOut) {
        stringRedisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.MILLISECONDS);
    }

    @Override
    public void multiSetList(String key, Collection<String> listData) {
        stringRedisTemplate.opsForList().leftPushAll(key, listData);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public List<Object> getRightListMultValueAfterDel(final String key, final long count) {
        final RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        final Long allCount = stringRedisTemplate.opsForList().size(key);
        if(allCount == 0){
            return null;
        }

        List<Object> rightList = stringRedisTemplate.executePipelined(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] listName  = serializer.serialize(key);
                for (int i=0 ;i<count ;i++) {
                    redisConnection.rPop(listName);
                }
                return null;
            }
        });
        removeNullObject(rightList);
        return rightList;
    }

    private void removeNullObject(List<Object> rightList) {
        if(rightList != null){
            Iterator iterator = rightList.iterator();
            while (iterator.hasNext()){
                Object obj = iterator.next();
                if(obj == null){
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public List<Object> getLeftListMultValueAfterDel(final String key, final long count) {
        final Long allCount = stringRedisTemplate.opsForList().size(key);
        if(allCount == 0){
            return null;
        }

        List<Object> leftList = stringRedisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                for (int i=0 ;i<count ;i++) {
                    redisOperations.opsForList().rightPop(key);
                }
                return null;
            }
        });
        removeNullObject(leftList);
        return leftList;
    }

    @Override
    public void addSetMembers(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean isSetMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public boolean deleteSetMembers(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values) == values.length;
    }

    @Override
    public List<Object> getHashMultValue(String hashName, Set<String> keys, boolean isDelete) {
        if(keys == null || keys.size() < 1 || StringUtils.isEmpty(hashName)){
            return null;
        }
        Long allCount = stringRedisTemplate.opsForHash().size(hashName);
        if(allCount == null || allCount < 1){
            return null;
        }
        List<Object> dataList = stringRedisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                for (String key : keys) {
                    if(StringUtils.isEmpty(key)){
                        continue;
                    }
                    redisOperations.opsForHash().get(hashName,key);
                    if(isDelete){
                        redisOperations.opsForHash().delete(hashName,key);
                    }
                }
                return null;
            }
        });

        return dataList;
    }

    @Override
    public void setHashMultValue(String hashName, Map<String, String> data) {
        if(data == null || data.size() < 1 || StringUtils.isEmpty(hashName)){
            return;
        }
        stringRedisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                for (String key : data.keySet()) {
                    redisOperations.opsForHash().put(hashName,key,data.get(key));
                }
                return null;
            }
        });
    }

    @Override
    public Long getHashCount(String hashName) {
        return stringRedisTemplate.opsForHash().size(hashName);
    }

    @Override
    public void deleteHashKey(String hashName, String dataKey) {
        stringRedisTemplate.opsForHash().delete(hashName, dataKey);
    }

    @Override
    public void convertAndSend(String channel, String message) {
        if(StringUtils.isEmpty(channel) || StringUtils.isEmpty(message) ){
            return;
        }
        stringRedisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void convertAndSend(String channel, List<String> messages) {
        if(StringUtils.isEmpty(channel) || messages == null || messages.size() < 1 ){
            return;
        }
        stringRedisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                for (String message : messages) {
                    redisOperations.convertAndSend(channel, message);
                }
                return null;
            }
        });
    }

    @Override
    public Set<String> getHashKeys(String hashName) {
        Set<Object> keys = stringRedisTemplate.opsForHash().keys(hashName);
        Set<String> result = new HashSet<>();
        if(keys != null && !keys.isEmpty()){
            for(Object obj : keys){
                result.add(obj.toString());
            }
        }

        return result;
    }

    @Override
    public Boolean hexists(String hashName, String dataKey) {
        return stringRedisTemplate.opsForHash().hasKey(hashName, dataKey);
    }
}
