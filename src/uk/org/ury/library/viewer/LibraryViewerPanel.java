/**
 * 
 */
package uk.org.ury.library.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
  
  private JLabel searchLabel;
  
  private JTextField searchField;
  
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
    
    searchLabel = new JLabel ("Search");
    searchLabel.setFont(new Font("Verdana", Font.BOLD, 14));

    searchField = new JTextField ();
    
    searchField.setPreferredSize (new Dimension (250, 25));
    searchField.setFont(new Font("Verdana", Font.BOLD, 14));
    
    searchLabel.setDisplayedMnemonic ('T');
    searchLabel.setLabelFor (searchField);
        
    searchButton = new JButton ("Search");
    
    searchField.addActionListener(new ActionListener() {
    	public void actionPerformed (ActionEvent event) {
    		master.doSearch (searchField.getText ());
            resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
    	}
    });
        
    searchButton.addActionListener (new ActionListener () 
    {
      public void
      actionPerformed (ActionEvent event)
      {
        master.doSearch (searchField.getText ());
        resultsTable.setModel (new LibraryTableModel (master.getLibraryList ()));
      }
    });
    
    
    // Layout
    
    
    layout.setHorizontalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (searchLabel))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (searchField))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (searchButton))
      );
    
    layout.setVerticalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (searchLabel)
            .addComponent (searchField)
            .addComponent (searchButton))
      );

    layout.linkSize (SwingConstants.HORIZONTAL, searchField);
    layout.linkSize (SwingConstants.VERTICAL, searchField);
    
    add (groupPanel, BorderLayout.NORTH);
    
    
    // Table
    
    resultsModel = new LibraryTableModel (master.getLibraryList ());
    resultsTable = new JTable (resultsModel);
    
    resultsPane = new JScrollPane (resultsTable); 
    
    add (resultsPane, BorderLayout.CENTER);
    
    
    // Explanation (TODO: Subclass?)
    
    JTextArea explanation = new JTextArea ("To narrow your search, type part or all of the record title or artist into the box above.");
    
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
