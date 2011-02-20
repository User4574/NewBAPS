/**
 * 
 */
package uk.org.ury.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.exceptions.QueryFailureException;

import uk.org.ury.library.LibraryItem.LibraryProperty;


/**
 * A set of common utility routines to facilitate the extraction of 
 * library items from the library areas of the URY database. 
 * 
 * @author  Matt Windsor
 *
 */

public class LibraryUtils
{ 
  /**
   * Perform a library search.
   * 
   * Presently, the title and artist comparisons are logically ANDed.
   * 
   * @param db      The database to query.
   * 
   * @param title   The title fragment to include in the search.
   *                Can be empty or null.
   *                
   * @param artist  The artist fragment to include in the search.
   *                Can be empty or null.
   *                
   * @throws        IllegalArgumentException if db, title or artist 
   *                are null.
   *                
   * @throws        QueryFailureException if the database backend 
   *                yielded an error while executing the search 
   *                query.
   *                
   * @return        a list of LibraryItems matching the search terms.
   */
  
  public static List<LibraryItem>
  search (DatabaseDriver db, String title, String artist)
  throws QueryFailureException
  {
    if (db == null)
      throw new IllegalArgumentException ("Database handle is null.");

    if (title == null)
      throw new IllegalArgumentException ("Title string is null.");    

    if (artist == null)
      throw new IllegalArgumentException ("Artist string is null.");
    
    
    ResultSet rs = null;
    List<LibraryItem> results = new ArrayList<LibraryItem> ();
    Object[] params = {"%" + title + "%", "%" + artist + "%"};
    
    try
      {
        rs = db.executeQuery (
            "SELECT title, artist, recordlabel AS label, status, media AS medium, format,"
            + " datereleased, EXTRACT(EPOCH FROM dateadded) as dateadded,"
            + " EXTRACT(EPOCH FROM datetime_lastedit) AS dateedited,"
            + " shelfletter, shelfnumber, cdid, memberid_add, memberid_lastedit,"
            + " a.fname AS fnameadd, a.sname AS snameadd, b.fname AS fnameedit, b.sname AS snameedit"
            + " FROM rec_record AS r"
            + " INNER JOIN member AS a ON (a.memberid = r.memberid_add)"
            + " LEFT JOIN member AS b ON (b.memberid = r.memberid_lastedit)" 
            + " WHERE title ILIKE ?"
            + " AND artist ILIKE ?;", params, 50);
      }
    catch (SQLException e)
      {
        throw new QueryFailureException (e.getMessage ());
      }
    
    try
      {
        while (rs.next ())
          {
            // Translate SQL columns into a list of properties.
            
            HashMap<LibraryProperty, String> properties = new HashMap<LibraryProperty, String> ();
            
            for (LibraryProperty p : LibraryProperty.values ())
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
             
            results.add (new LibraryItem (properties));
          }
      }
    catch (SQLException e)
      { 
        throw new QueryFailureException (e.getMessage ());
      }
    
    return results;
  }
}