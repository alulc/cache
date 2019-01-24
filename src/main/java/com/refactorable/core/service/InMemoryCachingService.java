package com.refactorable.core.service;

import com.refactorable.core.util.Gzip;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InMemoryCachingService<T extends Serializable> implements CachingService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger( InMemoryCachingService.class );
    private static final int DEFAULT_CACHE_SIZE = 1000;

    private final ExpiringMap<UUID, byte[]> expiringMap;

    /**
     * @param expiringMap cannot be null
     */
    public InMemoryCachingService( ExpiringMap<UUID, byte[]> expiringMap ) {
        LOGGER.info( "creating {}!", this.getClass().getSimpleName() );
        this.expiringMap = Validate.notNull( expiringMap );
    }

    public InMemoryCachingService() {
        this( ExpiringMap
                .builder()
                .maxSize( DEFAULT_CACHE_SIZE )
                .expirationPolicy( ExpirationPolicy.CREATED )
                .variableExpiration()
                .build() );
    }

    @Override
    public UUID add(
            int ttlInMinutes,
            T target ) {

        Validate.isTrue( ttlInMinutes > 0 && ttlInMinutes <= 525600 );
        Validate.notNull( target );

        UUID key = UUID.randomUUID();
        expiringMap.put( key, Gzip.compress( target ), ttlInMinutes, TimeUnit.MINUTES );
        return key;
    }

    @Override
    public Optional<T> get(
            UUID key,
            Class<T> clazz ) {

        Validate.notNull( key );
        Validate.notNull( clazz );

        byte[] compressed = expiringMap.get( key );
        if( compressed == null ) return Optional.empty();
        T result = Gzip.decompress( compressed, clazz );
        return Optional.of( result );
    }
}
