package com.refactorable.core.model;

import com.refactorable.rs.BadGatewayException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  Behaviorally rich domain model, see https://www.martinfowler.com/bliki/AnemicDomainModel.html
 */
public class CacheableGetResult implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger( CacheableGetResult.class );
    private static final long serialVersionUID = -1485513695846146366L;

    // thread safe
    static Client CLIENT = ClientBuilder.newClient();

    private final URI uri;
    private final Map<String, String> headers;
    private final String body;

    /**
     *
     * @param uri cannot be null
     * @param headers cannot be null
     * @param body cannot be null
     */
    CacheableGetResult(
            URI uri,
            Map<String, String> headers,
            String body ) {
        this.uri = Validate.notNull( uri );
        this.headers = Validate.notNull( headers );
        this.body = Validate.notNull( body );
    }

    /**
     * Attempts to construct {@link CacheableGetResult} using the {@link URI} provided
     * by making a GET call to the {@link URI}.
     *
     * @param uri cannot be null
     *
     * @throws BadGatewayException if a request to the upstream server does not result in 2XX
     *
     * @return {@link CacheableGetResult} representation of result from a GET call to the {@link URI}.
     */
    public static CacheableGetResult fromUri( URI uri ) {

        Validate.notNull( uri );

        Response response = null;
        try {
            response = CLIENT.target( uri ).request().get();
            if( response.getStatus() < 200 || response.getStatus() >= 300 ) {
                // treat any request that doesn't result in a 2XX (Successful) as an issue with the upstream server.
                LOGGER.warn( "received '{}' status GET '{}'", response.getStatus(), uri );
                throw new BadGatewayException();
            }
            return new CacheableGetResult(
                    uri,
                    multivaluedMapToMap( response.getStringHeaders() ),
                    response.readEntity( String.class ) );
        } catch( ProcessingException pe ) {
            LOGGER.warn( "issue connecting to '{}'", uri, pe );
            throw new BadGatewayException();
        } finally {
            closeQuietly( response );
        }
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    /**
     *
     * {@link MultivaluedMap} is not serializable so we need to convert is to a {@link Map}.
     *
     * If during conversion a header has multiple values, they are combined into
     * a String that is comma delimited.
     *
     * @param multivaluedMap cannot be null
     *
     * @return {@link Map} representation of {@link MultivaluedMap}
     */
    static Map<String, String> multivaluedMapToMap( MultivaluedMap<String, String> multivaluedMap ) {

        Validate.notNull( multivaluedMap );

        Map<String, String> map = new HashMap<>( multivaluedMap.size() );
        for( Map.Entry<String, List<String>> entry : multivaluedMap.entrySet() ) {
            map.put( entry.getKey(), String.join( ",", entry.getValue() ) );
        }
        LOGGER.debug( "{} to {}", multivaluedMap, map );
        return map;
    }

    static void closeQuietly( Response response ) {
        if( response != null ) {
            LOGGER.debug( "closing response connection" );
            try {
                response.close();
                LOGGER.debug( "response connection closed" );
            } catch( Exception e ) {
                LOGGER.warn( "failed to close response connection!", e );
            }
        }
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        CacheableGetResult that = ( CacheableGetResult ) o;
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
        return "CacheableGetResult{" +
                "uri=" + uri +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
