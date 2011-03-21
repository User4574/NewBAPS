/**
 * 
 */
package uk.org.ury.backend.database.exceptions;

/**
 * Exception thrown when the database backend fails to connect to
 * the database server, in absence of a more specific exception.
 * 
 * @author Matt Windsor
 */

public class ConnectionFailureException extends Exception
{
 /**
   * 
   */
  private static final long serialVersionUID = -7353531873142099828L;


/**
  * Construct a new ConnectionFailureException with a 
  * default reason.
  */
 
 public
 ConnectionFailureException ()
 {
   super ("Connection failure.");
 }
 
 
 /**
  * Construct a new ConnectionFailureException.
  * 
  * @param reason  The explanation for the exception.
  */
 
 public
 ConnectionFailureException (String reason)
 {
   super (reason);
 }
}
