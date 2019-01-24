package com.refactorable.core.service;

import com.refactorable.core.model.GenericGetResult;
import com.refactorable.core.util.Gzip;
import com.refactorable.mother.GenericGetResultMother;
import net.jodah.expiringmap.ExpiringMap;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InMemoryCachingServiceTest {

    ExpiringMap<UUID, byte[]> expiringMap = ExpiringMap
            .builder()
            .maxSize( 1 )
            .variableExpiration()
            .build();

    CachingService<GenericGetResult> cachingService = new InMemoryCachingService<>( expiringMap );

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
        cachingService.add( -1, GenericGetResultMother.google() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void add_ttlGreaterThanOneYear_throwsIllegalArgumentException() {
        cachingService.add( 999999999, GenericGetResultMother.google() );
    }

    @Test
    public void add_validParameters_returnsUUID() {
        UUID id = cachingService.add( 1, GenericGetResultMother.google() );
        assertNotNull( id );
    }

    @Test
    public void add_overCapacity_returnsLastInsertedButNotFirst() {
        UUID firstId = cachingService.add( 1, GenericGetResultMother.google() );
        UUID secondId = cachingService.add( 1, GenericGetResultMother.google() );
        assertTrue( !cachingService.get( firstId, GenericGetResult.class ).isPresent() );
        assertTrue( cachingService.get( secondId, GenericGetResult.class ).isPresent() );
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
        UUID id = UUID.randomUUID();
        byte[] compressed = Gzip.compress( "test" );
        expiringMap.put( id, compressed );
        Optional<GenericGetResult> cacheableGetResult = cachingService.get( id, GenericGetResult.class );
        assertTrue( cacheableGetResult.isPresent() );
    }

    @Test
    public void get_validKey_returnCacheableGetResult() {
        UUID id = UUID.randomUUID();
        byte[] compressed = Gzip.compress( GenericGetResultMother.google() );
        expiringMap.put( id, compressed );
        Optional<GenericGetResult> cacheableGetResult = cachingService.get( id, GenericGetResult.class );
        assertTrue( cacheableGetResult.isPresent() );
    }
}