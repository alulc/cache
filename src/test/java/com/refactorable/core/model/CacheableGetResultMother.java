package com.refactorable.core.model;

import java.net.URI;
import java.util.HashMap;

public final class CacheableGetResultMother {

    CacheableGetResultMother() {}

    public static CacheableGetResult google() {
        return new CacheableGetResult(
                URI.create( "https://www.google.com" ),
                new HashMap<>(),
                "<html></html>" );
    }
}
