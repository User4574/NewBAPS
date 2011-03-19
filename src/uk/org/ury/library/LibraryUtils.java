/**
 * 
 */
package uk.org.ury.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.exceptions.QueryFailureException;

import uk.org.ury.library.exceptions.EmptySearchException;
import uk.org.ury.library.item.LibraryItem;
import uk.org.ury.library.item.LibraryItemProperty;
import uk.org.ury.server.protocol.Directive;


/**
 * A set of common utility routines to facilitate the extraction of 
 * library items from the library areas of the URY database. 
 * 
 * @author  Matt Windsor
 */

public class LibraryUtils
{ 
  /**
   * Perform a library search.
   * 
   * @param db      The database to query.
   * 
   * @param search  The search fragment to include in the search.
   *                Can be empty or null.
   *                
   * @throws        IllegalArgumentException if the search term is 
   *                are null.
   *                
   * @throws        QueryFailureException if the database backend 
   *                yielded an error while executing the search 
   *                query.
   *                
   * @throws        EmptySearchException if the search term is 
   *                empty (to be handled as a user error).
   *                
   * @return        a list of LibraryItems matching the search terms.
   */
  
  public static List<LibraryItem>
  search (DatabaseDriver db, String search)
  throws QueryFailureException, EmptySearchException
  {
    if (db == null)
      throw new IllegalArgumentException ("Database handle is null.");

    if (search == null)
      throw new IllegalArgumentException ("Search string is null.");

    List<LibraryItem> results = new ArrayList<LibraryItem> ();
    
    
    // Return empty set if the search term is null.
    
    if (search.equals(""))
    	throw new EmptySearchException ();
    
    
    ResultSet rs = null;

    Object[] params = {"%" + search + "%", "%" + search + "%", "%" + search + "%"};
    
    try
      {
        rs = db.executeQuery (
            "SELECT r.title AS album, t.title,"
            + " t.artist, recordlabel AS label, status, media AS medium, format,"
            + " datereleased, EXTRACT(EPOCH FROM dateadded) as dateadded,"
            + " EXTRACT(EPOCH FROM datetime_lastedit) AS dateedited,"
            + " shelfletter, shelfnumber, cdid, digitised, clean"
            + " FROM rec_record AS r"
            + " INNER JOIN rec_track AS t ON (r.recordid = t.recordid)"
            + " WHERE t.title ILIKE ?"
            + " OR t.artist ILIKE ?"
            + " OR r.title ILIKE ?"
            + " ORDER BY digitised DESC, medium ASC, r.title ASC,"
            + " t.artist ASC, t.title ASC;", params, 50);
      }
    catch (SQLException e)
      {
        throw new QueryFailureException (e.getMessage ());
      }
    
    try
      {
        while (rs.next ())
          {
            results.add (translateRow (rs));
          }
      }
    catch (SQLException e)
      { 
        throw new QueryFailureException (e.getMessage ());
      }
  
    return results;
  }
  
  
  /**
   * Translate a row retrieved from the database into a LibraryItem.
   * 
   * @param rs  The result-set, or database cursor, pointing to the 
   *            row to translate.
   *            
   * @return    A new LibraryItem containing the properties extracted 
   *            from the translated row.
   */
  
  private static LibraryItem
  translateRow (ResultSet rs)
  {
    // Translate SQL columns into a list of properties.
            
    Map<LibraryItemProperty, String> properties = new HashMap<LibraryItemProperty, String> ();
            
    for (LibraryItemProperty p : LibraryItemProperty.values ())
      {
        try
          {
            properties.put (p, rs.getString (p.sql));
          }
        catch (SQLException e)
          {
            // Ignore this, as it is almost certainly just a non-existent 
            // property.
          }
      }
    
  
    return new LibraryItem (properties);
  }
  
  
  /**
   * Construct items from a server response body.
   * 
   * @param response  The list of strings that constitute the response.
   * 
   * @return          a list of LibraryItems corresponding to the item 
   *                  stanzas in the response.
   */
  
  public static List<LibraryItem>
  extractItemsFromResponse (List<String> response)
  {
    List<LibraryItem> result = new ArrayList<LibraryItem> ();
    
    boolean inItem = false;
    List<String> itemBuffer = new ArrayList<String> ();
    
    for (String line : response)
      {
        if (inItem == false)
          {
            if (line.startsWith (Directive.ITEM_START.toString ()))
              {
                inItem = true;
                itemBuffer.clear ();
              }
          }
        
        if (inItem == true)
          {
            itemBuffer.add (line);
            
            if (line.startsWith (Directive.ITEM_END.toString ()))
              {
                inItem = false;
                result.add (createItemFromResponse (itemBuffer));
              }
          }
      }
    
    return result;
  }
  
  
  /**
   * Construct a new item from a server response fragment.
   * 
   * @param response  The list of strings that constitutes the response.
   *                  The list must begin with Directive.ITEM_START and 
   *                  end with Directive.ITEM_END's string 
   *                  representations and otherwise contain solely 
   *                  Directive.ITEM_PROPERTY lines.
   *                  
   * @return          a LibraryItem embodying the properties described 
   *                  in the response fragment.
   *                  
   * @throws          IllegalArgumentException if the response is 
   *                  malformed or null, or if the instantiation of 
   *                  DatabaseItem does not use String as its data type.
   */
  
  public static LibraryItem
  createItemFromResponse (List<String> response)
  {
    // TODO: More appropriate exceptions.
    
    if (response == null)
      throw new IllegalArgumentException ("Response is null.");
    else if (response.get (0).equals (Directive.ITEM_START.toString ())
             && response.get (response.size () - 1)
                              .equals (Directive.ITEM_END.toString ()))
      {
        // Response of size 2 must be blank.
        if (response.size () <= 2)
          throw new IllegalArgumentException ("Blank response.");
        
        Map<LibraryItemProperty, String> properties = new HashMap<LibraryItemProperty, String> ();
        
        for (int i = 0; i < response.size () - 1; i++)
          {
            // TODO: fan out impl. details to separate class
            if (response.get (i)
                .startsWith (Directive.ITEM_PROPERTY.toString ()))
              {
                String[] responseTuple = response.get (i).split (":=");
                
                properties.put (LibraryItemProperty.getFromSQL (responseTuple[1]), 
                                responseTuple[2]);
              }
          }
        
        return new LibraryItem (properties);
      }
    else
      throw new IllegalArgumentException ("Malformed response.");
  }
}
