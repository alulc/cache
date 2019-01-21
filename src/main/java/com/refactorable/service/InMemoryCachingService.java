package com.refactorable.service;

import com.refactorable.core.CacheableGetResult;
import com.refactorable.util.Gzip;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InMemoryCachingService implements CachingService {

    private static final Logger LOGGER = LoggerFactory.getLogger( InMemoryCachingService.class );
    private static final int DEFAULT_CACHE_SIZE = 10000;

    private final ExpiringMap<UUID, byte[]> expiringMap;

    /**
     *
     * @param expiringMap cannot be null
     */
    public InMemoryCachingService( ExpiringMap<UUID, byte[]> expiringMap ) {
        this.expiringMap = Validate.notNull( expiringMap );
    }

    public InMemoryCachingService() {
        this( ExpiringMap
                .builder()
                .maxSize( DEFAULT_CACHE_SIZE )
                .variableExpiration()
                .build() );
    }

    @Override
    public UUID add(
            int ttlInMinutes,
            CacheableGetResult cacheableGetResult ) {

        Validate.isTrue( ttlInMinutes > 0 );
        Validate.notNull( cacheableGetResult );

        UUID key = UUID.randomUUID();
        expiringMap.put( key, Gzip.compress( cacheableGetResult ), ttlInMinutes, TimeUnit.MINUTES );
        return key;
    }

    @Override
    public Optional<CacheableGetResult> get( UUID key ) {

        Validate.notNull( key );

        byte[] compressed = expiringMap.get( key );
        if( compressed == null ) return Optional.empty();
        CacheableGetResult cacheableGetResult = Gzip.decompress( compressed, CacheableGetResult.class );
        return Optional.of( cacheableGetResult );
    }
}
