package uk.org.ury.testrig;


import uk.org.ury.frontend.FrontendControlPanel;


/**
 * Control panel for the demo system.
 * 
 * @author  Matt Windsor
 *
 */

public class DemoControlPanel extends FrontendControlPanel
{
  
  public
  DemoControlPanel ()
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
