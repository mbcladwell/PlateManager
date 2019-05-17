package pm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/** */
public class DatabaseManager {
  Connection conn;
  // CustomTable table;
  DatabaseInserter dbInserter;
  DatabaseRetriever dbRetriever;
  DialogMainFrame dmf;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  // psql -U pm_admin -h 192.168.1.7 -d pmdb


  /**
   * Use 'pmdb' as the database name. Regular users will connect as pm_user and will have restricted
   * access (no delete etc.). Connect as pm_admin to get administrative priveleges.
   */
  public DatabaseManager(DialogMainFrame _dmf) {
    dmf = _dmf;

    try {
      Class.forName("org.postgresql.Driver");

// String url = "jdbc:postgresql://localhost/postgres";
      String url = "jdbc:postgresql://192.168.1.11/pmdb";
      Properties props = new Properties();
      props.setProperty("user", "pm_admin");
      props.setProperty("password", "welcome");

      conn = DriverManager.getConnection(url, props);
      dbInserter = new DatabaseInserter(this);
      dbRetriever = new DatabaseRetriever(this);
    } catch (ClassNotFoundException e) {
      LOGGER.severe("Class not found: " + e);
    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception: " + sqle);
    }
  }

  /**
   * Initializes a session that remains active and provides user id information throughout database
   * activity. All regular users connected with pm_user priveleges.
   *
   * @param _user user name managed by plate_manager
   * @param _password password controlled by user
   */
  public Long initializeSession(String _user, String _password) throws SQLException {
    // return session ID
    Long insertKey = 0L;
    try {
      // LOGGER.info("in init : ");

      PreparedStatement pstmt =
          conn.prepareStatement(
              //  "SELECT password = crypt( ?,password) FROM pmuser WHERE pmuser_name = ?;");
              "SELECT password = ?, password FROM pmuser WHERE pmuser_name = ?;");
      pstmt.setString(1, _password);
      pstmt.setString(2, _user);
      LOGGER.info(pstmt.toString());
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      boolean pass = rs.getBoolean(1);
      LOGGER.info("pass : " + pass);

      rs.close();
      pstmt.close();

      if (pass) {
        // LOGGER.info("pass is true; user: " + _user);
        String insertSql =
            "INSERT INTO pmsession (pmuser_id) SELECT id FROM pmuser WHERE pmuser_name = ?;";
        PreparedStatement insertPs =
            conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        insertPs.setString(1, _user);
        LOGGER.info(insertPs.toString());
        insertPs.executeUpdate();
        ResultSet rsKey = insertPs.getGeneratedKeys();

        if (rsKey.next()) {
          insertKey = rsKey.getLong(1);
          // LOGGER.info("rsKey: " + insertKey);
        }

      } else {
        LOGGER.info("Authentication failed, no session generated.");
      }

    } catch (SQLException sqle) {
      throw sqle;
    }
    return insertKey;
  }

    /*
  public CustomTable getPlateSetTableData(String _project_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_set.plate_set_sys_name AS \"PlateSetID\", plate_set_name As \"Name\", format AS \"Format\", num_plates AS \"# plates\" , plate_type.plate_type_name AS \"Type\", plate_layout_name.name AS \"Layout\"   , plate_set.descr AS \"Description\" FROM plate_set, plate_format, plate_type, plate_layout_name WHERE plate_format.id = plate_set.plate_format_id AND plate_set.plate_layout_name_id = plate_layout_name.id  AND plate_set.plate_type_id = plate_type.id AND project_id = (select id from project where project_sys_name like ?) ORDER BY plate_set.id DESC;");

      pstmt.setString(1, _project_sys_name);
      ResultSet rs = pstmt.executeQuery();

      CustomTable table = new CustomTable(dmf, buildTableModel(rs));
      rs.close();
      pstmt.close();
      return table;
    } catch (SQLException sqle) {

    }
    return null;
  }
    */
  public void updateSessionWithProject(String _project_sys_name) {
    int results = 0;
    String project_sys_name = _project_sys_name;
    this.getSession().setProjectSysName(project_sys_name);

    try {
      String query =
          new String("SELECT id FROM project WHERE project_sys_name = '" + project_sys_name + "';");
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      rs.next();
      results = rs.getInt("id");
      // LOGGER.info("projectID: " + results);
      this.getSession().setProjectID(results);

      rs.close();
      st.close();
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }
    /*
  public CustomTable getPlateTableData(String _plate_set_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate.plate_sys_name AS \"PlateID\", plate_plate_set.plate_order AS \"Order\",  plate_type.plate_type_name As \"Type\", plate_format.format AS \"Format\" FROM plate_set, plate, plate_type, plate_format, plate_plate_set WHERE plate_plate_set.plate_set_id = (select id from plate_set where plate_set_sys_name like ?) AND plate.plate_type_id = plate_type.id AND plate_plate_set.plate_id = plate.id AND plate_plate_set.plate_set_id = plate_set.id  AND plate_format.id = plate.plate_format_id ORDER BY plate_plate_set.plate_order DESC;");
      pstmt.setString(1, _plate_set_sys_name);
      LOGGER.info("statement: " + pstmt.toString());
      ResultSet rs = pstmt.executeQuery();

      CustomTable table = new CustomTable(dmf, buildTableModel(rs));
      LOGGER.info("Got plate table " + table.getSelectedRowsAndHeaderAsStringArray().toString());
      rs.close();
      pstmt.close();
      return table;

    } catch (SQLException sqle) {
	LOGGER.info("Exception in dbm.getPlateTableData: " + sqle);
    }
    return null;
  }

  public CustomTable getWellTableData(String _plate_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
   
      "SELECT plate.plate_sys_name AS \"PlateID\", well_numbers.well_name AS \"Well\", well.by_col AS \"Well_NUM\", sample.sample_sys_name AS \"Sample\", sample.accs_id as \"Accession\" FROM  plate, sample, well_sample, well JOIN well_numbers ON ( well.by_col= well_numbers.by_col)  WHERE plate.id = well.plate_id AND well_sample.well_id=well.id AND well_sample.sample_id=sample.id AND well.plate_id = (SELECT plate.id FROM plate WHERE plate.plate_sys_name = ?) AND  well_numbers.plate_format = (SELECT plate_format_id  FROM plate_set WHERE plate_set.ID =  (SELECT plate_set_id FROM plate_plate_set WHERE plate_id = plate.ID LIMIT 1) ) ORDER BY well.by_col DESC;");


      
      pstmt.setString(1, _plate_sys_name);
      ResultSet rs = pstmt.executeQuery();

      CustomTable table = new CustomTable(dmf, buildTableModel(rs));
      rs.close();
      pstmt.close();
      return table;
    } catch (SQLException sqle) {
      LOGGER.severe("Failed to retrieve well data: " + sqle);
    }
    return null;
  }
*/

  public DefaultTableModel buildTableModel(ResultSet _rs) {

    try {
      ResultSet rs = _rs;
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();

      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      Vector<String> columnNames = new Vector<String>();
      /*
      String[] columnNames = new String[columnCount];
      for (int column = 0; column < columnCount; column++) {
        columnNames[column] = metaData.getColumnName(column + 1);
      }
      */
      for (int column = 0; column < columnCount; column++) {
        columnNames.addElement(metaData.getColumnName(column + 1));
      }

      // data of the table
      while (rs.next()) {
        Vector<Object> vector = new Vector<Object>();

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
          vector.add(rs.getObject(columnIndex + 1));
        }
        data.add(vector);
      }
      // LOGGER.info("data: " + data);
      return new DefaultTableModel(data, columnNames);

      //          data.stream().map(List::toArray).toArray(Object[][]::new), columnNames);

    } catch (SQLException sqle) {
      LOGGER.severe("SQLException in buildTableModel: " + sqle);
    }

    return null;
  }

  /**
   * Incoming variables: ( 'plate set name' 'description' '10' '96' 'assay')
   *
   * <p>Method signature in Postgres: CREATE OR REPLACE FUNCTION new_plate_set(_descr
   * VARCHAR(30),_plate_set_name VARCHAR(30), _num_plates INTEGER, _plate_format_id INTEGER,
   * _plate_type_id INTEGER, _project_id INTEGER, _with_samples boolean)
   */
  public void insertPlateSet(
      String _name,
      String _description,
      String _num_plates,
      String _plate_format_id,
      int _plate_type_id,
      int _plate_layout_id) {

    try {
      int project_id = dmf.getSession().getProjectID();
      int plate_format_id =
          Integer.parseInt(_plate_format_id);
      int plate_type_id = _plate_type_id;
      int plate_layout_id = _plate_layout_id;
         

      String insertSql = "SELECT new_plate_set ( ?, ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _description);
      insertPs.setString(2, _name);
      insertPs.setInt(3, Integer.valueOf(_num_plates));
      insertPs.setInt(4, plate_format_id);
      insertPs.setInt(5, plate_type_id);
      insertPs.setInt(6, project_id);
      insertPs.setInt(7, plate_layout_id);    
      insertPs.setBoolean(8, true);

      // LOGGER.info(insertPs.toString());
      int rowsAffected   = insertPs.executeUpdate();
       ResultSet rsKey = insertPs.getGeneratedKeys();
       rsKey.next();
       int new_ps_id = rsKey.getInt(1);
       insertPs.close();
      //  SELECT new_plate_set ( 'descrip', 'myname', '10', '96', 'assay', 0, 't')
    } catch (SQLException sqle) {
      LOGGER.severe("Failed to create plate set: " + sqle);
    }

 this.getDmf().showPlateSetTable(this.getDmf().getSession().getProjectSysName());    
  }
  /**
   * ******************************************************************
   *
   * <p>Generic DB activities
   *
   * <p>****************************************************************
   */
  public void insertPreparedStatement(PreparedStatement _preparedStatement) {
    PreparedStatement preparedStatement = _preparedStatement;
    //  LOGGER.info(preparedStatement.toString());

    try {
      preparedStatement.executeUpdate();

    } catch (SQLException sqle) {
      LOGGER.warning("Failed to execute prepared statement: " + preparedStatement.toString());
      LOGGER.warning("Exception: " + sqle);
    }
  }

  /** TableModel Columns: PSID Name Descr Format  called from the PlateSet menu item "group" */
  public void groupPlateSets(JTable _table) {
    // 4 columns in the plate set table
    JTable plate_set_table = _table;
    TableModel tableModel = plate_set_table.getModel();
    int[] selection = plate_set_table.getSelectedRows();
    String[][] results = new String[selection.length][4];

    //  LOGGER.info("selection: " + selection.toString());
    ArrayList<String> plateSet = new ArrayList<String>();
    Set<String> plateFormatSet = new HashSet<String>();

    for (int i = 0; i < selection.length; i++) {
      for (int j = 0; j < 4; j++) {
        results[i][j] = tableModel.getValueAt(selection[i], j).toString();
        // LOGGER.info("i: " + i + " j: " + j + " results[i][j]: " + results[i][j]);
      }
    }
    for (int k = 0; k < selection.length; k++) {
      plateSet.add(results[k][0]);
      // LOGGER.info("prjID: " + results[k][0]);

      plateFormatSet.add(results[k][2]);
      // LOGGER.info("pltformat: " + results[k][2]);
    }
    LOGGER.info("Size of plateFormatSet: " + plateFormatSet.size());
    if (plateFormatSet.size() == 1) {
      HashMap<String, String> numberOfPlatesInPlateSets =
          dbRetriever.getNumberOfPlatesInPlateSets(plateSet);
      String format = new String();
      for (Iterator<String> it = plateFormatSet.iterator(); it.hasNext(); ) {
        format = it.next();
      }
      new DialogGroupPlateSet(dmf, numberOfPlatesInPlateSets, format, plateSet);
    } else {
      JOptionPane.showMessageDialog(
          dmf,
          "Plate sets to be grouped must be of the same formats\n and of the same layout!",
          "Error!",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Launched by the Plate menu item "group". Since by definition a plate set can only have one format of
   * plate, no need to check that there is only one format
   */
  public void groupPlates(CustomTable _table) {
    // 4 columns in the plate  table
    // plate_sys_name Order type format
    CustomTable plate_table = _table;
    TableModel tableModel = plate_table.getModel();
    int[] selection = plate_table.getSelectedRows();
    String[][] results = new String[selection.length][4];
    String numberOfPlates = Integer.valueOf(selection.length).toString();

    //  LOGGER.info("selection: " + selection.toString());
    Set<String> plateSet = new HashSet<String>();

    for (int i = 0; i < selection.length; i++) {
      for (int j = 0; j < 4; j++) {
        results[i][j] = tableModel.getValueAt(selection[i], j).toString();
        // LOGGER.info("i: " + i + " j: " + j + " results[i][j]: " + results[i][j]);
      }
    }
    for (int k = 0; k < selection.length; k++) {
      plateSet.add(results[k][0]);
      // LOGGER.info("prjID: " + results[k][0]);

    }
    String format = new String();
    format = results[1][3];
    new DialogGroupPlates(dmf, plateSet, format);
  }

     /**
      * Collapse multiple plates by quadrant. 
      *
      *<p> Plate set table:  id|plate_set_name|descr| plate_set_sys_name | num_plates|
      *plate_format_id|plate_type_id|project_id |updated            
      */
    public void reformatPlateSet(CustomTable _table){
    CustomTable plate_set_table = _table;
    TableModel tableModel = plate_set_table.getModel();
    int[] selection = plate_set_table.getSelectedRows();
    if (selection.length > 1){
       JOptionPane.showMessageDialog(dmf,
    "Select one plate set.",
    "Error",
    JOptionPane.ERROR_MESSAGE);
    }else{
       
	String format = (String)tableModel.getValueAt(selection[0], 2).toString();
	    String[] plate_set_sys_name = new String[1];
	    plate_set_sys_name[0] = tableModel.getValueAt(selection[0], 0).toString();
	    Integer[] plate_set_id = this.getDatabaseRetriever().getIDsForSysNames(plate_set_sys_name, "plate_set", "plate_set_sys_name");
	    String descr = (String)tableModel.getValueAt(selection[0], 4);
	    int num_plates = (int)tableModel.getValueAt(selection[0], 3);
	    String plate_type = (String)tableModel.getValueAt(selection[0], 4);
	    int num_samples = this.getDatabaseRetriever().getNumberOfSamplesForPlateSetID(plate_set_id[0]);
	    int plate_layout_name_id = this.getDatabaseRetriever().getPlateLayoutNameIDForPlateSetID((int)plate_set_id[0]);
	    LOGGER.info("plate_set_id[0]: " + plate_set_id[0]);
	    switch(format){
	    case "96":
			DialogReformatPlateSet drps = new DialogReformatPlateSet( dmf, (int)plate_set_id[0], plate_set_sys_name[0], descr, num_plates, num_samples, plate_type, format, plate_layout_name_id);
		
		    break;
	    case "384":	 drps = new DialogReformatPlateSet( dmf, (int)plate_set_id[0], plate_set_sys_name[0], descr, num_plates, num_samples, plate_type, format, plate_layout_name_id);
		
		    break;
	    case "1536":  JOptionPane.showMessageDialog(dmf,
    "1536 well plates can not be reformatted.",
    "Error", JOptionPane.ERROR_MESSAGE);
		    break;
		    
		    }	
    }
    }

    
  public DialogMainFrame getDmf() {
    return this.dmf;
  }

  public DatabaseInserter getDatabaseInserter() {
    return this.dbInserter;
  }

  public DatabaseRetriever getDatabaseRetriever() {
    return this.dbRetriever;
  }

  public Connection getConnection() {
    return this.conn;
  }

  public Session getSession() {
    return dmf.getSession();
  }
}
