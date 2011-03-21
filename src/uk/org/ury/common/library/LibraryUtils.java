/**
 * 
 */
package uk.org.ury.common.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ury.backend.database.DatabaseDriver;
import uk.org.ury.backend.database.exceptions.QueryFailureException;
import uk.org.ury.common.library.exceptions.EmptySearchException;
import uk.org.ury.common.library.item.LibraryItem;
import uk.org.ury.common.library.item.LibraryItemProperty;



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
}
