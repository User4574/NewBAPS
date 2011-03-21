/**
 * 
 */
package uk.org.ury.backend.server.exceptions;

/**
 * Generic exception thrown when the server cannot handle a request.
 * 
 * @author Matt Windsor
 */

public class HandlingException extends Exception
{
  /**
   * TODO: Change this!  ---v
   */
  
  private static final long serialVersionUID = -397479334359858162L;
  
  
  /**
   * Construct a HandlingException with a reason.
   * 
   * @param string  The reason to present.
   */
  
  public
  HandlingException (String string)
  {
    super (string);
  }

  
  /**
   * Construct a HandlingException with a reason and a cause to
   * chain.
   * 
   * @param string  The reason to present.
   * 
   * @param cause   The thrown cause that this exception should wrap.
   */
  
  public
  HandlingException (String string, Throwable cause)
  {
    super (string, cause);
  }
}
