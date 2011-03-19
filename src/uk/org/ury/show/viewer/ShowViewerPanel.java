/**
 * 
 */
package uk.org.ury.show.viewer;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import uk.org.ury.database.exceptions.QueryFailureException;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.LoadFailureException;
import uk.org.ury.frontend.exceptions.UICreationFailureException;
import uk.org.ury.show.ShowChannel;


/**
 * Frontend panel providing access to an underlying show viewer.
 * 
 * The various show user interfaces (show editor, playout etc.) 
 * are derived from this.
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
  
  private JPanel channelGroupPanel;
  private JPanel binGroupPanel; 
  
  
  /**
   * Construct a new ShowViewerPanel.
   * 
   * The panel will interface with the given show viewer and is 
   * expected to be placed as a sub-component in the given 
   * FrontendMaster.
   * 
   * @param viewer  The ShowViewer controlling this LibraryViewerPanel.
   * 
   * @param master  The FrontendMaster driving the frontend.
   * 
   * @throws        UICreationFailureException if the UI creation fails.
   */
  
  public
  ShowViewerPanel (ShowViewer viewer, FrontendMaster master)
  throws UICreationFailureException
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
    List<String> binNames = null;
    
    try
      {
        binNames = viewer.getBins ();
      }
    catch (QueryFailureException e)
      {
        master.fatalError (e.getMessage ());
      }
    
    TrackBin tb;
    
    for (String name : binNames)
      {
        tb = new TrackBin (name);
        
        binGroupPanel.add (tb);
      }
    
    binGroupPanel.setLayout (new GridLayout (1, binNames.size ()));
  }
  
  
  /**
   * @return  the name of the panel.
   * 
   * @see     uk.org.ury.frontend.FrontendModulePanel#getModuleName()
   */
  
  @Override
  public String
  getModuleName ()
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
    try
      {
        master.loadModule ("library.viewer.LibraryViewer",
                           "show.viewer.LibraryControlPanel");
      }
    catch (LoadFailureException e)
      {
        master.fatalError (e.getMessage ());
      }
  }
}
