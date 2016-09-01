/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zaubersoftware.gnip4j.api.impl.formats.Unmarshaller;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Juan F. Codagnone
 * @since Oct 3, 2013
 */
public abstract class EDCStreamBuilder {
    protected Long dataCollector;
    protected Unmarshaller unmarshaller; 
    protected boolean activity = true;
    protected Format format;
    
    private final int streamDefaultWorkers = Runtime.getRuntime().availableProcessors();
    protected ExecutorService executorService = Executors.newFixedThreadPool(streamDefaultWorkers);
    protected String account;
    protected StreamNotification observer;
    
    /**  if your EDC URL starts with http://acme.gnip.com  then this value is acme*/
    public final EDCStreamBuilder withAccount(final String account) {
        this.account = account;
        return this;
    }
    
    public final EDCStreamBuilder withObserver(final StreamNotification observer) {
        this.observer = observer;
        return this;
    }
    
    public final EDCStreamBuilder withExecutorService(final ExecutorService executor) {
        this.executorService = executor;
        return this;
    }

    public final EDCStreamBuilder withDataCollector(final long dataCollector) {
        this.dataCollector = dataCollector;
        return this;
    }
    
    
    public enum Format {
        JSON,
        ATOM,
    }
    
    public final EDCStreamBuilder  andFormatIsLeavingDataInItsOriginalFormat(
            final Format format, 
            final Unmarshaller unmarshaller) {
        activity = false;
        this.format = format;
        this.unmarshaller = unmarshaller;
        return this;
    }
    
    public EDCStreamBuilder andFormatNormalizeToActivity() {
        activity = true;
        return this;
    }
    
    /** build the stream*/
    public GnipStream build() {
        if(account == null) {
            throw new IllegalArgumentException("you must set the account");
        }
        
        if(dataCollector == null) {
            throw new IllegalArgumentException("you must set the dataCollector");
        }
        
        if(observer == null) {
            throw new IllegalArgumentException("you must set the observer");
        }
        
        return buildStream();
    }

    protected abstract GnipStream buildStream();
}
