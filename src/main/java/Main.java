package pm;

import java.sql.SQLException;

/**
 */
public class Main {

  public static void main(String[] args) throws SQLException {

    // new DialogMainFrame();

    DialogMainFrame dmf = new DialogMainFrame();
    //ScatterPlot sp = new ScatterPlot(dmf, 1);

     AssayRunViewer lv = new AssayRunViewer(dmf);
    
    // DialogReformatPlateSet drps = new DialogReformatPlateSet(dmf, 16, "PS-16","description 1", 20,6000,"assay", "96");

    //    DatabaseInserter dbi = new DatabaseInserter(dmf.getDatabaseManager());
    // dbi.associateDataWithPlateSet(
    //  "assayName1", "descr1", "PS-10", "assay", "8 controls columns 47, 48", null);
  }
}
