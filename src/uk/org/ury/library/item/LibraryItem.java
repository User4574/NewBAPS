/**
 * 
 */
package uk.org.ury.library.item;


import java.util.Map;

import uk.org.ury.database.DatabaseItem;


/**
 * An item in the URY library.
 * 
 * @author Matt Windsor
 */

public class LibraryItem extends DatabaseItem<LibraryItemProperty,
                                              String>
{
  public
  LibraryItem (Map<LibraryItemProperty, String> properties)
  {
    super (properties);
  }  
}