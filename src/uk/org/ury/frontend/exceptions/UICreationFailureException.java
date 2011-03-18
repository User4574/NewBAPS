/**
 * 
 */
package uk.org.ury.frontend.exceptions;


/**
 * Exception thrown when the creation of a UI element fails.
 * 
 * @author Matt Windsor
 */

public class UICreationFailureException extends Exception
{
 /**
   * 
   */
  private static final long serialVersionUID = -7353531873142099828L;


/**
  * Construct a new UICreationFailureException with a 
  * default reason.
  */
 
 public
 UICreationFailureException ()
 {
   super ("UI creation failure.");
 }
 
 
 /**
  * Construct a new UICreationFailureException.
  * 
  * @param reason  The explanation for the exception.
  */
 
 public
 UICreationFailureException (String reason)
 {
   super (reason);
 }
}
