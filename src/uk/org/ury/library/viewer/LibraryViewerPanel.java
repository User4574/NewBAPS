/**
 * 
 */
package uk.org.ury.library.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.org.ury.frontend.FrontendPanel;

/**
 * @author Matt Windsor
 */

public class LibraryViewerPanel extends FrontendPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -2441616418398056712L;
  

  /* Controller of this panel. */
  private LibraryViewer master;
  
  /* Panel widgets. */
  private JLabel titleLabel;
  private JLabel artistLabel;
  
  private JTextField titleField;
  private JTextField artistField;
  
  private JButton searchButton;
  
  
  /**
   * Construct a new LibraryViewerPanel.
   * 
   * @param master  The LibraryViewer controlling this LibraryViewerPanel.
   */
  
  public
  LibraryViewerPanel (LibraryViewer master)
  {
    super ();
    
    this.master = master;
    
    GroupLayout layout = new GroupLayout (this);
    
    setLayout (layout);
    
    layout.setAutoCreateGaps (true);
    layout.setAutoCreateContainerGaps (true);
    
    titleLabel = new JLabel ("By title: ");
    artistLabel = new JLabel ("By artist: ");

    titleField = new JTextField ("Type part of the title here.");
    titleLabel.setDisplayedMnemonic ('T');
    titleLabel.setLabelFor (titleField);
    
    artistField = new JTextField ("Type part of the artist name here.");
    artistLabel.setDisplayedMnemonic ('A');
    artistLabel.setLabelFor (artistField);
    
    searchButton = new JButton ("Search");
        
    searchButton.addActionListener (new ActionListener () 
    {
      public void
      actionPerformed (ActionEvent event)
      {
      }
    });
    
    
    // Layout
    
    
    layout.setHorizontalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (titleLabel)
            .addComponent (artistLabel))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (titleField)
            .addComponent (artistField))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (searchButton))
      );
    
    layout.setVerticalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (titleLabel)
            .addComponent (titleField)
            .addComponent (searchButton))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (artistLabel)
            .addComponent (artistField))
      );

    layout.linkSize(SwingConstants.HORIZONTAL, titleField, artistField);
    layout.linkSize(SwingConstants.VERTICAL, titleField, artistField);
  }
  
  
  /**
   * @return  the name of the panel.
   * 
   * @see     uk.org.ury.frontend.FrontendPanel#getName()
   */
  
  @Override
  public String
  getName ()
  {
    return "Library Viewer Demo";
  }

}
