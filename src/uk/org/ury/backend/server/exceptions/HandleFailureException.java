/**
 * 
 */
package uk.org.ury.backend.server.exceptions;

/**
 * Generic exception thrown when a server request handler fails to 
 * handle a request.
 * 
 * @author Matt Windsor
 */

public class HandleFailureException extends HandlingException
{
  
  /**
   * Change this!  ---v
   */
  
  private static final long serialVersionUID = -397479334359858162L;


  /**
   * Construct a new HandleFailureException with a 
   * default reason.
   */
  
  public
  HandleFailureException ()
  {
    super ("Server request handler failed to handle the request.");
  }
  
  
  /**
   * Construct a new HandleFailureException.
   * 
   * @param reason  The explanation for the exception.
   */
  
  public
  HandleFailureException (String reason)
  {
    super (reason);
  }
}
