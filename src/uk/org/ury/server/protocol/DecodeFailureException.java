/**
 * 
 */
package uk.org.ury.server.protocol;

/**
 * Exception thrown when the protocol decoder fails.
 * 
 * @author  Matt Windsor
 */

public class DecodeFailureException extends Exception
{
  /**
   * 
   */
  private static final long serialVersionUID = -3972492943653273528L;

  
  /**
   * Construct a new DecodeFailureException with a reason.
   * 
   * @param reason  The reason for throwing the exception.
   */
  
  public
  DecodeFailureException (String reason)
  {
    super (reason);
  }
}
