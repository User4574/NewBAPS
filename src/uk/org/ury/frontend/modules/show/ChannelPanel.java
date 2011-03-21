/**
 * 
 */
package uk.org.ury.frontend.modules.show;

import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import uk.org.ury.common.show.ShowChannel;
import uk.org.ury.frontend.FrontendPanel;
import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * A panel displaying channel information.
 * 
 * @author  Matt Windsor.
 */

public class ChannelPanel extends FrontendPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = 897125684384350966L;


  /* Components created and exposed by the XML engine. */

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
   * 
   * @throws         UICreationFailureException if the UI creation fails.
   */
  
  public
  ChannelPanel (int number, ShowChannel channel)
  throws UICreationFailureException
  {
    super ("channel_panel.xml", null);

    
    // Tweak buttons to add function key mnemonics, if function keys are available.
    
    if (number <= 3)
      {
        int base = (number - 1) * 4;
        
        playButton.setText      ("Play (F"      + (base + 1) + ")");
        playButton.setMnemonic  (KeyEvent.VK_F1 + (base    ));
        playButton.setDisplayedMnemonicIndex (7);
        
        pauseButton.setText     ("Stop (F"      + (base + 2) + ")");
        pauseButton.setMnemonic (KeyEvent.VK_F2 + (base    ));
        pauseButton.setDisplayedMnemonicIndex (7);
        
        stopButton.setText      ("Pause (F"     + (base + 3) + ")");
        stopButton.setMnemonic  (KeyEvent.VK_F3 + (base    ));
        stopButton.setDisplayedMnemonicIndex (8);
      }
      
    // Test stuff
    itemList.setModel (channel);
    channelName.setText ("Channel " + number);
  }
}
