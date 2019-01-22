package com.refactorable.mother;

import javax.ws.rs.core.*;

public final class ResponseMother {

    ResponseMother() {}

    public static Response statusCode100() {
        return Response.status( 100 ).build();
    }

    public static Response statusCode302() {
        return Response.status( 302 ).build();
    }

    public static Response statusCode400() {
        return Response.status( 400 ).build();
    }

    public static Response statusCode404() {
        return Response.status( 404 ).build();
    }

    public static Response statusCode500() {
        return Response.status( 500 ).build();
    }
}
