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

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.model.Activity;


/**
 * Test for {@link StreamNotification}
 *
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class StreamNotificationAdapterTest {

    /** nothing to do! */
    @Test
    public final void testNull() {
        final StreamNotification<Activity> n = new StreamNotificationAdapter<Activity>() {
            @Override
            public void notify(final Activity activity, final GnipStream stream) {
            }
        };
        n.notifyConnectionError(null);
        n.notifyReConnectionError(null);
        n.notifyReConnectionAttempt(-1, -1000);
    }
}
