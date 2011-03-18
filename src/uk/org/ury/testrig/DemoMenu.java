package uk.org.ury.testrig;

import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;
import uk.org.ury.frontend.exceptions.LoadFailureException;


/**
 * Application frontend, for testing early-stage frontend code.
 * 
 * @author  Matt Windsor
 *
 */

public class DemoMenu extends AbstractFrontendModule
{
  /**
   * 
   */
  private static final long serialVersionUID = -5264235507636809476L;
  private FrontendMaster master;
  
  
  /**
   * 
   */
  
  public void
  loadModule (String module)
  {
    try
      {
        master.loadModule (module, "testrig.DemoControlPanel");
      }
    catch (LoadFailureException e)
      {
        master.fatalError (e.getMessage ());
      }
  }
  
  
  /**
   * Run the demo menu, creating a user interface.
   */
  
  public void
  run ()
  { 

  }

  
  /**
   * Run the demo menu in frontend mode.
   */

  @Override
  public FrontendModulePanel
  runFrontend (FrontendMaster master)
  {
    return new DemoMenuPanel (master);
  }
}
