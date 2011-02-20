/**
 * 
 */
package uk.org.ury.frontend;


import javax.swing.BorderFactory;
import javax.swing.JTextArea;


/**
 * A non-editable text area displaying a hint about how to use a 
 * frontend screen.
 * 
 * @author  Matt Windsor
 *
 */

public class HintField extends JTextArea
{
  /**
   * 
   */
  private static final long serialVersionUID = -6221888920919127273L;

  
  /**
   * Construct a new HintField with no initial hint.
   */
  
  public
  HintField ()
  {
    super ();
    
    setLineWrap (true);
    setWrapStyleWord (true);
    setEditable (false);
    setOpaque (false);
  }
  
  
  /**
   * Construct a new HintField.
   * 
   * @param hint  The hint to display in the HintField.
   */
  
  public
  HintField (String hint)
  {
    super (hint);
    
    setLineWrap (true);
    setWrapStyleWord (true);
    setEditable (false);
    setOpaque (false);
    setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
  }
}
