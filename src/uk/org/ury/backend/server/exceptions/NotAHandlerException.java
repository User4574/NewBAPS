/**
 * 
 */
package uk.org.ury.backend.server.exceptions;

/**
 * Exception thrown if the class requested as a handler by the client 
 * is, in fact, not a handler (it does not implement RequestHandler).
 * 
 * @author Matt Windsor
 */

public class NotAHandlerException extends HandlingException
{
  /**
   * 
   */
  private static final long serialVersionUID = -7268289187311868036L;

  /**
   * Construct a NotAHandlerException with the name of the class that 
   * is not a handler.
   * 
   * @param className  The name of the offending class.
   */

  public
  NotAHandlerException (String className)
  {
    super ("Class " + className + " is not a request handler.");
    // TODO Auto-generated constructor stub
  }

}
