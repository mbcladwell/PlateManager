package pm;

import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.JFileChooser.*;
import javax.swing.table.*;

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
        String[] values = line.split("\t");
        // LOGGER.info("values: " + values);
        table.add(values);
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
