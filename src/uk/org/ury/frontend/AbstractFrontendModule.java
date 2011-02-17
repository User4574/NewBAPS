package uk.org.ury.frontend;

import javax.swing.JApplet;

/**
 * An abstract implementation of the FrontendModule interface.
 * 
 * @author  Matt Windsor
 *
 */

public abstract class AbstractFrontendModule extends JApplet implements FrontendModule
{

  /**
   * 
   */
  private static final long serialVersionUID = 5309585577127763538L;
  
  
  /**
   * Initialise the module as an applet.
   */
  
  public abstract void
  init ();
}
