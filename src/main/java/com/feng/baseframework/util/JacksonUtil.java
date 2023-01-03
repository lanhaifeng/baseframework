package com.feng.baseframework.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: svc-search-biz
 * @description: jackson工具类
 * @author: lanhaifeng
 * @create: 2018-05-16 19:21
 * @UpdateUser:
 * @UpdateDate: 2018/5/16 19:21
 * @UpdateRemark:
 **/
public class JacksonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JacksonUtil() {
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * @author: lanhaifeng
     * @description 列表数组转换为json字符串
     * @date: 2018/5/16 19:27
     * @param obj
     * @return java.lang.String
     */
    public static String bean2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description 列表数组转换为json字符串,忽略空值
     * @date: 2018/5/16 19:27
     * @param obj
     * @return java.lang.String
     */
    public static String obj2jsonIgnoreNull(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description json 转JavaBean
     * @date: 2018/5/16 19:28
     * @param jsonString
     * @param clazz
     * @return T
     */
    public static <T> T json2pojo(String jsonString, Class<T> clazz) {
        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description json 转JavaBean
     * @date: 2018/5/16 19:28
     * @param jsonString
     * @param clazz
     * @return T
     */
    public static <T> T json2pojo(String jsonString, JavaType javaType) {
        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description json 转JavaBean
     * @date: 2018/5/16 19:28
     * @param jsonString
     * @param clazz
     * @return T
     */
    public static <T> T json2pojo(String jsonString, TypeReference typeReference) {
        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }


    /**
     * @author: lanhaifeng
     * @description json字符串转换为map
     * @date: 2018/5/16 19:28
     * @param jsonString
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static <T> Map<String, T> json2map(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description json字符串转换为map
     * @date: 2018/5/16 19:29
     * @param jsonString
     * @param clazz
     * @return java.util.Map<java.lang.String,T>
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap<String, T>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
            return result;
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description 深度转换json成map
     * @date: 2018/5/16 19:29
     * @param json
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String, Object> json2mapDeeply(String json) {
        try {
            return json2MapRecursion(json, objectMapper);
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description 解析工具，把json解析成list，如果list内部的元素存在jsonString，继续解析
     * @date: 2018/5/16 19:30
     * @param json
     * @param mapper
     * @return java.util.List<java.lang.Object>
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        List<Object> list = mapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str, mapper);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str, mapper);
                }
            }
        }

        return list;
    }

    /**
     * @author: lanhaifeng
     * @description 把json解析成map，如果map内部的value存在jsonString，继续解析
     * @date: 2018/5/16 19:30
     * @param json
     * @param mapper
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = mapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }

    /**
     * @author: lanhaifeng
     * @description json数组字符串转换为列表
     * @date: 2018/5/16 19:30
     * @param jsonArrayStr
     * @param clazz
     * @return java.util.List<T>
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {

        try {
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            List<T> lst = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
            return lst;
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description 获取泛型的Collection Type
     * @date: 2018/5/16 19:31
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return com.fasterxml.jackson.databind.JavaType Java类型
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * @author: lanhaifeng
     * @description 转JavaBean
     * @date: 2018/5/16 19:32
     * @param map
     * @param clazz
     * @return T
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * @author: lanhaifeng
     * @description map 转json
     * @date: 2018/5/16 19:32
     * @param map
     * @return java.lang.String
     */
    public static String mapToJson(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.JACKSON_PARSE_ERROR,e);
        }
    }

    /**
     * @author: lanhaifeng
     * @description 转JavaBean
     * @date: 2018/5/16 19:32
     * @param obj
     * @param clazz
     * @return T
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    /**
     * 读取json文件数据
     * @param path
     * @return
     */
    public static String getJsonFromFile(String path) throws IOException {
        File file = FileUtils.getFileByRelativePath(path);
        return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
    }
}
