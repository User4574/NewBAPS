package uk.org.ury.library;

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
    LibraryItemProperty (String sql)
    {
      this.sql = sql;
    }
  };