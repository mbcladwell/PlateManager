package pm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class LayoutViewer extends JDialog implements java.awt.event.ActionListener {
  static JButton button;
  static JLabel label;
  static JComboBox<Integer> formatList;
  static JComboBox<String> layoutList;
  static JButton okButton;
  static JButton cancelButton;
  final DialogMainFrame dmf;
  final Session session;
    private String owner;
  private JTable table;
  private JScrollPane scrollPane;
    private  JPanel pane2;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
  private MyModel tableModel;
    private String [] layoutNames;
    private Object[][] gridData;
    
    private DefaultComboBoxModel<String> layout_names_list_model;


    
  public LayoutViewer(DialogMainFrame _dmf) {
    this.dmf = _dmf;
    this.session = dmf.getSession();
    owner = session.getUserName();
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    // this.em = em;
    layoutNames = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayoutNames(96);
    layout_names_list_model = new DefaultComboBoxModel<String>( layoutNames );
    
    JPanel parentPane = new JPanel(new BorderLayout());
    parentPane.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel pane1 = new JPanel(new GridBagLayout());
    pane1.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    this.setTitle("Plate Layout Viewer");
    // c.gridwidth = 2;

    label = new JLabel("Format:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    pane1.add(label, c);
 
    Integer[] formats = {96, 384, 1536};

    formatList = new JComboBox<Integer>(formats);
    formatList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 0;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    formatList.addActionListener(this);
    pane1.add(formatList, c);
 

    label = new JLabel("Layout:");
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane1.add(label, c);

    layoutList = new JComboBox<String>();
//formatList.setSelectedIndex(0);
    c.gridx = 3;
    c.gridy = 0;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    layoutList.setModel(layout_names_list_model );
    layoutList.addActionListener(this);
   
    pane1.add(layoutList, c);

    
    pane2 = new JPanel(new BorderLayout());
    pane2.setBorder(BorderFactory.createRaisedBevelBorder());
    this.refreshTable(3);
   
    this.getContentPane().add(parentPane, BorderLayout.CENTER);
    parentPane.add(pane1, BorderLayout.NORTH);
    parentPane.add(pane2, BorderLayout.CENTER);
    
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

    private void addToDB() {}

    public void actionPerformed(ActionEvent e) {

    if (e.getSource() == formatList) {
       layoutNames = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayoutNames((int)formatList.getSelectedItem());
       layout_names_list_model = new DefaultComboBoxModel<String>( layoutNames );
       layoutList.setModel(layout_names_list_model );
       layoutList.setSelectedIndex(-1);
	
    }

    if (e.getSource() == layoutList) {
	if(layoutList.getSelectedIndex() > -1){
	    String selected = (String)layoutList.getSelectedItem();
	    int plate_layout_id  = dmf.getDatabaseManager().getDatabaseRetriever().getIDforLayoutName(selected);
	    this.refreshTable(plate_layout_id); 
	}
    }
  }
    public void refreshTable(int _plate_layout_id){
	pane2.removeAll();
	CustomTable  table2 = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayout(_plate_layout_id);
	gridData =  dmf.getUtilities().getPlateLayoutGrid(table2);
	tableModel = new MyModel(gridData);
	
	LOGGER.info("griddata length: " + gridData.length + "  " + gridData[0].length  );
	table = new JTable( tableModel);
	javax.swing.table.JTableHeader header = table.getTableHeader();
	header.setBackground(java.awt.Color.DARK_GRAY);
	header.setForeground(java.awt.Color.WHITE);
	table.setDefaultRenderer(String.class, new MyRenderer());
	scrollPane = new JScrollPane(table);
	JTable rowTable = new RowNumberTable(table);
	scrollPane.setRowHeaderView(rowTable);
	scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
			     rowTable.getTableHeader());
	pane2.add(scrollPane, BorderLayout.CENTER);
	table.setFillsViewportHeight(true);
	pane2.revalidate();
	pane2.repaint();
	
    
    }

    private static class MyRenderer extends DefaultTableCellRenderer {
     private static final long serialVersionUID = 1L;
	
     //Color backgroundColor = getBackground();

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            MyModel model = (MyModel) table.getModel();
	    //LOGGER.info("6,11: " + (String)model.getValueAt(row,column));
	    switch((String)model.getValueAt(row,column)){
	    case "unknown":
		c.setBackground(java.awt.Color.WHITE);
		break;
	    case "blank":
		c.setBackground(java.awt.Color.LIGHT_GRAY);
		break;
	    case "positive":
		c.setBackground(java.awt.Color.GREEN);
		break;
	    case "negative":
		c.setBackground(java.awt.Color.RED);
		break;
		
	    }
	    
            return c;
        }
    }

      private static class MyModel extends AbstractTableModel {

	  //private final List<Row> list = new ArrayList<Row>();
	  private static final long serialVersionUID = 1L;
	  private static final String[] COLUMN_NAMES = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
	  Object[][] data;
	  
        public MyModel(Object[][] _data) {
	    this.data = _data;
	     fireTableDataChanged();
            //list.add(new Row("One", true));
            //list.add(new Row("Two", false));
            //list.add(new Row("Three", false));
        }


	  public void setBackgroundColor(int row, int col){
	      
	  }
        public boolean getState(int row) {
            //return list.get(row).state.booleanValue();
	    return false;
        }

        public void setState(int row, boolean state) {
            //list.get(row).state = state;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return data[0].length;
        }

	  public String getColumnName(int column){
         return COLUMN_NAMES[column];
    }
	  
        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
	    //  list.get(row).name = (String) aValue;
            //fireTableCellUpdated(row, col);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        private static class Row {

            private String name;
            private Boolean state;

            public Row(String name, Boolean state) {
                this.name = name;
                this.state = state;
            }
        }
    }
}
