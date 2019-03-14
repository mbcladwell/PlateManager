
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
  private DatabaseManager dbm;
  private JPanel cards; // a panel that uses CardLayout
  private CardLayout card_layout;

  private ProjectPanel project_card;
  private JPanel plate_set_card;
  private JPanel plate_card;
  private JPanel well_card;

  private static final Session session = new Session();
  private static Utilities utils;

  private Long sessionID;

  public static final String PROJECTPANEL = "Card with projects";
  public static final String PLATESETPANEL = "Card with plate sets";
  public static final String PLATEPANEL = "Card with plates";
  public static final String WELLPANEL = "Card with wells";

 
  public DialogMainFrame() throws SQLException {
    dbm = new DatabaseManager(this);
    utils = new Utilities(this);
      this.setTitle("LIMS*Nucleus");
    // new DialogLogin();
    // Create and set up the window.
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    try {
      ImageIcon img = new ImageIcon(this.getClass().getResource("images/mwplate.png"));
      this.setIconImage(img.getImage());
      session.setSessionID(dbm.initializeSession("admin", "welcome"));
      session.setUserName("admin");
      session.setUserID(1);
      session.setUserGroupID(1);
      session.setUserGroup("pm_admin");
      session.setTempDir((new File(System.getProperty("java.io.tmpdir")).toString()));
      //LOGGER.info("Temporary directory set to: " + session.getTempDir());
      session.setWorkingDir((new File(System.getProperty("user.dir")).toString()));
      //LOGGER.info("Working directory set to: " + session.getWorkingDir());

    } catch (SQLException sqle) {
      System.out.println("Invalid password");
    }
    //    ImageIcon ii = new ImageIcon(getResource("images/mwplate.png"));
    /////////////////////////////////////////////
    // set up the project table
    project_card = new ProjectPanel(this, dbm.getProjectTableData());

    cards = new JPanel(new CardLayout());
    card_layout = (CardLayout) cards.getLayout();
    cards.add(project_card, PROJECTPANEL);

    this.getContentPane().add(cards, BorderLayout.CENTER);

    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

  public void showProjectTable() {
    card_layout.show(cards, DialogMainFrame.PROJECTPANEL);
  }

  public ProjectPanel getProjectPanel() {
    return project_card;
  }

  public void showPlateSetTable(String _project_sys_name) {
    plate_set_card =
        new PlateSetPanel(this, dbm.getPlateSetTableData(_project_sys_name), _project_sys_name);

    cards.add(plate_set_card, PLATESETPANEL);
    card_layout.show(cards, DialogMainFrame.PLATESETPANEL);
  }

  public void flipToPlateSet() {
    card_layout.show(cards, DialogMainFrame.PLATESETPANEL);
  }

  public void showPlateTable(String _plate_set_sys_name) {
      CustomTable ct = dbm.getPlateTableData(_plate_set_sys_name);
      LOGGER.info("CustomTable: " + ct);
      LOGGER.info("valueAt: " + ct.getValueAt(1,0));
      plate_card = new PlatePanel(this, ct);
    LOGGER.info("_plate_set_sys_name: " + _plate_set_sys_name);
    LOGGER.info("_plate card: " + plate_card);
    
    cards.add(plate_card, PLATEPANEL);
    card_layout.show(cards, DialogMainFrame.PLATEPANEL);
  }

  public void flipToPlate() {
    card_layout.show(cards, DialogMainFrame.PLATEPANEL);
  }

  public void showWellTable(String _plate_sys_name) {
    well_card = new WellPanel(this, dbm.getWellTableData(_plate_sys_name));
    cards.add(well_card, WELLPANEL);
    card_layout.show(cards, DialogMainFrame.WELLPANEL);
  }

  public void flipToWell() {
    card_layout.show(cards, DialogMainFrame.WELLPANEL);
  }

  public DatabaseManager getDatabaseManager() {
    return this.dbm;
  }

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
  public void updateProjectPanel() {}
}
