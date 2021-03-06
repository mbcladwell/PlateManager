
package pm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Toolkit;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DialogMainFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private JPanel cards; // a panel that uses CardLayout
  private CardLayout card_layout;

  private ProjectPanel project_card;
  private PlateSetPanel plate_set_card;
  private PlatePanel plate_card;
  private WellPanel well_card;

  private static Utilities utils;
    //private DatabaseManager dbm;
    //private DatabaseRetriever dbr;
    private static Session session;
 
  private Long sessionID;

    public static final int PROJECT = 1; //Card with projects
    public static final int PLATESET = 2; //Card with plate sets
    public static final int PLATE = 3; //Card with plates
    public static final int WELL = 4; //Card with wells

 
  public DialogMainFrame( Session _s ) throws SQLException {
      session = _s;
        utils = new Utilities(this);
      this.setTitle("LIMS*Nucleus");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ImageIcon img = new ImageIcon(this.getClass().getResource("images/mwplate.png"));
      this.setIconImage(img.getImage());
      //dbr = session.getDatabaseRetriever();
   
    /////////////////////////////////////////////
    // set up the project table
   
    cards = new JPanel(new CardLayout());
    card_layout = (CardLayout) cards.getLayout();
    project_card = new ProjectPanel(this, session.getDatabaseRetriever().getDMFTableData(0, DialogMainFrame.PROJECT));
    cards.add(project_card, "ProjectPanel");
  
    
    this.getContentPane().add(cards, BorderLayout.CENTER);

    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

    
  public ProjectPanel getProjectPanel() {
    return project_card;
  }

      public PlateSetPanel getPlateSetPanel() {
    return plate_set_card;
  }


  public void showProjectTable() {
    project_card = new ProjectPanel(this, session.getDatabaseRetriever().getDMFTableData(0, DialogMainFrame.PROJECT));
    cards.add(project_card, "ProjectPanel");
    card_layout.show(cards, "ProjectPanel");
  }

    
  public void showPlateSetTable(String _project_sys_name) {
      int project_id = Integer.parseInt(_project_sys_name.substring(4));
     
      //  plate_set_card = new PlateSetPanel(this, dbm.getPlateSetTableData(_project_sys_name), _project_sys_name);
      plate_set_card = new PlateSetPanel(this, session.getDatabaseRetriever().getDMFTableData(project_id, DialogMainFrame.PLATESET), _project_sys_name);

    cards.add(plate_set_card, "PlateSetPanel");
    card_layout.show(cards, "PlateSetPanel");
  }


  public void showPlateTable(String _plate_set_sys_name) {
      int plate_set_id = Integer.parseInt(_plate_set_sys_name.substring(3));
      
      plate_card = new PlatePanel(this, session.getDatabaseRetriever().getDMFTableData(plate_set_id, DialogMainFrame.PLATE));
    
    cards.add(plate_card, "PlatePanel");
    card_layout.show(cards, "PlatePanel");
  }


  public void showWellTable(String _plate_sys_name) {
      int plate_id = Integer.parseInt(_plate_sys_name.substring(4));
      
      well_card = new WellPanel(this, session.getDatabaseRetriever().getDMFTableData(plate_id, DialogMainFrame.WELL));
    cards.add(well_card, "Well");
    card_layout.show(cards, "Well");
  }

    /**
     * The "flip" methods are used with the up button to return to a previous card
     */

  public void flipToProjectTable() {
    card_layout.show(cards, "ProjectPanel");
  }
    
    public void flipToPlateSet() {
    card_layout.show(cards, "PlateSetPanel");
  }


  public void flipToPlate() {
    card_layout.show(cards, "PlatePanel");
  }
    
  public void flipToWell() {
    card_layout.show(cards, "Well");
  }

    /*    
  public DatabaseManager getDatabaseManager() {
    return this.dbm;
  }
    */
    
  public Session getSession() {
    return session;
  }
    
    
  public Utilities getUtilities() {
    return utils;
  }

    public void setMainFrameTitle(String s){
	if(s==""){this.setTitle("LIMS*Nucleus");}else{
	    this.setTitle("LIMS*Nucleus::" + s);}
    }
  public void updateProjectPanel() {


      
  }
}
