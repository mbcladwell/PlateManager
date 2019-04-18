package pm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;


public class HitListViewer extends JDialog implements java.awt.event.ActionListener {
  static JButton select_all_button;
  static JButton export_hits_button;
  static JButton exportHitList;
  static JButton rearrayHitList;
  static JButton closeButton;
    
  static JLabel label;
    //  static JComboBox<ComboItem> projectList;
    private  JComboBox<ComboItem> all_hit_lists_in_project;
  final DialogMainFrame dmf;
    final Session session;
    private int project_id;
    private int hit_list_id;
    
    private String owner;
  private JTable hits_table;
  private JTable counts_table;
  private JScrollPane hits_scroll_pane;
    private JScrollPane counts_scroll_pane;
    private  JPanel parent_pane;
    private  JPanel hits_pane;
    private  JPanel counts_pane;
    private JPanel arButtons;
    private JPanel hlButtons;
    private int current_project_id;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
    
    
    public HitListViewer(DialogMainFrame _dmf, int _hit_list_id) {
    this.dmf = _dmf;
    this.session = dmf.getSession();
      this.setTitle("Hit List Viewer - " + session.getProjectSysName());
    project_id = session.getProjectID();
    owner = session.getUserName();
    hit_list_id = _hit_list_id;
    parent_pane = new JPanel(new BorderLayout());

        hits_table = dmf.getDatabaseManager().getDatabaseRetriever().getSamplesForHitList(hit_list_id);

    hits_pane = new JPanel(new BorderLayout());
    hits_pane.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder hits_pane_border = BorderFactory.createTitledBorder("Hits (Total count = "+ hits_table.getRowCount()  +  "):");
    hits_pane_border.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    hits_pane.setBorder(hits_pane_border);


    hits_scroll_pane = new JScrollPane(hits_table);
    hits_table.setFillsViewportHeight(true);
    hits_pane.add(hits_scroll_pane, BorderLayout.CENTER);

    
    GridLayout buttonLayout = new GridLayout(1,4,5,5);

      JPanel     hits_buttons = new JPanel(buttonLayout);
      
      //get all the hit lists in the current project       
      current_project_id = session.getProjectID();
      all_hit_lists_in_project = new JComboBox(dmf.getDatabaseManager().getDatabaseRetriever().getHitListsForProject(current_project_id));
    for(int i=0; i < all_hit_lists_in_project.getItemCount(); i++){
	if(((ComboItem)all_hit_lists_in_project.getItemAt(i)).getKey() == current_project_id){
		all_hit_lists_in_project.setSelectedIndex(i);
	    }
    }
    all_hit_lists_in_project.addActionListener(this);
   
   select_all_button = new JButton("Select All");
    select_all_button.addActionListener(this);
   
    export_hits_button = new JButton("Export Hits");
    export_hits_button.addActionListener(this);
    hits_buttons.add(all_hit_lists_in_project);
    
   hits_buttons.add(select_all_button);
   hits_buttons.add(export_hits_button);
   hits_pane.add(hits_buttons, BorderLayout.SOUTH);
 
    /*
    projectList = new JComboBox(dmf.getDatabaseManager().getDatabaseRetriever().getAllProjects());
    projectList.setSelectedIndex(9);
    projectList.addActionListener(this);
    exportAssayRun = new JButton("Export");
    exportAssayRun.addActionListener(this);
    viewAssayRun = new JButton("View");
    viewAssayRun.addActionListener(this);
    arButtons = new JPanel(buttonLayout);
    arButtons.add(projectList);
    arButtons.add(exportAssayRun);
    arButtons.add(viewAssayRun);
    pane1.add(arButtons, BorderLayout.SOUTH);
    */
    

    counts_pane  = new JPanel(new BorderLayout());
    counts_pane.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder counts_paneBorder = BorderFactory.createTitledBorder("Hits available in Plate Sets:");
    counts_paneBorder.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    counts_pane.setBorder(counts_paneBorder);

    counts_table = dmf.getDatabaseManager().getDatabaseRetriever().getHitCountPerPlateSet(session.getProjectID(), hit_list_id);

    counts_scroll_pane = new JScrollPane(counts_table);
    counts_table.setFillsViewportHeight(true);
    counts_pane.add(counts_scroll_pane, BorderLayout.CENTER);
   
        hlButtons = new JPanel(buttonLayout);
    closeButton = new JButton("Close");
    closeButton.addActionListener(this);
   
    exportHitList = new JButton("Export");
    exportHitList.addActionListener(this);
   rearrayHitList = new JButton("Rearray");
     rearrayHitList.addActionListener(this);
   hlButtons.add(closeButton);
    
    hlButtons.add(exportHitList);
    hlButtons.add(rearrayHitList);
    counts_pane.add(hlButtons, BorderLayout.SOUTH);


    this.getContentPane().add(parent_pane, BorderLayout.CENTER);
    parent_pane.add(hits_pane, BorderLayout.WEST);
    parent_pane.add(counts_pane, BorderLayout.EAST);
    
    GridBagConstraints c = new GridBagConstraints();
   
    
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

    
   
    public void actionPerformed(ActionEvent e) {

    if (e.getSource() == all_hit_lists_in_project) {
	int selected_hit_list_id = ((ComboItem)all_hit_lists_in_project.getSelectedItem()).getKey();
	JTable new_hits_table = dmf.getDatabaseManager().getDatabaseRetriever().getSamplesForHitList(selected_hit_list_id);
	TableModel new_model = new_hits_table.getModel();
	hits_table.setModel(new_model); 

	JTable new_counts_table = dmf.getDatabaseManager().getDatabaseRetriever().getHitCountPerPlateSet(session.getProjectID(), selected_hit_list_id);
	TableModel new_model2 = new_counts_table.getModel();
	counts_table.setModel(new_model2);

    }

	if (e.getSource() == select_all_button) {
    	hits_table.selectAll();
    }

	
    if (e.getSource() == export_hits_button) {
	Object[][] results = dmf.getUtilities().getSelectedRowsAndHeaderAsStringArray(hits_table);
	if(results.length>1){
	   LOGGER.info("hit list table: " + results);
	       POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Hits", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }	 
	
	}else{
	    JOptionPane.showMessageDialog(dmf, "Select one or more or all Hits!");	
	}
    	
    }
    
	        
	
    


        if (e.getSource() == closeButton) {
	    HitListViewer.this.dispose();
    }

        if (e.getSource() == exportHitList) {
	Object[][] results = dmf.getUtilities().getSelectedRowsAndHeaderAsStringArray(counts_table);
	if(results.length>1){
	//   LOGGER.info("hit list table: " + results);
	       POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Counts", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }	 
	
	}else{
	    JOptionPane.showMessageDialog(dmf, "Select one or more Plate Sets!");	
	}
    	
    }

	
    if (e.getSource() == rearrayHitList) {
    		 TableModel hits_count_model = counts_table.getModel();
		 int row = counts_table.getSelectedRow();
		 int plate_set_id =  (int)counts_table.getModel().getValueAt(row, 0);
		 String plate_set_sys_name =  counts_table.getModel().getValueAt(row, 1).toString();
		 LOGGER.info("counts_table.getModel().getValueAt(row, 4): " + counts_table.getModel().getValueAt(row, 4));
		 int sample_count = (int)Integer.valueOf(counts_table.getModel().getValueAt(row, 4).toString());
		 //int sample_count = 92;
		 int source_plate_set_format =  (int)counts_table.getModel().getValueAt(row, 3);
		 if(source_plate_set_format==1536){
		     JOptionPane.showMessageDialog(dmf, "1536 well plates can't be rarrayed!");
		     return;
		 }

    		 TableModel hit_list_model = hits_table.getModel();		 
		 int hit_list_id =  Integer.valueOf(hits_table.getModel().getValueAt(0, 0).toString());
		 String hit_list_sys_name =  new String("HL-" + hit_list_id);
		 new DialogRearrayHitList(dmf, plate_set_id, plate_set_sys_name, source_plate_set_format, hit_list_id, hit_list_sys_name, sample_count);
	
    }


    }			 
  }

