package com.refactorable.mother;

import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class ResponseMother {

    ResponseMother() {}

    public static Response statusCode100() {
        return new Response() {
            @Override
            public int getStatus() {
                return 100;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType ) {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass, Annotation[] annotations ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType, Annotation[] annotations ) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink( String s ) {
                return false;
            }

            @Override
            public Link getLink( String s ) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder( String s ) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString( String s ) {
                return null;
            }
        };
    }

    public static Response statusCode302() {
        return new Response() {
            @Override
            public int getStatus() {
                return 302;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType ) {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass, Annotation[] annotations ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType, Annotation[] annotations ) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink( String s ) {
                return false;
            }

            @Override
            public Link getLink( String s ) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder( String s ) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString( String s ) {
                return null;
            }
        };
    }

    public static Response statusCode400() {
        return new Response() {
            @Override
            public int getStatus() {
                return 400;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType ) {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass, Annotation[] annotations ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType, Annotation[] annotations ) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink( String s ) {
                return false;
            }

            @Override
            public Link getLink( String s ) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder( String s ) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString( String s ) {
                return null;
            }
        };
    }

    public static Response statusCode404() {
        return new Response() {
            @Override
            public int getStatus() {
                return 404;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType ) {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass, Annotation[] annotations ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType, Annotation[] annotations ) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink( String s ) {
                return false;
            }

            @Override
            public Link getLink( String s ) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder( String s ) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString( String s ) {
                return null;
            }
        };
    }

    public static Response statusCode500() {
        return new Response() {
            @Override
            public int getStatus() {
                return 500;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType ) {
                return null;
            }

            @Override
            public <T> T readEntity( Class<T> aClass, Annotation[] annotations ) {
                return null;
            }

            @Override
            public <T> T readEntity( GenericType<T> genericType, Annotation[] annotations ) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink( String s ) {
                return false;
            }

            @Override
            public Link getLink( String s ) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder( String s ) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString( String s ) {
                return null;
            }
        };
    }
}
