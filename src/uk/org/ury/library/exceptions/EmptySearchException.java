/**
 * 
 */
package uk.org.ury.library.exceptions;

/**
 * Exception thrown when a library search is initiated 
 * in which the query string is null.
 * 
 * Frontends should handle this nicely.  Do NOT treat this 
 * as a fatal error!
 * 
 * @author Matt Windsor
 */

public class EmptySearchException extends Exception
{
  
  /**
   * Change this!  ---v
   */
  
  private static final long serialVersionUID = -397479334359858162L;


  /**
   * Construct a new EmptySearchException with a 
   * default reason.
   */
  
  public
  EmptySearchException ()
  {
    super ("Query string was empty.");
  }
  
  
  /**
   * Construct a new EmptySearchException.
   * 
   * @param reason  The explanation for the exception.
   */
  
  public
  EmptySearchException (String reason)
  {
    super (reason);
  }
}
