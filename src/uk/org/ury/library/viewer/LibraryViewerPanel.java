/**
 * 
 */
package uk.org.ury.library.viewer;


import java.net.URL;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.swixml.SwingEngine;

import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendPanel;
import uk.org.ury.frontend.HintField;
import uk.org.ury.library.LibraryTableModel;


/**
 * Frontend panel providing access to an underlying library viewer.
 * 
 * @author Matt Windsor, Nathan Lasseter
 */

public class LibraryViewerPanel extends FrontendPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -2441616418398056712L;
 
  
  /**
   * Action method for performing a search, bound by the UI XML
   * manifest to the search field and button
   */
  
  public void
  search ()
  {
    // TODO: SwingWorker?
    
    master.doSearch (searchField.getText ());
    
    // This is necessary to force the table to update with the new results.
    
    resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
  }
  

  /* Controller of this panel. */
  private LibraryViewer master;
  
  /* Panel widgets exposed by the SwiXML user interface. */
  
  private JTable resultsTable;
  private JTextField searchField;
  
  /**
   * Construct a new LibraryViewerPanel.
   * 
   * @param inMaster  The LibraryViewer controlling this LibraryViewerPanel.
   */
  
  public
  LibraryViewerPanel (LibraryViewer inMaster)
  {
    super ();
    
    master = inMaster;
   
    
    URL path = getClass ().getResource ("library_viewer_gui.xml");
    
    if (path == null)
      throw new IllegalArgumentException ("XML file does not exist.");
  
    SwingEngine se = new SwingEngine (this);
    se.getTaglib ().registerTag ("hint", HintField.class);
    
    
    /* The UI implementation is contained in library_viewer_gui.xml.
     *
     * It is hooked into the object resultsTable and the action 
     * method search.
     * 
     * See the XML file for more details.
     */
    
    try
      {
        se.insert (path, this);
      }
    catch (Exception e)
      {
        FrontendError.reportFatal ("UI creation failure: " + e.getMessage (), null);
      }
  }
  
  
  /**
   * @return  the name of the panel.
   * 
   * @see     uk.org.ury.frontend.FrontendPanel#getName()
   */
  
  @Override
  public String
  getName ()
  {
    return "Library Viewer Demo";
  }

}
