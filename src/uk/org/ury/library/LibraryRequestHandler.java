/**
 * 
 */
package uk.org.ury.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.database.exceptions.QueryFailureException;
import uk.org.ury.library.exceptions.EmptySearchException;
import uk.org.ury.library.item.LibraryItem;
import uk.org.ury.server.Server;
import uk.org.ury.server.RequestHandler;
import uk.org.ury.server.exceptions.HandleFailureException;
import uk.org.ury.server.protocol.Directive;


/**
 * A request handler for library queries.
 * 
 * @author Matt Windsor
 */

public class LibraryRequestHandler implements RequestHandler
{
  /**
   * Handle a server GET request (that is, a request for data 
   * output).
   * 
   * @param parameters  A key-value map of parameters supplied with
   *                    the server request.  Typically, the function 
   *                    parameter will detail the function that the 
   *                    request handler is expected to perform.
   *                    
   * @param server      The server from which the request originated.
   *                    This will be able to provide the handler with 
   *                    pooled resources, for example the database.
   * 
   * @return            A list of lines to return in the body of the 
   *                    server's response to the client.
   *                  
   * @throws            HandleFailureException if the handler cannot 
   *                    handle the request.
   */
  
  @Override
  public Map<String, Object>
  handleGetRequest (Map<String, String> parameters, Server server)
  throws HandleFailureException
  {
    Map<String, Object> response = new HashMap<String, Object>();
    
    if (parameters.containsKey ("function"))
      {
        String function = parameters.get ("function");
        
        if (function.equals ("search"))
          {
            doSearch (parameters, response, server);
          }
        else if (function.equals ("help"))
          {
            response.put (Directive.INFO.toString (),
                          "Available functions:");
            response.put (Directive.INFO.toString (), "search (string) - search library for string.");
          }
        else
          throw new HandleFailureException ("Unknown function: "
                                            + function + ". (Try 'function=help'.)");
            
      }
    else
      throw new HandleFailureException ("No function provided.  (Try 'function=help'.)");
    
    return response;
  }

  
  /**
   * Perform a library search, populating the response list.
   * 
   * @param parameters  A key-value map of parameters supplied with 
   *                    the server request.  Typically, the function 
   *                    parameter will detail the function that the 
   *                    request handler is expected to perform.
   * 
   * @param response    The response list to populate.
   * 
   * @param server      The server providing database resources.
   * 
   * @throws            HandleFailureException if an error occurs
   *                    that thwarts the handling of the request.
   */
  
  private void
  doSearch (Map<String, String> parameters, 
            Map<String, Object> response, Server server)
  throws HandleFailureException
  {
    if (parameters.containsKey ("search") == false)
      throw new HandleFailureException ("Search term is missing.");
    else if (parameters.get ("search") == null)
      throw new HandleFailureException ("Search term is null.");
    
    String search = parameters.get ("search");
    DatabaseDriver dd = null;
    
    try
      {
        dd = server.getDatabaseConnection (UserClass.READ_ONLY);
      }
    catch (MissingCredentialsException e)
      {
        throw new HandleFailureException (e.getMessage ());
      }
    catch (ConnectionFailureException e)
      {
        throw new HandleFailureException (e.getMessage ());
      }
    
    try
      {
        List<Map<String, String>> itemArray = new ArrayList<Map<String, String>> ();
        
        for (LibraryItem li : LibraryUtils.search (dd, search))
          {
            itemArray.add (li.asResponse ());
          }
        
        response.put (Directive.ITEMS.toString (), itemArray);
      }
    catch (QueryFailureException e)
      {
        throw new HandleFailureException (e.getMessage ());
      }
    catch (EmptySearchException e)
      {
        throw new HandleFailureException (e.getMessage ());
      }
  }
}
