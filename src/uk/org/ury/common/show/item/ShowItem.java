/**
 * 
 */
package uk.org.ury.common.show.item;


import java.util.Map;

import uk.org.ury.backend.database.DatabaseItem;
import uk.org.ury.backend.database.exceptions.MissingPropertyException;


/**
 * An item in the show database.
 * 
 * @author Matt Windsor
 */

public class ShowItem extends DatabaseItem<ShowItemProperty, String>
{
  /**
   * Construct a new ShowItem.
   * 
   * @param properties  The map of properties to store in the show item.
   */
  
  public
  ShowItem (Map<ShowItemProperty, String> properties)
  {
    super (properties);
  }
  
  
  /**
   * @return  a string representation of the ShowItem.
   */
  
  @Override
  public String
  toString ()
  {
    String name1;
    String name2;
    
    try
      {
        name1 = get (ShowItemProperty.NAME1);
      }
    catch (MissingPropertyException e1)
      {
        name1 = "Unknown";
      }

    try
      {
        name2 = get (ShowItemProperty.NAME2);
      }
    catch (MissingPropertyException e2)
      {
        name2 = null;
      }
    
    if (name2 != null)
      return name1 + " - " + name2;
    else
      return name1;
  }
}
