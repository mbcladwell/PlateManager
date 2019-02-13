package pm;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.JTable;

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

  /**
   * ******************************************************************
   *
   * <p>Project related
   *
   * <p>****************************************************************
   */
  public String getDescriptionForProject(String _project_sys_name) {
    String result = new String();
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT descr  FROM  project WHERE project_sys_name =  ?;");

      pstmt.setString(1, _project_sys_name);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      result = rs.getString("descr");
      // LOGGER.info("Description: " + result);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting description: " + sqle);
    }
    return result;
  }

  public int getProjectIDForProjectSysName(String _project_sys_name) {
    String project_sys_name = _project_sys_name;
    // int plate_set_id;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT project.id FROM   project WHERE project_sys_name = ?;");

      pstmt.setString(1, project_sys_name);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      int project_id = Integer.valueOf(rs.getString("id"));

      // LOGGER.info("result: " + plate_set_id);
      rs.close();
      pstmt.close();
      return project_id;

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting project_id: " + sqle);
    }
    int dummy = -1;
    return dummy;
  }

  /**
   * ******************************************************************
   *
   * <p>Plate Set related ********************************************
   *
   * <p>****************************************************************
   */
  public String getPlateSetSysNameForPlateSysName(String _plate_sys_name) {
    String result = new String();
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_set.plate_set_sys_name  FROM  plate_set, plate, plate_plate_set WHERE plate_plate_set.plate_set_id = plate_set.id AND plate_plate_set.plate_id = plate.id AND plate_sys_name =  ?;");

      pstmt.setString(1, _plate_sys_name);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      result = rs.getString("plate_set_sys_name");
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plateset_sys_name from plate_sys_name: " + sqle);
    }
    return result;
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
      // LOGGER.info("Description: " + result);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting description: " + sqle);
    }
    return result;
  }

  public int getPlateSetIDForPlateSetSysName(String _plateset_sys_name) {
    String plateset_sys_name = _plateset_sys_name;
    // int plate_set_id;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_set.id FROM   plate_set WHERE plate_set_sys_name = ?;");

      pstmt.setString(1, plateset_sys_name);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      int plate_set_id = Integer.valueOf(rs.getString("id"));

      // LOGGER.info("result: " + plate_set_id);
      rs.close();
      pstmt.close();
      return plate_set_id;

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plateset_id: " + sqle);
    }
    int dummy = -1;
    return dummy;
  }

  /**
   * Return a key/value HashMap with the number of plates in each plate set. Used to inform user
   * when grouping plate sets. @Set projectSet a set that contains plate_set IDs to be iterated
   * over.
   */
  public HashMap<String, String> getNumberOfPlatesInPlateSets(ArrayList<String> _plate_setSet) {
    ArrayList<String> plate_setSet = _plate_setSet;
    HashMap<String, String> plate_setPlateCount = new HashMap<String, String>();
    String result;
    for (String s : plate_setSet) {
      try {
        PreparedStatement pstmt =
            conn.prepareStatement(
                "SELECT count(*) AS exact_count FROM (SELECT plate.plate_sys_name  FROM  plate, plate_set, plate_plate_set WHERE plate_plate_set.plate_set_id = plate_set.id AND plate_plate_set.plate_id = plate.id AND plate_set_sys_name = ?) AS count;");

        pstmt.setString(1, s);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        result = rs.getString("exact_count");
        plate_setPlateCount.put(s, result);
        // LOGGER.info("s: " + s + " result: " + result);
        rs.close();
        pstmt.close();

      } catch (SQLException sqle) {
        LOGGER.severe("SQL exception getting plate count: " + sqle);
      }
    }
    return plate_setPlateCount;
  }

  public Set<Integer> getAllPlateIDsForPlateSetID(int _plate_set_id) {
    int plate_set_id = _plate_set_id;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_id  FROM  plate_plate_set WHERE plate_plate_set.plate_set_id = ?;");

      pstmt.setInt(1, plate_set_id);
      ResultSet rs = pstmt.executeQuery();
      Set<Integer> all_plate_ids = new HashSet<>();

      while (rs.next()) {
        all_plate_ids.add(rs.getInt(1));
        // LOGGER.info("A plate set ID: " + rs.getInt(1));
      }

      rs.close();
      pstmt.close();
      return all_plate_ids;

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plate count: " + sqle);
    }

    return null;
  }

  /**
   * Needed when grouping plate sets. Calls public Set<Integer> getAllPlateIDsForPlateSetID(int
   * _plate_set_id) to get the plate IDs for each plate.
   *
   * @param HashMap<String, String> holds the map: "plate_set_sys_name"="number of plates"
   */
  public Set<Integer> getAllPlateIDsForMultiplePlateSetSysNames(
      HashMap<String, String> _plate_set_sys_names) {
    // for each plate_set_sys_name get the plate_set.id
    HashMap<String, String> plate_set_sys_names = _plate_set_sys_names;
    Set<Integer> plate_set_IDs = new TreeSet<Integer>();
    LOGGER.info("plate_set_sys_names: " + plate_set_sys_names);

    Iterator it = plate_set_sys_names.entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry pair = (HashMap.Entry) it.next();
      LOGGER.info("id: " + this.getIDForSysName((String) pair.getKey(), "plate_set"));

      plate_set_IDs.add(this.getIDForSysName((String) pair.getKey(), "plate_set"));
      it.remove(); // avoids a ConcurrentModificationException
    }
    Set<Integer> plate_IDs = new TreeSet<Integer>();
    for (int i : plate_set_IDs) {
      // get the plate_ids for one plate_set
      Set<Integer> one_plate_sets_plate_ids = this.getAllPlateIDsForPlateSetID(i);
      for (int j : one_plate_sets_plate_ids) {

        plate_IDs.add(j);
      }
    }
    return plate_IDs;
  }

  /**
   * ******************************************************************
   *
   * <p>Plate related
   *
   * <p>****************************************************************
   */
  public String[] getPlateTypes() {
    String[] output = null;
    Array results = null;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT ARRAY (select plate_type_name from plate_type);");

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      results = rs.getArray("array");
      // LOGGER.info("Description: " + results);
      rs.close();
      pstmt.close();
      output = (String[]) results.getArray();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plate types: " + sqle);
    }
    return output;
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

  public int getIDForPlateType(String _plate_type) {
    String plate_type = _plate_type;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate_type.id FROM   plate_type WHERE plate_type_name = ?;");

      pstmt.setString(1, plate_type);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      int plateTypeID = Integer.valueOf(rs.getString("id"));

      // LOGGER.info("result: " + plateTypeID);
      rs.close();
      pstmt.close();
      return plateTypeID;

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plateset_id: " + sqle);
    }
    int dummy = -1;
    return dummy;
  }
  /**
   * ******************************************************************
   *
   * <p>Well related
   *
   * <p>****************************************************************
   */

  /**
   * ******************************************************************
   *
   * <p>Sample related
   *
   * <p>****************************************************************
   */

  /**
   * ******************************************************************
   *
   * <p>Generic (refactored)
   *
   * <p>****************************************************************
   */
  public int getIDForSysName(String _sys_name, String _entity) {
    String sys_name = _sys_name;
    String entity = _entity;

    String custom_statement = new String();

    int id = 0;
    //  "SELECT plate_set.id FROM   plate_set WHERE plate_set_sys_name = ?;");

    switch (entity) {
      case "project":
        custom_statement = "SELECT project.id FROM project WHERE project_sys_name =?;";
        break;
      case "plate_set":
        custom_statement = "SELECT plate_set.id FROM plate_set WHERE plate_set_sys_name =?;";
        break;
      case "plate":
        custom_statement = "SELECT plate.id FROM plate WHERE plate_sys_name =?;";
        break;
      case "sample":
        custom_statement = "SELECT sample.id FROM sample WHERE sample_sys_name =?;";
        break;
      case "":
        custom_statement = "SELECT p FROM p WHERE _sys_name =?;";
        break;
    }

    try {
      PreparedStatement pstmt = conn.prepareStatement(custom_statement);
      pstmt.setString(1, sys_name);
      LOGGER.info("prepared statement: " + pstmt);

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      id = rs.getInt("id");

      // LOGGER.info("result: " + id);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plateset_id: " + sqle);
    }
    return id;
  }

  /**
   * @param _sys_names array of system_names
   * @param _table table to be queried
   * @param _column name of the sys_name column e.g. plate_sys_name, plate_set_sys_name
   */
  public Integer[] getIDsForSysNames(String[] _sys_names, String _table, String _column) {
    String[] sys_names = _sys_names;
    String table = _table;
    String column = _column;
    Integer[] sys_ids = new Integer[sys_names.length];

    String sqlstring = "SELECT get_ids_for_sys_names (?, ?, ?);";

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setArray(1, conn.createArrayOf("VARCHAR", sys_names));
      preparedStatement.setString(2, table);
      preparedStatement.setString(3, column);

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      sys_ids = (Integer[]) (resultSet.getArray("get_ids_for_sys_names")).getArray();

      // LOGGER.info("resultset: " + result);

    } catch (SQLException sqle) {
      LOGGER.warning("SQLE at getIDsForSysNames: " + sqle);
    }

    return sys_ids;
  }

  public String[] getAssayTypes() {
    String[] output = null;
    Array results = null;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT ARRAY (select assay_type_name from assay_type);");

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      results = rs.getArray("array");
      // LOGGER.info("Description: " + results);
      rs.close();
      pstmt.close();
      output = (String[]) results.getArray();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plate types: " + sqle);
    }
    return output;
  }

  public String[] getPlateLayoutNames(int format_id) {
    String[] output = null;
    Array results = null;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT ARRAY (select name from plate_layout_name WHERE plate_format_id = ?);");
      pstmt.setInt(1, format_id);

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      results = rs.getArray("array");
      // LOGGER.info("Description: " + results);
      rs.close();
      pstmt.close();
      output = (String[]) results.getArray();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting plate types: " + sqle);
    }
    return output;
  }

   
  public CustomTable getPlateLayout(int _plate_layout_name_id) {
CustomTable table;
int plate_layout_name_id = _plate_layout_name_id;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT *  FROM plate_layout WHERE plate_layout_name_id = ? ORDER BY well_by_col;");

      pstmt.setString(1, plate_layout_name_id);
      ResultSet rs = pstmt.executeQuery();

      CustomTable table = new CustomTable(dmf, buildTableModel(rs));
      // LOGGER.info("Got plate table ");
      rs.close();
      pstmt.close();
    

    } catch (SQLException sqle) {

    }
    return table;
  }



    
    
  public int getAssayIDForAssayType(String _assay_name) {
    int result = 0;
    String assay_name = _assay_name;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement("select id from assay_type WHERE assay_type_name = ?;");
      pstmt.setString(1, assay_name);

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      result = rs.getInt("id");
      // LOGGER.info("Description: " + results);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting assay_type_id: " + sqle);
    }
    return result;
  }

  public int getPlateLayoutIDForPlateLayoutName(String _plate_layout_name) {
    int result = 0;
    String plate_layout_name = _plate_layout_name;

    try {
      PreparedStatement pstmt =
          conn.prepareStatement("select id from plate_layout_name WHERE name = ?;");
      pstmt.setString(1, plate_layout_name);

      ResultSet rs = pstmt.executeQuery();
      rs.next();
      result = rs.getInt("id");
      LOGGER.info("resuklt plate layout name: " + result);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {
      LOGGER.severe("SQL exception getting assay_type_id: " + sqle);
    }
    return result;
  }
}
