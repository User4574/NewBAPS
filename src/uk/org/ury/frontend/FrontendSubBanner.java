package uk.org.ury.frontend;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;


/**
 * A banner, displaying a title, intended for use at the top of
 * subsections.
 * 
 * @author  Matt Windsor
 */

public class FrontendSubBanner extends JLabel
{
  /**
   * 
   */
  private static final long serialVersionUID = 7843563245601622086L;
  private static final String TITLE_PREFIX = "<html><b>";
  private static final String TITLE_SUFFIX = "</b></html>";
  
  
  /**
   * Construct a new FrontendSubBanner.
   */
  
  public
  FrontendSubBanner ()
  {
    super ();
  
    //setLineWrap (true);
    //setWrapStyleWord (true);
    //setEditable (false);
    
    setBackground (UIManager.getColor ("textHighlight"));
    setForeground (UIManager.getColor ("textHighlightText"));
    setBorder (BorderFactory.createEmptyBorder (3, 6, 3, 6));
    setOpaque (true);
  }
  
  
  public void
  setText (String text)
  {
    super.setText (TITLE_PREFIX + text + TITLE_SUFFIX);
  }
}
