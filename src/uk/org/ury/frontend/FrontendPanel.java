package uk.org.ury.frontend;

import java.net.URL;

import javax.swing.JPanel;

import org.swixml.SwingEngine;


/**
 * An extension of JPanel providing common functionality for user 
 * interface panels in the URY system frontend.
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
   * @param master   The FrontendMaster driving the frontend.
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
   * uses SWIXml to construct the panel layout.
   * 
   * @param xmlPath  The path, relative from this source file, to the
   *                 XML file from which this panel will read its 
   *                 layout.
   *                 
   * @param master   The FrontendMaster driving the frontend.
   */
 
  public
  FrontendPanel (String xmlPath, FrontendMaster master)
  {
   super ();

   this.master = master;
   
   
   // Acquire path.
   
   URL path = getClass ().getResource (xmlPath);
   
   if (path == null)
     FrontendError.reportFatal ("UI creation failure: XML layout "
                                + xmlPath + " does not exist.", null);
 
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
       FrontendError.reportFatal ("UI creation failure: " + e.getMessage (), null);
     }
  }
}
