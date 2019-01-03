package pm;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** */
public class DatabaseManager {
  Connection conn;
  JTable table;
  DatabaseInserter dbInserter;
  DatabaseRetriever dbRetriever;
  DialogMainFrame parent;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * Use 'pmdb' as the database name. Regular users will connect as pm_user and will have restricted
   * access (no delete etc.). Connect as pm_admin to get administrative priveleges.
   */
  public DatabaseManager(DialogMainFrame _parent) {
    parent = _parent;
    try {
      Class.forName("org.postgresql.Driver");

      // String url = "jdbc:postgresql://localhost/postgres";
      String url = "jdbc:postgresql://192.168.1.7/pmdb";
      Properties props = new Properties();
      props.setProperty("user", "pm_admin");
      props.setProperty("password", "welcome");

      conn = DriverManager.getConnection(url, props);
      dbInserter = new DatabaseInserter(this);
      dbRetriever = new DatabaseRetriever(this);
    } catch (ClassNotFoundException e) {

    } catch (SQLException sqle) {

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
      LOGGER.info("in init : ");

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
        LOGGER.info("pass is true; user: " + _user);
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
          LOGGER.info("rsKey: " + insertKey);
        }

      } else {
        LOGGER.info("Authentication failed, no session generated.");
      }

    } catch (SQLException sqle) {
      throw sqle;
    }
    return insertKey;
  }

  public JTable getProjectTableData() {

    try {
      Statement st = conn.createStatement();
      ResultSet rs =
          st.executeQuery(
              "SELECT project_sys_name AS ProjectID, project_name As Name, pmuser_name AS Owner, descr AS Description FROM project, pmuser WHERE pmuser_id = pmuser.id ORDER BY project.id DESC;");

      table = new JTable(buildTableModel(rs));
      // LOGGER.info(table);
      rs.close();
      st.close();
    } catch (SQLException sqle) {
    }
    return table;
  }

  public JTable getPlateSetTableData(String _project_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_set_sys_name AS PlateSetID, plate_set_name As Name FROM plate_set WHERE project_id = (select id from project where project_sys_name like ?) ORDER BY plate_set.id DESC;");

      pstmt.setString(1, _project_sys_name);
      ResultSet rs = pstmt.executeQuery();

      table = new JTable(buildTableModel(rs));
      LOGGER.info("PlateSet table: " + table);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {

    }
    return table;
  }

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
      LOGGER.info("projectID: " + results);
      this.getSession().setProjectID(results);

      rs.close();
      st.close();
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }

  public JTable getPlateTableData(String _plate_set_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate.plate_sys_name AS PlateID, plate_seq_num AS Order,  plate_type.plate_type_name As Type, plate_size.format AS Size FROM plate_set, plate, plate_type, plate_size WHERE plate.plate_set_id = (select id from plate_set where plate_set_sys_name like ?) AND plate.plate_type_id = plate_type.id AND plate.plate_set_id = plate_set.id AND plate_size.id = plate.plate_size_id;");

      pstmt.setString(1, _plate_set_sys_name);
      ResultSet rs = pstmt.executeQuery();

      table = new JTable(buildTableModel(rs));
      LOGGER.info("Got plate table ");
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {

    }
    return table;
  }

  public JTable getWellTableData(String _plate_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate.plate_sys_name AS PlateID, well.well_name AS Well, sample.sample_sys_name AS Sample FROM  plate, well, sample WHERE plate.id = well.plate_id AND sample.id = well.sample_id AND well.plate_id = (SELECT plate.id FROM plate WHERE plate.plate_sys_name = ?);");

      pstmt.setString(1, _plate_sys_name);
      ResultSet rs = pstmt.executeQuery();

      table = new JTable(buildTableModel(rs));
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {

    }
    return table;
  }

  public static DefaultTableModel buildTableModel(ResultSet _rs) throws SQLException {

    ResultSet rs = _rs;
    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();
    Vector<String> columnNames = new Vector<String>();
    for (int column = 1; column <= columnCount; column++) {
      columnNames.add(metaData.getColumnName(column));
    }

    // data of the table
    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    while (rs.next()) {
      Vector<Object> vector = new Vector<Object>();
      for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
        vector.add(rs.getObject(columnIndex));
      }
      data.add(vector);
    }

    return new DefaultTableModel(data, columnNames);
  }

  public String getDescriptionForProject(String _project_sys_name) {
    String result = new String();
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT descr  FROM  project WHERE project_sys_name =  ?;");

      pstmt.setString(1, _project_sys_name);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      result = rs.getString("descr");
      LOGGER.info("Description: " + result);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting description: " + sqle);
    }
    return result;
  }

  /**
   * Incoming variables: ( 'plate set name' 'description' '10' '96' 'assay')
   *
   * <p>Method signature in Postgres: CREATE OR REPLACE FUNCTION new_plate_set(_descr
   * VARCHAR(30),_plate_set_name VARCHAR(30), _num_plates INTEGER, _plate_size_id INTEGER,
   * _plate_type_id INTEGER, _project_id INTEGER, _with_samples boolean)
   */
  public void insertPlateSet(
      String _name,
      String _description,
      String _num_plates,
      String _plate_size_id,
      String _plate_type_id) {

    try {
      int project_id = parent.getSession().getProjectID();
      int plate_size_id =
          parent.getDatabaseManager().getDatabaseRetriever().getPlateFormatID(_plate_size_id);
      int plate_type_id =
          parent.getDatabaseManager().getDatabaseRetriever().getPlateTypeID(_plate_type_id);

      String insertSql = "SELECT new_plate_set ( ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _description);
      insertPs.setString(2, _name);
      insertPs.setInt(3, Integer.valueOf(_num_plates));
      insertPs.setInt(4, plate_size_id);
      insertPs.setInt(5, plate_type_id);
      insertPs.setInt(6, project_id);
      insertPs.setBoolean(7, true);

      LOGGER.info(insertPs.toString());
      insertPs.executeUpdate();
      //  SELECT new_plate_set ( 'descrip', 'myname', '10', '96', 'assay', 0, 't')
    } catch (SQLException sqle) {
      LOGGER.severe("Failed to create plate set: " + sqle);
    }
  }

  public String[] getPlateTypes() {
    String[] output = null;
    Array results = null;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT ARRAY (select plate_type_name from plate_type);");

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      results = rs.getArray("array");
      LOGGER.info("Description: " + results);
      rs.close();
      pstmt.close();
      output = (String[]) results.getArray();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plate types: " + sqle);
    }
    return output;
  }

  /*
   public HashMap<Integer, String> getAssayTypes(){

  try {
       PreparedStatement pstmt =
           conn.prepareStatement("SELECT * FROM assay_type;");

       ResultSet rs = pstmt.executeQuery();
       HashMap<Integer, String> assay_types = new HashMap<Integer, String>(50);

       while (rs.next()){

  for(int i=0; i<=50; ++i){
        row.put(md.getColumnName(i),rs.getObject(i));
      }
       list.add(row);
   }


     rs.close();
       pstmt.close();

     } catch (SQLException sqle) {

     }


  return assay_types;

     return assay_types;
   }
   */

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
    return parent.getSession();
  }
}
