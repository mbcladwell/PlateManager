package pm;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** */
public class DatabaseManager {
  Connection conn;
  JTable table;

  public DatabaseManager() {
    try {
      Class.forName("org.postgresql.Driver");

      // String url = "jdbc:postgresql://localhost/postgres";
      String url = "jdbc:postgresql://192.168.1.7/postgres";
      Properties props = new Properties();
      props.setProperty("user", "postgres");
      props.setProperty("password", "postgres");

      conn = DriverManager.getConnection(url, props);
    } catch (ClassNotFoundException e) {

    } catch (SQLException sqle) {

    }
  }

  public Long initializeSession(String _user, String _password) throws SQLException {
    // return session ID
    Long insertKey = 0L;
    try {
      System.out.println("in init : ");

      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT password = crypt( ?,password) FROM pmuser WHERE pmuser_name = ?;");
      pstmt.setString(1, _password);
      pstmt.setString(2, _user);
      System.out.println(pstmt);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      boolean pass = rs.getBoolean(1);
      System.out.println("pass : " + pass);

      rs.close();
      pstmt.close();

      if (pass) {
        System.out.println("pass is true; user: " + _user);
        String insertSql =
            "INSERT INTO pmsession (pmuser_id) SELECT id FROM pmuser WHERE pmuser_name = ?;";
        PreparedStatement insertPs =
            conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        insertPs.setString(1, _user);
        System.out.println(insertPs);
        insertPs.executeUpdate();
        ResultSet rsKey = insertPs.getGeneratedKeys();

        if (rsKey.next()) {
          insertKey = rsKey.getLong(1);
          System.out.println("rsKey: " + insertKey);
        }

      } else {
        System.out.println("Authentication failed, no session generated.");
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
              "SELECT project_sys_name AS ProjectID, project_name As Name, pmuser_name AS Owner FROM project, pmuser WHERE pmuser_id = pmuser.id ORDER BY project.id DESC;");

      table = new JTable(buildTableModel(rs));
      // System.out.println(table);
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
              "SELECT plate_set_sys_name AS PlateSetID, plate_set_name As Name FROM plate_set WHERE project_id = (select id from project where project_sys_name like ?);");

      pstmt.setString(1, _project_sys_name);
      ResultSet rs = pstmt.executeQuery();

      table = new JTable(buildTableModel(rs));
      System.out.println("PlateSet table: " + table);
      rs.close();
      pstmt.close();

    } catch (SQLException sqle) {

    }
    return table;
  }

  public void insertProject(String _name, String _description, int _pmuser_id) {

    try {
      String insertSql =
          "INSERT INTO pmsession (project_name, description, pmuser_id) VALUES (?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _name);
      insertPs.setString(2, _description);
      insertPs.setString(3, Integer.toString(_pmuser_id));
      System.out.println(insertPs);
      insertPs.executeUpdate();

    } catch (SQLException sqle) {

    }
  }

  public JTable getPlateTableData(String _plate_set_sys_name) {
    try {
      PreparedStatement pstmt =
          conn.prepareStatement(
              "SELECT plate.plate_sys_name AS PlateID, plate_seq_num AS Order,  plate_type.plate_type_name As Type FROM plate_set, plate, plate_type WHERE plate.plate_set_id = (select id from plate_set where plate_set_sys_name like ?) AND plate.plate_type_id = plate_type.id AND plate.plate_set_id = plate_set.id;");

      pstmt.setString(1, _plate_set_sys_name);
      ResultSet rs = pstmt.executeQuery();

      table = new JTable(buildTableModel(rs));
      System.out.println("Got plate table ");
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
}
