package com.refactorable.mother;

import com.refactorable.core.model.GenericGetResult;

import java.net.URI;
import java.util.HashMap;

public final class GenericGetResultMother {

    GenericGetResultMother() {
    }

    public static GenericGetResult google() {
        return new GenericGetResult(
                URI.create( "https://www.google.com" ),
                new HashMap<>(),
                new byte[]{} );
    }
}
