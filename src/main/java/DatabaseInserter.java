package pm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** */
public class DatabaseInserter {
  DatabaseManager dbm;
  DialogMainFrame dmf;
  // private DatabaseRetriever dbr;
  Connection conn;
  JTable table;
  Utilities utils;
    Session session;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** */
  public DatabaseInserter(DatabaseManager _dbm) {
    this.dbm = _dbm;
    this.conn = dbm.getConnection();
    // this.dbr = dbm.getDatabaseRetriever();
    this.dmf = dbm.getDmf();
    this.utils = dmf.getUtilities();
    this.session = dmf.getSession();
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
      int new_plate_set_id;

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
      //ResultSet resultSet = insertPs.getResultSet();
      //resultSet.next();
      //new_plate_set_id = resultSet.getInt("new_plate_set");
     
    } catch (SQLException sqle) {
	LOGGER.warning("SQLE at inserting new plate set: " + sqle);
    }
    
  }

    /**
     * Modification of insertPlateSet using integers and returning ps_id
     */
public int insertPlateSet2(
      String _description,
      String _name,
      int _num_plates,
      int _plate_format_id,
      int _plate_type_id,
      int _project_id,
      int _plate_layout_name_id,
      boolean _withSamples) {
    
      int new_plate_set_id=0;

    try {
      String insertSql = "SELECT new_plate_set2 ( ?, ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _description);
      insertPs.setString(2, _name);
      insertPs.setInt(3, _num_plates);
      insertPs.setInt(4, _plate_format_id);
      insertPs.setInt(5, _plate_type_id);
      insertPs.setInt(6, _project_id);
      insertPs.setInt(7, _plate_layout_name_id);      
      insertPs.setBoolean(8, _withSamples);

      LOGGER.info(insertPs.toString());
      insertPs.execute();
      ResultSet resultSet = insertPs.getResultSet();
      resultSet.next();
      new_plate_set_id = resultSet.getInt("new_plate_set2");
     
    } catch (SQLException sqle) {
	LOGGER.warning("SQLE at inserting new plate set: " + sqle);
    }
    return new_plate_set_id;
  }


    
  /**
   * called from DialogGroupPlateSet; Performs the creation of a new plate set from existing plate
   * sets. The HashMap contains the pair plateset_sys_name:number of plates. A dedicated Postgres
   * function "new_plate_set_from_group" will create the plateset and return the id without making
   * any plates, nor will it associate the new plate_set.id with plates.
   *
   *<p>Note that the method groupPlatesIntoPlateSet is for grouping individual plates
   */
  public void groupPlateSetsIntoNewPlateSet(
      String _description,
      String _name,
      HashMap<String, String> _plate_set_num_plates,
      String _plate_format,
      String _plate_type,
      int _project_id,
      int _plate_layout_name_id,
      ArrayList<String> _plate_sys_names) {

    String description = _description;
    String name = _name;
    HashMap<String, String> plate_set_num_plates = _plate_set_num_plates;
    String plate_format = _plate_format;
    String plate_type = _plate_type;
    int project_id = _project_id;
    int format_id = 0;
    int new_plate_set_id = 0;
    int plate_layout_name_id = _plate_layout_name_id;
    ArrayList<String> plate_sys_names = _plate_sys_names;
  
    // determine total number of plates in new plate set
    int total_num_plates = 0;

    Iterator<Map.Entry<String,String>> it = plate_set_num_plates.entrySet().iterator();
    while (it.hasNext()) {
	HashMap.Entry<String, String> pair = (HashMap.Entry<String, String>) it.next();
      total_num_plates = total_num_plates + Integer.parseInt((String) pair.getValue());
      // it.remove(); // avoids a ConcurrentModificationException
    }
    // LOGGER.info("total: " + total_num_plates);

    // determine format id
    LOGGER.info("format: " + plate_format);

    format_id = Integer.parseInt(plate_format);
    //    format_id = dbr.getPlateFormatID(plate_format);

    // determine type id
    int plateTypeID = dbm.getDatabaseRetriever().getIDForPlateType(plate_type);

    // determine plate.ids for plate_sys_names
    // use   public Integer[] getIDsForSysNames(String[] _sys_names, String _table, String _column)
    // {
    // from DatabaseRetriever
    //LOGGER.info("plate_sys_names: " + plate_sys_names);
    //LOGGER.info("hashsetplate_sys_names: " + new HashSet<String>(plate_sys_names));

    //LOGGER.info(
    //  "set: "
    //      + dmf.getUtilities().getStringArrayForStringSet(new HashSet<String>(plate_sys_names)));
    Integer[] plate_ids =
        dbm.getDatabaseRetriever()
            .getIDsForSysNames(
                dmf.getUtilities().getStringArrayForStringSet(new HashSet<String>(plate_sys_names)),
                "plate",
                "plate_sys_name");

    // insert new plate set
    // INSERT INTO plate_set(descr, plate_set_name, num_plates, plate_size_id, plate_type_id,
    // project_id)

    String sqlstring = "SELECT new_plate_set_from_group (?, ?, ?, ?, ?, ?, ?);";

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, description);
      preparedStatement.setString(2, name);
      preparedStatement.setInt(3, total_num_plates);
      preparedStatement.setInt(4, format_id);
      preparedStatement.setInt(5, plateTypeID);
      preparedStatement.setInt(6, project_id);
       preparedStatement.setInt(7, plate_layout_name_id);
     
      //      preparedStatement.setArray(7, conn.createArrayOf("VARCHAR",
      // (plate_sys_names.toArray())));

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      new_plate_set_id = resultSet.getInt("new_plate_set_from_group");
      // LOGGER.info("resultset: " + result);

    } catch (SQLException sqle) {
      LOGGER.warning("SQLE at inserting plate set from group: " + sqle);
    }

    // associate old (existing) plates with new plate set id
    //

    Set<Integer> all_plate_ids = new HashSet<Integer>();
    Iterator<Map.Entry<String,String>> it2 = plate_set_num_plates.entrySet().iterator();
    while (it2.hasNext()) {
	HashMap.Entry<String,String> pair = (HashMap.Entry<String,String>) it2.next();
      int plate_set_id =
          dbm.getDatabaseRetriever().getPlateSetIDForPlateSetSysName((String) pair.getKey());
      all_plate_ids.addAll(dbm.getDatabaseRetriever().getAllPlateIDsForPlateSetID(plate_set_id));
      it2.remove(); // avoids a ConcurrentModificationException
    }

    LOGGER.info("keys: " + plate_ids);

    this.associatePlateIDsWithPlateSetID(all_plate_ids, new_plate_set_id);
    dbm.getDmf().showPlateSetTable(dbm.getDmf().getSession().getProjectSysName());
  }

  /** Called from DialogGroupPlates from the plate panel/menubar */
  public void groupPlatesIntoPlateSet(
      String _description,
      String _name,
      Set<String> _plates,
      String _format,
      String _type,
      int _projectID,
      int _plate_layout_name_id) {
    String description = _description;
    String name = _name;
    Set<String> plates = _plates;
    String format = _format;
    String type = _type;
    int projectID = _projectID;
    int new_plate_set_id = 0;
    int plate_layout_name_id = _plate_layout_name_id;
    // ResultSet resultSet;
    // PreparedStatement preparedStatement;

    int format_id = Integer.parseInt(format);
        

    int plateTypeID = dbm.getDatabaseRetriever().getIDForPlateType(type);
    int num_plates = plates.size();

    String sqlstring = "SELECT new_plate_set_from_group (?, ?, ?, ?, ?, ?, ?);";

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, description);
      preparedStatement.setString(2, name);
      preparedStatement.setInt(3, num_plates);
      preparedStatement.setInt(4, format_id);
      preparedStatement.setInt(5, plateTypeID);
      preparedStatement.setInt(6, projectID);
      preparedStatement.setInt(7, plate_layout_name_id);
      

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      new_plate_set_id = resultSet.getInt("new_plate_set_from_group");
      LOGGER.info(" new_plate_set_id: " + new_plate_set_id);

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
    Integer[] plate_ids =
        Arrays.stream(dmf.getUtilities().getIntArrayForIntegerSet(plateIDs))
            .boxed()
            .toArray(Integer[]::new);

    String sqlString = "SELECT assoc_plate_ids_with_plate_set_id(?,?)";
    // LOGGER.info("insertSql: " + insertSql);
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
      preparedStatement.setArray(1, conn.createArrayOf("INTEGER", plate_ids));
      preparedStatement.setInt(2, plate_set_id);
      preparedStatement.execute(); // executeUpdate expects no returns!!!

    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
  }
  /* Method signature in DialogAddPlateSetData
      dbi.associateDataWithPlateSet(
          nameField.getText(),
          descrField.getText(),
          plate_set_sys_name,
          (ComboItem) assayTypes.getSelectedItem().getKey(),
          (ComboItem) plateLayouts.getSelectedItem().getKey(),
          dmf.getUtilities().loadDataFile(fileField.getText()),
          checkBox.isSelected()
          (ComboItem) algoritmList.getSelectedItem().getKey());
  */

  /** Called from DialogAddPlateSetData */
  public void associateDataWithPlateSet(
      String _assayName,
      String _descr,
      String _plate_set_sys_name,
      int _format_id,
      int _assay_type_id,
      int _plate_layout_name_id,
      ArrayList<String[]> _table,
      boolean _auto_select_hits,
      int _hit_selection_algorithm,
      int _top_n_number) {

    String assayName = _assayName;
    String descr = _descr;
    int format_id = _format_id;
    String[] plate_set_sys_name = new String[1];
    plate_set_sys_name[0] = _plate_set_sys_name;

    int assay_type_id = _assay_type_id;
    int plate_layout_name_id = _plate_layout_name_id;
    ArrayList<String[]> table = _table;
    boolean auto_select_hits = _auto_select_hits;
    int hit_selection_algorithm = _hit_selection_algorithm;
    int top_n_number = _top_n_number;

Integer[] plate_set_id =
        dbm.getDatabaseRetriever()
            .getIDsForSysNames(plate_set_sys_name, "plate_set", "plate_set_sys_name");


    int assay_run_id =
        createAssayRun(assayName, descr, assay_type_id, plate_set_id[0], plate_layout_name_id);


    
    // read in data file an populate assay_result with data;
    // only continue if successful
    // if (table.get(0)[0] == "plate" & table.get(0)[1] == "plate" & table.get(0)[2] == "plate") {
    String sql_statement = new String("INSERT INTO assay_result (assay_run_id, plate_order, well, response) VALUES ");

    table.remove(0); // get rid of the header
    for (String[] row : table) {
      sql_statement =
          sql_statement
	  + "("
	  + assay_run_id
	  + ", "
	  + Integer.parseInt(row[0])
	  + ", "
	  + Integer.parseInt(row[1])
	  + ", "
	  + Double.parseDouble(row[2])
	  + "), ";
    }

    String insertSql = sql_statement.substring(0, sql_statement.length() - 2) + ";";

    PreparedStatement insertPs;
    try {
      insertPs = conn.prepareStatement(insertSql);
      insertPreparedStatement(insertPs);
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
      JOptionPane.showMessageDialog(
          dmf, "Problems parsing data file!.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    
    
    /**
     *
     * <p>Diagnostic query
     *
     * <p>SELECT temp_data.plate, temp_data.well, temp_data.response ,plate_plate_set.plate_order ,
     * plate_set.plate_set_sys_name , plate.plate_sys_name, well.id, well.well_name,
     * sample.sample_sys_name FROM temp_data, plate_plate_set, plate_set, plate, well,sample,
     * well_sample, well_numbers WHERE temp_data.plate = plate_plate_set.plate_order AND
     * plate_plate_set.plate_id = plate.id AND well.plate_id = plate.id AND well_sample.well_id =
     * well.id AND well_sample.sample_id = sample.id AND plate_plate_set.plate_set_id = plate_set.id
     * AND plate_plate_set.plate_set_id = 21 AND temp_data.well = well_numbers.by_col AND
     * well_numbers.well_name = well.well_name AND well_numbers.plate_format = 96 ORDER BY
     * plate_plate_set.plate_order, well_numbers.by_col;
     */
    // table assay_result: sample_id, response, assay_run_id
    // table temp_data: plate, well, response, bkgrnd_sub, norm, norm_pos
    //                                plate is the plate number
    //                                norm is normalized to max signal of unknowns
    //                                norm_pos is normalised setting mean of positives to 1
    // table assay_run: id, plate_set_id, plate_layout_name_id
    // plate_layout:  plate_layout_name_id, well_by_col, well_type_id
    // plate:  id
    // well: plate_id id
    // sample: id
    // well_sample:  well_id  sample_id
    // well_numbers: format  well_name  by_col


    //here I need to call process_assay_run_data(_assay_run_id integer) to normalize and background subtract
    //normalized data is in existing columns in assay_result
    
    String sql1 =
        "SELECT process_assay_run_data( " + assay_run_id + ");";

    LOGGER.info("insertSql: " + sql1);
    PreparedStatement insertPs2;
    try {
      insertPs2 = conn.prepareStatement(sql1);
      insertPreparedStatement(insertPs2);
    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }

    //Now I need to select hits if requested by user.  I have the assay_run_id, and the algorithm for hit selection.
    // stored procedure: new_hit_list(_name VARCHAR, _descr VARCHAR, _num_hits INTEGER, _assay_run_id INTEGER, hit_list integer[])
    // DialogNewHitList(DialogMainFrame _dmf, int  _assay_run_id, double[][] _selected_response, int _num_hits)
    // table = dmf.getDatabaseManager().getDatabaseRetriever().getDataForScatterPlot(assay_run_id);
    // 	norm_response = new ResponseWrangler(table, ResponseWrangler.NORM);
   //    double[][]  sortedResponse [response] [well] [type_id] [sample_id];
    // selected_response.getHitsAboveThreshold(threshold))
 
    
    if(auto_select_hits){

	ResponseWrangler rw = new ResponseWrangler(dmf.getDatabaseManager().getDatabaseRetriever().getDataForScatterPlot(assay_run_id),ResponseWrangler.NORM);
	double[][] sorted_response = rw.getSortedResponse();
	int number_of_hits = 0;
       
	
	switch(hit_selection_algorithm){
	case 1: //Top N
	    number_of_hits = top_n_number;
	    break;
	case 2: // mean(background) + 2SD
    
	    number_of_hits =  rw.getHitsAboveThreshold(rw.getMean_2_sd() );
	    break;
	case 3:  // mean(background) + 3SD
	    number_of_hits =  rw.getHitsAboveThreshold(rw.getMean_3_sd() );
	    break;	    	    
	}
	DialogNewHitList dnhl = new DialogNewHitList(dmf, assay_run_id, sorted_response, number_of_hits);
	
    }
  
    
  }

    
  public int createAssayRun(
      String _assayName,
      String _descr,
      int _assay_type_id,
      int _plate_set_id,
      int _plate_layout_name_id) {

    String assayName = _assayName;
    String descr = _descr;

    int plate_set_id = _plate_set_id;
    int assay_type_id = _assay_type_id;
    int plate_layout_name_id = _plate_layout_name_id;

    int new_assay_run_id = 0;

    String sqlstring = "SELECT new_assay_run(?, ?, ?, ?, ?);";
    // LOGGER.info("sql: " + sqlstring);

    try {
      PreparedStatement preparedStatement =
          conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, assayName);
      preparedStatement.setString(2, descr);
      preparedStatement.setInt(3, assay_type_id);
      preparedStatement.setInt(4, plate_set_id);
      preparedStatement.setInt(5, plate_layout_name_id);

      preparedStatement.execute(); // executeUpdate expects no returns!!!

      ResultSet resultSet = preparedStatement.getResultSet();
      resultSet.next();
      new_assay_run_id = resultSet.getInt("new_assay_run");
      // LOGGER.info("resultset: " + result);

    } catch (SQLException sqle) {
      LOGGER.warning("SQLE at inserting plate set from group: " + sqle);
    }
    // LOGGER.info("new assay id: " + new_assay_run_id);
    return new_assay_run_id;
  }



     
    /**
     * Called from DialogReformatPlateSet OK action listener
     */
    public void reformatPlateSet(int _source_plate_set_id, DialogMainFrame _dmf,  String _dest_plate_set_name, int _source_plate_num,
				 String _dest_descr, int _dest_plate_format_id, int _dest_plate_type_id,  int _dest_plate_layout_name_id, int _n_reps_source){
	int source_plate_set_id = _source_plate_set_id;
	DialogMainFrame dmf = _dmf;
	String dest_plate_set_name = _dest_plate_set_name;
	String dest_descr = _dest_descr;
	int dest_plate_format_id = _dest_plate_format_id;
	int dest_plate_type_id= _dest_plate_type_id;
	int dest_plate_layout_name_id = _dest_plate_layout_name_id;
	int source_plate_num = _source_plate_num;
	int n_reps_source = _n_reps_source;
       
	int dest_plate_num = (int)Math.ceil(source_plate_num*n_reps_source/4.0);
	int project_id = dbm.getSession().getProjectID();
	int dest_plate_set_id=0;

      // method signature:  reformat_plate_set(source_plate_set_id INTEGER, source_num_plates INTEGER, n_reps_source INTEGER, dest_descr VARCHAR(30), dest_plate_set_name VARCHAR(30), dest_num_plates INTEGER, dest_plate_format_id INTEGER, dest_plate_type_id INTEGER, project_id INTEGER, dest_plate_layout_name_id INTEGER )
      
      
    try {
      String insertSql = "SELECT reformat_plate_set( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setInt(1, source_plate_set_id);
	      insertPs.setInt(2, source_plate_num);
      insertPs.setInt(3, n_reps_source);
      
      insertPs.setString(4, dest_descr);
      insertPs.setString(5, dest_plate_set_name);
      insertPs.setInt(6, dest_plate_num);
      insertPs.setInt(7, dest_plate_format_id);
      insertPs.setInt(8, dest_plate_type_id);
      insertPs.setInt(9, project_id);
      insertPs.setInt(10, dest_plate_layout_name_id);      

      LOGGER.info(insertPs.toString());
      insertPs.execute();
      ResultSet resultSet = insertPs.getResultSet();
      resultSet.next();
      dest_plate_set_id = resultSet.getInt("reformat_plate_set");
      
      dmf.showPlateSetTable(dmf.getSession().getProjectSysName());
    } catch (SQLException sqle) {
	LOGGER.warning("SQLE at reformat plate set: " + sqle);
    }
      
	
	
	LOGGER.info("new_(reformatted)plate_set_id: " + dest_plate_set_id);
      
}
    
    public void insertUser(String _name, String _tags, String _password, int _group){

	    String sqlString = "SELECT new_user(?,?, ?, ?)";
    // LOGGER.info("insertSql: " + insertSql);
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
      preparedStatement.setString(1, _name);
      preparedStatement.setString(2, _tags);
      preparedStatement.setString(3, _password);
      preparedStatement.setInt(4, _group);
      preparedStatement.execute(); // executeUpdate expects no returns!!!

    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
	
    }

    public void insertPlateLayout(String _name, String _descr,  String _file_name){
	String name = _name;
	String descr = _descr;
	int format = 0;
	ArrayList<String[]> data = dmf.getUtilities().loadDataFile(_file_name);

	Object[][]  dataObject = dmf.getUtilities().getObjectArrayForArrayList(data); 

	switch(data.size()-1){
	case 96:
	    format = 96;
	    // ImportLayoutViewer ilv = new ImportLayoutViewer(dmf, dataObject);   
	    break;
	case 384:
	    format = 384;
	    
	    break;
	case 1536:
	    format = 1536;
	    
	    break;
	default:
	    JOptionPane.showMessageDialog( dmf, "Expecting 96, 384, or 1536 lines of data. Found " + (data.size()-1) +  "!", "Error", JOptionPane.ERROR_MESSAGE);	    
	}
	      
	    String sqlString = "SELECT new_plate_layout(?,?, ?, ?)";
    // LOGGER.info("insertSql: " + insertSql);
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, descr);
      preparedStatement.setInt(3, format);
      preparedStatement.setArray(4, conn.createArrayOf("VARCHAR", (data.toArray())));
      preparedStatement.execute(); // executeUpdate expects no returns!!!

    } catch (SQLException sqle) {
      LOGGER.warning("Failed to properly prepare  prepared statement: " + sqle);
    }
    }

    /**
     * @param sorted_response  [response] [well] [type_id] [sample_id]
     * the number of hits are "unknown" hits so must screen for type_id == 1
     * an object array must be passed to the stored procedure
     */
      public void insertHitList(String _name,
				String _description,
				int _num_hits,
				int  _assay_run_id,
				double[][] sorted_response) {
	  
      int new_hit_list_id;
      
      Object[] hit_list = new Object[_num_hits];
      int counter = 0;
      for(int i = 0; i < sorted_response.length; i++){
	  if(sorted_response[i][2]== 1 && counter < _num_hits){
	  hit_list[counter] = (Object)Math.round(sorted_response[i][3]);
	  counter++;
      }
	  //System.out.println("i: " + i + " " + hit_list[i]);
      }
      
      
    try {
      String insertSql = "SELECT new_hit_list ( ?, ?, ?, ?, ?);";
      PreparedStatement insertPs =
          conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      insertPs.setString(1, _name);
      insertPs.setString(2, _description);
      insertPs.setInt(3, _num_hits);
      insertPs.setInt(4, _assay_run_id);
      insertPs.setArray(5, conn.createArrayOf("INTEGER", hit_list));
   
      LOGGER.info(insertPs.toString());
      insertPs.executeUpdate();
      //ResultSet resultSet = insertPs.getResultSet();
      //resultSet.next();
      //new_plate_set_id = resultSet.getInt("new_plate_set");
     
    } catch (SQLException sqle) {
	LOGGER.warning("SQLE at inserting new plate set: " + sqle);
    }
    
  }

    public void importAccessionsByPlateSet(int _plate_set_id){
	int plate_set_id = _plate_set_id;
	String plate_set_sys_name = new String("PS-" + String.valueOf(plate_set_id));
	int plate_num = dmf.getDatabaseManager().getDatabaseRetriever().getNumberOfPlatesForPlateSetID(plate_set_id);
	int format_id = dmf.getDatabaseManager().getDatabaseRetriever().getFormatForPlateSetID(plate_set_id);
	   
	new DialogImportPlateSetAccessionIDs(dmf, plate_set_sys_name, plate_set_id, format_id, plate_num);
	
	
    }
}
