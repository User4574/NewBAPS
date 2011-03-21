/*
 * HttpWorkerThread.java
 * ---------------------
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

package uk.org.ury.backend.server;

import java.io.IOException;
import java.io.InterruptedIOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

/**
 * A worker thread in the server HTTP interface.
 * 
 * This thread handles requests from the connected client, passing them to the
 * request handler(s).
 * 
 * @author Matt Windsor, Apache Software Foundation
 */
public class HttpWorkerThread extends Thread {
    private final HttpService service;
    private final HttpServerConnection conn;

    /**
     * Construct a new HttpWorkerThread.
     * 
     * @param service
     *            The HTTP service the thread is working for.
     * @param conn
     *            The connection the thread is listening on.
     */
    public HttpWorkerThread(HttpService service, HttpServerConnection conn) {
	super();
	this.service = service;
	this.conn = conn;
    }

    /**
     * Thread execution body.
     */
    public void run() {
	HttpContext context = new BasicHttpContext(null);

	try {
	    while (Thread.interrupted() == false && conn.isOpen()) {
		service.handleRequest(conn, context);
	    }
	} catch (ConnectionClosedException e) {
	    System.out.println("Client closed connection.");
	} catch (InterruptedIOException e) {
	    System.out.println("Interrupted IO: " + e.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (HttpException e) {
	    e.printStackTrace();
	} finally {
	    try {
		conn.shutdown();
	    } catch (IOException e) {
		// Ignore
	    }
	}
    }
}
