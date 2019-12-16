package com.graduation.oa.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public ConvertUtils() {
    }

    public static ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    public static <T> T getBean(String s, Class<T> clazz) {
        try {
            return objectMapper.readValue(s, clazz);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> T getBean(InputStream in, Class<T> clazz) {
        try {
            return objectMapper.readValue(in, clazz);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> getBeanList(String s, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{clazz});
            return (List)objectMapper.readValue(s, javaType);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> getBeanList(InputStream in, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{clazz});
            return (List)objectMapper.readValue(in, javaType);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = initObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }
}
