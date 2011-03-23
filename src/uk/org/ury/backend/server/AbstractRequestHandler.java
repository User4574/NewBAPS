/**
 * 
 */
package uk.org.ury.backend.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import uk.org.ury.backend.server.exceptions.BadRequestException;
import uk.org.ury.backend.server.exceptions.HandleFailureException;
import uk.org.ury.backend.server.exceptions.HandlerNotFoundException;
import uk.org.ury.backend.server.exceptions.HandlerSetupFailureException;
import uk.org.ury.backend.server.exceptions.HandlingException;
import uk.org.ury.backend.server.exceptions.NotAHandlerException;
import uk.org.ury.backend.server.exceptions.UnknownFunctionException;
import uk.org.ury.common.protocol.Directive;
import uk.org.ury.common.protocol.ProtocolUtils;
import uk.org.ury.common.protocol.Status;
import uk.org.ury.common.protocol.exceptions.EncodeFailureException;

/**
 * An abstract request handler for HttpCore, providing basic functionality such
 * as uniform error response.
 * 
 * @author Matt Windsor
 */
public abstract class AbstractRequestHandler implements HttpRequestHandler {
    protected Server server;
    protected String mount;

    /**
     * Constructs a new AbstractRequestHandler.
     * 
     * Obviously, this class cannot be instantiated directly.
     * 
     * @param server
     *            The instance of the URY server responsible for the request.
     * 
     * @param mount
     *            The directory to which this handler is to be mounted.
     */
    public AbstractRequestHandler(Server server, String mount) {
	this.server = server;
	this.mount = mount;
    }

    /**
     * Begins handling of a HTTP request.
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
     * Serves a HTTP plain-text error as the HTTP response for a request.
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
    protected void serveError(HttpRequest request, HttpResponse response,
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

	    entity = new StringEntity(ProtocolUtils.encode(content));
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (EncodeFailureException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (entity != null) {
	    entity.setContentType(HTTP.PLAIN_TEXT_TYPE);
	    response.setEntity(entity);
	}
    }

    /**
     * Handles a HTTP GET request.
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
    protected abstract void handleGet(HttpRequest request,
	    HttpResponse response, HttpContext context)
	    throws HandlerNotFoundException, HandlerSetupFailureException,
	    HandleFailureException, BadRequestException, NotAHandlerException,
	    UnknownFunctionException;

    /**
     * Parses a query string, populating a key-value map of the URL-unescaped
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
    protected final Map<String, String> parseQueryString(String query)
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

    /**
     * Gets the query string element of a URI.
     * 
     * @param uri
     *            The Uniform Resource Indicator whose query string should be
     *            extracted.
     * 
     * @return The query string, or null if it does not exist.
     */
    protected final String getQueryString(String uri) {
	String result = null;

	if (uri.contains("?") && uri.endsWith("?") == false) {
	    result = uri.split("\\?")[1];
	}

	return result;
    }
}