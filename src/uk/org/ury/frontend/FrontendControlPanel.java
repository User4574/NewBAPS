/**
 * 
 */
package uk.org.ury.frontend;


/**
 * Abstract class for frontend module control panels.
 * 
 * Control panels are installed as a means of exposing module 
 * intercommunication to the user.  For example, when the library 
 * viewer is launched from the playout module, a control panel 
 * is installed allowing the user to add library items to the 
 * playout system.
 * 
 * @author  Matt Windsor
 */

public abstract class FrontendControlPanel extends FrontendPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -5628250552779928189L;
  
  protected FrontendControlPanel prevCPanel;
  protected FrontendModulePanel parent;
  protected FrontendModulePanel child;
  
  
  /**
   * Alternative constructor allowing an XML file to be used to 
   * create the layout of the ControlPanel.
   * 
   * This is provided for the benefit of subclasses of this class 
   * that use it in their default constructors.
   *
   * @param xmlPath  The path, relative from this source file, to the
   *                 XML file from which this panel will read its 
   *                 layout.
   */
     
  public
  FrontendControlPanel (String xmlPath)
  {
     super (xmlPath, null);
  }


  /**
   * Set the frontend master to which this panel is bound.
   * 
   * @param master  The master to set.
   */
  
  public void
  setMaster (FrontendMaster master)
  {
    this.master = master;
  }
  
  
  /**
   * Set the parent and child panels that this ControlPanel 
   * facilitates intercommunication.
   * 
   * @param parent  The panel belonging to the parent module, 
   *                or the module that was switched out in place of 
   *                the child.
   *                
   * @param child   The panel belonging to the child module,
   *                or the module that was switched in in place of
   *                the parent.
   */

  public void
  setPanels (FrontendModulePanel parent, FrontendModulePanel child)
  {
    this.parent = parent;
    this.child = child;
  }


  /**
   * Set the previous control panel (if any), so that it can be 
   * restored when this control panel returns control to the 
   * parent.
   * 
   * @param cpanel  The previous control panel.
   */
  
  public void
  setPreviousCPanel (FrontendControlPanel cpanel)
  {
    prevCPanel = cpanel;
  }
}
