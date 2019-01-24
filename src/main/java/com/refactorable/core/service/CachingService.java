package com.refactorable.core.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public interface CachingService<T extends Serializable> {

    /**
     *
     * @param ttlInMinutes cannot be less than 1 and must be less than or equal to 525600 (1 year)
     * @param target cannot be null
     *
     * @return id of cached resource
     *
     */
    UUID add( int ttlInMinutes, T target );

    /**
     *
     * @param key cannot be null
     *
     * @throws ClassCastException if the {@link Class} supplied can not be cast from the cached resource
     *
     * @return cached resource
     *
     */
    Optional<T> get( UUID key, Class<T> clazz );
}
