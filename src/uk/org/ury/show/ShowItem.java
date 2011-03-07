/**
 * 
 */
package uk.org.ury.show;


import java.util.Map;

import uk.org.ury.database.DatabaseItem;


/**
 * An item in the show database.
 * 
 * @author Matt Windsor
 */

public class ShowItem extends DatabaseItem<ShowItemProperty, String>
{
  public
  ShowItem (Map<ShowItemProperty, String> properties)
  {
    super (properties);
  }  
}
