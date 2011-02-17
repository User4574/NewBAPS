/**
 * 
 */
package uk.org.ury.frontend;

import javax.swing.JOptionPane;

/**
 * Factory for descriptive error dialogues.
 * 
 * @author  Matt Windsor
 *
 */

public class FrontendError
{
  private final static String FATAL_TITLE = "Unrecoverable Error";
  
  private final static String FATAL_PREFIX = 
  "An unrecoverable error has occurred, and the program must close "
  + "immediately.\n"
  + "Any unsaved work may have been permanently lost.\n\n"
  + "Please let URY Computing know about this error.\n\n"
  + "The error message reported was:\n\t";
  
  private final static String FATAL_SUFFIX = "";
  
  
  /** 
   * Create an error dialogue to report a fatal error.
   * 
   * @string message  The message, eg the exception message, 
   *                  to report to the user.
   */
  
  public static void
  reportFatal (String message, FrontendFrame frame)
  {
    // TODO: Log
    
    // TODO: Replace with bespoke error dialogue?
    
    JOptionPane.showMessageDialog (frame, 
        FATAL_PREFIX + message + FATAL_SUFFIX, 
        FATAL_TITLE,
        JOptionPane.ERROR_MESSAGE);
    
    System.exit (-1);
  }
}
