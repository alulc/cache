package com.refactorable.rs;

import javax.ws.rs.WebApplicationException;

public class BadGatewayException extends WebApplicationException {

    public BadGatewayException() {
        super( 502 );
    }
}
