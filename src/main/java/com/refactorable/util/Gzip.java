package com.refactorable.util;

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

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream( compressed );
            GZIPInputStream gzipIn = new GZIPInputStream( bais );
            ObjectInputStream objectIn = new ObjectInputStream( gzipIn );
            T decompressed = clazz.cast( objectIn.readObject() );
            objectIn.close();
            return decompressed;
        } catch( Exception e ) {
            throw new InternalServerErrorException( String.format( "failed to decompress '%s'", clazz.getSimpleName() ) );
        }
    }

    public static <T extends Serializable> byte[] compress( T target ) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzipOut = new GZIPOutputStream( baos );
            ObjectOutputStream objectOut = new ObjectOutputStream( gzipOut );
            objectOut.writeObject( target );
            objectOut.close();
            byte[] compressed = baos.toByteArray();
            LOGGER.debug( "compressed to '{}' bytes", compressed.length );
            return compressed;
        } catch( Exception e ) {
            throw new InternalServerErrorException( String.format( "failed to compress '%s'", target.getClass().getSimpleName() ) );
        }
    }
}
