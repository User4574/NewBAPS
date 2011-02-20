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

import uk.org.ury.library.LibraryItem;

import uk.org.ury.library.LibraryUtils;

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
  private static ConfigReader config;
  
  
  /**
   * Main function, for running this module as a standalone 
   * application.
   * 
   * @param args  The command-line argument array.
   */
  
  public static void
  main (String[] args)
  {
	  try {
		  config = new ConfigReader("res/conf.xml");
	  }
	  catch(MissingCredentialsException e) {
		  System.out.println(e);
	  }
    LibraryViewer lv = new LibraryViewer ();
    lv.runFrontendInFrame ();
  }
  
  
  /**
   * Construct a new LibraryViewer.
   */
  
  public
  LibraryViewer ()
  {
    frame = null;
    libraryList = new ArrayList<LibraryItem> ();
    panel = new LibraryViewerPanel (this);
  }
  
  
  /**
   * Initialise the library viewer frontend as an applet.
   */
  
  public void
  init ()
  {
    frame = null;
    libraryList = new ArrayList<LibraryItem> ();
    panel = new LibraryViewerPanel (this);
    
    
    try
    {
      SwingUtilities.invokeAndWait (new Runnable ()
      {
        public void
        run ()
        {
          panel.setOpaque (true);
          setContentPane (panel);
          
          runFrontend ();
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
    panel = new LibraryViewerPanel (this);
    
    add (panel);
  } 
  
  
  /**
   * Run the library viewer frontend in a FrontendFrame.
   */
  
  public void
  runFrontendInFrame ()
  {
    FrontendFrame frame = new FrontendFrame (panel);
    this.frame = frame;
    
    runFrontend ();
  }
  
  
  /**
   * Run the library viewer frontend.
   */
  
  private void
  runFrontend ()
  { 
    dd = null;
    
    try
      {
        dd = new DatabaseDriver(config, UserClass.READ_ONLY);
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
  }


  /**
   * Do a library search.
   * 
   * This will update the library list to reflect the results of the 
   * search.
   * 
   * @param title   The title fragment to include in the search.  
   *                Can be empty or null.
   *                
   * @param artist  The artist fragment to include in the search.
   *                Can be empty or null.
   */
  
  public void
  doSearch (String search)
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
