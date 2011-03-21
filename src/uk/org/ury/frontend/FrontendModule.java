/**
 * 
 */
package uk.org.ury.frontend;

/**
 * Interface for all system modules that are to be reachable from the frontend
 * array.
 * 
 * Frontend-exposed modules must:
 * 
 * - be runnable standalone, as either an application or an applet;
 * 
 * - contain their user interface in a subclass of FrontendModulePanel which can
 * be embedded either in a FrontendFrame, a web page or another module;
 * 
 * - use the frontend error reporting systems.
 * 
 * An abstract implementation of this interface, AbstractFrontendModule, is
 * provided to simplify the creation of frontend modules.
 * 
 * @author Matt Windsor
 * 
 */
public interface FrontendModule {
    /**
     * Begin execution of the frontend module.
     * 
     * @param master
     *            The FrontendMaster driving the frontend.
     * 
     * @return the frontend panel to insert into the FrontendMaster.
     */
    public FrontendModulePanel runFrontend(FrontendMaster master);
}
