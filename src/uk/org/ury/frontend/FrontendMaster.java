/**
 * 
 */
package uk.org.ury.frontend;


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
   */
  
  public void
  loadModule (String moduleName);

  
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
   * @param cpanelName  The fully qualified class-name of the control
   *                    panel to install, minus the leading 
   *                    "uk.org.ury." domain.
   */
  
  public void 
  loadModule (String moduleName, String cPanelName);
  
  
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
}
