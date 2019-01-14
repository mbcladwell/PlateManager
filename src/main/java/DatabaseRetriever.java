package pm;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** */
public class DatabaseRetriever {
  DatabaseManager dbm;
  Connection conn;
  JTable table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** */
  public DatabaseRetriever(DatabaseManager _dbm) {
    this.dbm = _dbm;
    this.conn = dbm.getConnection();
  }

  /** To reduce traffic, hardcode as the formats are unlikely to change */
  public int getPlateFormatID(String _format) {
    int results = 0;
    switch (_format) {
      case "96":
        results = 1;
        break;
      case "384":
        results = 2;
        break;
      case "1536":
        results = 3;
        break;
    }
    return results;
  }

  public int getPlateTypeID(String _type) {
    int results = 0;

    try {
      String query =
          new String("SELECT id FROM plate_type WHERE plate_type_name = '" + _type + "';");
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      rs.next();
      results = rs.getInt("id");
      LOGGER.info("ID: " + results);

      rs.close();
      st.close();
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
    return results;
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

  public String getDescriptionForPlateSet(String _plateset_sys_name) {
    String result = new String();
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT descr  FROM  plate_set WHERE plate_set_sys_name =  ?;");

      pstmt.setString(1, _plateset_sys_name);
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
   * Return a key/value HashMap with the number of plates in each plate set. Used to inform user
   * when grouping plate sets. @Set projectSet a set that contains plate_set IDs to be iterated
   * over.
   */
  public HashMap<String, String> getNumberOfPlatesInPlateSets(Set<String> _plate_setSet) {
    Set<String> plate_setSet = _plate_setSet;
    HashMap<String, String> plate_setPlateCount = new HashMap<String, String>();
    String result;
    for (String s : plate_setSet) {
      try {
        PreparedStatement pstmt =
            conn.prepareStatement(
                "SELECT count(*) AS exact_count FROM (SELECT plate.plate_sys_name  FROM  plate, plate_set WHERE plate.plate_set_id = plate_set.id AND plate_set_sys_name = ?) AS count;");

        pstmt.setString(1, s);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        result = rs.getString("exact_count");
        plate_setPlateCount.put(s, result);
        LOGGER.info("s:: " + s + " result: " + result);
        rs.close();
        pstmt.close();

      } catch (SQLException sqle) {
        LOGGER.severe("SQL exception getting plate count: " + sqle);
      }
    }
    return plate_setPlateCount;
  }
}
