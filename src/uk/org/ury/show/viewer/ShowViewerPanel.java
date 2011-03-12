/**
 * 
 */
package uk.org.ury.show.viewer;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
 
  
  /* Panel widgets exposed by the SwiXML user interface. */
  
  private JSplitPane mainSplit;
  private JSplitPane binSplit;
  private JPanel channelGroupPanel;
  private JPanel binGroupPanel; 
  
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
    
    channelGroupPanel.setLayout (new GridLayout (1, channelNumber - 1));
    
    
    // TEST
    String binNames[] = {"Jingles", "Beds", "Adverts"};
    TrackBin tb;
    
    for (String name : binNames)
      {
        tb = new TrackBin (name);
        
        binGroupPanel.add (tb);
      }
    
    binGroupPanel.setLayout (new GridLayout (1, 3));
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
