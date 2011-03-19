/**
 * 
 */
package uk.org.ury.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONValue;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.server.exceptions.BadRequestException;
import uk.org.ury.server.exceptions.HandleFailureException;
import uk.org.ury.server.exceptions.HandlerNotFoundException;
import uk.org.ury.server.exceptions.HandlerSetupFailureException;
import uk.org.ury.server.exceptions.HandlingException;
import uk.org.ury.server.exceptions.NotAHandlerException;
import uk.org.ury.server.protocol.Directive;
import uk.org.ury.server.protocol.Status;

/**
 * The unified URY server, accepting requests over HTTP.
 * 
 * @author  Matt Windsor
 */

public class Server
{

  private ServerSocket serverSocket;
  
  private static final String SERVER_VERSION = "SLUT 0.0";
  private static final String DOCTYPE        = 
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\""
    + "\"http://www.w3.org/TR/html4/strict.dtd\">";
  private static final String INDEX_HTML     =
      "\n<html>"
    + "\n  <head>"
    + "\n    <title>" + SERVER_VERSION + "</title>"
    + "\n  </head>"
    + "\n  <body>"
    + "\n    <h1>Welcome to the " + SERVER_VERSION + " server</h1>"
    + "\n    <p>This server exposes a class-based API for accessing"
    + "\n    the internals of the " + SERVER_VERSION + " system.</p>"
    + "\n    <p>See the documentation for details.</p>"
    + "\n  </body>"
    + "\n</html>";
  
  
  /**
   * The main method, which serves to create a server.
   * 
   * @param args  The argument vector.
   */
  
  public static void
  main (String[] args)
  {
    Server srv = new Server ();
    srv.run ();
  }

  
  /**
   * Run the server.
   */
  
  private void
  run ()
  {
    try
      {
        serverSocket = new ServerSocket (8000);
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    
    Socket clientSocket = null;
    
    while (true)
    {
      System.out.println ("Accepting connections... bring 'em on!");
      
      try
      {
        clientSocket = serverSocket.accept ();
      }
      catch (IOException e)
      {
        System.out.println ("SLUT: Accept failed on port 8000.  I'm bailing.");
        System.exit (-1);
      }
    
      try
        {
          doConnection (clientSocket);
        }
      catch (IOException e)
        {
          e.printStackTrace ();
        }
      finally
        {
          try
            {
              clientSocket.close ();
            }
          catch (IOException e)
            {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
    }
  }

  
  public void
  doConnection (Socket clientSocket)
  throws IOException
  {
    PrintWriter out = new PrintWriter (clientSocket.getOutputStream(), true);
    BufferedReader in = new BufferedReader (new InputStreamReader (
                                            clientSocket.getInputStream()));
    String inputLine;

    //initiate conversation with client
    
    List<String> buffer = new ArrayList<String> ();
    
    
    for (inputLine = in.readLine (); inputLine != null; inputLine = in.readLine ())
      {
        if (inputLine.equals (""))
          break;
        
        buffer.add (inputLine);

        if (inputLine.startsWith ("Expect:") 
            && inputLine.split (":")[1].startsWith ("100-continue"))
          out.print ("HTTP/1.1 100 Continue\n\r\n");

        out.flush ();
      }
    
    processBuffer (buffer, out);
    
    out.flush ();
    out.close ();
    in.close ();
    
    System.out.println ("Just finished with this one...");
  }
  
  
  public void
  processBuffer (List<String> buffer, PrintWriter out)
  { 
    String requestStart = buffer.get (0);
    
    System.out.println (requestStart);
    
    HttpResponse response;
    
    if (requestStart.startsWith ("GET"))
      {
        System.out.println ("That was a GET..."); 
        try
          {
            response = handleGet (buffer);
          }
        catch (HandlerNotFoundException e)
          {
            // TODO: log
            response = serveError (HttpStatus.SC_NOT_FOUND,
                                   e.getMessage ());
          }
        catch (BadRequestException e)
          {
            // TODO: log
            response = serveError (HttpStatus.SC_BAD_REQUEST,
                                   e.getMessage ());
          }
        catch (HandlingException e)
          {
            response = serveError (HttpStatus.SC_INTERNAL_SERVER_ERROR,
                e.getMessage ());
          }
      }
    else
      {
        System.out.println ("Uh-oh! I don't know what to do!");
        response = serveError (HttpStatus.SC_NOT_IMPLEMENTED, 
                               "Feature not implemented yet.");
      }
    
    
    // Now send the response.
    
    for (Header h : response.getAllHeaders ())
      {
        out.println (h);
      }
    
    try
      {
        out.print (EntityUtils.toString (response.getEntity ()));
      }
    catch (ParseException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  }
  
  
  /**
   * Handle a HTTP GET request.
   * 
   * @param buffer  The HTTP request as a list of strings.
   * 
   * @return        The HTTP response.
   * 
   * @throws        HandlerNotFoundException if the client requested 
   *                a request handler that could not be found on the 
   *                class path.
   *                
   * @throws        HandlerSetupFailureException if the handler was 
   *                found but could not be set up (eg does not 
   *                implement appropriate interface or cannot be 
   *                instantiated).
   * 
   * @throws        HandleFailureException if an appropriate handler 
   *                was contacted, but it failed to process the 
   *                request.
   *                
   * @throws        BadRequestException if the request was malformed 
   *                or invalid.
   *                
   * @throws        NotAHandlerException if the class requested to 
   *                handle the request is not a handler.
   */
  
  public HttpResponse
  handleGet (List<String> buffer)
  throws HandlerNotFoundException, HandlerSetupFailureException,
    HandleFailureException, BadRequestException, NotAHandlerException
  {
    HttpResponse response = null;
    
    String[] getsplit = buffer.get (0).split (" ");
    String   path     = getsplit[1];
    
    if (path.equals ("/index.html")
        || path.equals ("/"))
      {
        // Someone's trying to get the index page!
        // Humour them.
        
        response = new BasicHttpResponse (HttpVersion.HTTP_1_1,
                                          HttpStatus.SC_OK,
                                          "OK");
        
        StringEntity entity = null;
        
        try
          {
            entity = new StringEntity (DOCTYPE + INDEX_HTML);
          }
        catch (UnsupportedEncodingException e)
          {
            throw new HandlerSetupFailureException ("(Index page)", e);
          }

        response.setEntity (entity);
      }
    else
      {
        // Convert this into a URL and fan out the various parts of it.
        
        URL pathURL = null;
        
        try
          {
            pathURL = new URL ("http://localhost" + path);
          }
        catch (MalformedURLException e)
          {
            throw new BadRequestException (e);
          }

        String className    = "uk.org.ury" + pathURL.getPath ().replace ('/', '.');
        System.out.println (className);
        Class<?> newClass   = null;
     
        
        try
          {
            newClass = Class.forName (className);
          }
        catch (ClassNotFoundException e)
          {
            throw new HandlerNotFoundException (className, e);
          }
        
        
        // Check for error (response set) here.
        
        if (response == null
            && RequestHandler.class.isAssignableFrom (newClass))
          {
            String queryString = pathURL.getQuery ();
            Map<String, String> parameters;
            
            try
              {
                parameters = parseQueryString (queryString);
              }
            catch (UnsupportedEncodingException e)
              {
                throw new HandlerSetupFailureException (className, e);
              }
            
            Map<String, Object> content = null;
                
            try
              {
                RequestHandler srh = ((RequestHandler) newClass.newInstance ());
                content = srh.handleGetRequest (parameters, this);
              }
            catch (InstantiationException e)
              {
                throw new HandlerSetupFailureException (className, e);
              }
            catch (IllegalAccessException e)
              {
                throw new HandlerSetupFailureException (className, e);
              }
            
            
            // Everything seems OK, so make the response.
            
            response = new BasicHttpResponse (HttpVersion.HTTP_1_1, 
                                                  HttpStatus.SC_OK,
                                                  "OK");
             
            content.put (Directive.STATUS.toString (),
                         Status.OK.toString ());
            
            StringEntity entity = null;
                
            try
              {
                entity = new StringEntity (JSONValue.toJSONString (content));
              }
            catch (UnsupportedEncodingException e)
              {
                throw new HandlerSetupFailureException (className, e);
              }
                
            entity.setContentType (HTTP.PLAIN_TEXT_TYPE);
            response.setEntity (entity);
          }
        else
          throw new NotAHandlerException (className);
      }
    
    return response;
  }
  
  
  /**
   * Serve a HTTP plain-text error as a HTTP response.
   * 
   * @param code    HTTP status code to use.
   * @param reason  The reason to display to the client.
   * 
   * @return        the HTTP response for the error.
   */
  
  private HttpResponse
  serveError (int code, String reason)
  {  
    // Get the reason string to put in the error response.
    // TODO: standards?
    
    String statusReason = "";
    
    switch (code)
      {
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
    
    HttpResponse response = new BasicHttpResponse (HttpVersion.HTTP_1_1,
                                                   code, statusReason);
    StringEntity entity = null;
    
    try
      {
        Map<String, Object> content = new HashMap<String, Object> ();
        
        content.put (Directive.STATUS.toString (),
                     Status.ERROR.toString ());
        content.put (Directive.REASON.toString (),
                     reason);
        
        entity = new StringEntity (JSONValue.toJSONString (content));
      }
    catch (UnsupportedEncodingException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    
    if (entity != null)
      {
        entity.setContentType (HTTP.PLAIN_TEXT_TYPE);
        response.setEntity (entity);
      }
    
    return response;
  }
  
  
  /**
   * Parse a query string, populating a key-value map of the 
   * URL-unescaped results.
   * 
   * @param query  The query string to parse.
   * 
   * @return       A map associating parameter keys and values.
   * 
   * @throws       UnsupportedEncodingException if the URL decoder
   *               fails.
   */
  
  public Map<String, String>
  parseQueryString (String query)
  throws UnsupportedEncodingException
  {
    Map<String, String> params = new HashMap<String, String> ();
    
    // At least one parameter
    if (query != null
        && query.endsWith ("&") == false)
      {
        String[] qsplit = {query};
        
        // More than one parameter - split the query.
        if (query.contains ("&"))
          qsplit = query.split ("&");

        
        for (String param : qsplit)
          {
            // Has a value
            if (param.contains ("=")
                && param.endsWith ("=") == false)
              {
                String[] paramsplit = param.split ("=");
                params.put (URLDecoder.decode (paramsplit[0], "UTF-8"), 
                            URLDecoder.decode (paramsplit[1], "UTF-8"));
              }
            // Doesn't have a value
            else if (param.contains ("=") == false)
              {
                params.put (URLDecoder.decode (param, "UTF-8"), null);
              }
          }
      }
    
    return params;
  }
  
  
  /**
   * Get a database connection using the given user class.
   * 
   * @param  userClass  The user class to get a connection for.
   * 
   * @return            a database connection, which may or may not 
   *                    have been created on this call.
   *                    
   * @throw             MissingCredentialsException if the credentials
   *                    for the given userclass are missing.
   * 
   * @throw             ConnectionFailureException if the connection 
   *                    failed.
   */
  
  public DatabaseDriver
  getDatabaseConnection (UserClass userClass)
  throws MissingCredentialsException, ConnectionFailureException
  {
    // TODO: Singleton
  
    ConfigReader config = new ConfigReader ("res/conf.xml");
    
    return new DatabaseDriver (config, UserClass.READ_ONLY);
  }


  /**
   * @return  the version string of the server.
   */
  
  public String
  getVersion ()
  {
    return SERVER_VERSION;
  }
}
