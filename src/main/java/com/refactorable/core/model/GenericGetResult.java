package com.refactorable.core.model;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class GenericGetResult implements Serializable {

    private static final long serialVersionUID = -1485513695846146366L;

    private final URI uri;
    private final Map<String, String> headers;
    private final byte[] body;

    /**
     * @param uri cannot be null
     * @param headers cannot be null
     * @param body cannot be null
     */
    public GenericGetResult(
            URI uri,
            Map<String, String> headers,
            byte[] body ) {
        this.uri = Validate.notNull( uri );
        this.headers = Validate.notNull( headers );
        this.body = Validate.notNull( body );
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        GenericGetResult that = ( GenericGetResult ) o;
        return Objects.equals( uri, that.uri ) &&
                Objects.equals( headers, that.headers ) &&
                Objects.equals( body, that.body );
    }

    @Override
    public int hashCode() {

        return Objects.hash( uri, headers, body );
    }

    @Override
    public String toString() {
        return "GenericGetResult{" +
                "uri=" + uri +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
