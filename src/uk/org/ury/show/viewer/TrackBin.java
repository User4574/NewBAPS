/**
 * 
 */
package uk.org.ury.show.viewer;

import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.swixml.SwingEngine;

import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendSubBanner;


/**
 * A panel displaying track bin contents.
 * 
 * @author  Matt Windsor.
 */

public class TrackBin extends JPanel
{
  private JLabel binName;
  private JList itemList;
  
  
  /**
   * Construct a new TrackBin.
   * 
   * This constructor reads the channel panel layout from the 
   * XML manifest "track_bin.xml" in the same directory as 
   * this class file.
   * 
   * @param name  The name of the bin.
   * 
   * TODO: add track list into constructor
   */
  
  public
  TrackBin (String name)
  {
    super ();
    
    // Acquire path.
    
    URL path = getClass ().getResource ("track_bin.xml");
    
    if (path == null)
      FrontendError.reportFatal ("UI creation failure: XML layout does not exist.", null);
  
    SwingEngine se = new SwingEngine (this);
    se.getTaglib ().registerTag ("subbanner", FrontendSubBanner.class);
    
   // Read the XML.
    
    try
      {
        se.insert (path, this);
      }
    catch (Exception e)
      {
        FrontendError.reportFatal ("UI creation failure: " + e.getMessage (), null);
      }
    
    // Tweak name.
    
    binName.setText (name);
 
  }
}
