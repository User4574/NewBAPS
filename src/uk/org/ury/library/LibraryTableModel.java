/**
 * 
 */
package uk.org.ury.library;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.org.ury.library.LibraryItem.LibraryProperty;


/**
 * A table model for the library viewer.
 * 
 * @author Matt Windsor
 */

public class LibraryTableModel extends AbstractTableModel
{

  /**
   * 
   */
  
  private static final long serialVersionUID = -1744980619128903509L;

  private List<LibraryItem> data;
  
  
  /**
   * Construct a new table model.
   * 
   * @param data  The list of data on which the model will be based.
   */
  
  public
  LibraryTableModel (List<LibraryItem> data)
  {
    this.data = data;
  }
  
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  
  @Override
  public int
  getColumnCount ()
  {
    return 6;
  }

  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  
  @Override
  public int
  getRowCount ()
  {
    return data.size ();
  }

  
  /**
   * @param c  The column whose class should be investigated.
   * 
   * @return   the column class of column c.
   */
  
  @Override
  public Class<?>
  getColumnClass (int c) 
  {
    return getValueAt (0, c).getClass ();
  }
  
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  
  @Override
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    LibraryItem li = data.get (rowIndex);
    
    switch (columnIndex)
      {
      case 0: // Title
        return li.get (LibraryProperty.TITLE);
        
      case 1: // Artist
        return li.get (LibraryProperty.ARTIST);
        
      case 2: // Album
        return li.get (LibraryProperty.ALBUM);
        
      case 3: // Medium
        
        // TODO: Make this less kludge-y
        
        String mediumString = li.get (LibraryProperty.MEDIUM);
        
        if (mediumString.equals ("c"))
          return "Compact Disc";
        else if (mediumString.equals ("7"))
          return "7\" Vinyl";
        else if (mediumString.equals ("2"))
          return "12\" Vinyl";
        else
          return "Unrecognised";
        
      case 4: // Clean?

        // Return true if marked true, false if marked false or unknown etc.
        
        String cleanString = li.get (LibraryProperty.IS_CLEAN); 

        // TODO: Nicer way of showing this
        
        if (cleanString.equals ("y"))
          return "Yes";
        else if (cleanString.equals ("n"))
          return "No";
        else
          return "???";
        
      case 5: // isDigitised
        
        // Return true if marked true, false if marked false or unknown etc.
        
        String digitisedString = li.get (LibraryProperty.IS_DIGITISED); 
        
        if (digitisedString.equals ("t"))
          return true;
        else
          return false;
        
      default:
        return "";
      }
  }
  
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnName(int, int)
   */
  
  @Override
  public String
  getColumnName (int index)
  {
    switch (index)
      {
      case 0:
        return "Title";
      case 1:
        return "Artist";
      case 2:
        return "Album";
      case 3:
        return "Medium";
      case 4:
        return "Clean?";
      case 5:
        return "On system?";
        
      default:
        return "ERROR";
      }
  }
}
