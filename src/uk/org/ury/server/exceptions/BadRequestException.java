/**
 * 
 */
package uk.org.ury.server.exceptions;

/**
 * Exception thrown when the server meets a malformed request, or 
 * part of one.
 * 
 * @author Matt Windsor
 */

public class BadRequestException extends HandlingException
{
  
  /**
   * TODO: Change this!  ---v
   */
  
  private static final long serialVersionUID = -397479334359858162L;


  /**
   * Construct a new BadRequestException with a default reason.
   */
  
  public
  BadRequestException ()
  {
    super ("Bad request.");
  }
  
  
  /**
   * Construct a new HandlerNotFoundException with a chained 
   * exception.
   * 
   * @param cause      The exception that this new exception is to 
   *                   wrap.
   */
  
  public
  BadRequestException (Throwable cause)
  {
    super ("Bad request.  ("
           + cause.getMessage () + ")", cause);
  }
}
