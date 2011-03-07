package uk.org.ury.show;

import java.util.ArrayList;
import java.util.List;


/**
 * A channel of ShowItems in a show.
 * 
 * @author  Matt Windsor
 */

public class ShowChannel
{
  /* Items enqueued in channel. */
  private List<ShowItem> items;
  
  
  /**
   * Construct a new, empty channel.
   */
  
  public
  ShowChannel ()
  {
    items = new ArrayList<ShowItem> ();
  }
  
  
  /**
   * Add a new item to the channel.
   * 
   * @param index  The position at which to add the item.
   *               
   * @param item   The new item to add.
   * 
   * @throws       IllegalArgumentException if the item is 
   *               null, the index is negative or the index 
   *               is out of bounds.
   */
  
  public void
  add (int index, ShowItem item)
  {
    if (item == null)
      throw new IllegalArgumentException ("Item is null.");
    
    if (index < 0 || index >= items.size ())
      throw new IllegalArgumentException ("Index " + index + 
                                          " out of bounds.");
    
    items.add (index, item);
  }
  
  
  /**
   * Add a new item to the end of the channel.
   * 
   * @param item  The new item to add.
   */
  
  public void
  add (ShowItem item)
  {
    if (item == null)
      throw new IllegalArgumentException ("Item is null.");
    
    items.add (item);
  }
  
  
  /**
   * Retrieve an item from the channel.
   * 
   * @param index  The index of the item to retrieve from the channel.
   * 
   * @return       the item at the given index in the list.
   * 
   * @throws       IllegalArgumentException if the index is negative
   *               or overflowing.
   */
  
  public ShowItem
  get (int index)
  {
    if (index < 0 || index >= items.size ())
      throw new IllegalArgumentException ("Index " + index + 
                                          " out of bounds.");
    
    return items.get (index);
  }
}
