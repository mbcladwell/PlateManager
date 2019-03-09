package pm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

public class Utilities {

  private DialogMainFrame dmf;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private File file;
  private static final String newline = "\n";
  private JFileChooser fc;

  public Utilities(DialogMainFrame _dmf) {
    dmf = _dmf;
  }
  /** Returns selected rows only */
  public Object[][] getTableDataWithHeader(CustomTable _table) {
    CustomTable table = _table;

    DefaultTableModel tm = table.getTableModel();

    int nRow = tm.getRowCount() + 1, nCol = tm.getColumnCount();
    Object[][] tableData = new Object[nRow][nCol];

    for (int i = 0; i < nCol; i++) {
      tableData[0][i] = table.getColumnName(i);
    }

    for (int i = 1; i < nRow; i++)
      for (int j = 0; j < nCol; j++) tableData[i][j] = tm.getValueAt(i - 1, j);
    return tableData;
  }

  /**
   * Take a columnar data file and load it into an ArrayList of String[]. Header is included. This
   * is called by OK button of DialogAddPlateSetData.
   */
  public ArrayList<String[]> loadDataFile(String _fileName) {
    String fileName = _fileName;
    Path path = Paths.get(fileName);
    ArrayList<String[]> table = new ArrayList<String[]>();

    try (Stream<String> lines = Files.lines(path)) {
      for (String line : (Iterable<String>) lines::iterator) {
        if (line != null && !line.isEmpty()) {
          String[] values = line.split("\t");

          // LOGGER.info("values: " + values);
          table.add(values);
        }
      }
    } catch (IOException ioe) {
      LOGGER.severe("IOException: " + ioe);
    }

    System.out.println(table.size());
    System.out.println(table.get(1).length);
    System.out.println(table.get(1)[1]);

    return table;
  }

  public int[] getIntArrayForIntegerSet(Set<Integer> input) {
    int count = input.size();
    Integer[] inputArray = input.toArray(new Integer[count]);
    int result[] = new int[count];

    for (int i = 0; i < count; i++) {
      result[i] = inputArray[i];
    }
    return result;
  }

  public String[] getStringArrayForStringSet(Set<String> input) {
    LOGGER.info("input: " + input);
    int count = input.size();
    String[] inputArray = input.toArray(new String[count]);
    String result[] = new String[count];

    for (int i = 0; i < count; i++) {
      result[i] = inputArray[i];
      LOGGER.info("result: " + result[i]);
    }
    return result;
  }

    public Object[][] getPlateLayoutGrid(CustomTable _custom_table){
	CustomTable custom_table = _custom_table;
    DefaultTableModel tm = custom_table.getTableModel();
    int row = 0;
    int col = 0;
    int nRow = tm.getRowCount();
    LOGGER.info("nRow: " + nRow);
    //96 well plate  tableData[row][col]
    switch(nRow){
    case 96:
    row = 8; col=12;
    break;
case 384:
    row = 16; col=24;
    break;
case 1536:
    row = 32; col=48;
    break;    
    }
        Object[][] tableData = new Object[row][col];


    for (int i = 0; i < nRow; i++) {
	//LOGGER.info("tdata i: " + i + " " + tm.getValueAt(i,1) );
	//System.out.println("i: " + i + " array[" + ((i)%8) +"][" + (int)Math.floor((i)/8)+ "] = " + tm.getValueAt(i,1) );
       
       	tableData[((i)%row)][(int)Math.floor((i)/row)] = tm.getValueAt(i,1);
    }

    return tableData;	
    }

    /**
     * Converts ArrayList of String[] to Object[][].
     */
    public Object[][] getObjectArrayForArrayList(ArrayList<String[]> _array_list){
	ArrayList<String[]> array_list = _array_list;
	int nrow = array_list.size();
	int ncol = array_list.get(1).length;
	Object[][] results = new Object[nrow][ncol];
	
	int counter = 0;
	for (String[] row : array_list) {
	    for(int i=0; i < ncol; i++){
		results[counter][i] = row[i];
	    LOGGER.info("results  counter(row): " + counter + " column " + i + " element " + results[counter][i] + "\n");
	
	}
    counter++;
	}

	return results;
    }

    /**
     * Convert a columnar table with well identifiers to a column/row table object.  The Object array is filled by columns.  Header will be stripped.  Column_name is the column to be converted to a plate layout.
     * @param int _format 96, 384, or 1536
@param String column_name column to be processed
     */
    public Object[][] convertTableToPlate(Object[][] _input,  String _column_name){
       
   
	String column_name = _column_name;
	int column_of_interest = 0;
	Object[][] input = _input;
	
	int row = 0;
	int col = 0;
	int nRow = input.length-1;
	    
    LOGGER.info("nRow: " + nRow);
    //96 well plate  tableData[row][col]
    switch(nRow){
    case 96:
    row = 8; col=12;
    break;
case 384:
    row = 16; col=24;
    break;
case 1536:
    row = 32; col=48;
    break;    
    }
        Object[][] output = new Object[row][col];

	//which column to process?
	for(int i=0; i < input[0].length; i++){
	    if(input[0][i].equals(column_name)){
		column_of_interest = i;
		LOGGER.info("column of interest: " + i);
		break;
	    }
	}

    for (int i = 1; i < nRow; i++) {
	//LOGGER.info("tdata i: " + i + " " + tm.getValueAt(i,1) );
	System.out.println("i: " + i + " array[" + ((i)%8) +"][" + (int)Math.floor((i)/8)+ "] = " + input[i][column_of_interest]);
       	//tableData[((i)%row)][(int)Math.floor((i)/row)] = tm.getValueAt(i,1);
	
	output[((i)%row)][(int)Math.floor((i)/row)] = input[i][column_of_interest];
	//LOGGER.info("Object row: " + i + " " + tm.getValueAt(i,1) );
    }

    return output;	
       
	
    }

}
    /*
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.next();
        LOGGER.info("data: " + data);

        String[] values = data.split("\t");
        // LOGGER.info("values: " + values);

        LOGGER.info("index 0: " + values[0]);
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    */
