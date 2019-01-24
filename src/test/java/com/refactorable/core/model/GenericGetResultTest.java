package com.refactorable.core.model;

import org.junit.Test;

import java.net.URI;
import java.util.HashMap;

public class GenericGetResultTest {

    @Test( expected = NullPointerException.class )
    public void constructor_nullUri_throwsNullPointerException() {
        new GenericGetResult(
                null,
                new HashMap<>(),
                new byte[]{} );
    }

    @Test( expected = NullPointerException.class )
    public void constructor_nullHeaders_throwsNullPointerException() {
        new GenericGetResult(
                URI.create( "http://www.google.com" ),
                null,
                new byte[]{} );
    }

    @Test( expected = NullPointerException.class )
    public void constructor_nullBody_throwsNullPointerException() {
        new GenericGetResult(
                URI.create( "http://www.google.com" ),
                new HashMap<>(),
                null );
    }
}