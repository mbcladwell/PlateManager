package pm;

import java.awt.*;
import java.awt.event.*;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class DialogMainFrame extends JFrame {

  DatabaseManager dbm = new DatabaseManager();
  JPanel cards; // a panel that uses CardLayout
  public static final String PROJECTPANEL = "Card with projects";
  public static final String PLATESETPANEL = "Card with plate sets";
  public static final JFrame frame = new JFrame("Plate Manager");
  private static final long serialVersionUID = 1L;
  private JTable table;

  public DialogMainFrame() {

    // Create and set up the window.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //    Image img =
    //  new ImageIcon(DialogMainFrame.class.getResource("resources/mwplate.png")).getImage();
    // frame.setIconImage(img);

    /////////////////////////////////////////////
    // set up the project table
    JPanel card1 = new JPanel(new BorderLayout());
    card1.add(new MenuBarForProject(), BorderLayout.NORTH);

    String[] columnNames = {"Timestamp", "Project ID", "Owner", "Description"};

    Object[][] data = dbm.getTableData("project");

    table = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);

    card1.add(scrollPane, BorderLayout.CENTER);

    //////////////////////////////////////////////
    // this will show plate sets
    JPanel card2 = new JPanel(new BorderLayout());
    card2.add(new pm.MenuBarForPlateSet(), BorderLayout.NORTH);
    String[] columnNames2 = {"Timestamp", "Plate Set ID", "Type", "Description"};

    Object[][] data2 = dbm.getPlateSetTableData("PRJ3");

    JTable table2 = new JTable(data2, columnNames2);
    JScrollPane scrollPane2 = new JScrollPane(table2);
    table2.setFillsViewportHeight(true);

    card2.add(new pm.MenuBarForPlates(), BorderLayout.CENTER);
    String[] columnNames3 = {"Timestamp", "Plate Set ID", "Type", "Description"};

    //     Object[][] data3 = dbm.getPlateTableData("PLT3");

    // JTable table3 = new JTable(data3, columnNames3);
    // JScrollPane scrollPane3 = new JScrollPane(table3);
    // table3.setFillsViewportHeight(true);

    card2.add(scrollPane2, BorderLayout.CENTER);
    //  card2.add(scrollPane3, BorderLayout.CENTER);

    cards = new JPanel(new CardLayout());
    cards.add(card1, PROJECTPANEL);
    cards.add(card2, PLATESETPANEL);

    frame.getContentPane().add(cards, BorderLayout.CENTER);

    frame.pack();
    frame.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    frame.setVisible(true);
  }
}
