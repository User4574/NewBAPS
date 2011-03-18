package uk.org.ury.library.viewer;

import java.util.ArrayList;
import java.util.List;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.database.exceptions.QueryFailureException;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;

import uk.org.ury.library.LibraryUtils;
import uk.org.ury.library.exceptions.EmptySearchException;
import uk.org.ury.library.item.LibraryItem;

public class LibraryViewer extends AbstractFrontendModule
{
  /**
   * 
   */
  
  private static final long serialVersionUID = -2782366476480563739L;
  private DatabaseDriver dd;
  private List<LibraryItem> libraryList;
  private LibraryViewerPanel panel;
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
    
    libraryList = new ArrayList<LibraryItem> ();
    panel = null;
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
        master.fatalError (e.getMessage ());
      }
    catch (Exception f)
      {
        master.fatalError (f.getMessage ());
      }
    
    try
      {
        panel = new LibraryViewerPanel (this, master);
      }
    catch (UICreationFailureException e)
      {
        master.fatalError (e.getMessage ());
      }
  
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
   *                
   * @throws        QueryFailureException if the search query 
   *                fails (from LibraryUtils.search).
   */
  
  public void
  doSearch (String search)
  throws EmptySearchException, QueryFailureException
  {
    libraryList = LibraryUtils.search (dd, search);
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
