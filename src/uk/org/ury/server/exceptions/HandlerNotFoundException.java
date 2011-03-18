/**
 * 
 */
package uk.org.ury.server.exceptions;

/**
 * Exception thrown when the server request handler requested 
 * by the client is not * found in the class space.
 * 
 * @author Matt Windsor
 */

public class HandlerNotFoundException extends HandlingException
{
  
  /**
   * TODO: Change this!  ---v
   */
  
  private static final long serialVersionUID = -397479334359858162L;


  /**
   * Construct a new HandlerNotFoundException with a 
   * default reason.
   */
  
  public
  HandlerNotFoundException ()
  {
    super ("Handler not found.");
  }
  
  
  /**
   * Construct a new HandlerNotFoundException with a class name and 
   * chained exception.
   * 
   * @param className  The name of the missing handler class.
   * 
   * @param cause      The exception that this new exception is to 
   *                   wrap.
   */
  
  public
  HandlerNotFoundException (String className, Throwable cause)
  {
    super ("Handler " + className + " not found.  ("
           + cause.getMessage () + ")", cause);
  }
}
