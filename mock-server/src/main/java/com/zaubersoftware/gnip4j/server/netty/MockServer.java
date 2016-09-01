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
package com.zaubersoftware.gnip4j.server.netty;

import java.io.InputStream;

import com.zaubersoftware.gnip4j.server.GnipServer;
import com.zaubersoftware.gnip4j.server.GnipServerFactory;

/**
 * 
 * Implementation of Mock Server  
 * 
 * @author Ignacio De Maio
 * @since Jan 4, 2012
 */
public class MockServer {

    private static GnipServer gnipServer;


    private NettyChunkedInputFactory chunkedInputFactory;

    private int portNumber;
   
    public MockServer(int portNumber){
        this.portNumber = portNumber;
    }    
    
    void start() {
            final GnipServerFactory gnipServerFactory = new NettyGnipServerFactory();
            gnipServer = gnipServerFactory.createServer(portNumber, chunkedInputFactory);

            gnipServer.start();
            System.out.println("Gnip mock server started at port " + portNumber);
    }
    
    public void start(final InputStream activities) {
        chunkedInputFactory = new NettyChunkedInputFactory(activities);
        start();
    }
    
    public void startSlowly(final InputStream activities, final int timeBetweenChunks) {
        chunkedInputFactory = new SlowNettyChunkedInputFactory(activities, timeBetweenChunks);
        start();
    }
    
    public void startLimited(final InputStream activities, final int numberOfChunks) {
        chunkedInputFactory = new LimitedNettyChunkedInputFactory(activities, numberOfChunks);
        start();
    }
    
    public static void shutdown() {
        gnipServer.shutdown();
    }
    
}
