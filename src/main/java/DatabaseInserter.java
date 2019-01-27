package pm;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** */
public class DatabaseInserter {
  DatabaseManager dbm;
  DatabaseRetriever dbr;
  Connection conn;
  JTable table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** */
  public DatabaseInserter(DatabaseManager _dbm) {
    this.dbm = _dbm;
    this.conn = dbm.getConnection();
    this.dbr = this.dbm.getDatabaseRetriever();
  }

  public void insertProject(String _name, String _description, int _pmuser_id) {

    String insertSql = "SELECT new_project(?, ?, ?);";
    PreparedStatement insertPs;
    try {
      insertPs = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _description);
      insertPs.setString(2, _name);
      insertPs.setInt(3, _pmuser_id);
      insertPreparedStatement(insertPs);
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }

  public void updateProject(String _name, String _description, String _project_sys_name) {

    String sqlstring = "UPDATE project SET project_name = ?, descr = ? WHERE project_sys_name = ?;";
    PreparedStatement preparedStatement;
    try {
      preparedStatement = conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, _name);
      preparedStatement.setString(2, _description);
      preparedStatement.setString(3, _project_sys_name);
      preparedStatement.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }

  public void insertPreparedStatement(PreparedStatement _preparedStatement) {
    PreparedStatement preparedStatement = _preparedStatement;
    LOGGER.info(preparedStatement.toString());

    try {
      preparedStatement.executeUpdate();

    } catch (SQLException sqle) {
      LOGGER.warning("Failed to execute prepared statement: " + preparedStatement.toString());
      LOGGER.warning("Exception: " + sqle);
    }
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

  public void insertPlateSet(
      String _description,
      String _name,
      String _num_plates,
      String _plate_size_id,
      String _plate_type_id,
      String _project_id,
      String _withSamples) {

    try {
      String insertSql = "SELECT new_plate_set ( ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _description);
      insertPs.setString(2, _name);
      insertPs.setString(3, _num_plates);
      insertPs.setString(4, _plate_size_id);
      insertPs.setString(5, _plate_type_id);
      insertPs.setString(6, _project_id);
      insertPs.setString(7, _withSamples);

      LOGGER.info(insertPs.toString());
      insertPs.executeUpdate();

    } catch (SQLException sqle) {

    }
  }

  /**
   * called from DialogGroupPlates; Performs the creation of a new plate set from existing plates.
   * The HashMap contains the pair plateset_sys_name:number of plates. A dedicated function
   * "new_plate_set_from_group" will create the plateset and return the id without making any
   * plates.
   */
  public void makePlateSetFromGroup(
      String _description,
      String _name,
      HashMap<String, String> _plate_set_num_plates,
      String _plate_format,
      String _plate_type,
      int _project_id) {

    String description = _description;
    String name = _name;
    HashMap<String, String> plate_set_num_plates = _plate_set_num_plates;
    String plate_format = _plate_format;
    String plate_type = _plate_type;
    int project_id = _project_id;
    int format_id = 0;
    int new_plate_set_id = 0;
    // ResultSet resultSet;
    // PreparedStatement preparedStatement;

    // determine total number of plates in new plate set
    int total_num_plates = 0;

    Iterator it = plate_set_num_plates.entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry pair = (HashMap.Entry) it.next();
      total_num_plates = total_num_plates + Integer.parseInt((String) pair.getValue());
      // it.remove(); // avoids a ConcurrentModificationException
    }
    // LOGGER.info("total: " + total_num_plates);

    // determine format id
    LOGGER.info("format: " + plate_format);

    format_id = dbm.getDatabaseRetriever().getPlateFormatID(plate_format);
    //    format_id = dbr.getPlateFormatID(plate_format);

    // determine type id
    int plateTypeID = dbm.getDatabaseRetriever().getIDForPlateType(plate_type);
    // insert new plate set
    // INSERT INTO plate_set(descr, plate_set_name, num_plates, plate_size_id, plate_type_id,
    // project_id)
    String sqlstring = "SELECT new_plate_set_from_group (?, ?, ?, ?, ?, ?);";

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, description);
      preparedStatement.setString(2, name);
      preparedStatement.setInt(3, total_num_plates);
      preparedStatement.setInt(4, format_id);
      preparedStatement.setInt(5, plateTypeID);
      preparedStatement.setInt(6, project_id);

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      new_plate_set_id = resultSet.getInt("new_plate_set_from_group");
      // LOGGER.info("resultset: " + result);

    } catch (SQLException sqle) {
      LOGGER.warning("SQLE at inserting plate set from group: " + sqle);
    }

    // associate old plates with new plate set id
    Set<Integer> plate_ids = new HashSet<Integer>();
    Iterator it2 = plate_set_num_plates.entrySet().iterator();
    while (it2.hasNext()) {
      HashMap.Entry pair = (HashMap.Entry) it2.next();
      int plate_set_id =
          dbm.getDatabaseRetriever().getPlateSetIDForPlateSetSysName((String) pair.getKey());
      plate_ids.addAll(dbm.getDatabaseRetriever().getAllPlateIDsForPlateSetID(plate_set_id));
      it2.remove(); // avoids a ConcurrentModificationException
    }

    LOGGER.info("keys: " + plate_ids);

    this.associatePlateIDsWithPlateSetID(plate_ids, new_plate_set_id);
    dbm.getDmf().showPlateSetTable(dbm.getDmf().getSession().getProjectSysName());
  }

  /** Called from DialogGroupPlates from the plate panel/menubar */
  public void groupPlatesIntoPlateSet(
      String _description,
      String _name,
      Set<String> _plates,
      String _format,
      String _type,
      int _projectID) {
    String description = _description;
    String name = _name;
    Set<String> plates = _plates;
    String format = _format;
    String type = _type;
    int projectID = _projectID;
    int new_plate_set_id = 0;
    // ResultSet resultSet;
    // PreparedStatement preparedStatement;

    int format_id = dbm.getDatabaseRetriever().getPlateFormatID(format);
    int plateTypeID = dbm.getDatabaseRetriever().getIDForPlateType(type);
    int num_plates = plates.size();

    String sqlstring = "SELECT new_plate_set_from_group (?, ?, ?, ?, ?, ?);";

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, description);
      preparedStatement.setString(2, name);
      preparedStatement.setInt(3, num_plates);
      preparedStatement.setInt(4, format_id);
      preparedStatement.setInt(5, plateTypeID);
      preparedStatement.setInt(6, projectID);

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      new_plate_set_id = resultSet.getInt("new_plate_set_from_group");
      // LOGGER.info("resultset: " + result);

    } catch (SQLException sqle) {
      LOGGER.warning("SQLE at inserting plate set from group: " + sqle);
    }

    // associate old plates with new plate set id
    Set<Integer> plate_ids = new HashSet<Integer>();
    for (String temp : plates) {
      plate_ids.add(dbm.getDatabaseRetriever().getIDForSysName(temp, "plate"));
    }

    LOGGER.info("keys: " + plate_ids);

    this.associatePlateIDsWithPlateSetID(plate_ids, new_plate_set_id);
    dbm.getDmf().showPlateSetTable(dbm.getDmf().getSession().getProjectSysName());
  }

  public void associatePlateIDsWithPlateSetID(Set<Integer> _plateIDs, int _plate_set_id) {
    Set<Integer> plateIDs = _plateIDs;
    int plate_set_id = _plate_set_id;

    String holder = new String("INSERT INTO plate_plate_set (plate_set_id, plate_id) VALUES ");
    for (int temp : plateIDs) {
      holder = holder + "('" + plate_set_id + "', '" + temp + "'), ";
    }

    String insertSql = holder.substring(0, holder.length() - 2) + ";";
    LOGGER.info("insertSql: " + insertSql);
    PreparedStatement insertPs;
    try {
      insertPs = conn.prepareStatement(insertSql);
      insertPreparedStatement(insertPs);
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }
}
