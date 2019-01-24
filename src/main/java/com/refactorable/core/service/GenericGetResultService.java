package com.refactorable.core.service;

import com.refactorable.core.model.GenericGetResult;
import com.refactorable.rs.BadGatewayException;

import java.net.URI;

public interface GenericGetResultService {

    /**
     * Attempts to construct {@link GenericGetResult} using the {@link URI} provided
     * by making a GET call to the {@link URI}.
     *
     * @param uri cannot be null
     * @return {@link GenericGetResult} representation of result from a GET call to the {@link URI}.
     * @throws BadGatewayException if a request to the upstream server does not result in 2XX
     */
    GenericGetResult get( URI uri );
}
