/**
 * 
 */
package uk.org.ury.database.exceptions;

/**
 * Exception thrown when the database backend fails to execute
 * a query.
 * 
 * @author Matt Windsor
 */

public class QueryFailureException extends Exception
{
 /**
   * 
   */
  private static final long serialVersionUID = -7353531873142099828L;


/**
  * Construct a new QueryFailureException with a 
  * default reason.
  */
 
 public
 QueryFailureException ()
 {
   super ("Query failure.");
 }
 
 
 /**
  * Construct a new QueryFailureException.
  * 
  * @param reason  The explanation for the exception.
  */
 
 public
 QueryFailureException (String reason)
 {
   super (reason);
 }
}
