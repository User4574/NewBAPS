package uk.org.ury.show.viewer;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendFrame;
import uk.org.ury.frontend.FrontendMaster;
import uk.org.ury.frontend.FrontendModulePanel;

import uk.org.ury.show.ShowChannel;
import uk.org.ury.show.ShowUtils;


/**
 * Frontend module for viewing show details.
 * 
 * This serves as the base for the show playout and editor classes,
 * but can be used stand-alone as an (admittedly rather pointless) 
 * module.
 * 
 * @author  Matt Windsor
 *
 */

public class ShowViewer extends AbstractFrontendModule
{
  /**
   * 
   */
  
  private static final long serialVersionUID = -2782366476480563739L;
  private DatabaseDriver dd;
  private ShowChannel[] channels;
  private ShowViewerPanel panel;
  private FrontendFrame frame;
  private ConfigReader config;
  
  
  /**
   * Construct a new ShowViewer as a frontend object.
   */
  
  public
  ShowViewer ()
  {
    try
      {
        config = new ConfigReader ("res/conf.xml");
      }
    catch (MissingCredentialsException e)
      {
        System.out.println(e);
      }
    
    frame = null;
    channels = new ShowChannel[ShowUtils.NUM_CHANNELS];
  }
  
  
  /**
   * Initialise the library viewer frontend as an applet.
   */
  
  public void
  init ()
  {
    frame = null;
    channels = new ShowChannel[ShowUtils.NUM_CHANNELS];
    panel = new ShowViewerPanel (this, null);
    
    
    try
    {
      SwingUtilities.invokeAndWait (new Runnable ()
      {
        public void
        run ()
        {
          panel.setOpaque (true);
          setContentPane (panel);
          
          runFrontend (null);
        }
        
      });
    }
    catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (InvocationTargetException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  }
  
  
  /**
   * Run the library viewer frontend as an applet.
   */
  
  public void
  start ()
  {
    frame = null;
    panel = new ShowViewerPanel (this, null);
    
    add (panel);
  } 
  
  
  /**
   * Run the library viewer frontend.
   */
  
  @Override
  public FrontendModulePanel
  runFrontend (FrontendMaster master)
  { 
    dd = null;
    
    try
      {
        dd = new DatabaseDriver (config, UserClass.READ_ONLY);
      }
    catch (MissingCredentialsException e)
      {
        // TODO: Privilege de-escalation
        FrontendError.reportFatal (e.getMessage (), frame);
      }
    catch (Exception f)
      {
        FrontendError.reportFatal (f.getMessage (), frame);
      }
    
    panel = new ShowViewerPanel (this, master);
    return panel;
  }


  /**
   * @return  the channel array.
   */
  
  public ShowChannel[]
  getChannels ()
  {
    // TODO Auto-generated method stub
    return channels;
  }
}
