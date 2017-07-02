package com.jm.staticcode.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

public class RedisUtils {

    private JedisPool pool = null;
    private Jedis jedis;

    /**
     * <p>传入ip和端口号构建redis 连接池</p>
     * @param ip ip
     * @param prot 端口
     */
    public RedisUtils(String ip, int prot) {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            // pool = new JedisPool(config, "192.168.0.121", 6379, 100000);
            pool = new JedisPool(config, ip, prot, 100000);
        }
    }

    public Jedis getJedis(){
        jedis = pool.getResource();
        return jedis;
    }

    public <T>T  getObject(String key,Class<T> tClass) throws Exception {
        Jedis jedis  = pool.getResource();
        byte[] keyBytes = key.getBytes();
        byte[] objBytes = jedis.get(keyBytes);
        if(objBytes!=null&&objBytes.length>0){
            T t = SerializeUtil.unserialize(objBytes,tClass);
            return t;
        }
        return null;
    }

}
