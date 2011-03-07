/**
 * 
 */
package uk.org.ury.show;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.exceptions.QueryFailureException;

import uk.org.ury.show.ShowItem;
import uk.org.ury.show.ShowItemProperty;


/**
 * A set of common utility routines to facilitate the extraction of 
 * show items from the show storage areas of the URY database. 
 * 
 * @author  Matt Windsor
 *
 */

public class ShowUtils
{ 
  /**
   * The number of channels reserved for show items.
   * 
   * TODO: move this somewhere more appropriate.
   */
  
  public static final int NUM_CHANNELS = 3;
  
  
  // Maximum number of results to pull from database
  private static final int MAX_RESULTS = 50;
  
  
  /**
   * Given a show and a channel, retrieve a list of all show items
   * bound to that channel for the show.
   * 
   * @param db       The database to query.
   * 
   * @param showID   The unique number that identifies the show.
   * 
   * @param channel  The index of the channel to query.
   *                
   * @throws        IllegalArgumentException if the database is 
   *                null, the show ID is negative or the 
   *                channel index falls out of bounds.
   *                
   * @throws        QueryFailureException if the database backend 
   *                yielded an error while executing the search 
   *                query.
   *                           
   * @return        a list of ShowItems extracted from the show and 
   *                channel.  The list may be empty.
   */
  
  public static List<ShowItem>
  getChannelList (DatabaseDriver db, int showID, int channel)
  throws QueryFailureException
  {
    if (db == null)
      throw new IllegalArgumentException ("Database handle is null.");

    if (showID < 0)
      throw new IllegalArgumentException ("Show ID is negative.");

    if (channel < 0 || channel >= NUM_CHANNELS)
      throw new IllegalArgumentException ("Channel index is out of bounds.");
    
    List<ShowItem> results = new ArrayList<ShowItem> ();
    
    
    ResultSet rs = null;

    Object[] params = {showID, channel};
    
    try
      {
        rs = db.executeQuery ("SELECT name1, name2, positionid"
                              + " FROM baps_show"
                              + " INNER JOIN baps_listing"
                              + "    ON baps_show.showid"
                              + "        = baps_listing.showid"
                              + " INNER JOIN baps_item"
                              + "    ON baps_listing.listingid"
                              + "        = baps_show.showid"
                              + " WHERE baps_show.showid"
                              + "    = ?"
                              + " AND baps_listing.channel"
                              + "    = ?", params, MAX_RESULTS);
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
  
  private static ShowItem
  translateRow (ResultSet rs)
  {
    // Translate SQL columns into a list of properties.
            
    HashMap<ShowItemProperty, String> properties = new HashMap<ShowItemProperty, String> ();
            
    for (ShowItemProperty p : ShowItemProperty.values ())
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
    
  
    return new ShowItem (properties);
  }
}