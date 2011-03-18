package uk.org.ury.frontend;

import java.net.URL;

import javax.swing.JPanel;

import org.swixml.SwingEngine;

import uk.org.ury.frontend.exceptions.UICreationFailureException;


/**
 * An extension of JPanel providing common functionality for user 
 * interface panels in the URY system frontend.
 * 
 * Most notably, this includes automated access to XML-based 
 * preparation of the user interface provided by the panel, 
 * using an appropriate constructor call giving the XML file from 
 * which the user interface form should be read.
 * 
 * @author  Matt Windsor
 *
 */

public class FrontendPanel extends JPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -4481079599056565279L;
  protected FrontendMaster master;

  
  /**
   * Construct a new, blank FrontendPanel.
   * 
   * @param master   The FrontendMaster driving the frontend, if any.
   *                 For direct instantiations of this class,
   *                 providing null here is guaranteed to be safe.
   */
  
  public
  FrontendPanel (FrontendMaster master)
  {
    super ();
    
    this.master = master;
  }
  
  
  /**
   * Construct a new FrontendPanel from an XML layout.
   *
   * This is the preferred means of constructing FrontendPanels, and 
   * uses an XML-based engine to construct the panel layout.
   * 
   * @param xmlPath  The path, relative from this source file, to the
   *                 XML file from which this panel will read its 
   *                 layout.
   *                 
   * @param master   The FrontendMaster driving the frontend, if any.
   *                 For direct instantiations of this class,
   *                 providing null here is guaranteed to be safe.
   * 
   * @throws         UICreationFailureException if the UI creation fails.
   */
 
  public
  FrontendPanel (String xmlPath, FrontendMaster master)
  throws UICreationFailureException
  {
   super ();

   this.master = master;
   
   
   // Acquire path.
   
   URL path = getClass ().getResource (xmlPath);
   
   if (path == null)
     throw new UICreationFailureException ("UI creation failure:" 
                                           + "XML layout "
                                           + xmlPath
                                           + " does not exist.");
 
   SwingEngine se = new SwingEngine (this);
   
   
   // Custom UI element tag registration.
   
   se.getTaglib ().registerTag ("hint", HintField.class);
   se.getTaglib ().registerTag ("subbanner", FrontendSubBanner.class);
   
   
   // Read the XML.
   
   try
     {
       se.insert (path, this);
     }
   catch (Exception e)
     {
       throw new UICreationFailureException ("UI creation failure: "
                                             + e.getMessage ());
     }
  }
  
  
  /**
   * Set the frontend master.
   * 
   * @param master  The new frontend master to use.
   */
  
  public void
  setMaster (FrontendMaster master)
  {
    this.master = master;
  }
}
