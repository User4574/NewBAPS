/**
 * 
 */
package uk.org.ury.frontend.modules.show;

import javax.swing.JLabel;

import uk.org.ury.frontend.FrontendPanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * A panel displaying track bin contents.
 * 
 * @author  Matt Windsor.
 */

public class TrackBin extends FrontendPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -5414923374901972511L;
  
  
  /* Components created and exposed by the XML engine. */
  
  private JLabel binName;
  
  
  /**
   * Construct a new TrackBin.
   * 
   * This constructor reads the channel panel layout from the 
   * XML manifest "track_bin.xml" in the same directory as 
   * this class file.
   * 
   * TODO: add track list into constructor
   * 
   * @param name  The name of the bin.
   * 
   * @throws      UICreationFailureException if the UI creation fails.
   */
  
  public
  TrackBin (String name)
  throws UICreationFailureException
  {
    super ("track_bin.xml", null);
 
    // Tweak name.
    
    binName.setText (name);
 
  }
}
