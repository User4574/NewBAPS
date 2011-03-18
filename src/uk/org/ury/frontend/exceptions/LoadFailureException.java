/**
 * 
 */
package uk.org.ury.frontend.exceptions;


/**
 * Exception thrown when the loading of a new frontend module fails.
 * 
 * @author Matt Windsor
 */

public class LoadFailureException extends Exception
{
 /**
   * 
   */
  private static final long serialVersionUID = -7353531873142099828L;


/**
  * Construct a new LoadFailureException with a 
  * default reason.
  */
 
 public
 LoadFailureException ()
 {
   super ("Module load failure.");
 }
 
 
 /**
  * Construct a new LoadFailureException.
  * 
  * @param reason  The explanation for the exception.
  */
 
 public
 LoadFailureException (String reason)
 {
   super (reason);
 }
}
