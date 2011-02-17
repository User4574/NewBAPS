/**
 * 
 */
package uk.org.ury.library;

/**
 * An item in the URY library.
 * 
 * @author Matt Windsor
 */

public class LibraryItem
{
  private String title;
  private String artist;
  private String label;
  private String status;
  private String medium;
  private String format;
  private String dateReleased;
  private String dateAdded;
  private String dateEdited;
  private String shelfLetter;
  private String shelfNumber;
  private String addForename;
  private String addSurname;
  private String editForename;
  private String editSurname;
  private String cdID;
  private int addMemberID;
  private int editMemberID;
  
  public
  LibraryItem ()
  {
  }

  
  /**
   * @return  the title
   */
  
  public String
  getTitle ()
  {
    return title;
  }

  
  /**
   * @param title  the title to set
   */
  
  public void
  setTitle (String title)
  {
    this.title = title;
  }

  
  /**
   * @return  the artist
   */
  
  public String
  getArtist ()
  {
    return artist;
  }

  
  /**
   * @param artist  the artist to set
   */
  
  public void
  setArtist (String artist)
  {
    this.artist = artist;
  }

  
  /**
   * @return  the label
   */
  
  public String
  getLabel ()
  {
    return label;
  }

  
  /**
   * @param label   the label to set
   */
  
  public void
  setLabel (String label)
  {
    this.label = label;
  }

  
  /**
   * @return  the status
   */
  
  public String
  getStatus ()
  {
    return status;
  }

  
  /**
   * @param status  the status to set
   */
  
  public void
  setStatus (String status)
  {
    this.status = status;
  }

  
  /**
   * @return  the medium
   */
  
  public String
  getMedium ()
  {
    return medium;
  }

  
  /**
   * @param medium  the medium to set
   */
  
  public void
  setMedium (String medium)
  {
    this.medium = medium;
  }

  
  /**
   * @return  the format
   */
  
  public String
  getFormat ()
  {
    return format;
  }

  
  /**
   * @param format  the format to set
   */
  
  public void
  setFormat (String format)
  {
    this.format = format;
  }

  
  /**
   * @return  the dateReleased
   */
  
  public String
  getDateReleased ()
  {
    return dateReleased;
  }

  
  /**
   * @param dateReleased  the dateReleased to set
   */
  
  public void
  setDateReleased (String dateReleased)
  {
    this.dateReleased = dateReleased;
  }

  
  /**
   * @return  the dateAdded
   */
  
  public String
  getDateAdded ()
  {
    return dateAdded;
  }

  
  /**
   * @param dateAdded  the dateAdded to set
   */
  
  public void
  setDateAdded (String dateAdded)
  {
    this.dateAdded = dateAdded;
  }

  
  /**
   * @return  the dateEdited
   */
  
  public String
  getDateEdited ()
  {
    return dateEdited;
  }

  
  /**
   * @param dateEdited  the dateEdited to set
   */
  
  public void
  setDateEdited (String dateEdited)
  {
    this.dateEdited = dateEdited;
  }

  
  /**
   * @return  the shelfLetter
   */
  
  
  public String
  getShelfLetter ()
  {
    return shelfLetter;
  }

  
  /**
   * @param shelfLetter  the shelfLetter to set
   */
  
  public void
  setShelfLetter (String shelfLetter)
  {
    this.shelfLetter = shelfLetter;
  }

  
  /**
   * @return  the shelfNumber
   */
  
  public String
  getShelfNumber ()
  {
    return shelfNumber;
  }

  
  /**
   * @param shelfNumber  the shelfNumber to set
   */
  
  public void
  setShelfNumber (String shelfNumber)
  {
    this.shelfNumber = shelfNumber;
  }

  
  /**
   * @return  the addForename
   */
  
  public String
  getAddForename ()
  {
    return addForename;
  }

  
  /**
   * @param addForename  the addForename to set
   */
  
  public void
  setAddForename (String addForename)
  {
    this.addForename = addForename;
  }

  
  /**
   * @return  the addSurname
   */
  
  public String
  getAddSurname ()
  {
    return addSurname;
  }

  
  /**
   * @param addSurname  the addSurname to set
   */
  
  public void
  setAddSurname (String addSurname)
  {
    this.addSurname = addSurname;
  }

  
  /**
   * @return  the editForename
   */
  
  public String
  getEditForename ()
  {
    return editForename;
  }

  
  /**
   * @param editForename  the editForename to set
   */
  
  public void
  setEditForename (String editForename)
  {
    this.editForename = editForename;
  }

  
  /**
   * @return  the editSurname
   */
  
  public String
  getEditSurname ()
  {
    return editSurname;
  }

  
  /**
   * @param editSurname  the editSurname to set
   */
  
  public void
  setEditSurname (String editSurname)
  {
    this.editSurname = editSurname;
  }

  
  /**
   * @return  the cdID
   */
  
  public String
  getCdID ()
  {
    return cdID;
  }

  
  /**
   * @param cdID  the cdID to set
   */
  
  public void
  setCdID (String cdID)
  {
    this.cdID = cdID;
  }

  
  /**
   * @return  the addMemberID
   */
  
  public int
  getAddMemberID ()
  {
    return addMemberID;
  }

  
  /**
   * @param addMemberID  the addMemberID to set
   */
  
  public void
  setAddMemberID (int addMemberID)
  {
    this.addMemberID = addMemberID;
  }

  
  /**
   * @return  the editMemberID
   */
  
  public int
  getEditMemberID ()
  {
    return editMemberID;
  }

  
  /**
   * @param editMemberID  the editMemberID to set
   */
  
  public void
  setEditMemberID (int editMemberID)
  {
    this.editMemberID = editMemberID;
  }
  
  
  /**
   * @return a human-readable string representation of the item.
   */
  
  @Override
  public String
  toString ()
  {
    return getArtist () + " - " + getTitle () + " (" + getShelfLetter () + getShelfNumber () + ")";
  }
}
