package uk.org.ury.testrig;


import uk.org.ury.frontend.FrontendControlPanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * Control panel for the demo system.
 * 
 * @author  Matt Windsor
 *
 */

public class DemoControlPanel extends FrontendControlPanel
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 7558888612002013312L;


  /**
   * Constructs a new DemoControlPanel.
   * 
   * @throws  UICreationFailureException if the UI creation fails.
   */
  
  public
  DemoControlPanel ()
  throws UICreationFailureException
  {
    super ("demo_control_panel.xml");
  }
  
  
  /**
   * Go back to the previous module.
   */
  
  public void
  back ()
  {
    master.restoreModule (parent, prevCPanel);
  }
}
