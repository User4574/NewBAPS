/**
 * 
 */
package uk.org.ury.testrig;

import uk.org.ury.frontend.FrontendFrame;


/**
 * An application-based entry point into the frontend.
 * 
 * If provided with no arguments, this will launch the main menu.
 * 
 * @author  Matt Windsor
 */

public class ApplicationLauncher implements Launcher
{
  /**
   * Main method.
   * 
   * @param args  The command-line arguments to the program.  These 
   *              will currently be ignored.
   */
  
  public static void
  main (String[] args)
  {
    FrontendFrame fr = new FrontendFrame (DEFAULT_MODULE_NAME);
    fr.setupUI ();
  }
}
