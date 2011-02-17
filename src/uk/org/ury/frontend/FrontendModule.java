/**
 * 
 */
package uk.org.ury.frontend;

/**
 * Interface for all system modules that are to be reachable from 
 * the frontend array.
 * 
 * Frontend-exposed modules must:
 * 
 * - be runnable standalone, as either an application or an applet;
 * 
 * - contain their user interface in a subclass of FrontendPanel 
 *   which can be embedded either in a FrontendFrame, a web page 
 *   or another module;
 *   
 * - use the frontend error reporting systems.
 * 
 * An abstract implementation of this interface, 
 * AbstractFrontendModule, is provided to simplify the creation of 
 * frontend modules.
 * 
 * @author Matt Windsor
 *
 */

public interface FrontendModule
{
  // Space for rent
}
