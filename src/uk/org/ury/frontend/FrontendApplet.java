/**
 * 
 */
package uk.org.ury.frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.org.ury.frontend.exceptions.LoadFailureException;
import uk.org.ury.testrig.Launcher;

/**
 * A frame that hosts a FrontendModulePanel, used for serving frontend panels in
 * a window (application mode).
 * 
 * @author Matt Windsor
 * 
 */

public class FrontendApplet extends JApplet implements FrontendMaster, Launcher {
    /**
   * 
   */

    private static final long serialVersionUID = 740928181256928433L;

    private FrontendModulePanel child;
    private FrontendControlPanel cpanel;

    /**
     * Main method.
     */

    @Override
    public void init() {
	try {
	    javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
		public void run() {
		    try {
			loadModule(DEFAULT_MODULE_NAME);
		    } catch (LoadFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    setupUI();
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println("createGUI didn't successfully complete");
	}
    }

    /**
     * Set up the user interface of the frame.
     */

    @Override
    public void setupUI() {
	try {
	    // Set System L&F
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (UnsupportedLookAndFeelException e) {
	    // handle exception
	} catch (ClassNotFoundException e) {
	    // handle exception
	} catch (InstantiationException e) {
	    // handle exception
	} catch (IllegalAccessException e) {
	    // handle exception
	}

	// Composition

	add(child, BorderLayout.CENTER);

	setPreferredSize(new Dimension(800, 600));
	setMinimumSize(new Dimension(800, 600));

	setVisible(true);
    }

    /**
     * Load a module into the frontend frame.
     * 
     * Loading will fail with a fatal error if the class is not found, or is not
     * an implementor of FrontendModule.
     * 
     * @param moduleName
     *            The fully qualified class-name of the module, minus the
     *            leading "uk.org.ury." domain.
     * 
     * @throws LoadFailureException
     *             if the class is not found, or is not an implementor of
     *             FrontendModule.
     */

    @Override
    public void loadModule(String moduleName) throws LoadFailureException {
	Class<?> moduleClass = null;

	try {
	    moduleClass = Class.forName("uk.org.ury." + moduleName);
	} catch (ClassNotFoundException e) {
	    throw new LoadFailureException("Could not load module: "
		    + e.getMessage());
	}

	if (FrontendModule.class.isAssignableFrom(moduleClass) == false) {
	    throw new LoadFailureException(
		    "Could not load module: Not a FrontendModule");
	} else {
	    FrontendModulePanel temp = child;

	    try {
		child = ((FrontendModule) moduleClass.newInstance())
			.runFrontend(this);
	    } catch (InstantiationException e) {
		throw new LoadFailureException("Could not load module: "
			+ e.getMessage());
	    } catch (IllegalAccessException e) {
		throw new LoadFailureException("Could not load module: "
			+ e.getMessage());
	    }

	    if (temp != null)
		remove(temp);

	    getContentPane().add(child, BorderLayout.CENTER);
	    child.setMaster(this);

	    repaint();
	}
    }

    /**
     * Load a module into the frontend frame, additionally installing a control
     * panel to communicate with the previous module.
     * 
     * Loading will fail with a fatal error if the class is not found, or is not
     * an implementor of FrontendModule.
     * 
     * @param moduleName
     *            The fully qualified class-name of the module, minus the
     *            leading "uk.org.ury." domain.
     * 
     * @param cPanelName
     *            The fully qualified class-name of the control panel to
     *            install, minus the leading "uk.org.ury." domain.
     * 
     * @throws LoadFailureException
     *             if the class is not found, or is not an implementor of
     *             FrontendModule.
     */

    @Override
    public void loadModule(String moduleName, String cPanelName)
	    throws LoadFailureException {
	FrontendModulePanel newParent = child;
	loadModule(moduleName);
	FrontendModulePanel newChild = child;

	loadControlPanel(cPanelName, newParent, newChild);
    }

    /**
     * Load and install a control panel class given the class name.
     * 
     * @param cPanelName
     *            The fully qualified class-name of the control panel to load,
     *            minus the leading "uk.org.ury." domain.
     * 
     * @param parent
     *            The parent panel in the relationship modelled by the control
     *            panel interface.
     * 
     * @param child
     *            The child panel in the relationship modelled by the control
     *            panel interface.
     * 
     * @throws LoadFailureException
     *             if the class is not found, or is not an implementor of
     *             FrontendControlPanel.
     */

    private void loadControlPanel(String cPanelName,
	    FrontendModulePanel parent, FrontendModulePanel child)
	    throws LoadFailureException {
	Class<?> cPanelClass = null;

	try {
	    cPanelClass = Class.forName("uk.org.ury." + cPanelName);
	} catch (ClassNotFoundException e) {
	    throw new LoadFailureException("Could not load control panel: "
		    + e.getMessage());
	}

	if (FrontendControlPanel.class.isAssignableFrom(cPanelClass)) {
	    FrontendControlPanel temp = cpanel;

	    try {
		cpanel = ((FrontendControlPanel) cPanelClass.newInstance());
	    } catch (InstantiationException e) {
		throw new LoadFailureException("Could not load control panel: "
			+ e.getMessage());
	    } catch (IllegalAccessException e) {
		throw new LoadFailureException("Could not load control panel: "
			+ e.getMessage());
	    }

	    if (temp != null)
		remove(temp);

	    cpanel.setPanels(parent, child);
	    cpanel.setMaster(this);
	    cpanel.setPreviousCPanel(temp);

	    add(cpanel, BorderLayout.SOUTH);
	    repaint();
	}
    }

    /**
     * Restore an existing module and control panel into the frontend frame.
     * 
     * @param mpanel
     *            The module panel to restore.
     * 
     * @param cpanel
     *            The control panel to restore, if any. A null value signifies a
     *            lack of control panel.
     * 
     * @throws IllegalArgumentException
     *             if the mpanel is null.
     */

    @Override
    public void restoreModule(FrontendModulePanel mpanel,
	    FrontendControlPanel cpanel) {
	if (mpanel == null)
	    throw new IllegalArgumentException("mpanel is null.");

	remove(child);
	remove(this.cpanel);

	child = mpanel;
	add(child);

	if (cpanel != null)
	    add(cpanel, BorderLayout.SOUTH);

	this.cpanel = cpanel;

	repaint();
    }

    /**
     * Report a fatal error,
     * 
     * @param message
     *            The message, eg the exception message, to report to the user.
     */

    @Override
    public void fatalError(String message) {
	FrontendError.reportFatal(message, this);
    }

    /**
     * @return the resource directory.
     */

    @Override
    public String getResourceDirectory() {
	return getCodeBase() + "res/";
    }
}