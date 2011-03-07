/**
 * 
 */
package uk.org.ury.frontend;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * A frame that hosts a FrontendModulePanel, used for serving frontend 
 * panels in a window (application mode).
 * 
 * @author Matt Windsor
 *
 */

public class FrontendFrame extends JFrame implements FrontendMaster
{
  /**
   * 
   */
  
  private static final long serialVersionUID = 740928181256928433L;
  
  private FrontendBanner banner;
  private FrontendModulePanel child;
  private FrontendControlPanel cpanel;
  
  public
  FrontendFrame (FrontendModulePanel parent)
  {
    super (parent.getName ());
    
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    this.child = parent;
    
    Container cp = getContentPane ();
    
    // Banner
    
    banner = new FrontendBanner (parent.getName ());
    
    // Composition
    
    cp.add (banner, BorderLayout.NORTH);
    cp.add (parent, BorderLayout.CENTER);
    
    setPreferredSize (new Dimension (640, 480));
    
    pack ();
    setVisible (true);
  }


  /**
   * Load a module into the frontend frame.
   * 
   * Loading will fail with a fatal error if the class is not found,
   * or is not an implementor of FrontendModule.
   * 
   * @param moduleName  The fully qualified class-name of the module,
   *                    minus the leading "uk.org.ury." domain.
   */
  
  @Override
  public void
  loadModule (String moduleName)
  {
    Class<?> moduleClass = null;
    
    try
      {
        moduleClass = Class.forName ("uk.org.ury." + moduleName);
      }
    catch (ClassNotFoundException e)
      {
        FrontendError.reportFatal ("Could not load module: " + e.getMessage (), this);
      }

    
    if (FrontendModule.class.isAssignableFrom (moduleClass))
      {
        FrontendModulePanel temp = child;
        
        try
          {
            child = ((FrontendModule) moduleClass.newInstance ()).runFrontend (this);         
          }
        catch (InstantiationException e)
          {
            FrontendError.reportFatal ("Could not load module: " + e.getMessage (), this);
          }
        catch (IllegalAccessException e)
          {
            FrontendError.reportFatal ("Could not load module: " + e.getMessage (), this);
          }
        
        remove (temp);
        add (child);
        
        banner.setTitle (child.getName ());
        pack ();
      }
  }
  
  
  /**
   * Load a module into the frontend frame, additionally installing 
   * a control panel to communicate with the previous module.
   * 
   * Loading will fail with a fatal error if the class is not found,
   * or is not an implementor of FrontendModule.
   * 
   * @param moduleName  The fully qualified class-name of the module,
   *                    minus the leading "uk.org.ury." domain.
   *                 
   * @param cPanelName  The fully qualified class-name of the control
   *                    panel to install, minus the leading 
   *                    "uk.org.ury." domain.
   */
  
  @Override
  public void
  loadModule (String moduleName, String cPanelName)
  {
    FrontendModulePanel newParent = child;
    loadModule (moduleName);
    FrontendModulePanel newChild = child;
    
    loadControlPanel (cPanelName, newParent, newChild);
  }
  
  
  /**
   * Load and install a control panel class given the class name.
   * 
   * @param cPanelName  The fully qualified class-name of the control
   *                    panel to load, minus the leading 
   *                    "uk.org.ury." domain.
   *                    
   * @param parent      The parent panel in the relationship modelled 
   *                    by the control panel interface.
   *                    
   * @param child       The child panel in the relationship modelled
   *                    by the control panel interface.
   */
 
  private void
  loadControlPanel (String cPanelName, FrontendModulePanel parent, 
                    FrontendModulePanel child)
  {
    Class<?> cPanelClass = null;
    
    try
      {
        cPanelClass = Class.forName ("uk.org.ury." + cPanelName);
      }
    catch (ClassNotFoundException e)
      {
        FrontendError.reportFatal ("Could not load control panel: " + e.getMessage (), this);
      }

    
    if (FrontendControlPanel.class.isAssignableFrom (cPanelClass))
      {
        FrontendControlPanel temp = cpanel;
        
        try
          {
            cpanel = ((FrontendControlPanel) cPanelClass.newInstance ());         
          }
        catch (InstantiationException e)
          {
            FrontendError.reportFatal ("Could not load module: " + e.getMessage (), this);
          }
        catch (IllegalAccessException e)
          {
            FrontendError.reportFatal ("Could not load module: " + e.getMessage (), this);
          }
        
        if (temp != null)
          remove (temp);
        
        cpanel.setPanels (parent, child);
        cpanel.setMaster (this);
        cpanel.setPreviousCPanel (temp);
        
        add (cpanel, BorderLayout.SOUTH);
        pack ();
      }    
  }

  
  /**
   * Restore an existing module and control panel into the frontend 
   * frame.
   * 
   * @param mpanel  The module panel to restore.
   * 
   * @param cpanel  The control panel to restore, if any.  A null 
   *                value signifies a lack of control panel.
   * 
   * @throws        IllegalArgumentException if the mpanel is null.
   */
  
  @Override
  public void
  restoreModule (FrontendModulePanel mpanel,
                 FrontendControlPanel cpanel)
  {
    if (mpanel == null)
      throw new IllegalArgumentException ("mpanel is null.");
    
    remove (child);
    remove (this.cpanel);
    
    child = mpanel;
    add (child);
    banner.setTitle (child.getName ());
    
    if (cpanel != null)
      add (cpanel, BorderLayout.SOUTH);
    
    this.cpanel = cpanel;
    
    pack ();
    repaint ();
  }
}
