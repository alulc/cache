package com.refactorable.core.service;

import com.refactorable.core.model.CacheableGetResult;

import java.util.Optional;
import java.util.UUID;

public interface CachingService {

    /**
     *
     * @param ttlInMinutes cannot be less than 1 and must be less than or equal to 525600 (1 year)
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
