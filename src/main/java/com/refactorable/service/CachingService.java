package com.refactorable.service;

import com.refactorable.core.CacheableGetResult;

import java.util.Optional;
import java.util.UUID;

public interface CachingService {

    /**
     *
     * @param ttlInMinutes cannot be less than 1
     * @param cacheableGetResult cannot be null
     *
     * @return id of cached resource
     *
     */
    UUID add( int ttlInMinutes, CacheableGetResult cacheableGetResult );

    /**
     *
     * @param key cannot be null
     *
     * @return cached resource
     *
     */
    Optional<CacheableGetResult> get( UUID key );
}
