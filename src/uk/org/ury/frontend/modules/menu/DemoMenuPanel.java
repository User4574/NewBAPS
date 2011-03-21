package uk.org.ury.frontend.modules.menu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.LoadFailureException;

public class DemoMenuPanel extends FrontendModulePanel {
    /**
     * 
     */
    private static final long serialVersionUID = 5268978856289909262L;

    /**
     * Construct a new DemoMenuPanel.
     * 
     * @param inMaster
     *            The new frontend master of the panel, if any.
     */
    public DemoMenuPanel(FrontendMaster inMaster) {
	super(null, inMaster);

	setLayout(new GridLayout(2, 1));

	JButton lb = new JButton("Library Viewer Demo");
	JButton sb = new JButton("Show Viewer Demo");

	lb.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    master.loadModule("library.LibraryViewer",
			    "menu.DemoControlPanel");
		} catch (LoadFailureException e) {
		    master.fatalError(e.getMessage());
		}
	    }
	});

	sb.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    master.loadModule("show.ShowViewer",
			    "menu.DemoControlPanel");
		} catch (LoadFailureException e) {
		    master.fatalError(e.getMessage());
		}
	    }
	});

	add(lb);
	add(sb);
    }

    /**
     * @return the name of the module.
     */
    @Override
    public String getModuleName() {
	return "Demo Menu";
    }
}
