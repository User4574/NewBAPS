/**
 * 
 */
package uk.org.ury.library;


import java.util.Map;


/**
 * An item in the URY library.
 * 
 * @author Matt Windsor
 */

public class LibraryItem
{
  /**
   * The parameters that are stored in the LibraryItem.
   * 
   * @author  Matt Windsor
   */
  
  public enum LibraryProperty
  {
    // Constant     SQL identifier       
    TITLE           ("title"),
    ALBUM           ("album"),
    ARTIST          ("artist"),
    LABEL           ("label"),
    STATUS          ("status"),
    MEDIUM          ("medium"),
    FORMAT          ("format"),
    DATE_RELEASED   ("datereleased"),
    DATE_ADDED      ("dateadded"),
    DATE_EDITED     ("dateedited"),
    SHELF_LETTER    ("shelfletter"),
    SHELF_NUMBER    ("shelfnumber"),
    CD_ID           ("cdid"),
    ADD_MEMBER_ID   ("memberid_add"),
    EDIT_MEMBER_ID  ("memberid_lastedit"),
    ADD_FORENAME    ("fnameadd"),
    ADD_SURNAME     ("snameadd"),
    EDIT_FORENAME   ("fnameedit"),
    EDIT_SURNAME    ("snameedit"),
    IS_DIGITISED    ("digitised"),
    IS_CLEAN        ("clean");
    
    
    public final String sql;
    
    private
    LibraryProperty (String sql)
    {
      this.sql = sql;
    }
  };
  
  
  private Map<LibraryProperty, String> properties;
  
  
  /**
   * Construct a new library item from an existing list of 
   * properties.
   * 
   * @param properties  The map of properties that the new item will 
   *                    inherit.
   */
  
  public
  LibraryItem (Map<LibraryProperty, String> properties)
  {
    this.properties = properties;
  }
  
  
  /**
   * Query this item for a property.
   * 
   * @param property  The property to query.
   * @return          The property, if it exists, or "Unknown" otherwise.
   */
  
  public String
  get (LibraryProperty property)
  {
    if (properties.containsKey (property))
      return properties.get (property);
    else
      return "Unknown";
  }
}
