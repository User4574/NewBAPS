package uk.org.ury.library.item;

/**
 * The parameters that are stored in the LibraryItem.
 * 
 * @author  Matt Windsor
 */

public enum LibraryItemProperty
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
    IS_DIGITISED    ("digitised"),
    IS_CLEAN        ("clean");
    
    
    public final String sql;
    
    
    private
    LibraryItemProperty (String sql)
    {
      this.sql = sql;
    }


    /**
     * Retrieve a LibraryItemProperty given its SQL identifier.
     * 
     * @param string  The SQL identifier.
     * @return        The first property to match.
     * 
     * @throws        IllegalArgumentException if no matches were
     *                found.
     */
    
    public static LibraryItemProperty
    getFromSQL (String string)
    {
      // TODO: Better exception?
      
      for (LibraryItemProperty prop : values ())
        {
          if (prop.sql.equals (string))
            return prop;
        }
      
      throw new IllegalArgumentException ("Nonexistent property SQL.");
    }
  };