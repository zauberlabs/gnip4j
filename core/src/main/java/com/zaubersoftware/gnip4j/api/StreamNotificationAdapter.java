/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api;

import javax.validation.constraints.NotNull;

import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * An adapter for {@link StreamNotification} that implements all the method
 * except for {@link StreamNotification#notify(Activity, GnipStream)} by doing 
 * nothing.
 * 
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public abstract class StreamNotificationAdapter implements StreamNotification {

    @Override
    public void notifyConnectionError(@NotNull final TransportGnipException e) {
        // Do nothing
    }

    @Override
    public void notifyReConnectionError(@NotNull final GnipException e) {
        // Do nothing
    }

    @Override
    public void notifyReConnection(final int attempt, final long waitTime) {
        // Do nothing
    }

}
