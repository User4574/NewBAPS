/*
 * LibraryRequestHandler.java
 * --------------------------
 * 
 * Part of the URY Backend Platform
 * 
 * V0.00  2011/03/24
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.backend.handlers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import uk.org.ury.backend.database.DatabaseDriver;
import uk.org.ury.backend.database.UserClass;
import uk.org.ury.backend.database.exceptions.ConnectionFailureException;
import uk.org.ury.backend.database.exceptions.MissingCredentialsException;
import uk.org.ury.backend.database.exceptions.QueryFailureException;
import uk.org.ury.backend.server.AbstractRequestHandler;
import uk.org.ury.backend.server.Server;
import uk.org.ury.backend.server.exceptions.BadRequestException;
import uk.org.ury.backend.server.exceptions.HandleFailureException;
import uk.org.ury.backend.server.exceptions.HandlerNotFoundException;
import uk.org.ury.backend.server.exceptions.HandlerSetupFailureException;
import uk.org.ury.backend.server.exceptions.NotAHandlerException;
import uk.org.ury.backend.server.exceptions.UnknownFunctionException;
import uk.org.ury.common.library.LibraryUtils;
import uk.org.ury.common.library.exceptions.EmptySearchException;
import uk.org.ury.common.library.item.LibraryItem;
import uk.org.ury.common.protocol.Directive;

/**
 * A request handler for library queries.
 * 
 * @author Matt Windsor
 */
public class LibraryRequestHandler extends AbstractRequestHandler {
    /**
     * Construct a new LibraryRequestHandler.
     * 
     * @param server
     *            The instance of the URY server responsible for the request.
     * 
     * @param mount
     *            The directory to which this handler is to be mounted.
     */
    public LibraryRequestHandler(Server server, String mount) {
	super(server, mount);
    }

    /**
     * Perform a library search, populating the response list.
     * 
     * @param parameters
     *            A key-value map of parameters supplied with the server
     *            request. Typically, the function parameter will detail the
     *            function that the request handler is expected to perform.
     * 
     * @param response
     *            The response list to populate.
     * 
     * @param server
     *            The server providing database resources.
     * 
     * @throws HandleFailureException
     *             if an error occurs that thwarts the handling of the request.
     */
    private void doSearch(Map<String, String> parameters,
	    Map<String, Object> response, Server server)
	    throws HandleFailureException {
	if (parameters.containsKey("search") == false)
	    throw new HandleFailureException("Search term is missing.");
	else if (parameters.get("search") == null)
	    throw new HandleFailureException("Search term is null.");

	String search = parameters.get("search");
	DatabaseDriver dd = null;

	try {
	    dd = server.getDatabaseConnection(UserClass.READ_ONLY);
	} catch (MissingCredentialsException e) {
	    throw new HandleFailureException(e.getMessage());
	} catch (ConnectionFailureException e) {
	    throw new HandleFailureException(e.getMessage());
	}

	try {
	    List<Map<String, String>> itemArray = new ArrayList<Map<String, String>>();

	    for (LibraryItem li : LibraryUtils.search(dd, search)) {
		itemArray.add(li.asResponse());
	    }

	    response.put(Directive.ITEMS.toString(), itemArray);
	} catch (QueryFailureException e) {
	    throw new HandleFailureException(e.getMessage());
	} catch (EmptySearchException e) {
	    throw new HandleFailureException(e.getMessage());
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
     * 
     * @throws UnknownFunctionException
     *             if the request is for a path that does not correspond to one
     *             of this handler's functions.
     */
    @Override
    public void handleGet(HttpRequest request, HttpResponse response,
	    HttpContext context) throws HandlerNotFoundException,
	    HandlerSetupFailureException, HandleFailureException,
	    BadRequestException, NotAHandlerException, UnknownFunctionException {
	String path;
	String uri = request.getRequestLine().getUri();
	String query = getQueryString(uri);

	if (query == null)
	    path = uri.toLowerCase(Locale.ENGLISH);
	else
	    path = uri.split("\\?" + query)[0].toLowerCase(Locale.ENGLISH);

	Map<String, Object> content = new HashMap<String, Object>();

	if (path.equals("/library/tracks")) {
	    try {
		doSearch(parseQueryString(query), content, server);
	    } catch (UnsupportedEncodingException e) {
		throw new HandleFailureException(e.getMessage());
	    }
	} else {
	    throw new UnknownFunctionException(path);
	}
	
	serveContent(request, response, content);
    }
}
