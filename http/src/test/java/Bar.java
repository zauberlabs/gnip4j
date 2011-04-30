/**
 * Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>
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
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.Validate;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import ar.com.zauber.commons.dao.Closure;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.http.HttpGnipFacade;

/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class Bar {

    @Test
    public void testname() throws Exception {
        final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        final String domain = System.getProperty("gnip.domain");
        
        Validate.notNull(username, "Missing gnip.username");
        Validate.notNull(password, "Missing gnip.password");
        Validate.notNull(domain, "Missing gnip.domain");
        
        try {
            final GnipFacade gnip = new HttpGnipFacade(domain);
            
            final GnipAuthentication auth = new InmutableGnipAuthentication(username, password);
            
            final GnipStream stream = gnip.createStream(1, auth);
            stream.addObserver(new Closure<Activity>() {
                private AtomicInteger i = new AtomicInteger();
                @Override
                public void execute(final Activity t) {
                    if(i.incrementAndGet() == 10) {
                        stream.close();
                    }
                }
            });
            stream.await();
            System.out.println("Shutting down");

        }   catch(Throwable t) {
            t.printStackTrace();
        }
        
        
        
    }
}
