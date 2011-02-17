package uk.org.ury.library.viewer;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.MissingCredentialsException;
import uk.org.ury.frontend.AbstractFrontendModule;
import uk.org.ury.frontend.FrontendError;
import uk.org.ury.frontend.FrontendFrame;
import uk.org.ury.frontend.FrontendModule;
import uk.org.ury.library.LibraryItem;

public class LibraryViewer extends AbstractFrontendModule
{
  /**
   * 
   */
  private static final long serialVersionUID = -2782366476480563739L;
  private List<LibraryItem> libraryList;
  private LibraryViewerPanel panel;
  private FrontendFrame frame;
  
  
  /**
   * Main function, for running this module as a standalone 
   * application.
   * 
   * @param args  The command-line argument array.
   */
  
  public static void
  main (String[] args)
  {
    LibraryViewer lv = new LibraryViewer ();
    lv.runFrontendInFrame ();
  }
  
  
  /**
   * Construct a new LibraryViewer.
   */
  
  public
  LibraryViewer ()
  {
    frame = null;
    panel = new LibraryViewerPanel (this);
  }
  
  
  /**
   * Initialise the library viewer frontend as an applet.
   */
  
  public void
  init ()
  {
    frame = null;
    panel = new LibraryViewerPanel (this);
    
    
    try
    {
      SwingUtilities.invokeAndWait (new Runnable ()
      {
        public void
        run ()
        {
          panel.setOpaque (true);
          setContentPane (panel);
          
          runFrontend ();
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
    panel = new LibraryViewerPanel (this);
    
    add (panel);
  } 
  
  /**
   * Run the library viewer frontend in a FrontendFrame.
   */
  
  public void
  runFrontendInFrame ()
  {
    FrontendFrame frame = new FrontendFrame (panel);
    this.frame = frame;
    
    runFrontend ();
  }
  
  
  /**
   * Run the library viewer frontend.
   */
  
  private void
  runFrontend ()
  { 
    DatabaseDriver dd = null;
    
    try
      {
        dd = new DatabaseDriver (UserClass.READ_ONLY);
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

      
    System.out.println ("connection success");
    
    ResultSet rs = dd.executeQuery ("SELECT title, artist, recordlabel AS label, status, media AS medium, format,"
        + " datereleased, EXTRACT(EPOCH FROM dateadded) as dateadded,"
        + " EXTRACT(EPOCH FROM datetime_lastedit) AS dateedited,"
        + " shelfletter, shelfnumber, cdid, memberid_add, memberid_lastedit,"
        + " a.fname AS fnameadd, a.sname AS snameadd, b.fname AS fnameedit, b.sname AS snameedit"
        + " FROM rec_record AS r"
        + " INNER JOIN member AS a ON (a.memberid = r.memberid_add)"
        + " LEFT JOIN member AS b ON (b.memberid = r.memberid_lastedit)");
    
    
      libraryList = new ArrayList<LibraryItem> ();
    
      try
        {
          while (rs.next ())
            {
              LibraryItem li = new LibraryItem ();
              
              li.setTitle        (rs.getString ("title"));
              li.setArtist       (rs.getString ("artist"));
              li.setLabel        (rs.getString ("label"));
              li.setStatus       (rs.getString ("status"));
              li.setMedium       (rs.getString ("medium"));
              li.setFormat       (rs.getString ("format"));
              li.setDateReleased (rs.getString ("datereleased"));
              li.setDateEdited   (rs.getString ("dateedited"));
              li.setShelfLetter  (rs.getString ("shelfletter"));
              li.setShelfNumber  (rs.getString ("shelfnumber"));
              li.setCdID         (rs.getString ("cdid"));
              li.setAddMemberID  (rs.getInt    ("memberid_add"));
              li.setEditMemberID (rs.getInt    ("memberid_lastedit"));
              li.setAddForename  (rs.getString ("fnameadd"));
              li.setAddSurname   (rs.getString ("snameadd"));
              li.setEditForename (rs.getString ("fnameedit"));
              li.setEditSurname  (rs.getString ("snameedit"));
              
              System.out.println (li);
              
              libraryList.add (li);
            }
        }
      catch (SQLException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
  }
}
