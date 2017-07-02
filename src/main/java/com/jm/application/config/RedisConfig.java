package com.jm.application.config;

import org.springframework.context.annotation.Configuration;

/*
 * <p></p>
 *
 * @author wukf
 * @version 1.1
 * @date 2017-01-20
*/

//@Configuration
//@EnableRedisHttpSession
public class RedisConfig {

    /*@Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.database}")
    private Integer redisDatabase;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setTestOnBorrow(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        //jedisConnectionFactory.setDatabase(redisDatabase);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new StringRedisTemplate(connectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }*/


}
