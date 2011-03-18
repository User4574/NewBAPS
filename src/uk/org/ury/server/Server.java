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

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.server.exceptions.HandleFailureException;

/**
 * The unified URY server, accepting requests over HTTP.
 * 
 * @author  Matt Windsor
 */

public class Server
{

  private ServerSocket serverSocket;
  
  private static final String HTTP_VERSION   = "HTTP/1.1";
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
    
    if (requestStart.startsWith ("GET"))
      {
        System.out.println ("That was a GET..."); 
        handleGet (buffer, out);
      }
    else
      {
        System.out.println ("Uh-oh! I don't know what to do!");
        
        out.println (HTTP_VERSION + " 501 Not Implemented");
        out.println ("Connection: close");
        out.print ("\r\n");
      }
  }
  
  public void
  handleGet (List<String> buffer, PrintWriter out)
  {
    String[] getsplit = buffer.get (0).split (" ");
    String   path     = getsplit[1];
    
    if (path.equals ("/index.html")
        || path.equals ("/"))
      {
        // Someone's trying to get the index page!
        // Humour them.
        
        out.println (HTTP_VERSION + " 200 OK");
        out.println ("Connection: close");
        out.print ("\r\n");
        
        out.println (DOCTYPE + INDEX_HTML);
      }
    else
      {
        // Convert this into a URL and fan out the various parts of it.
        
        URL pathURL = null;
        
        try
          {
            pathURL = new URL ("http://localhost" + path);
          }
        catch (MalformedURLException e1)
          {
            serveError (400, "Malformed URL.", out);
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
            serveError (404, "Class " + className + " not found.", out);
            return;
          }
        
        if (RequestHandler.class.isAssignableFrom (newClass))
          {
            String queryString = pathURL.getQuery ();
            Map<String, String> parameters;
            try
              {
                parameters = parseQueryString (queryString);
              }
            catch (UnsupportedEncodingException e)
              {
                serveError (500, "URL decode failure for class "
                    + className
                    + " (" + e.getMessage () + ").", out);
                return;
              }
            
            List<String> response;
            
            try
              {
                RequestHandler srh = ((RequestHandler) newClass.newInstance ());
                response = srh.handleGetRequest (parameters, this);
              }
            catch (InstantiationException e)
              {
                serveError (500, "Instantiation exception for class "
                            + className
                            + " (" + e.getMessage () + ").", out);
                return;
              }
            catch (IllegalAccessException e)
              {
                serveError (500, "Illegal access exception for class "
                            + className 
                            + " (" + e.getMessage () + ").", out);
                return;
              }
            catch (HandleFailureException e)
              {
                serveError (500, "Failed to handle request for class "
                            + className
                            + " (" + e.getMessage () + ").", out);
                return;
              }
            
            // If we made it this far, the response is A-OK.
            
            out.println (HTTP_VERSION + " 200 OK");
            out.println ("Content-Type: text/plain");
            out.print ("\r\n");
            
            out.println ("START");
            
            for (String line : response)
              out.println (line);
            
            out.println ("END");
            
            out.flush ();            
          }
        else
          {
            serveError (404, "Class " + className + " does not handle requests.", out);
            return;
          }
      }
  }
  
  
  /**
   * Serve a HTTP plain-text error.
   * 
   * @param code    HTTP status code to use.
   * @param reason  The reason to display to the client.
   * @param out     The output stream.
   */
  
  private void
  serveError (int code, String reason, PrintWriter out)
  {
    String errorStatus = "";
    
    switch (code)
      {
      case 400:
        errorStatus = "400 Bad Request";
        break;
      case 404:
        errorStatus = "404 Not Found";
        break;
      default:
        errorStatus = "500 Internal Server Error";
        break;
      }
    
    out.println (HTTP_VERSION + " " + errorStatus);
    out.println ("Content-Type: text/plain");
    out.println ("Connection: close");
    out.print ("\r\n");
   
    out.println ("ERROR: " + reason);    
    out.flush ();
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
