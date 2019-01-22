package com.refactorable.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Gzip {

    private static final Logger LOGGER = LoggerFactory.getLogger( Gzip.class );

    Gzip() {}

    public static <T> T decompress(
            byte[] compressed,
            Class<T> clazz ) {

        Validate.notNull( compressed );
        Validate.notNull( clazz );

        try(
            ByteArrayInputStream bais = new ByteArrayInputStream( compressed );
            GZIPInputStream gzipIn = new GZIPInputStream( bais );
            ObjectInputStream objectIn = new ObjectInputStream( gzipIn )
        ) {
            T decompressed = clazz.cast( objectIn.readObject() );
            LOGGER.debug( "'{}' decompressed", clazz.getSimpleName() );
            return decompressed;
        } catch( Exception e ) {
            LOGGER.error( "failed to decompress '{}'", clazz.getSimpleName(), e );
            throw new InternalServerErrorException( String.format( "failed to decompress '%s'", clazz.getSimpleName() ) );
        }
    }

    public static <T extends Serializable> byte[] compress( T target ) {

        Validate.notNull( target );

        try(
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzipOut = new GZIPOutputStream( baos );
            ObjectOutputStream objectOut = new ObjectOutputStream( gzipOut )
        ) {
            objectOut.writeObject( target );
            objectOut.close(); // <- required
            byte[] compressed = baos.toByteArray();
            LOGGER.debug( "compressed to '{}' bytes", compressed.length );
            return compressed;
        } catch( Exception e ) {
            LOGGER.error( "failed to compress '{}'", target.getClass().getSimpleName(), e );
            throw new InternalServerErrorException( String.format( "failed to compress '%s'", target.getClass().getSimpleName() ) );
        }
    }
}
