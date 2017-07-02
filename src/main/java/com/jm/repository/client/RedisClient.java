package com.jm.repository.client;

import com.jm.repository.client.dto.auth.AccessToken;
import com.jm.repository.client.dto.auth.AuthAccessToken;
import com.jm.repository.client.dto.auth.AuthInfo;
import com.jm.repository.client.dto.auth.CommonAccessToken;
import com.jm.staticcode.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

/**
 * <p>Redis Client</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
@Slf4j
@Repository
public class RedisClient {

    @Autowired
    public StringRedisTemplate redisTemplate;

    private Jackson2JsonRedisSerializer<AccessToken> jacksonJsonRedisSerializer = new Jackson2JsonRedisSerializer<>(AccessToken.class);

    private Jackson2JsonRedisSerializer<CommonAccessToken> commonAccessTokenSerializer = new Jackson2JsonRedisSerializer<>(CommonAccessToken.class);

    private Jackson2JsonRedisSerializer<AuthAccessToken> accessTokenSerializer = new Jackson2JsonRedisSerializer<>(AuthAccessToken.class);


    /**
     * 保存commonAccessToken
     * @param commonAppId
     * @param commonAccessToken
     */
    public void setCommonAccessToken(final String commonAppId, final CommonAccessToken commonAccessToken) {
        try {
            redisTemplate.execute(new RedisCallback<CommonAccessToken>() {
                @Override
                public CommonAccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(getAuthKey(commonAppId), commonAccessTokenSerializer.serialize(commonAccessToken));
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Set redis commonAccessToken error!", e);
        }
    }

    /**
     * 获取commonAccessToken
     * @param commonAppId
     * @return
     */
    public CommonAccessToken getCommonAccessToken(final String commonAppId) {
        CommonAccessToken accessToken = null;
        try {
            accessToken = (CommonAccessToken) redisTemplate.execute(new RedisCallback<CommonAccessToken>() {
                @Override
                public CommonAccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] value = connection.get(getAuthKey(commonAppId));
                    return commonAccessTokenSerializer.deserialize(value);
                }
            });
        } catch (Exception e) {
            log.error("Get redis accessToken error!", e);
        }
        return accessToken;
    }

    /**
     * 保存第三方平台获取某个公众号的AccessToken
     * @param accessToken
     */
    public void setAuthAccessToken(final AuthAccessToken accessToken) {
        try {
            redisTemplate.execute(new RedisCallback<AuthAccessToken>() {
                @Override
                public AuthAccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(getAuthAccessTokenKey(accessToken.getAuthorizerAppid()), accessTokenSerializer.serialize(accessToken));
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Set redis accessToken error!", e);
        }
    }

    /**
     * 获取第三方平台获取某个公众号的AccessToken
     * @param appId
     * @return
     */
    public AuthAccessToken getAuthAccessToken(final String appId) {
        AuthAccessToken accessToken = null;
        try {
            accessToken = (AuthAccessToken) redisTemplate.execute(new RedisCallback<AuthAccessToken>() {
                @Override
                public AuthAccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] value = connection.get(getAuthAccessTokenKey(appId));
                    return accessTokenSerializer.deserialize(value);
                }
            });
        } catch (Exception e) {
            log.error("Set redis accessToken error!", e);
        }
        return accessToken;
    }

    /**
     * 获取公众号AccessToken
     * @param appId
     * @return
     */
    public AccessToken getAccessToken(final String appId) {
        AccessToken accessToken = null;
        try {
            accessToken = (AccessToken) redisTemplate.execute(new RedisCallback<AccessToken>() {
                @Override
                public AccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] value = connection.get(getAuthKey(appId));
                    return jacksonJsonRedisSerializer.deserialize(value);
                }
            });
        } catch (Exception e) {
            log.error("Get redis accessToken error!", e);
        }
        return accessToken;
    }

    /**
     * 保存公众号AccessToken
     * @param appId
     * @param accessToken
     */
    public void setAccessToken(final String appId, final AccessToken accessToken) {
        try {
            redisTemplate.execute(new RedisCallback<AccessToken>() {
                @Override
                public AccessToken doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(getAuthKey(appId), jacksonJsonRedisSerializer.serialize(accessToken));
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Set redis accessToken error!", e);
        }
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置redis值
     * @param key
     * @param value
     */
    public void setValue(final String key, final String value) {
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(key.getBytes(),value.getBytes());
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Set redis value error!", e);
        }
    }

    /**
     * 获取redis值
     * @param key
     * @return
     */
    public String getValue(final String key) {
        String str = null;
        try {
            str = (String) redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] value = connection.get(key.getBytes());
                    return new String(value);
                }
            });
        } catch (Exception e) {
            log.error("Get redis value error!", e);
        }
        return str;
    }

    private byte[] getAuthKey(String key){
        return redisTemplate.getStringSerializer().serialize(Constant.REDIS_AUTH+":"+key);
    }

    private byte[] getAuthAccessTokenKey(String key){
        return redisTemplate.getStringSerializer().serialize(Constant.REDIS_AUTH+":"+Constant.ACCESS_TOKEN+":"+key);
    }

    private byte[] getAuthCodeKey(String key){
        return redisTemplate.getStringSerializer().serialize(Constant.REDIS_AUTH+":"+Constant.AUTH_CODE+":"+key);
    }

}
