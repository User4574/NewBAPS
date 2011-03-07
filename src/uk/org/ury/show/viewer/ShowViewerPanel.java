/**
 * 
 */
package uk.org.ury.show.viewer;

import javax.swing.JButton;
import javax.swing.JPanel;

import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.show.ShowChannel;


/**
 * Frontend panel providing access to an underlying lshow viewer.
 * 
 * @author Matt Windsor, Nathan Lasseter
 */

public class ShowViewerPanel extends FrontendModulePanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -2441616418398056712L;
  
  private JPanel channelGroupPanel;
  
  /* Panel widgets exposed by the SwiXML user interface. */
  
  
  /**
   * Construct a new ShowViewerPanel.
   * 
   * @param viewer  The ShowViewer controlling this LibraryViewerPanel.
   * 
   * @param master  The FrontendMaster driving the frontend.
   */
  
  public
  ShowViewerPanel (ShowViewer viewer, FrontendMaster master)
  {
    super (viewer, "show_viewer_gui.xml", master);
    
    int channelNumber = 1;
    ChannelPanel cp;
    
    for (ShowChannel channel : viewer.getChannels ())
      {
        cp = new ChannelPanel (channelNumber, channel);
        
        channelGroupPanel.add (cp);
        channelNumber++;
      }
  }
  
  
  /**
   * @return  the name of the panel.
   * 
   * @see     uk.org.ury.frontend.FrontendModulePanel#getName()
   */
  
  @Override
  public String
  getName ()
  {
    return "Show Viewer Demo";
  }
  
  
  /**
   * Initialise the library viewer for the purposes of adding tracks 
   * and/or browsing the library.
   */
  
  public void
  search ()
  {
    master.loadModule ("library.viewer.LibraryViewer", "show.viewer.LibraryControlPanel");
  }
}
