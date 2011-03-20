/**
 * 
 */
package uk.org.ury.protocol;

/**
 * Directives supported by the protocol.
 * 
 * @author  Matt Windsor
 */

public enum Directive
  {
    INFO,    // Information string (can usually be ignored)
    ITEMS,   // Item 
    STATUS,  // Status code (from the enum Status)
    REASON;  // Error reason
  }
