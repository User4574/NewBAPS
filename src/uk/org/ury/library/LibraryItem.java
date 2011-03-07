/**
 * 
 */
package uk.org.ury.library;


import java.util.Map;

import uk.org.ury.database.DatabaseItem;
import uk.org.ury.library.LibraryItemProperty;


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