/**
 * 
 */
package uk.org.ury.database.exceptions;


/**
 * Exception thrown when a DatabaseItem is queried for a property
 * that does not exist.
 * 
 * This is (usually) not a fatal error.
 * 
 * @author Matt Windsor
 */

public class MissingPropertyException extends Exception
{
 /**
   * 
   */
  private static final long serialVersionUID = -7353531873142099828L;


/**
  * Construct a new MissingPropertyException with a 
  * default reason.
  */
 
 public
 MissingPropertyException ()
 {
   super ("Query failure.");
 }
 
 
 /**
  * Construct a new MissingPropertyException.
  * 
  * @param reason  The explanation for the exception.
  */
 
 public
 MissingPropertyException (String reason)
 {
   super (reason);
 }
}
