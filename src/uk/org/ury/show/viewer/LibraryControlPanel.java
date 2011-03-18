package uk.org.ury.show.viewer;


import uk.org.ury.frontend.FrontendControlPanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * Control panel for the demo system.
 * 
 * @author  Matt Windsor
 *
 */

public class LibraryControlPanel extends FrontendControlPanel
{
  
  /**
   * 
   */
  
  private static final long serialVersionUID = -4260057656619439704L;


  /**
   * Construct a new LibraryControlPanel.
   * 
   * @throws  UICreationFailureException if the UI creation fails.
   */
  
  public
  LibraryControlPanel ()
  throws UICreationFailureException
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
