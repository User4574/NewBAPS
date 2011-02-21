/**
 * 
 */
package uk.org.ury.library.viewer;


import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.swixml.SwingEngine;

import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendPanel;
import uk.org.ury.frontend.HintField;
import uk.org.ury.library.LibraryTableModel;
import uk.org.ury.library.exceptions.EmptySearchException;


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
 

  /* Controller of this panel. */
  
  private LibraryViewer master;
  
  
  /* Panel widgets exposed by the SwiXML user interface. */
  
  private JTable resultsTable;
  private JScrollPane resultsPane;
  private JPanel messagePanel;
  private JLabel messageLabel;
  private JTextField searchField;
  private JButton searchButton;
  
  
  /* This contains the last search failure message, for use in 
   * letting the user know what happened.
   */
  
  private String searchFailureMessage = "ALEX!";
  
  
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

  
  /**
   * Action method for performing a search, bound by the UI XML
   * manifest to the search field and button.
   */
  
  public void
  search ()
  {
    /* We can't let the user search while another search is going on, 
     * but it's not good to let the search "freeze" the UI.
     * 
     * Hence the search function disables all sensitive parts of the 
     * interface and launches a search as a background process.
     * 
     * We also swap the results table or no-results panel out for a 
     * panel that says "Searching...", in the interests of 
     * user-friendliness.
     */
    
    searchField.setEnabled (false);
    searchButton.setEnabled (false);
    resultsPane.setVisible (false);
    messageLabel.setText ("Searching...");
    messagePanel.setVisible (true);
    
    
    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void> ()
    {
      /**
       * Perform a task in a separate thread from event-dispatch.
       * 
       * In this case, perform a search.
       * 
       * @return  whether or not the search was successful.
       */
      
      @Override
      public Boolean
      doInBackground ()
      {
        try
          {
            master.doSearch (searchField.getText ());
          }
        catch (EmptySearchException e)
          {
            searchFailureMessage = "Please type in a search term.";
            return false;
          }
        
        return true;
      }
      
      
      /**
       * Perform post-search cleanup and finalisation.
       */
      
      @Override
      public void
      done ()
      {
        // Figure out whether or not the search succeeded.
        
        boolean hasSucceeded = false;
        
        try
          {
            hasSucceeded = this.get ();
          }
        catch (InterruptedException e)
          {
            // Ignore
          }
        catch (ExecutionException e)
          {
            // Ignore
          }
        
        
          // Force table update with new results.
  
          resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
          
          
          /* Re-enable widgets and swap panels according to whether 
           * or not results were found. 
           */
          
          searchField.setEnabled (true);
          searchButton.setEnabled (true);
          messagePanel.setVisible (true);
          
          if (hasSucceeded == false)
            {
              messageLabel.setText (searchFailureMessage);
            }
          else if (master.getLibraryList ().size () == 0)
            {
              messageLabel.setText ("Sorry, but no results were "
                                    + "found for that term.");
            }
          else
            {
              messagePanel.setVisible (false);
              resultsPane.setVisible (true);            
            }
        }
    };
    
    
    worker.execute ();
  }
}
