package uk.org.ury.library.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ury.client.Client;

import uk.org.ury.config.ConfigReader;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;

import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.database.exceptions.QueryFailureException;

import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;

import uk.org.ury.library.exceptions.EmptySearchException;

import uk.org.ury.library.item.LibraryItem;
import uk.org.ury.library.item.LibraryItemProperty;

import uk.org.ury.server.protocol.DecodeFailureException;
import uk.org.ury.server.protocol.Directive;
import uk.org.ury.server.protocol.Status;


/**
 * Module for investigating the track library.
 *
 * @author  Matt Windsor
 *
 */

public class LibraryViewer extends AbstractFrontendModule
{
  /**
   * 
   */
  
  private static final long serialVersionUID = -2782366476480563739L;
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
    // TODO: fan out?
    
    Client cl = new Client ();
    Map<?, ?> response = null;
    libraryList.clear ();
    
    try
      {
        response = cl.get ("/library/LibraryRequestHandler?function=search&search=" + search);
      }
    catch (DecodeFailureException e)
      {
        throw new QueryFailureException (e.getMessage ());
      }
    
    // Check to see if this is Map<String, ?> by looking for the status,
    // which should always be in a valid response.
    if (response.containsKey (Directive.STATUS.toString ()) == false
        || (response.get (Directive.STATUS.toString ()) instanceof String) == false)
      throw new QueryFailureException ("Status not provided.");
    
    if (((String) response.get (Directive.STATUS.toString ()))
        .equals (Status.OK.toString ()) == false)
      throw new QueryFailureException ((String) response.get (Directive.REASON.toString ()));

    // Should contain a list of items, even if there are no items.
    if (response.containsKey (Directive.ITEMS.toString ()) == false)
      throw new QueryFailureException ("No item set returned.");
      
    if ((response.get (Directive.ITEMS.toString ()) instanceof List<?>) == false)
      throw new QueryFailureException ("Malformed item list.");
    
    
    for (Object obj : (List<?>) response.get (Directive.ITEMS.toString ()))
      {
        Map<LibraryItemProperty, String> properties
          = new HashMap<LibraryItemProperty, String> ();
        
        if (obj instanceof Map<?, ?> == false)
          throw new QueryFailureException ("Malformed item.");

        Set<?> keySet = ((Map<?, ?>) obj).keySet ();   
        
        // Check to make sure this item has only String-String mappings.
        for (Object key : keySet)
          {
            if ((key instanceof String
                && ((Map<?, ?>) obj).get (key) instanceof String)
                == false)
              throw new QueryFailureException ("Not a valid property.");
            else if (LibraryItemProperty.valueOf ((String) key) == null)
              throw new QueryFailureException ("Property type "
                                               + key + " not recognised.");
            else
              properties.put (LibraryItemProperty.valueOf ((String) key), 
                              (String) ((Map<?, ?>) obj).get (key));
              
          }
        
        libraryList.add (new LibraryItem (properties));
      }
 
        //libraryList = LibraryUtils.search (dd, search);
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
