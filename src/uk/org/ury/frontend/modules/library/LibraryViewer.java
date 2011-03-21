/*
 * LibraryViewer.java
 * ------------------
 * 
 * Part of the URY Frontend Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.frontend.modules.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ury.common.library.exceptions.EmptySearchException;
import uk.org.ury.common.library.item.LibraryItem;
import uk.org.ury.common.library.item.LibraryItemProperty;
import uk.org.ury.common.protocol.Directive;
import uk.org.ury.common.protocol.ProtocolUtils;
import uk.org.ury.common.protocol.exceptions.DecodeFailureException;
import uk.org.ury.common.protocol.exceptions.InvalidMessageException;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.client.Client;
import uk.org.ury.frontend.exceptions.UICreationFailureException;

/**
 * Module for investigating the track library.
 * 
 * The <code>LibraryViewer</code> and its corresponding user 
 * interface, <code>LibraryViewerPanel</code>, provide a 
 * user interface for querying the server's library services 
 * for track information.
 * 
 * Subclasses of this module provide editing features for 
 * the track library.
 * 
 * @author Matt Windsor
 */
public class LibraryViewer extends AbstractFrontendModule {
    /**
     * 
     */
    private static final long serialVersionUID = -2782366476480563739L;
    private List<LibraryItem> libraryList;
    private LibraryViewerPanel panel;

    /**
     * Constructs a new LibraryViewer as a frontend object.
     */
    public LibraryViewer() {
	libraryList = new ArrayList<LibraryItem>();
	panel = null;
    }

    /**
     * Runs the library viewer frontend.
     */
    @Override
    public FrontendModulePanel runFrontend(FrontendMaster master) {
	try {
	    panel = new LibraryViewerPanel(this, master);
	} catch (UICreationFailureException e) {
	    master.fatalError(e.getMessage());
	}

	return panel;
    }

    /**
     * Does a library search.
     * 
     * This will update the library list to reflect the results of the search.
     * 
     * @param search
     *            The string fragment to use in searches. Cannot be empty or
     *            null.
     * 
     * @throws EmptySearchException
     *             if the search string is empty or null (from
     *             LibraryUtils.search).
     * 
     * @throws InvalidMessageException
     *             if the response from the server is invalid.
     */
    public void doSearch(String search) throws EmptySearchException,
	    InvalidMessageException {
	// TODO: fan out?

	if (search == null || search == "")
	    throw new EmptySearchException();

	Client cl = new Client();
	Map<?, ?> response = null;
	libraryList.clear();

	try {
	    response = cl
		    .get("/library/tracks?search="
			    + search);
	} catch (DecodeFailureException e) {
	    throw new InvalidMessageException(e.getMessage());
	}

	/*
	 * Check to see if this is Map<String, ?> by looking for the status,
	 * which should always be in a valid response.
	 */

	if (ProtocolUtils.responseIsOK(response) == false)
	    throw new InvalidMessageException(
		    (String) response.get(Directive.REASON.toString()));

	// Should contain a list of items, even if there are no items.
	if (response.containsKey(Directive.ITEMS.toString()) == false)
	    throw new InvalidMessageException("No item set returned.");

	if ((response.get(Directive.ITEMS.toString()) instanceof List<?>) == false)
	    throw new InvalidMessageException("Malformed item list.");

	for (Object obj : (List<?>) response.get(Directive.ITEMS.toString())) {
	    Map<LibraryItemProperty, String> properties = new HashMap<LibraryItemProperty, String>();

	    if (obj instanceof Map<?, ?> == false)
		throw new InvalidMessageException("Malformed item.");

	    Set<?> keySet = ((Map<?, ?>) obj).keySet();

	    // Check to make sure this item has only String-String mappings.
	    for (Object key : keySet) {
		if ((key instanceof String && ((Map<?, ?>) obj).get(key) instanceof String) == false)
		    throw new InvalidMessageException("Not a valid property.");
		else if (LibraryItemProperty.valueOf((String) key) == null)
		    throw new InvalidMessageException("Property type " + key
			    + " not recognised.");
		else
		    properties.put(LibraryItemProperty.valueOf((String) key),
			    (String) ((Map<?, ?>) obj).get(key));

	    }

	    libraryList.add(new LibraryItem(properties));
	}
    }

    /**
     * @return the current library list.
     */

    public List<LibraryItem> getLibraryList() {
	return libraryList;
    }
}
