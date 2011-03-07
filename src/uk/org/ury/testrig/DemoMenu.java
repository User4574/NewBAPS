package uk.org.ury.testrig;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import uk.org.ury.frontend.FrontendFrame;
import uk.org.ury.frontend.FrontendModulePanel;


/**
 * Application frontend, for testing early-stage frontend code.
 * 
 * @author  Matt Windsor
 *
 */

public class DemoMenu
{
  private FrontendFrame frame;
  
  /**
   * Main method.
   * 
   * @param args  The command-line arguments to the program.  These 
   *              will be ignored.
   */
  
  public static void
  main (String[] args)
  {
    DemoMenu dm = new DemoMenu ();
    dm.run ();
  }
  
  
  /**
   * 
   */
  
  public void
  loadModule (String module)
  {
    frame.loadModule (module, "testrig.DemoControlPanel");
  }
  
  /**
   * Run the demo menu, creating a user interface.
   */
  
  public void
  run ()
  {
    FrontendModulePanel panel = new FrontendModulePanel (null, frame)
    {
      private static final long serialVersionUID = 1L;
      
      {
        setLayout (new GridLayout (2, 1));
        
        JButton lb = new JButton ("Library Viewer Demo");
        JButton sb = new JButton ("Show Viewer Demo");
        
        lb.addActionListener (new ActionListener ()
        {

          @Override
          public void
          actionPerformed (ActionEvent arg0)
          {
            loadModule ("library.viewer.LibraryViewer");
          }
          
        });
        
        
        sb.addActionListener (new ActionListener ()
        {

          @Override
          public void
          actionPerformed (ActionEvent arg0)
          {
            loadModule ("show.viewer.ShowViewer");
          }
          
        });
        
        
        add (lb);
        add (sb);
      }

      
      /**
       * @return  the name of the module.
       */
      
      @Override
      public String
      getName ()
      {
        return "Demo Menu";
      }
    };
    
    
    frame = new FrontendFrame (panel);
  }
}
