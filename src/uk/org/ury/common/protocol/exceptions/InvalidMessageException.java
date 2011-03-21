/**
 * 
 */
package uk.org.ury.common.protocol.exceptions;


/**
 * Generic exception thrown when a protocol function cannot process a 
 * message due to an issue with the message.
 * 
 * @author  Matt Windsor
 */

public class InvalidMessageException extends Exception
{
  /**
   * 
   */
  private static final long serialVersionUID = -3972492943653273528L;

  
  /**
   * Construct a new InvalidMessageException with a reason.
   * 
   * @param reason  The reason for throwing the exception.
   */
  
  public
  InvalidMessageException (String reason)
  {
    super (reason);
  }
}
