/**
 * 
 */
package uk.org.ury.server.protocol;

/**
 * Directives supported by the protocol.
 * 
 * @author  Matt Windsor
 */

public enum Directive
  {
    // ID           String representation  Singleton?
    
    /** Directive marking the start of an item block. */ 
    ITEM_START      ("ITEM-START"        , false),
    
    /** Directive marking a property inside an item block. */  
    ITEM_PROPERTY   ("PROP"              , false),
    
    /** Directive marking the end of an item block. */
    ITEM_END        ("ITEM-END"          , false);
    
    
    
    private String strRep;        // String representation
    private boolean isSingleton;  // Is a singleton?

    
    /**
     * Construct a new Directive.
     * 
     * @param strRep  The string representation of the Directive.
     * 
     * @param isSingleton  If true, then the Directive accepts no 
     *                     properties.  If false, then the Directive 
     *                     must be provided with at least one.
     */
    
    private
    Directive (String strRep, boolean isSingleton)
    {
      this.strRep = strRep;
      this.isSingleton = isSingleton;
    }
    
    
    /**
     * @return  the string representation.
     */
    
    public String
    toString ()
    {
      return strRep;
    }
    
    
    /**
     * @return  true if the directive has no properties.
     */
    
    public Boolean
    isSingleton ()
    {
      return isSingleton;
    }
  }
