package com.refactorable.core.service;

import com.refactorable.core.util.Gzip;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import javax.ws.rs.ServiceUnavailableException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public class RedisCachingService<T extends Serializable> implements CachingService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger( RedisCachingService.class );

    private final JedisPool jedisPool;

    /**
     *
     * @param jedisPool cannot be null
     */
    public RedisCachingService( JedisPool jedisPool ) {
        LOGGER.info( "creating {}!", this.getClass().getSimpleName() );
        this.jedisPool = Validate.notNull( jedisPool );
    }

    @Override
    public UUID add(
            int ttlInMinutes,
            T target ) {

        Validate.isTrue( ttlInMinutes > 0 && ttlInMinutes <= 525600 );
        Validate.notNull( target );

        Jedis jedis = null;
        UUID key = UUID.randomUUID();
        byte[] keyAsBytes = key.toString().getBytes();
        try {
            jedis = jedisPool.getResource();
            Transaction transaction = jedis.multi();
            transaction.set( keyAsBytes, Gzip.compress( target ) );
            transaction.expire( keyAsBytes, ttlInMinutes * 60 );
            transaction.exec();
            return key;
        } catch( JedisException je ) {
            LOGGER.error( "failed to connect to redis", je );
            throw new ServiceUnavailableException();
        } finally {
            closeQuietly( jedis );
        }
    }

    @Override
    public Optional<T> get(
            UUID key,
            Class<T> clazz ) {

        Validate.notNull( key );
        Validate.notNull( clazz );

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] compressed = jedis.get( key.toString().getBytes() );
            if( compressed == null ) return Optional.empty();
            T result = Gzip.decompress( compressed, clazz );
            return Optional.of( result );
        } catch( JedisException je ) {
            LOGGER.error( "failed to connect to redis", je );
            throw new ServiceUnavailableException();
        } finally {
            closeQuietly( jedis );
        }
    }

    static void closeQuietly( Jedis jedis ) {
        if( jedis != null ) {
            LOGGER.debug( "closing redis connection" );
            try {
                jedis.close();
                LOGGER.debug( "redis connection closed" );
            } catch( Exception e ) {
                LOGGER.warn( "failed to close redis connection!", e );
            }
        }
    }
}
