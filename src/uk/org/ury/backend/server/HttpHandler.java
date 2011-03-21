/*
 * HttpHandler.java
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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.simple.JSONValue;

import uk.org.ury.backend.server.exceptions.BadRequestException;
import uk.org.ury.backend.server.exceptions.HandleFailureException;
import uk.org.ury.backend.server.exceptions.HandlerNotFoundException;
import uk.org.ury.backend.server.exceptions.HandlerSetupFailureException;
import uk.org.ury.backend.server.exceptions.NotAHandlerException;
import uk.org.ury.common.protocol.Directive;
import uk.org.ury.common.protocol.Status;

/**
 * @author Matt Windsor, Apache Software Foundation
 */
public class HttpHandler extends AbstractRequestHandler implements
	HttpRequestHandler {

    /**
     * Construct a new HttpHandler.
     * 
     * @param server
     *            The instance of the URY server responsible for the request.
     * 
     * @param mount
     *            The directory to which this handler is to be mounted.
     */
    public HttpHandler(Server server, String mount) {
	super(server, mount);
    }

    /**
     * Handle a HTTP GET request.
     * 
     * @param request
     *            The HTTP request.
     * 
     * @param response
     *            The response that the handler will populate during the
     *            handling of the request.
     * 
     * @param context
     *            The HTTP context.
     * 
     * @throws HandlerNotFoundException
     *             if the client requested a request handler that could not be
     *             found on the class path.
     * 
     * @throws HandlerSetupFailureException
     *             if the handler was found but could not be set up (eg does not
     *             implement appropriate interface or cannot be instantiated).
     * 
     * @throws HandleFailureException
     *             if an appropriate handler was contacted, but it failed to
     *             process the request.
     * 
     * @throws BadRequestException
     *             if the request was malformed or invalid.
     * 
     * @throws NotAHandlerException
     *             if the class requested to handle the request is not a
     *             handler.
     */
    @Override
    public void handleGet(HttpRequest request, HttpResponse response,
	    HttpContext context) throws HandlerNotFoundException,
	    HandlerSetupFailureException, HandleFailureException,
	    BadRequestException, NotAHandlerException {
	String path = request.getRequestLine().getUri();

	if (path.equals("/index.html") || path.equals("/")) {
	    // Someone's trying to get the index page!
	    // Humour them.

	    response.setStatusLine(request.getProtocolVersion(),
		    HttpStatus.SC_OK, "OK");

	    StringEntity entity = null;

	    try {
		entity = new StringEntity(Server.DOCTYPE + Server.INDEX_HTML);
	    } catch (UnsupportedEncodingException e) {
		throw new HandlerSetupFailureException("(Index page)", e);
	    }

	    entity.setContentType("text/html");

	    response.setEntity(entity);
	} else {
	    // Convert this into a URL and fan out the various parts of it.

	    URL pathURL = null;

	    try {
		pathURL = new URL("http://localhost" + path);
	    } catch (MalformedURLException e) {
		throw new BadRequestException(e);
	    }

	    String className = "uk.org.ury"
		    + pathURL.getPath().replace('/', '.');
	    System.out.println(className);
	    Class<?> newClass = null;

	    try {
		newClass = Class.forName(className);
	    } catch (ClassNotFoundException e) {
		throw new HandlerNotFoundException(className, e);
	    }

	    if (ApiRequestHandler.class.isAssignableFrom(newClass) == false)
		throw new NotAHandlerException(className);

	    String queryString = pathURL.getQuery();
	    Map<String, String> parameters;

	    try {
		parameters = parseQueryString(queryString);
	    } catch (UnsupportedEncodingException e) {
		throw new HandlerSetupFailureException(className, e);
	    }

	    Map<String, Object> content = null;

	    try {
		ApiRequestHandler srh = ((ApiRequestHandler) newClass
			.newInstance());
		content = srh.handleGetRequest(parameters, server);
	    } catch (InstantiationException e) {
		throw new HandlerSetupFailureException(className, e);
	    } catch (IllegalAccessException e) {
		throw new HandlerSetupFailureException(className, e);
	    }

	    // Everything seems OK, so make the response.

	    response.setStatusLine(request.getProtocolVersion(),
		    HttpStatus.SC_OK, "OK");

	    content.put(Directive.STATUS.toString(), Status.OK.toString());

	    StringEntity entity = null;

	    try {
		entity = new StringEntity(JSONValue.toJSONString(content));
	    } catch (UnsupportedEncodingException e) {
		throw new HandlerSetupFailureException(className, e);
	    }

	    entity.setContentType(HTTP.PLAIN_TEXT_TYPE);
	    response.setEntity(entity);
	}
    }
}
