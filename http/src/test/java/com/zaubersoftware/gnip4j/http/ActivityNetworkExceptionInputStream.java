package com.zaubersoftware.gnip4j.http;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * InputStream that simulates errors 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public final class ActivityNetworkExceptionInputStream extends FilterInputStream {

    private AtomicBoolean throwException = new AtomicBoolean(false);
    
    /** Creates the ActivityExceptionInputStream. */
    public ActivityNetworkExceptionInputStream(final InputStream in) {
        super(in);
    }
    
    /** Creates the ActivityExceptionInputStream. */
    public ActivityNetworkExceptionInputStream(final String classpath) {
        super(ActivityNetworkExceptionInputStream.class.getClassLoader().getResourceAsStream(classpath));
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int ret = -1; 
        if (throwException.get()) {
            throw new IOException("Throw Exception");
        } else {
            ret = super.read(b, off, len);
        }
        return ret;
    }
    
    /** Sets the throwException. */
    public void setThrowException(final boolean throwException) {
        this.throwException.set(throwException);
    }
}