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
}
