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
   * @param db      The database to query.
   * 
   * @param search  The search fragment to include in the search.
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
  search (DatabaseDriver db, String search)
  throws QueryFailureException
  {
    if (db == null)
      throw new IllegalArgumentException ("Database handle is null.");

    if (search == null)
      throw new IllegalArgumentException ("Search string is null.");
    
    if (search.equals(""))
    	//TODO: Be nicer about this
      System.exit(1);
    
    
    ResultSet rs = null;
    List<LibraryItem> results = new ArrayList<LibraryItem> ();
    Object[] params = {"%" + search + "%", "%" + search + "%", "%" + search + "%"};
    
    try
      {
        rs = db.executeQuery (
            "SELECT r.title AS album, t.title,"
            + " t.artist, recordlabel AS label, status, media AS medium, format,"
            + " datereleased, EXTRACT(EPOCH FROM dateadded) as dateadded,"
            + " EXTRACT(EPOCH FROM datetime_lastedit) AS dateedited,"
            + " shelfletter, shelfnumber, cdid, memberid_add, memberid_lastedit,"
            + " digitised,"
            + " a.fname AS fnameadd, a.sname AS snameadd, b.fname AS fnameedit, b.sname AS snameedit"
            + " FROM rec_record AS r"
            + " INNER JOIN rec_track AS t ON (r.recordid = t.recordid)"
            + " INNER JOIN member AS a ON (a.memberid = r.memberid_add)"
            + " LEFT JOIN member AS b ON (b.memberid = r.memberid_lastedit)" 
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