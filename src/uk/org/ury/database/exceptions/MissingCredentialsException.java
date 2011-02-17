/**
 * 
 */
package uk.org.ury.database.exceptions;

/**
 * Exception thrown when the database credentials required to 
 * log into the URY database under a user class are missing, 
 * and thus the log-in cannot continue.
 * 
 * The best practice for handling a MissingCredentialsException 
 * is to attempt to log into the database with a less privileged 
 * user class or, if the credentials for read-only access are 
 * missing, give up.
 * 
 * @author Matt Windsor
 */

public class MissingCredentialsException extends Exception
{
  
  /**
   *
   */
  
  private static final long serialVersionUID = -397479334359858162L;


  /**
   * Construct a new MissingCredentialsException with a 
   * default reason.
   */
  
  public
  MissingCredentialsException ()
  {
    super ("Missing credentials.");
  }
  
  
  /**
   * Construct a new MissingCredentialsException.
   * 
   * @param reason  The explanation for the exception.
   */
  
  public
  MissingCredentialsException (String reason)
  {
    super (reason);
  }
}
