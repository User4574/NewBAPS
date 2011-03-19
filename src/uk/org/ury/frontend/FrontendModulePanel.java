/**
 * 
 */
package uk.org.ury.frontend;

import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * A frontend user interface panel.
 * 
 * All frontend user interfaces should subclass this as their main 
 * interface space, so that frontend panels can include each other 
 * and panels can be embedded into application frames or applets.
 * 
 * @author Matt Windsor
 *
 */

public abstract class FrontendModulePanel extends FrontendPanel
{
  /**
   * 
   */
  
  private static final long serialVersionUID = 5616222530691425635L;

  private FrontendModule module;
  
  
  /**
   * Construct a new, blank FrontendModulePanel.
   * 
   * @param module  the module that the panel is viewing.
   * 
   * @param master   The FrontendMaster driving the frontend.
   */
  
  public
  FrontendModulePanel (FrontendModule module, FrontendMaster master)
  { 
    super (master);
    this.module = module;
  }
  
  
  /**
   * Construct a FrontendModulePanel using an XML layout manifest.
   * 
   * @param module  the module that the panel is viewing.
   * 
   * @param xmlPath  The path, relative from this source file, to the
   *                 XML file from which this panel will read its 
   *                 layout.
   *                 
   * @param master   The FrontendMaster driving the frontend.
   * 
   * @throws         UICreationFailureException if the UI creation fails.
   */
  
  public
  FrontendModulePanel (FrontendModule module, String xmlPath, 
                       FrontendMaster master)
  throws UICreationFailureException
  {
    super (xmlPath, master);
    this.module = module;
  }
  
  
  /**
   * @return  the name of the panel module.
   */
  
  public abstract String 
  getModuleName ();


  /**
   * Retrieve the module that this panel is serving as a view into.
   * 
   * @return  the module.
   */
  
  public FrontendModule
  getModule ()
  {
    return module;
  }
}
