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
    return 3;
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
      case 2: // Medium
        return li.get (LibraryProperty.MEDIUM);
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
        return "Medium";
      default:
        return "ERROR";
      }
  }
}
