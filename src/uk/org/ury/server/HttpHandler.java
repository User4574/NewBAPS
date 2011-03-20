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

package uk.org.ury.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.simple.JSONValue;

import uk.org.ury.protocol.Directive;
import uk.org.ury.protocol.Status;
import uk.org.ury.server.exceptions.BadRequestException;
import uk.org.ury.server.exceptions.HandleFailureException;
import uk.org.ury.server.exceptions.HandlerNotFoundException;
import uk.org.ury.server.exceptions.HandlerSetupFailureException;
import uk.org.ury.server.exceptions.HandlingException;
import uk.org.ury.server.exceptions.NotAHandlerException;

/**
 * @author Matt Windsor, Apache Software Foundation
 */
public class HttpHandler implements HttpRequestHandler {
    private Server server;

    /**
     * Construct a new HttpHandler.
     * 
     * @param server
     *            The server whose HTTP requests are to be handled by this
     *            handler.
     */
    public HttpHandler(Server server) {
	this.server = server;
    }

    /**
     * Handle a HTTP request.
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
     */
    @Override
    public void handle(HttpRequest request, HttpResponse response,
	    HttpContext context) throws HttpException, IOException {
	String method = request.getRequestLine().getMethod()
		.toUpperCase(Locale.ENGLISH);

	if (method.equals("GET")) {
	    try {
		handleGet(request, response, context);
	    } catch (HandlerNotFoundException e) {
		// TODO: log
		serveError(request, response, HttpStatus.SC_NOT_FOUND,
			e.getMessage());
	    } catch (BadRequestException e) {
		// TODO: log
		serveError(request, response, HttpStatus.SC_BAD_REQUEST,
			e.getMessage());
	    } catch (HandlingException e) {
		serveError(request, response,
			HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	    }
	} else {
	    serveError(request, response, HttpStatus.SC_NOT_IMPLEMENTED,
		    "Method not implemented.");
	}
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

    /**
     * Serve a HTTP plain-text error as the HTTP response for a request.
     * 
     * @param request
     *            The request that is being responded to.
     * 
     * @param response
     *            The response to populate with the error message.
     * 
     * @param code
     *            HTTP status code to use.
     * 
     * @param reason
     *            The reason to display to the client.
     */
    private void serveError(HttpRequest request, HttpResponse response,
	    int code, String reason) {
	// Get the reason string to put in the error response.
	String statusReason = "";

	switch (code) {
	case HttpStatus.SC_BAD_REQUEST:
	    statusReason = "Bad Request";
	    break;
	case HttpStatus.SC_NOT_FOUND:
	    statusReason = "Not Found";
	    break;
	default:
	case HttpStatus.SC_INTERNAL_SERVER_ERROR:
	    statusReason = "Internal Server Error";
	    break;
	}

	response.setStatusLine(request.getProtocolVersion(), code, statusReason);
	StringEntity entity = null;

	try {
	    Map<String, Object> content = new HashMap<String, Object>();

	    content.put(Directive.STATUS.toString(), Status.ERROR.toString());
	    content.put(Directive.REASON.toString(), reason);

	    entity = new StringEntity(JSONValue.toJSONString(content));
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (entity != null) {
	    entity.setContentType(HTTP.PLAIN_TEXT_TYPE);
	    response.setEntity(entity);
	}
    }

    /**
     * Parse a query string, populating a key-value map of the URL-unescaped
     * results.
     * 
     * @param query
     *            The query string to parse.
     * 
     * @return A map associating parameter keys and values.
     * 
     * @throws UnsupportedEncodingException
     *             if the URL decoder fails.
     */
    public Map<String, String> parseQueryString(String query)
	    throws UnsupportedEncodingException {
	Map<String, String> params = new HashMap<String, String>();

	// At least one parameter
	if (query != null && query.endsWith("&") == false) {
	    String[] qsplit = { query };

	    // More than one parameter - split the query.
	    if (query.contains("&"))
		qsplit = query.split("&");

	    for (String param : qsplit) {
		// Has a value
		if (param.contains("=") && param.endsWith("=") == false) {
		    String[] paramsplit = param.split("=");
		    params.put(URLDecoder.decode(paramsplit[0], "UTF-8"),
			    URLDecoder.decode(paramsplit[1], "UTF-8"));
		}
		// Doesn't have a value
		else if (param.contains("=") == false) {
		    params.put(URLDecoder.decode(param, "UTF-8"), null);
		}
	    }
	}

	return params;
    }
}
