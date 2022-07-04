package com.hezhan.shirodemo.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类,后续还可以继续维护
 * @Author Zhanzhan
 * @Date 2021/2/16 16:15
 **/
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取符合要求的所有key
     * @param patternKey 指定要求的key
     * @return 获取符合要求的所有key集合
     */
    public Set<String> getAllKey(String patternKey){
        return redisTemplate.keys(patternKey);
    }

    /**
     * 清空所有的redis数据库
     */
    public void flushAll(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    /**
     * 清空当前数据库
     */
    public void flushDB(){
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    /**
     * 指定缓存的有效期
     * @param key 缓存key
     * @param time 有效时间，已秒为单位
     * @return
     */
    public Boolean expire(String key, long time){
        if (time > 0){
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return true;
    }

    /**
     * 获取指定key的过期时间，返回0表示永久有效
     * @param key 缓存key
     * @return 过期时间
     */
    public Long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断指定的缓存key是否存在
     * @param key 缓存key
     * @return
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除指定的key，可以传入一个String数组，批量删除
     * @param key 缓存key数组
     */
    public void delete(String... key){
        if (key == null || key.length < 1){
            return;
        }
        if (key.length == 1){
            redisTemplate.delete(key[0]);
        } else {
            redisTemplate.delete(Arrays.asList(key));
        }
    }

    /**
     * String类型的添加缓存数据
     * @param key 缓存key
     * @param value 缓存value
     */
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * String类型的添加缓存数据，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param value 缓存value
     * @param time 过期时间
     */
    public void set(String key, Object value, long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 获取指定key的缓存数据
     * @param key 缓存key
     * @return
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 指定key的值递增
     * @param key 缓存key
     * @param delta 递增因子，即每次递增加几
     * @return
     */
    public Long incr(String key, long delta){
        if (delta < 0){
            throw new RuntimeException("递增因子要大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 指定key的值递减
     * @param key 缓存key
     * @param delta 递减因子，即每次递减减几
     * @return
     */
    public Long decr(String key, long delta){
        if (delta < 0){
            throw new RuntimeException("递减因子要大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * <p>Hash类型</p>
     * 获取指定key的指定键值对的值
     * @param key 缓存key
     * @param hashKey hash类型的键值对的key
     * @return
     */
    public Object hget(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * <p>Hash类型</p>
     * 获取指定key的所有键值对的值
     * @param key 缓存key
     * @return
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * <p>Hash类型</p>
     * 添加指定缓存key的多个键值对
     * @param key 缓存key
     * @param map 对应的多个键值对
     */
    public void hmset(String key, Map<Object, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * <p>Hash类型</p>
     * 添加指定缓存key的多个键值对，并设置过期时间，以秒为单位
     * @param key 缓存key
     * @param map 对应的多个键值对
     * @param time 过期时间
     */
    public void hmset(String key, Map<Object, Object> map, long time){
        redisTemplate.opsForHash().putAll(key, map);
        expire(key, time);
    }

    /**
     * <p>Hash类型</p>
     * 添加指定缓存key的一个键值对
     * @param key 换存key
     * @param hashKey 对应的键值对的key
     * @param value 对应的键值对的value
     */
    public void hSet(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * <p>Hash类型</p>
     * 添加指定缓存key的一个键值对，并设置过期时间，以秒为单位
     * @param key 缓存key
     * @param hashKey 对应的键值对的key
     * @param value 对应的键值对的value
     * @param time 过期时间
     */
    public void hSet(String key, String hashKey, Object value, long time){
        hSet(key, hashKey, value);
        expire(key, time);
    }

    /**
     * <p>Hash类型</p>
     * 删除指定key的指定键值对
     * @param key 缓存key
     * @param hashKey 要删除的键值对的key
     */
    public void hDelete(String key, Object... hashKey){
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * <p>Hash类型</p>
     * 判断缓存中是否有指定key的指定键值对
     * @param key 缓存key
     * @param hashKey 对应键值对的key
     * @return
     */
    public Boolean hHashKey(String key, Object hashKey){
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }
    
    /**
     * <p>Hash类型</p>
     * 递增指定key的指定键值对的值，如果不存在，则会创建，并且返回新增后的值
     * @param key 缓存key
     * @param hashKey 指定键值对的key
     * @param delta 递增因子，即要加几
     * @return
     */
    public double hIncr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
    
    /**
     * <p>Hash类型</p>
     * 递减指定key的指定键值对的值，如果不存在，则会创建，并且返回新增后的值
     * @param key 缓存key
     * @param hashKey 指定键值对的key
     * @param delta 递减因子，即要减几
     * @return
     */
    public double hDecr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    //================================Set===========================

    /**
     * <p>Set类型</p>
     * 根据key获取Set中的所有值
     * @param key 缓存key
     * @return
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * <p>Set类型</p>
     * 查询指定的key中指定的value是否存在
     * @param key 缓存key
     * @param value Set中的指定元素
     * @return
     */
    public Boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * <p>Set类型</p>
     * 将缓存数据放入指定的key中
     * @param key 缓存key
     * @param values Set集合
     * @return
     */
    public Long sSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * <p>Set类型</p>
     * 将缓存数据放入指定的key中，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param time 过期时间
     * @param values 要放入缓存的数据
     * @return
     */
    public Long sSet(String key, long time, Object... values) {
        Long l = sSet(key, values);
        expire(key, time);
        return l;
    }

    /**
     * <p>Set类型</p>
     * 获取指定的key中Set的长度
     * @param key 缓存key
     * @return
     */
    public Long sGetSetLength(String key) {
        Long l = redisTemplate.opsForSet().size(key);
        return l == null ? 0 : l;
    }

    /**
     * <p>Set类型</p>
     * 移除指定的key中指定的value
     * @param key 缓存key
     * @param values 指定要移除的value
     * @return
     */
    public long sRemove(String key, Object... values) {
        Long l = redisTemplate.opsForSet().remove(key, values);
        return l == null ? 0 : l;
    }


    //==========================List=======================

    /**
     * <p>List类型</p>
     * 获取指定的key中的list集合内容，根据起始下标获取
     * @param key 缓存key
     * @param start 集合的开始下标，包含在内
     * @param end 集合的截止下标，也包含在内
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>List类型</p>
     * 根据key获取list集合的长度
     * @param key 缓存key
     * @return
     */
    public long lGetLength(String key) {
        Long l = redisTemplate.opsForList().size(key);
        return l == null ? 0 : l;
    }

    /**
     * <p>List类型</p>
     * 获取指定key的指定下标的元素
     * @param key 缓存key
     * @param index 指定的list集合下标
     * @return
     */
    public Object lGetByIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>List类型</p>
     * 将值从左侧放入List类型的缓存中，头插法
     * @param key 缓存key
     * @param value 要放入缓存的值
     * @return
     */
    public void lSet(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * <p>List类型</p>
     * 将值从右侧放入List类型的缓存中，尾插法
     * @param key 缓存key
     * @param value 要放入缓存的值
     */
    public void rSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * <p>List类型</p>
     * 将值从左侧放入List类型的缓存中，头插法，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param value 要放入缓存的值
     * @param time 过期时间
     * @return
     */
    public void lSet(String key, Object value, long time) {
        redisTemplate.opsForList().leftPush(key, value);
        expire(key, time);
    }

    /**
     * <p>List类型</p>
     * 将值从右侧放入List类型的缓存中，尾插法，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param value 要放入缓存的值
     * @param time 过期时间
     */
    public void rSet(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
    }

    /**
     * <p>List类型</p>
     * 将list集合从左边放入缓存，头插法
     * @param key 缓存key
     * @param list 要放入缓存的集合
     */
    public void lSet(String key, List<Object> list) {
        redisTemplate.opsForList().leftPushAll(key, list);
    }

    /**
     * <p>List类型</p>
     * 将list集合从右边放入缓存，尾插法
     * @param key 缓存key
     * @param list 要放入缓存的集合
     */
    public void rSet(String key, List<Object> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * <p>List类型</p>
     * 将list集合从左边放入缓存，头插法，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param list 要放入缓存的集合
     * @param time 过期时间
     */
    public void lSet(String key, List<Object> list, long time) {
        redisTemplate.opsForList().leftPushAll(key, list);
        expire(key, time);
    }

    /**
     * <p>List类型</p>
     * 将list集合从右边放入缓存，尾插法，并设置过期时间，单位为秒
     * @param key 缓存key
     * @param list 要放入缓存的集合
     * @param time 过期时间
     */
    public void rSet(String key, List<Object> list, long time) {
        redisTemplate.opsForList().rightPushAll(key, list);
        expire(key, time);
    }

    /**
     * <p>List类型</p>
     * 修改指定key的list集合中指定下标的值
     * @param key 缓存key
     * @param index 要修改的值的下标
     * @param value 修改的值
     */
    public void lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * <p>List类型</p>
     * <p>移除指定key的list集合中指定个数的指定值</p>
     * <p>若count = 0，则表示移除全部</p>
     * <p>若count > 0，则表示从List集合的左侧开始数</p>
     * <p>若count < 0，则表示从List集合的右侧开始数</p>
     * @param key 缓存key
     * @param count 要移除的元素个数
     * @param value 要移除的值
     * @return
     */
    public Long lRemove(String key, long count, Object value) {
        Long l = redisTemplate.opsForList().remove(key, count, value);
        return l == null ? 0L : l;
    }


    //===============================ZSet=============================

    /**
     * <p>ZSet类型</p>
     * 添加ZSet类型的缓存，这是一个有序集合，是按照元素的scores的值由小到大排序的
     * @param key 缓存key
     * @param value 要添加的值
     * @param scores 值的scores,根据此值的大小来排序
     */
    public void zAdd(String key, String value, double scores) {
        redisTemplate.opsForZSet().add(key, value, scores);
    }

    /**
     * <p>ZSet类型</p>
     * 批量添加ZSet类型的缓存，将一个set集合添加的ZSet中
     * @param key 缓存key
     * @param values 要放入缓存的set集合
     */
    public void zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * <p>ZSet类型</p>
     * 移除指定key中ZSet集合中的一个或多个指定的元素
     * @param key 缓存key
     * @param values 要移除的元素
     * @return
     */
    public Long zRemove(String key, Object... values) {
        Long l = redisTemplate.opsForZSet().remove(key, values);
        return l == null ? 0L : l;
    }

    /**
     * <p>ZSet类型</p>
     * 增加指定key的ZSet集合中指定元素的scores，并返回增加后的scores值
     * @param key 缓存key
     * @param value 要增加scores的元素
     * @param delta 递增因子，即加几
     * @return 增加后的scores值
     */
    public Double zincr(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * <p>ZSet类型</p>
     * 返回指定key中ZSet集合中指定元素的下标
     * @param key 缓存key
     * @param value 要获取下标的元素
     * @return 指定元素的下标
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * <p>ZSet类型</p>
     * 获取指定key的ZSet集合中指定下标区间的元素，由小到大排序
     * @param key 缓存key
     * @param start 开始下标，包含在内
     * @param end 截止下标，也包含在内
     * @return 指定下标区间内的元素
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * <p>ZSet类型</p>
     * 获取指定key的ZSet集合中指定下标区间的元素，由大到小排序
     * @param key 缓存key
     * @param start 开始下标，包含在内
     * @param end 截止下标，也包含在内
     * @return 指定下标区间内的元素
     */
    public Set<Object> zReRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * <p>ZSet类型</p>
     * 获取指定key的ZSet集合中指定下标区间的元素，并带上元素的scores
     * @param key 缓存key
     * @param start 开始下标，包含在内
     * @param end 截止下标，也包含在内
     * @return 指定下标区间内的元素
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * <p>ZSet类型</p>
     * 获取指定key的ZSet集合中指定scores的元素
     * @param key 缓存key
     * @param scores 值的scores,根据此值的大小来排序
     * @return
     */
    public Object zScores(String key, double scores) {
        return redisTemplate.opsForZSet().score(key, scores);
    }
}
