/**
 * 
 */
package uk.org.ury.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A frame that hosts a FrontendPanel, used for serving frontend 
 * panels in a window (application mode).
 * 
 * @author Matt Windsor
 *
 */

public class FrontendFrame extends JFrame
{
  /**
   * 
   */
  
  private static final long serialVersionUID = 740928181256928433L;
  
  private FrontendPanel parent;
  
  public
  FrontendFrame (FrontendPanel parent)
  {
    super (parent.getName ());
    
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    this.parent = parent;
    
    Container cp = getContentPane ();
    
    // Banner
    
    JPanel banner = new JPanel ();
    JLabel bannerLabel = new JLabel ("<html><h1><font color=white>"
                                     + parent.getName () + "</font></h1></html>");
    
    bannerLabel.setBorder (BorderFactory.createEmptyBorder (5, 15, 5, 5));
    
    banner.setLayout (new FlowLayout (FlowLayout.LEFT));
    banner.setBackground (new Color (0, 0, 0));
    banner.add (new JLabel (new ImageIcon (getClass ().getResource ("images/ury.png"))));
    banner.add (bannerLabel);
    
    // Composition
    
    cp.add (banner, BorderLayout.NORTH);
    cp.add (parent, BorderLayout.CENTER);
    
    setPreferredSize (new Dimension (640, 480));
    
    pack ();
    setVisible (true);
  }
}
