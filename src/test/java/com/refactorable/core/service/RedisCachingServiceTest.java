package com.refactorable.core.service;

import com.refactorable.core.model.GenericGetResult;
import com.refactorable.core.util.Gzip;
import com.refactorable.mother.GenericGetResultMother;
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

    CachingService<GenericGetResult> cachingService = new RedisCachingService<>( jedisPool );

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
        cachingService.add( -1, GenericGetResultMother.google() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void add_ttlGreaterThanOneYear_throwsIllegalArgumentException() {
        cachingService.add( 999999999, GenericGetResultMother.google() );
    }

    @Test( expected = ServiceUnavailableException.class )
    public void add_redisConnectionIssue_throwsServiceUnavailableException() {
        Mockito.doThrow( new JedisException( "test" ) ).when( jedisPool ).getResource();
        cachingService.add( 1, GenericGetResultMother.google() );
    }

    @Test
    public void add_validParameters_returnsUUID() {
        Mockito.doReturn( jedis ).when( jedisPool ).getResource();
        Mockito.doReturn( transaction ).when( jedis ).multi();
        Mockito.doReturn( null ).when( transaction ).set( Mockito.any(), Mockito.any( byte[].class ) );
        Mockito.doReturn( null ).when( transaction ).expire( Mockito.any( byte[].class ), Mockito.anyInt() );
        Mockito.doReturn( null ).when( transaction ).exec();
        UUID id = cachingService.add( 1, GenericGetResultMother.google() );
        assertNotNull( id );
    }

    @Test( expected = NullPointerException.class )
    public void get_nullKey_throwsNullPointerException() {
        cachingService.get( null, GenericGetResult.class );
    }

    @Test( expected = NullPointerException.class )
    public void get_nullClass_throwsNullPointerException() {
        cachingService.get( UUID.randomUUID(), null );
    }

    @Test( expected = ClassCastException.class )
    public void get_incompatibleType_throwsClassCastException() {
        byte[] compressed = Gzip.compress( "test" );
        Mockito.doReturn( jedis ).when( jedisPool ).getResource();
        Mockito.doReturn( compressed ).when( jedis ).get( Mockito.any( byte[].class ) );
        Optional<GenericGetResult> cacheableGetResult = cachingService.get( UUID.randomUUID(), GenericGetResult.class );
        assertTrue( cacheableGetResult.isPresent() );
    }

    @Test
    public void get_validKey_returnCacheableGetResult() {
        byte[] compressed = Gzip.compress( GenericGetResultMother.google() );
        Mockito.doReturn( jedis ).when( jedisPool ).getResource();
        Mockito.doReturn( compressed ).when( jedis ).get( Mockito.any( byte[].class ) );
        Optional<GenericGetResult> cacheableGetResult = cachingService.get( UUID.randomUUID(), GenericGetResult.class );
        assertTrue( cacheableGetResult.isPresent() );
    }

    @Test
    public void closeQuietly_nullResponse_ok() {
        RedisCachingService.closeQuietly( null );
    }
}