/*
 * ShowViewer.java
 * ---------------
 * 
 * Part of the URY Frontend Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.frontend.modules.show;

import java.util.List;

import uk.org.ury.backend.config.ConfigReader;
import uk.org.ury.backend.database.DatabaseDriver;
import uk.org.ury.backend.database.UserClass;
import uk.org.ury.backend.database.exceptions.MissingCredentialsException;
import uk.org.ury.backend.database.exceptions.QueryFailureException;
import uk.org.ury.common.show.ShowChannel;
import uk.org.ury.common.show.ShowUtils;
import uk.org.ury.common.show.item.ShowItem;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;

/**
 * Frontend module for viewing show details.
 * 
 * This serves as the base for the show playout and editor classes, but can be
 * used stand-alone as an (admittedly rather pointless) module.
 * 
 * @author Matt Windsor
 * 
 */
public class ShowViewer extends AbstractFrontendModule {
    /**
     * 
     */

    private static final long serialVersionUID = -2782366476480563739L;
    private DatabaseDriver dd;
    private ShowChannel[] channels;
    private ShowViewerPanel panel;
    private ConfigReader config;

    /**
     * Construct a new ShowViewer as a frontend object.
     */
    public ShowViewer() {
	channels = new ShowChannel[ShowUtils.NUM_CHANNELS];
    }

    /**
     * Run the library viewer frontend.
     */
    @Override
    public FrontendModulePanel runFrontend(FrontendMaster master) {
	dd = null;
	config = null;

	try {
	    config = new ConfigReader(master.getResourceDirectory()
		    + "conf.xml");
	} catch (MissingCredentialsException e) {
	    System.out.println(e);
	}

	try {
	    dd = new DatabaseDriver(config, UserClass.READ_ONLY);
	} catch (MissingCredentialsException e) {
	    // TODO: Privilege de-escalation
	    master.fatalError(e.getMessage());
	} catch (Exception f) {
	    master.fatalError(f.getMessage());
	}

	for (int i = 0; i < channels.length; i++) {
	    channels[i] = new ShowChannel();

	    try {
		for (ShowItem item : ShowUtils.getChannelList(dd, 4696, i)) {
		    channels[i].add(item);
		}
	    } catch (QueryFailureException e) {
		master.fatalError(e.getMessage());
	    }
	}

	try {
	    panel = new ShowViewerPanel(this, master);
	} catch (UICreationFailureException e) {
	    master.fatalError(e.getMessage());
	}

	return panel;
    }

    /**
     * @return the channel array.
     */
    public ShowChannel[] getChannels() {
	// TODO Auto-generated method stub
	return channels;
    }

    /**
     * @return the list of bin names.
     * 
     * @throws QueryFailureException
     *             if the underlying database query fails.
     */
    public List<String> getBins() throws QueryFailureException {
	return ShowUtils.getPublicFolders(dd);
    }
}
