/**
 * 
 */
package uk.org.ury.frontend;

import javax.swing.JPanel;

/**
 * A frontend user interface panel.
 * 
 * All frontend user interfaces should subclass this as their main 
 * interface space, so that frontend panels can include each other 
 * and panels can be embedded into application frames or applets.
 * 
 * @author Matt Windsor
 *
 */

public abstract class FrontendPanel extends JPanel
{
  /**
   * 
   */
  
  private static final long serialVersionUID = 5616222530691425635L;

  public
  FrontendPanel ()
  {
    super ();
  }
}
