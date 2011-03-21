/*
 * ShowUtils.java
 * ------------------
 * 
 * Part of the URY Backend Platform
 * 
 * V0.00  2011/03/21
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.show;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.exceptions.QueryFailureException;

import uk.org.ury.show.item.ShowItem;
import uk.org.ury.show.item.ShowItemProperty;

/**
 * A set of common utility routines to facilitate the extraction of show items
 * from the show storage areas of the URY database.
 * 
 * @author Matt Windsor
 */
public class ShowUtils {
    /**
     * The number of channels reserved for show items.
     * 
     * TODO: move this somewhere more appropriate.
     */
    public static final int NUM_CHANNELS = 3;

    // Maximum number of results to pull from database
    private static final int MAX_RESULTS = 50;

    // Resource bundle (for exception reasons)
    private static ResourceBundle rb = ResourceBundle.getBundle(ShowUtils.class
	    .getPackage().getName() + ".ShowUtils");

    /**
     * Return the names of the public track folders, or "bins".
     * 
     * @param db
     *            The database to query.
     * 
     * @return a list of the public folder names. The list may be empty.
     * 
     * @throws IllegalArgumentException
     *             if the database is null, the show ID is negative or the
     *             channel index falls out of bounds.
     * 
     * @throws QueryFailureException
     *             if the database backend yielded an error while executing the
     *             search query.
     */
    public static List<String> getPublicFolders(DatabaseDriver db)
	    throws QueryFailureException {
	if (db == null)
	    throw new IllegalArgumentException(
		    rb.getString("ERR_DB_HANDLE_IS_NULL"));

	List<String> results = new ArrayList<String>();

	ResultSet rs = null;

	try {
	    rs = db.executeQuery("SELECT share AS name, description"
		    + " FROM baps_filefolder" + " WHERE baps_filefolder.public"
		    + "    = TRUE" + " ORDER BY filefolderid ASC", MAX_RESULTS);
	} catch (SQLException e1) {
	    throw new QueryFailureException(e1.getMessage());
	}

	try {
	    while (rs.next()) {
		results.add(rs.getString(2));
	    }
	} catch (SQLException e) {
	    throw new QueryFailureException(e.getMessage());
	}

	return results;
    }

    /**
     * Given a show and a channel, retrieve a list of all show items bound to
     * that channel for the show.
     * 
     * @param db
     *            The database to query.
     * 
     * @param showID
     *            The unique number that identifies the show.
     * 
     * @param channel
     *            The index of the channel to query.
     * 
     * @return a list of ShowItems extracted from the show and channel. The list
     *         may be empty.
     * 
     * @throws IllegalArgumentException
     *             if the database is null, the show ID is negative or the
     *             channel index falls out of bounds.
     * 
     * @throws QueryFailureException
     *             if the database backend yielded an error while executing the
     *             search query.
     */
    public static List<ShowItem> getChannelList(DatabaseDriver db, int showID,
	    int channel) throws QueryFailureException {
	if (db == null)
	    throw new IllegalArgumentException(
		    rb.getString("ERR_DB_HANDLE_IS_NULL"));

	if (showID < 0)
	    throw new IllegalArgumentException(
		    rb.getString("ERR_NEGATIVE_SHOW_ID"));

	if (channel < 0 || channel >= NUM_CHANNELS)
	    throw new IllegalArgumentException(
		    rb.getString("ERR_CH_OUT_OF_BOUNDS"));

	List<ShowItem> results = new ArrayList<ShowItem>();

	ResultSet rs = null;

	Object[] params = { showID, channel };

	try {
	    rs = db.executeQuery("SELECT name1, name2, position"
		    + " FROM baps_show" + " INNER JOIN baps_listing"
		    + "    ON baps_show.showid"
		    + "        = baps_listing.showid"
		    + " INNER JOIN baps_item"
		    + "    ON baps_listing.listingid"
		    + "        = baps_item.listingid"
		    + " WHERE baps_show.showid" 
		    + "    = ?"
		    + " AND baps_listing.channel"
		    + "    = ?"
		    + " ORDER BY position ASC", params, MAX_RESULTS);
	} catch (SQLException e) {
	    throw new QueryFailureException(e.getMessage());
	}

	try {
	    while (rs.next()) {
		results.add(translateRow(rs));
	    }
	} catch (SQLException e) {
	    throw new QueryFailureException(e.getMessage());
	}
	return results;
    }

    /**
     * Translate a row retrieved from the database into a ShowItem.
     * 
     * @param rs
     *            The result-set, or database cursor, pointing to the row to
     *            translate.
     * 
     * @return A new ShowItem containing the properties extracted from the
     *         translated row.
     */
    private static ShowItem translateRow(ResultSet rs) {
	// Translate SQL columns into a list of properties.

	HashMap<ShowItemProperty, String> properties = new HashMap<ShowItemProperty, String>();

	for (ShowItemProperty p : ShowItemProperty.values()) {
	    try {
		properties.put(p, rs.getString(p.sql));
	    } catch (SQLException e) {
		// Ignore this, as it is almost certainly just a non-existent
		// property.
	    }
	}

	return new ShowItem(properties);
    }
}