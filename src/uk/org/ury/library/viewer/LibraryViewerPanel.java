/**
 * 
 */
package uk.org.ury.library.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.org.ury.frontend.FrontendPanel;
import uk.org.ury.library.LibraryTableModel;

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
  
  private LibraryTableModel resultsModel;
  private JTable resultsTable;
  private JScrollPane resultsPane;
  
  private JLabel titleLabel;
  private JLabel artistLabel;
  
  private JTextField titleField;
  private JTextField artistField;
  
  private JButton searchButton;
  
  
  /**
   * Construct a new LibraryViewerPanel.
   * 
   * @param inMaster  The LibraryViewer controlling this LibraryViewerPanel.
   */
  
  public
  LibraryViewerPanel (LibraryViewer inMaster)
  {
    super ();
    
    master = inMaster;
    
    setLayout (new BorderLayout ());
    
    JPanel groupPanel = new JPanel ();
    
    GroupLayout layout = new GroupLayout (groupPanel);
    
    groupPanel.setLayout (layout);
    
    layout.setAutoCreateGaps (true);
    layout.setAutoCreateContainerGaps (true);
    
    titleLabel = new JLabel ("By title: ");
    artistLabel = new JLabel ("By artist: ");

    titleField = new JTextField ();
    
    titleField.setPreferredSize (new Dimension (250, 15));
    
    titleLabel.setDisplayedMnemonic ('T');
    titleLabel.setLabelFor (titleField);
    
    artistField = new JTextField ();
    artistLabel.setDisplayedMnemonic ('A');
    artistLabel.setLabelFor (artistField);
    
    searchButton = new JButton ("Search");
        
    searchButton.addActionListener (new ActionListener () 
    {
      public void
      actionPerformed (ActionEvent event)
      {
        master.doSearch (titleField.getText (), artistField.getText ());
        resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
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

    layout.linkSize (SwingConstants.HORIZONTAL, titleField, artistField);
    layout.linkSize (SwingConstants.VERTICAL, titleField, artistField);
    
    add (groupPanel, BorderLayout.NORTH);
    
    
    // Table
    
    resultsModel = new LibraryTableModel (master.getLibraryList ());
    resultsTable = new JTable (resultsModel);
    
    resultsPane = new JScrollPane (resultsTable); 
    
    add (resultsPane, BorderLayout.CENTER);
    
    
    // Explanation (TODO: Subclass?)
    
    JTextArea explanation = new JTextArea ("To narrow your search, type part or all of the record title or artist"
                                           + " into the respective box above.  If you leave a box blank, it will"
                                           + " not be used in the search.");
    
    explanation.setLineWrap (true);
    explanation.setWrapStyleWord (true);
    explanation.setEditable (false);
    explanation.setOpaque (false);
    explanation.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
    
    add (explanation, BorderLayout.SOUTH);
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
