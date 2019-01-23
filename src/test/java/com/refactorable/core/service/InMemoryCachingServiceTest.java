package com.refactorable.core.service;

import com.refactorable.core.model.CacheableGetResult;
import com.refactorable.core.model.CacheableGetResultMother;
import com.refactorable.core.util.Gzip;
import net.jodah.expiringmap.ExpiringMap;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InMemoryCachingServiceTest {

    ExpiringMap<UUID, byte[]> expiringMap = ExpiringMap.builder().variableExpiration().build();
    CachingService cachingService = new InMemoryCachingService( expiringMap );

    @Test( expected = NullPointerException.class )
    public void constructor_nullJedisPool_throwsNullPointerException() {
        new InMemoryCachingService( null );
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

    @Test
    public void add_validParameters_returnsUUID() {
        UUID id = cachingService.add( 1, CacheableGetResultMother.google() );
        assertNotNull( id );
    }

    @Test( expected = NullPointerException.class )
    public void get_nullKey_throwsNullPointerException() {
        cachingService.get( null );
    }

    @Test
    public void get_validKey_returnCacheableGetResult() {
        UUID id = UUID.randomUUID();
        byte[] compressed = Gzip.compress( CacheableGetResultMother.google() );
        expiringMap.put( id, compressed );
        Optional<CacheableGetResult> cacheableGetResult = cachingService.get( id );
        assertTrue( cacheableGetResult.isPresent() );
    }
}