package uk.org.ury.library.viewer;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.database.exceptions.QueryFailureException;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendFrame;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;

import uk.org.ury.library.LibraryItem;

import uk.org.ury.library.LibraryUtils;
import uk.org.ury.library.exceptions.EmptySearchException;

public class LibraryViewer extends AbstractFrontendModule
{
  /**
   * 
   */
  
  private static final long serialVersionUID = -2782366476480563739L;
  private DatabaseDriver dd;
  private List<LibraryItem> libraryList;
  private LibraryViewerPanel panel;
  private FrontendFrame frame;
  private ConfigReader config;
  
  
  /**
   * Construct a new LibraryViewer as a frontend object.
   */
  
  public
  LibraryViewer ()
  {
    try
      {
        config = new ConfigReader ("res/conf.xml");
      }
    catch (MissingCredentialsException e)
      {
        System.out.println(e);
      }
    
    frame = null;
    libraryList = new ArrayList<LibraryItem> ();
    panel = null;
  }
  
  
  /**
   * Initialise the library viewer frontend as an applet.
   */
  
  public void
  init ()
  {
    frame = null;
    libraryList = new ArrayList<LibraryItem> ();
    panel = null;
    
    
    try
    {
      SwingUtilities.invokeAndWait (new Runnable ()
      {
        public void
        run ()
        {
          panel.setOpaque (true);
          setContentPane (panel);
          
          runFrontend (null);
        }
        
      });
    }
    catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (InvocationTargetException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  }
  
  
  /**
   * Run the library viewer frontend as an applet.
   */
  
  public void
  start ()
  {
    frame = null;
    panel = new LibraryViewerPanel (this, null);
    
    add (panel);
  } 

  
  /**
   * Run the library viewer frontend.
   */
  
  public FrontendModulePanel
  runFrontend (FrontendMaster master)
  { 
    dd = null;
    
    try
      {
        dd = new DatabaseDriver (config, UserClass.READ_ONLY);
      }
    catch (MissingCredentialsException e)
      {
        // TODO: Privilege de-escalation
        FrontendError.reportFatal (e.getMessage (), frame);
      }
    catch (Exception f)
      {
        FrontendError.reportFatal (f.getMessage (), frame);
      }
    
    panel = new LibraryViewerPanel (this, master);
    return panel;
  }


  /**
   * Do a library search.
   * 
   * This will update the library list to reflect the results of the 
   * search.
   * 
   * @param search  The string fragment to use in searches.
   *                Cannot be empty or null.
   *                
   * @throws        EmptySearchException if the search string is 
   *                empty or null (from LibraryUtils.search).
   */
  
  public void
  doSearch (String search) throws EmptySearchException
  {
    try
      {
        libraryList = LibraryUtils.search (dd, search);
      }
    catch (QueryFailureException e)
      {
        FrontendError.reportFatal (e.getMessage (), frame);
      }
  }

  
  /**
   * @return  the current library list.
   */

  public List<LibraryItem> 
  getLibraryList ()
  {
    return libraryList;
  }
}
