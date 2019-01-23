package com.refactorable.core.service;

import com.refactorable.core.model.CacheableGetResult;
import com.refactorable.core.model.CacheableGetResultMother;
import com.refactorable.core.util.Gzip;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import javax.ws.rs.ServiceUnavailableException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RedisCachingServiceTest {

    JedisPool jedisPool = Mockito.mock( JedisPool.class );
    Jedis jedis = Mockito.mock( Jedis.class );
    Transaction transaction = Mockito.mock( Transaction.class );

    CachingService cachingService = new RedisCachingService( jedisPool );

    @Test( expected = NullPointerException.class )
    public void constructor_nullJedisPool_throwsNullPointerException() {
        new RedisCachingService( null );
    }

    @Test( expected = NullPointerException.class )
    public void add_nullCacheableGetResult_throwsNullPointerException() {
        cachingService.add( 1, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void add_ttlLessThanOne_throwsIllegalArgumentException() {
        cachingService.add( -1, CacheableGetResultMother.google() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void add_ttlGreaterThanOneYear_throwsIllegalArgumentException() {
        cachingService.add( 999999999, CacheableGetResultMother.google() );
    }

    @Test( expected = ServiceUnavailableException.class )
    public void add_redisConnectionIssue_throwsServiceUnavailableException() {
        Mockito.doThrow( new JedisException( "test" ) ).when( jedisPool ).getResource();
        cachingService.add( 1, CacheableGetResultMother.google() );
    }

    @Test
    public void add_validParameters_returnsUUID() {
        Mockito.doReturn( jedis ).when( jedisPool ).getResource();
        Mockito.doReturn( transaction ).when( jedis ).multi();
        Mockito.doReturn( null ).when( transaction ).set( Mockito.any(), Mockito.any( byte[].class ) );
        Mockito.doReturn( null ).when( transaction ).expire( Mockito.any( byte[].class ), Mockito.anyInt() );
        Mockito.doReturn( null ).when( transaction ).exec();
        UUID id = cachingService.add( 1, CacheableGetResultMother.google() );
        assertNotNull( id );
    }

    @Test( expected = NullPointerException.class )
    public void get_nullKey_throwsNullPointerException() {
        cachingService.get( null );
    }

    @Test
    public void get_validKey_returnCacheableGetResult() {
        byte[] compressed = Gzip.compress( CacheableGetResultMother.google() );
        Mockito.doReturn( jedis ).when( jedisPool ).getResource();
        Mockito.doReturn( compressed ).when( jedis ).get( Mockito.any( byte[].class ) );
        Optional<CacheableGetResult> cacheableGetResult = cachingService.get( UUID.randomUUID() );
        assertTrue( cacheableGetResult.isPresent() );
    }

    @Test
    public void closeQuietly_nullResponse_ok() {
        RedisCachingService.closeQuietly( null );
    }
}