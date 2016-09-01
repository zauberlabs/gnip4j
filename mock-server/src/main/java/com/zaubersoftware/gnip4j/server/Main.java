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
package com.zaubersoftware.gnip4j.server;

import java.io.InputStream;

import com.zaubersoftware.gnip4j.server.netty.MockServer;

/**
 * Main entry point to start the {@link GnipServer}
 */
public final class Main {
    private static final int DEFAUL_SERVER_PORT = 8080;

    private static final InputStream ACTIVITIES = Main.class.getClassLoader().getResourceAsStream(
            "com/zaubersoftware/gnip4j/server/activity/twitter.json");

    public static void main(final String[] args) throws InterruptedException  {
               final MockServer mockServer = new MockServer(DEFAUL_SERVER_PORT);
               mockServer.start(ACTIVITIES);
    }

}
