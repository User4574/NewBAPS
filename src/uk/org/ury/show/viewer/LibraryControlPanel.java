package uk.org.ury.show.viewer;


import uk.org.ury.frontend.FrontendControlPanel;


/**
 * Control panel for the demo system.
 * 
 * @author  Matt Windsor
 *
 */

public class LibraryControlPanel extends FrontendControlPanel
{
  
  public
  LibraryControlPanel ()
  {
    super ("library_control_panel.xml");
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
