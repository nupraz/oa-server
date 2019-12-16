package com.graduation.oa.common.redis;

//import com.bestvike.commons.utils.ConvertUtils;
import com.graduation.oa.common.util.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Cache {
    protected Log logger = LogFactory.getLog(this.getClass());
    private String prefix;
    private RedisTemplate redisTemplate;

    public Cache(RedisTemplate redisTemplate) {
        this.prefix = "";
        this.redisTemplate = redisTemplate;
    }

    public Cache(String prefix, RedisTemplate redisTemplate) {
        this.prefix = prefix;
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = ConvertUtils.jacksonSerializer();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    public void set(String field, Object value) {
        this.set(this.prefix, field, value);
    }

    public void setExpire(String field, Object value, long expire) {
        this.setExpire(this.prefix, field, value, expire);
    }

    public void setExpire(String field, Object value, Date expireAt) {
        this.setExpire(this.prefix, field, value, expireAt);
    }

    public void set(String key, String field, Object value) {
        this.setExpire(key, field, value, 0L);
    }

    public void setExpire(String key, String field, Object value, long expire) {
        this.setExpire(key, field, value, expire, (Date)null);
    }

    public void setExpire(String key, String field, Object value, Date expireAt) {
        this.setExpire(key, field, value, 0L, expireAt);
    }

    public void setExpire(String key, String field, Object value, long expire, Date expireAt) {
        this.redisTemplate.opsForValue().set(this.parseKey(key, field), value);
        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        } else if (expireAt != null) {
            this.redisTemplate.expireAt(this.parseKey(key, field), expireAt);
        }

    }

    public Object get(String field) {
        return this.get(field, Object.class);
    }

    public String get(String field, long expire) {
        return (String)this.get(field, String.class, expire);
    }

    public String get(String key, String field) {
        return (String)this.get(key, field, String.class);
    }

    public String get(String key, String field, long expire) {
        return (String)this.get(key, field, String.class, expire);
    }

    public <T> T get(String field, Class<T> clazz) {
        return this.get(this.prefix, field, clazz);
    }

    public <T> T get(String field, Class<T> clazz, long expire) {
        return this.get(this.prefix, field, clazz, expire);
    }

    public <T> T get(String key, String field, Class<T> clazz) {
        return this.get(key, field, clazz, 0L);
    }

    public <T> T get(String key, String field, Class<T> clazz, long expire) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        return (T) this.redisTemplate.opsForValue().get(this.parseKey(key, field));
    }

    /** @deprecated */
    @Deprecated
    public void del(String field) {
        this.del(this.prefix, field);
    }

    /** @deprecated */
    @Deprecated
    public void del(String key, String field) {
        this.redisTemplate.delete(this.parseKey(key, field));
    }

    public <T> List<T> gets(String fields, Class<T> clazz) {
        return this.gets(this.prefix, fields, clazz);
    }

    public <T> List<T> gets(String key, String fields, Class<T> clazz) {
        String[] arr = fields.split(",");
        List<String> fieldList = new ArrayList();
        String[] var6 = arr;
        int var7 = arr.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String field = var6[var8];
            fieldList.add(this.parseKey(key, field));
        }

        return this.redisTemplate.opsForValue().multiGet(fieldList);
    }

    public boolean exists(String field) {
        return this.exists(this.prefix, field);
    }

    public boolean exists(String key, String field) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        return this.redisTemplate.hasKey(this.parseKey(key, field));
    }

    public List<Object> keys(String pattern) {
        return this.keys(this.prefix, pattern, Object.class);
    }

    public <T> List<T> keys(String pattern, Class<T> clazz) {
        return this.keys(this.prefix, pattern, clazz);
    }

    public <T> List<T> keys(String key, String pattern, Class<T> clazz) {
        return new ArrayList(this.redisTemplate.keys(key + ":" + pattern));
    }

    public Boolean delete(String field) {
        return this.delete(this.prefix, field);
    }

    public Boolean delete(String key, String field) {
        return this.redisTemplate.delete(this.parseKey(key, field));
    }

    public Long deleteMapKey(String field, String mapKey) {
        return this.deleteMapKey(this.prefix, field, mapKey);
    }

    public Long deleteMapKey(String key, String field, String mapKey) {
        return this.redisTemplate.opsForHash().delete(this.parseKey(key, field), new Object[]{mapKey});
    }

    public void put(String field, String mapKey, Object mapValue) {
        this.putExpire(this.prefix, field, mapKey, mapValue, 0L);
    }

    public void put(String key, String field, String mapKey, Object mapValue) {
        this.putExpire(key, field, mapKey, mapValue, 0L);
    }

    public void putExpire(String key, String field, String mapKey, Object mapValue, long expire) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        this.redisTemplate.opsForHash().put(this.parseKey(key, field), mapKey, mapValue);
    }

    public void putAll(String field, Map mapValue) {
        this.putAllExpire(this.prefix, field, mapValue, 0L);
    }

    public void putAll(String key, String field, Map mapValue) {
        this.putAllExpire(key, field, mapValue, 0L);
    }

    public void putAllExpire(String key, String field, Map mapValue, long expire) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        this.redisTemplate.opsForHash().putAll(this.parseKey(key, field), mapValue);
    }

    public void incrementMap(String field, String mapKey, long value) {
        this.incrementMap(this.prefix, field, mapKey, value, 0L);
    }

    public void incrementMap(String key, String field, String mapKey, long value) {
        this.incrementMap(key, field, mapKey, value, 0L);
    }

    public void incrementMap(String key, String field, String mapKey, long value, long expire) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        this.redisTemplate.opsForHash().increment(this.parseKey(key, field), mapKey, value);
    }

    public void decrementMap(String field, String mapKey, long value) {
        this.decrementMap(this.prefix, field, mapKey, value, 0L);
    }

    public void decrementMap(String key, String field, String mapKey, long value) {
        this.incrementMap(key, field, mapKey, value, 0L);
    }

    public void decrementMap(String key, String field, String mapKey, long value, long expire) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        this.redisTemplate.opsForHash().increment(this.parseKey(key, field), mapKey, -1L * value);
    }

    public Object getMapValue(String field, String mapKey) {
        return this.getMapValue(field, mapKey, Object.class);
    }

    public <T> T getMapValue(String field, String mapKey, Class<T> clazz) {
        return this.getMapValue(this.prefix, field, mapKey, clazz);
    }

    public <T> T getMapValue(String key, String field, String mapKey, Class<T> clazz) {
        if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(this.prefix)) {
            key = this.prefix;
        }

        return (T) this.redisTemplate.opsForHash().get(this.parseKey(key, field), mapKey);
    }

    public boolean hasKey(String field, String mapKey) {
        return this.hasKey(this.prefix, field, mapKey);
    }

    public boolean hasKey(String key, String field, String mapKey) {
        return this.redisTemplate.opsForHash().hasKey(this.parseKey(key, field), mapKey);
    }

    public void renameField(String field, String mapKey, String oldMapKey) {
        this.renameField(this.prefix, field, mapKey, oldMapKey);
    }

    public void renameField(String key, String field, String mapKey, String oldMapKey) {
        Object value = this.redisTemplate.opsForHash().get(this.parseKey(key, field), oldMapKey);
        if (value != null) {
            this.redisTemplate.opsForHash().put(this.parseKey(key, field), mapKey, value);
            this.redisTemplate.opsForHash().delete(this.parseKey(key, field), new Object[]{oldMapKey});
        }

    }

    public Map getMap(String field) {
        return this.getMap(this.prefix, field);
    }

    public Map getMap(String key, String field) {
        return this.redisTemplate.opsForHash().entries(this.parseKey(key, field));
    }

    public void send(String channel, Object message) {
        this.redisTemplate.convertAndSend(channel, message);
    }

    public void push(String field, Object value) {
        this.push(this.prefix, field, value);
    }

    public void push(String key, String field, Object value) {
        this.redisTemplate.opsForList().leftPush(this.parseKey(key, field), value);
    }

    public <T> T pop(String field, Class<T> clazz) {
        return this.pop(this.prefix, field, clazz);
    }

    public <T> T pop(String key, String field, Class<T> clazz) {
        return (T) this.redisTemplate.opsForList().rightPop(this.parseKey(key, field));
    }

    public Long add(String field, Object value) {
        return this.add(this.prefix, field, value);
    }

    public Long add(String key, String field, Object value) {
        return this.redisTemplate.opsForSet().add(this.parseKey(key, field), new Object[]{value});
    }

    public Long add(String field, List<?> values) {
        return this.add(this.prefix, field, values);
    }

    public Long add(String key, String field, List<?> values) {
        return this.addExpire(key, field, values, 0L);
    }

    public Long addExpire(String field, List<?> values, long expire) {
        return this.addExpire(this.prefix, field, values, expire);
    }

    public Long addExpire(String key, String field, List<?> values, long expire) {
        if (expire > 0L) {
            this.redisTemplate.expire(this.parseKey(key, field), expire, TimeUnit.SECONDS);
        }

        return values != null && values.size() != 0 ? this.redisTemplate.opsForSet().add(this.parseKey(key, field), values.toArray()) : this.redisTemplate.opsForSet().add(this.parseKey(key, field), new Object[]{new ArrayList()});
    }

    public List<Object> getList(String field) {
        return this.getList(this.prefix, field, Object.class);
    }

    public <T> List<T> getList(String field, Class<T> clazz) {
        return this.getList(this.prefix, field, clazz);
    }

    public List<Object> getList(String key, String field) {
        return this.getList(key, field, Object.class);
    }

    public <T> List<T> getList(String key, String field, Class<T> clazz) {
        return new ArrayList(this.redisTemplate.opsForSet().members(this.parseKey(key, field)));
    }

    private String parseKey(String key, String field) {
        field = field.replace("/:id", "/{id}");
        return StringUtils.isEmpty(key) ? field : key + ":" + field;
    }
}
