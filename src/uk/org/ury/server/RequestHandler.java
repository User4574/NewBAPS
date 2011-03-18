package uk.org.ury.server;

import java.util.List;
import java.util.Map;

import uk.org.ury.server.exceptions.HandleFailureException;


/**
 * Interface for classes that can handle requests addressed to their 
 * class name from the main server.
 * 
 * For an example of how to implement a RequestHandler, see 
 * ServerRequestHandler.
 * 
 * @author  Matt Windsor
 */

public interface RequestHandler
{
  /**
   * Handle a server GET request (that is, a request for data 
   * output).
   * 
   * @param parameters  A key-value map of parameters supplied with the 
   *                    server request.  Typically, the "function" 
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
  
  public List<String>
  handleGetRequest (Map<String, String> parameters, Server server)
  throws HandleFailureException;
}
