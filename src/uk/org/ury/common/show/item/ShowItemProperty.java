package uk.org.ury.common.show.item;


/**
 * Enumeration of the parameters that are stored in a ShowItem.
 * 
 * @author  Matt Windsor
 */

public enum ShowItemProperty
  {
    // Constant     SQL identifier       
    NAME1           ("name1"),
    NAME2           ("name2"),
    POSITION        ("positionid");
  
  
    public final String sql;
  
  
    private
    ShowItemProperty (String sql)
    {
      this.sql = sql;
    }
  };