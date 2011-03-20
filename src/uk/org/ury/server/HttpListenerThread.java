/*
 * HttpListenerThread.java
 * -----------------------
 * 
 * Part of the URY Server Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 * 
 * Based on the HttpCore example code, which is available under the 
 * Apache License, version 2.0; the copyright notice provided with 
 * said code follows.
 * 
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package uk.org.ury.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

/**
 * Listener thread for the URY server HTTP interface.
 * 
 * @author Matt Windsor, Apache Software Foundation
 */
public class HttpListenerThread extends Thread {
    private ServerSocket ssocket;
    private HttpParams params;
    private HttpProcessor httpproc;
    private HttpRequestHandlerRegistry registry;
    private HttpService service;

    public HttpListenerThread(int port, Server server) throws IOException {
	ssocket = new ServerSocket(port);
	params = new SyncBasicHttpParams();
	
        params
        .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
        .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
        .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
        .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
        .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

        httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
            new ResponseDate(),
            new ResponseServer(),
            new ResponseContent(),
            new ResponseConnControl()
        });
        
	registry = new HttpRequestHandlerRegistry();
	registry.register("*", new HttpHandler(server));

	service = new HttpService(httpproc,
		new DefaultConnectionReuseStrategy(),
		new DefaultHttpResponseFactory(), registry, params);
    }

    /**
     * Thread execution body.
     */
    @Override
    public void run() {
	while (Thread.interrupted() == false) {
	    Socket csocket = null;
	    DefaultHttpServerConnection conn 
	            = new DefaultHttpServerConnection();
	    Thread thread = null;

	    try {
		csocket = ssocket.accept();
		conn.bind(csocket, params);
	    } catch (IOException e) {
		e.printStackTrace();
		break;
	    }
	    
	    thread = new HttpWorkerThread(service, conn);
	    thread.setDaemon(true);
	    thread.start();
	}
    }
}
