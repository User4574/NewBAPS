/**
 * 
 */
package uk.org.ury.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.org.ury.server.Server;
import uk.org.ury.server.RequestHandler;
import uk.org.ury.server.exceptions.HandleFailureException;


/**
 * A request handler for server queries.
 * 
 * @author Matt Windsor
 */

public class ServerRequestHandler implements RequestHandler
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
  public List<String>
  handleGetRequest (Map<String, String> parameters, Server server)
  throws HandleFailureException
  {
    List<String> response = new ArrayList<String> ();
    
    if (parameters.containsKey ("function"))
      {
        String function = parameters.get ("function");
        
        if (function.equals ("info"))
          {
            getInfo (response, server);
          }
        else if (function.equals ("help"))
          {
            response.add ("INFO: Available functions:");
            response.add ("INFO: info - Get server information.");
          }
        else if (function.equals ("test"))
          response.add ("INFO: Test succeeded.");
        else
          throw new HandleFailureException ("Unknown function: "
                                            + function + ". (Try 'function=help'.)");
            
      }
    else
      throw new HandleFailureException ("No function provided.  (Try 'function=help'.)");
    
    return response;
  }

  
  /**
   * Retrieve information about the server.
   * 
   * @param response    The response list to populate.
   * 
   * @param server      The server providing database resources.
   * 
   * @throws            HandleFailureException if an error occurs
   *                    that thwarts the handling of the request.
   */
  
  private void
  getInfo (List<String> response, Server server)
  throws HandleFailureException
  {
    response.add ("INFO: University Radio York BAPS Replacement");
    response.add ("INFO: Server version is " + server.getVersion ());
  }
    
}
