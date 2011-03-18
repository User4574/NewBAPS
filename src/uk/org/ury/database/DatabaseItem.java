package uk.org.ury.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.org.ury.database.exceptions.MissingPropertyException;
import uk.org.ury.server.protocol.Directive;


/**
 * An abstract class presenting a template for objects serving as 
 * a data structure for collections of properties retrieved from 
 * a SQL database.
 *
 * @param E  The enumeration type used as the property list.
 * 
 * @param T  The type of datum stored for each property.
 * 
 * @author   Matt Windsor
 */

public abstract class DatabaseItem<E, T>
{
  private Map<E, T> properties;
  
  
  /**
   * Construct a new item from an existing list of properties.
   * 
   * @param properties  The map of properties that the new item will 
   *                    inherit.
   */
  
  public
  DatabaseItem (Map<E, T> properties)
  {
    this.properties = properties;
  }
  
  
  /**
   * Check whether a property has been set in the item.
   * 
   * @return  true if the property has been set; false otherwise.
   */
  
  public boolean
  has (E property)
  {
    return properties.containsKey (property);
  }
  
  
  /**
   * Query this item for a property.
   * 
   * @param property  The property to query.
   * 
   * @return          The property, if it exists.
   * 
   * @throws          MissingPropertyException if the property does 
   *                  not exist.
   */
  
  public T
  get (E property) throws MissingPropertyException
  {
    if (properties.containsKey (property))
      return properties.get (property);
    else
      throw new MissingPropertyException (property.toString ());
  }
  
  
  /**
   * Map down the item into a server response body.
   * 
   * This relies on E and T having meaningful toString methods.
   * 
   * @return  a list of lines representing the response.
   */
  
  public List<String>
  asResponse ()
  {
    // TODO: Fan out implementation details into separate class
    
    List<String> response = new ArrayList<String> ();
    
    for (E property : properties.keySet ())
      {
        if (properties.get (property) != null)
          response.add (property.toString () + ": "
                        + properties.get (property).toString ());
      }
    
    return response;
  }
}
