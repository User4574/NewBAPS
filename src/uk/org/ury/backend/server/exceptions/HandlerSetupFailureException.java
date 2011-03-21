/**
 * 
 */
package uk.org.ury.backend.server.exceptions;

/**
 * Exception thrown when the server request handler requested 
 * by the client cannot be set up to process the request.
 * 
 * @author Matt Windsor
 */

public class HandlerSetupFailureException extends HandlingException
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
  HandlerSetupFailureException ()
  {
    super ("Handler setup failed.");
  }
  
  
  /**
   * Construct a new HandlerSetupFailureException with a class name 
   * and chained exception.
   * 
   * Use this to hide exception specifics from higher abstraction
   * layers.
   * 
   * @param className  The name of the failed handler class.
   * 
   * @param cause      The exception that this new exception is to 
   *                   wrap.
   */
  
  public
  HandlerSetupFailureException (String className, 
                                Throwable cause)
  {
    super ("Setup for handler " + className + " failed (reason: "
           + cause.getMessage () + ").", cause);
  }
}
