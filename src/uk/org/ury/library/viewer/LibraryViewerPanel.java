/**
 * 
 */
package uk.org.ury.library.viewer;


import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.library.LibraryTableModel;
import uk.org.ury.library.exceptions.EmptySearchException;


/**
 * Frontend panel providing access to an underlying library viewer.
 * 
 * @author Matt Windsor, Nathan Lasseter
 */

public class LibraryViewerPanel extends FrontendModulePanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -2441616418398056712L;
  
  
  /* Panel widgets exposed by the SwiXML user interface. */
  
  private JTable resultsTable;
  private JScrollPane resultsPane;
  private JPanel messagePanel; 
  private JLabel messageLabel;
  private JPanel searchingPanel;
  private JTextField searchField;
  private JButton searchButton;
  
  
  /* This contains the last search failure message, for use in 
   * letting the user know what happened.
   */
  
  private String searchFailureMessage = "Unknown error.";

  
  
  /**
   * Construct a new LibraryViewerPanel.
   * 
   * @param viewer  The LibraryViewer controlling this LibraryViewerPanel.
   * 
   * @param master   The FrontendMaster driving the frontend.
   */
  
  public
  LibraryViewerPanel (LibraryViewer viewer, FrontendMaster master)
  {
    super (viewer, "library_viewer_gui.xml", master);
 
    /* The UI implementation is contained in library_viewer_gui.xml.
     *
     * See this file for more details.
     */

    
    // Fine-tune table
    
    resultsTable.setAutoCreateRowSorter (true);
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
    messagePanel.setVisible (false);
    searchingPanel.setVisible (true);
    
    final LibraryViewer master = (LibraryViewer) getModule ();
    
    
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
          
          
          /* Re-enable widgets and swap panels according to whether 
           * or not results were found. 
           */
          
          searchField.setEnabled (true);
          searchButton.setEnabled (true);
          searchingPanel.setVisible (false);
          
          if (hasSucceeded == false)
            {
              messageLabel.setText (searchFailureMessage);
              messagePanel.setVisible (true);
            }
          else if (master.getLibraryList ().size () == 0)
            {
              messageLabel.setText ("Sorry, but no results were "
                                    + "found for that term.");
              messagePanel.setVisible (true);
            }
          else
            {
              // Force table update with new results.
              
              resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
              
              messagePanel.setVisible (false);
              resultsPane.setVisible (true);            
            }
        }
    };
    
    
    worker.execute ();
  }
}
