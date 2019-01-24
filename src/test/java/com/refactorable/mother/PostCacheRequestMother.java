package com.refactorable.mother;

import com.refactorable.api.PostCacheRequest;

public final class PostCacheRequestMother {

    PostCacheRequestMother() {
    }

    public static PostCacheRequest googleAndTtlOneMinute() {
        return new PostCacheRequest( 1, "https://www.google.com" );
    }

    public static PostCacheRequest badUrlAndTtlOneMinute() {
        return new PostCacheRequest( 1, "https://www.jndgpagipubgwpubg.com" );
    }

    public static PostCacheRequest invalidUrlAndTtlOneMinute() {
        return new PostCacheRequest( 1, "@#^#&%*$(fsdhjapojhrf}{|:L~`" );
    }

    public static PostCacheRequest blankUrlAndTtlOneMinute() {
        return new PostCacheRequest( 1, "" );
    }

    public static PostCacheRequest nullUrlAndTtlOneMinute() {
        return new PostCacheRequest( 1, null );
    }

    public static PostCacheRequest googleAndTtllessThanOneMinute() {
        return new PostCacheRequest( 0, "https://www.google.com" );
    }

    public static PostCacheRequest googleAndTtlGreaterThanOneYear() {
        return new PostCacheRequest( 999999999, "https://www.google.com" );
    }
}
