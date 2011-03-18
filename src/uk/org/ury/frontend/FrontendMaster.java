/**
 * 
 */
package uk.org.ury.frontend;

import uk.org.ury.frontend.exceptions.LoadFailureException;


/**
 * Interface for classes providing the parent unit of a frontend 
 * session.
 * 
 * This includes the FrontendFrame used in application mode as 
 * well as applets.
 * 
 * @author  Matt Windsor
 */

public interface FrontendMaster
{
  /**
   * Load a module into the frontend frame.
   * 
   * Loading will fail with a fatal error if the class is not found,
   * or is not an implementor of FrontendModule.
   * 
   * @param moduleName  The fully qualified class-name of the module,
   *                    minus the leading "uk.org.ury." domain.
   *                    
   * @throws            LoadFailureException if the class is 
   *                    not found, or is not an implementor of 
   *                    FrontendModule.
   */
  
  public void
  loadModule (String moduleName)
  throws LoadFailureException;

  
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
   *                    
   * @throws            LoadFailureException if the class is 
   *                    not found, or is not an implementor of 
   *                    FrontendModule.
   */
  
  public void 
  loadModule (String moduleName, String cPanelName)
  throws LoadFailureException;
  
  
  /**
   * Restore an existing module and control panel into the frontend 
   * master.
   * 
   * @param mpanel  The module panel to restore.
   * 
   * @param cpanel  The control panel to restore.
   * 
   * @throws        IllegalArgumentException if either are null.
   */
  
  public void
  restoreModule (FrontendModulePanel mpanel, FrontendControlPanel cpanel);
  
  
  /**
   * Report a fatal error,
   * 
   * @param message  The message, eg the exception message, to report
   *                 to the user.
   */
  
  public void
  fatalError (String message);
  
  
  /**
   * Set up the frontend master's user interface.
   */
  
  public void
  setupUI ();
  
  
  /**
   * @return  the resource directory.
   */
  
  public String
  getResourceDirectory ();
}
