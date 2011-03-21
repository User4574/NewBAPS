/*
 * LibraryViewerPanel.java
 * -----------------------
 * 
 * Part of the URY Frontend Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.frontend.modules.library;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import uk.org.ury.common.library.exceptions.EmptySearchException;
import uk.org.ury.common.protocol.exceptions.InvalidMessageException;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.HintField;
import uk.org.ury.frontend.exceptions.UICreationFailureException;

/**
 * Frontend panel providing access to an underlying library viewer.
 * 
 * @author Matt Windsor
 * @author Nathan Lasseter
 */
public class LibraryViewerPanel extends FrontendModulePanel {
    /**
     * 
     */
    private static final long serialVersionUID = -2441616418398056712L;

    /* Panel widgets exposed by the SwiXML user interface. */

    private JTable resultsTable;
    private JScrollPane resultsPane;

    private JPanel messagePanel;
    private JLabel messageLabel;

    private JPanel searchingPanel;
    private JLabel searchingLabel;

    private JTextField searchField;
    private JButton searchButton;
    private JLabel searchForLabel;

    private HintField hint;

    /*
     * This contains the last search failure message, for use in letting the
     * user know what happened.
     */

    private String searchFailureMessage;

    // Resource bundle.

    private ResourceBundle rb;

    /**
     * Construct a new LibraryViewerPanel.
     * 
     * @param viewer
     *            The LibraryViewer controlling this LibraryViewerPanel.
     * 
     * @param master
     *            The FrontendMaster driving the frontend.
     * 
     * @throws UICreationFailureException
     *             if the UI creation fails.
     */

    public LibraryViewerPanel(LibraryViewer viewer, FrontendMaster master)
	    throws UICreationFailureException {
	/*
	 * The UI implementation is contained in library_viewer_gui.xml.
	 * 
	 * See this file for more details.
	 */
	super(viewer, "library_viewer_gui.xml", master);

	// Fill in locale-specific data.
	rb = ResourceBundle
		.getBundle("uk.org.ury.frontend.modules.library.LibraryViewer");

	searchFailureMessage = rb.getString("ERR_UNKNOWN");

	searchingLabel.setText(rb.getString("MSG_SEARCHING"));
	searchForLabel.setText(rb.getString("LBL_SEARCHFOR"));
	searchButton.setText(rb.getString("BTN_SEARCH"));
	hint.setText(rb.getString("HINT"));

	// Fine-tune table
	resultsTable.setAutoCreateRowSorter(true);
    }

    /**
     * @return the name of the panel.
     * 
     * @see uk.org.ury.frontend.FrontendModulePanel#getModuleName()
     */
    @Override
    public String getModuleName() {
	return rb.getString("MODULE_NAME");
    }

    /**
     * Action method for performing a search, bound by the UI XML manifest to
     * the search field and button.
     */
    public void search() {
	/*
	 * We can't let the user search while another search is going on, but
	 * it's not good to let the search "freeze" the UI.
	 * 
	 * Hence the search function disables all sensitive parts of the
	 * interface and launches a search as a background process.
	 * 
	 * We also swap the results table or no-results panel out for a panel
	 * that says "Searching...", in the interests of user-friendliness.
	 */
	searchField.setEnabled(false);
	searchButton.setEnabled(false);
	resultsPane.setVisible(false);
	messagePanel.setVisible(false);
	searchingPanel.setVisible(true);
	searchingLabel.setText(String.format(rb.getString("MSG_SEARCHING"),
		searchField.getText()));

	final LibraryViewer master = (LibraryViewer) getModule();

	SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
	    private String searchTerm = "";

	    /**
	     * Perform a task in a separate thread from event-dispatch.
	     * 
	     * In this case, perform a search.
	     * 
	     * @return whether or not the search was successful.
	     */
	    @Override
	    public Boolean doInBackground() {
		searchTerm = searchField.getText();

		try {
		    master.doSearch(searchTerm);
		} catch (InvalidMessageException e) {
		    searchFailureMessage = String.format(
			    rb.getString("ERR_SEARCH_FAILED"), searchTerm,
			    e.getMessage());
		    return false;
		} catch (EmptySearchException e) {
		    searchFailureMessage = rb.getString("ERR_EMPTY_SEARCH");
		    return false;
		}

		return true;
	    }

	    /**
	     * Perform post-search cleanup and finalisation.
	     */
	    @Override
	    public void done() {
		// Figure out whether or not the search succeeded.
		boolean hasSucceeded = false;

		try {
		    hasSucceeded = this.get();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    searchFailureMessage = String.format(
			    rb.getString("ERR_SEARCH_FAILED"), searchTerm,
			    e.getMessage());
		}

		/*
		 * Re-enable widgets and swap panels according to whether or not
		 * results were found.
		 */
		searchField.setEnabled(true);
		searchButton.setEnabled(true);
		searchingPanel.setVisible(false);

		if (hasSucceeded == false) {
		    messageLabel.setText(searchFailureMessage);
		    messagePanel.setVisible(true);
		} else if (master.getLibraryList().size() == 0) {
		    messageLabel.setText(String.format(
			    rb.getString("ERR_NO_RESULTS"), searchTerm));
		    messagePanel.setVisible(true);
		} else {
		    // Force table update with new results.
		    resultsTable.setModel(new LibraryTableModel(master
			    .getLibraryList()));

		    messagePanel.setVisible(false);
		    resultsPane.setVisible(true);
		}
	    }
	};

	worker.execute();
    }
}
