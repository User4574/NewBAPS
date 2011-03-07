/**
 * 
 */
package uk.org.ury.show.viewer;

import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import org.swixml.SwingEngine;

import uk.org.ury.frontend.FrontendError;
import uk.org.ury.show.ShowChannel;


/**
 * A panel displaying channel informstion.
 * 
 * @author  Matt Windsor.
 */

public class ChannelPanel extends JPanel
{
  private JLabel channelName;
  private JList itemList;
  private JButton playButton;
  private JButton pauseButton;
  private JButton stopButton;
  
  
  /**
   * Construct a new ChannelPanel.
   * 
   * This constructor reads the channel panel layout from the 
   * XML manifest "channel_panel.xml" in the same directory as 
   * this class file.
   * 
   * @param number   The number of the channel.
   * 
   * @param channel  The channel whose data the ChannelPanel is viewing.
   */
  
  public
  ChannelPanel (int number, ShowChannel channel)
  {
    super ();
    
    // Acquire path.
    
    URL path = getClass ().getResource ("channel_panel.xml");
    
    if (path == null)
      FrontendError.reportFatal ("UI creation failure: XML layout does not exist.", null);
  
    SwingEngine se = new SwingEngine (this);
    
    
   // Read the XML.
    
    try
      {
        se.insert (path, this);
      }
    catch (Exception e)
      {
        FrontendError.reportFatal ("UI creation failure: " + e.getMessage (), null);
      }
    
    // Tweak buttons to add function key mnemonics, if function keys are available.
    
    if (number <= 4)
      {
        int base = number * 3;
        
        playButton.setText      ("Play (F"      + (base - 2) + ")");
        playButton.setMnemonic  (KeyEvent.VK_F1 + (base - 1));
        playButton.setDisplayedMnemonicIndex (7);
        
        pauseButton.setText     ("Stop (F"      + (base - 1) + ")");
        pauseButton.setMnemonic (KeyEvent.VK_F2 + (base - 1));
        pauseButton.setDisplayedMnemonicIndex (7);
        
        stopButton.setText      ("Pause (F"     + (base    ) + ")");
        stopButton.setMnemonic  (KeyEvent.VK_F3 + (base - 1));
        stopButton.setDisplayedMnemonicIndex (8);
      }
      
    // Test stuff
    DefaultListModel test = new DefaultListModel ();
    test.add (0, "Test");
    itemList.setModel (test);
    channelName.setText ("Channel " + number);
  }
}
