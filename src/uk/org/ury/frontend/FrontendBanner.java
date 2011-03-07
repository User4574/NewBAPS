package uk.org.ury.frontend;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * A banner, displaying a title, intended for use at the top of the 
 * frontend frame.
 * 
 * @author  Matt Windsor
 */

public class FrontendBanner extends JPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -3636933349004358394L;

  private static final String TITLE_PREFIX = "<html><h1><font color=white>";
  private static final String TITLE_SUFFIX = "</font></h1></html>";
  
  private JLabel titleLabel;
  
  
  /**
   * Construct a new banner.
   * 
   * @param title  The initial title to display in the banner.
   */
  
  public
  FrontendBanner (String title)
  {
    setLayout (new FlowLayout (FlowLayout.LEFT));
    setBackground (new Color (0, 0, 0));
    
    
    JLabel logo = new JLabel (new ImageIcon (getClass ().getResource ("images/ury.png")));
    
    
    titleLabel = new JLabel (TITLE_PREFIX + title + TITLE_SUFFIX);
    titleLabel.setBorder (BorderFactory.createEmptyBorder (5, 15, 5, 5));

    
    add (logo);
    add (titleLabel);
  }
  
  
  /**
   * Change the title displayed on the banner.
   * 
   * @param title  The new title to display.
   */
  
  public void
  setTitle (String title)
  {
   titleLabel.setText (TITLE_PREFIX + title + TITLE_SUFFIX); 
  }
}
