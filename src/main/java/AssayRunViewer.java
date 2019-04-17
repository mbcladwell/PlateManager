package pm;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;


public class AssayRunViewer extends JDialog implements java.awt.event.ActionListener {
  static JButton exportAssayRun;
  static JButton viewAssayRun;
  static JButton exportHitList;
  static JButton viewHitList;
  static JButton refreshHitListsButton;
    
  static JLabel label;
  static JComboBox<ComboItem> projectList;
  final DialogMainFrame dmf;
    final Session session;
    private int project_id;
    private String owner;
  private JTable assay_runs_table;
  private JTable hit_lists_table;
  private JScrollPane assay_runs_scroll_pane;
    private JScrollPane hit_lists_scroll_pane;
    private  JPanel parent_pane;
    private  JPanel assay_runs_pane;
    private  JPanel hit_lists_pane;
    private JPanel arButtons;
    private JPanel hlButtons;
    
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
    
    
  public AssayRunViewer(DialogMainFrame _dmf) {
    this.setTitle("Assay Run || Hit List Viewer");
    this.dmf = _dmf;
    this.session = dmf.getSession();
    project_id = session.getProjectID();
    owner = session.getUserName();

    parent_pane = new JPanel(new BorderLayout());

    assay_runs_pane = new JPanel(new BorderLayout());
    assay_runs_pane.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder assay_runs_pane_border = BorderFactory.createTitledBorder("Assay Runs:");
    assay_runs_pane_border.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    assay_runs_pane.setBorder(assay_runs_pane_border);

    assay_runs_table = dmf.getDatabaseManager().getDatabaseRetriever().getAssayRuns(session.getProjectID());
  assay_runs_table.getSelectionModel().addListSelectionListener(						     
	  new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
		  ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		  if(!e.getValueIsAdjusting() & assay_runs_table.getSelectedRow()>=0){
		      //LOGGER.info("source: " + e);
			int row = assay_runs_table.getSelectedRow();
			int assay_run_id = Integer.parseInt(( (String)assay_runs_table.getModel().getValueAt(row,0)).substring(3));

			//LOGGER.info("source_layout_id: " + source_layout_id);
			hit_lists_table.setModel(dmf.getDatabaseManager()
					   .getDatabaseRetriever()
						.getHitListsForAssayRun(assay_run_id).getModel());
			//
			//refreshLayoutTable(source_layout_id);

		  }
	      }
	  });



    
    assay_runs_scroll_pane = new JScrollPane(assay_runs_table);
    assay_runs_table.setFillsViewportHeight(true);
    assay_runs_pane.add(assay_runs_scroll_pane, BorderLayout.CENTER);

    GridLayout buttonLayout = new GridLayout(1,4,5,5);
    projectList = new JComboBox(dmf.getDatabaseManager().getDatabaseRetriever().getAllProjects());
    for(int i=0; i < projectList.getItemCount(); i++){
	if(((ComboItem)projectList.getItemAt(i)).getKey() == project_id){
		projectList.setSelectedIndex(i);
	    }
    }
    
    //projectList.setSelectedIndex(9);
    projectList.addActionListener(this);
    exportAssayRun = new JButton("Export");
    exportAssayRun.addActionListener(this);
    viewAssayRun = new JButton("Plot");
    viewAssayRun.addActionListener(this);
    arButtons = new JPanel(buttonLayout);
    arButtons.add(projectList);
    arButtons.add(exportAssayRun);
    arButtons.add(viewAssayRun);
    assay_runs_pane.add(arButtons, BorderLayout.SOUTH);
    
    
    
    
    

    hit_lists_pane  = new JPanel(new BorderLayout());
    hit_lists_pane.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder hit_lists_pane_border = BorderFactory.createTitledBorder("Hit Lists:");
    hit_lists_pane_border.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    hit_lists_pane.setBorder(hit_lists_pane_border);

    hit_lists_table = dmf.getDatabaseManager().getDatabaseRetriever().getHitLists(session.getProjectID());

    hit_lists_scroll_pane = new JScrollPane(hit_lists_table);
    hit_lists_table.setFillsViewportHeight(true);
    hit_lists_pane.add(hit_lists_scroll_pane, BorderLayout.CENTER);

    hlButtons = new JPanel(buttonLayout);
    refreshHitListsButton = new JButton("Refresh List");
    refreshHitListsButton.addActionListener(this);
   
    exportHitList = new JButton("Export");
    exportHitList.addActionListener(this);
   viewHitList = new JButton("View");
     viewHitList.addActionListener(this);
   hlButtons.add(refreshHitListsButton);
    
    hlButtons.add(exportHitList);
    hlButtons.add(viewHitList);
    hit_lists_pane.add(hlButtons, BorderLayout.SOUTH);


    this.getContentPane().add(parent_pane, BorderLayout.CENTER);
    parent_pane.add(assay_runs_pane, BorderLayout.WEST);
    parent_pane.add(hit_lists_pane, BorderLayout.EAST);
    
    GridBagConstraints c = new GridBagConstraints();
   
    
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

   
    public void actionPerformed(ActionEvent e) {
	
    if (e.getSource() == exportAssayRun) {
	Object[][] results = dmf.getUtilities().getSelectedRowsAndHeaderAsStringArray(assay_runs_table);
	if(results.length>1){
	//   LOGGER.info("hit list table: " + results);
	       POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Assay Runs", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }	 
	
	}else{
	    JOptionPane.showMessageDialog(dmf, "Select one or more  Assay Runs!");	
	}
    	
    }

        if (e.getSource() == viewAssayRun) {
    	    if(!assay_runs_table.getSelectionModel().isSelectionEmpty()){
		 
		 TableModel arModel = assay_runs_table.getModel();
		 int row = assay_runs_table.getSelectedRow();
		 String assay_runs_sys_name =  assay_runs_table.getModel().getValueAt(row, 0).toString();
		 int  assay_runs_id = Integer.parseInt(assay_runs_sys_name.substring(3));
		 new ScatterPlot(dmf, assay_runs_id);}
	    else{
	      JOptionPane.showMessageDialog(dmf, "Select an Assay Run!");	      
	    }
	
    }
    if (e.getSource() == exportHitList) {
	
	Object[][] results = dmf.getUtilities().getSelectedRowsAndHeaderAsStringArray(hit_lists_table);
	if(results.length>1){
	//   LOGGER.info("hit list table: " + results);
	       POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Hit Lists", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }	 
	
	}else{
	    JOptionPane.showMessageDialog(dmf, "Select one or more  Hit Lists!");	
	}
    	
    }
    if (e.getSource() == viewHitList) {
  if(!hit_lists_table.getSelectionModel().isSelectionEmpty()){
	 TableModel arModel = hit_lists_table.getModel();
		 int row = hit_lists_table.getSelectedRow();
		 String hit_list_sys_name =  hit_lists_table.getModel().getValueAt(row, 0).toString();
		 int  hit_list_id = Integer.parseInt(hit_list_sys_name.substring(3));
		 LOGGER.info("hit_list_id: " + hit_list_id);
		 new HitListViewer( dmf, hit_list_id);}
  else{
	      JOptionPane.showMessageDialog(dmf, "Select a Hit List!");	      
	    }

	
    }
        if (e.getSource() == refreshHitListsButton) {
	    refreshHitListsTable();
    }


    if (e.getSource() == projectList) {
	if(projectList.getSelectedIndex() > -1){
	    project_id  = ((ComboItem)projectList.getSelectedItem()).getKey();
	    this.refreshTables(); 
	}
    }  
  }

    public void refreshTables(){
       
	CustomTable arTable = dmf.getDatabaseManager().getDatabaseRetriever().getAssayRuns(project_id);
	TableModel arModel = arTable.getModel();
	assay_runs_table.setModel(arModel);	

		//LOGGER.info("project: " + project_id);
	CustomTable hlTable = dmf.getDatabaseManager().getDatabaseRetriever().getHitLists(project_id);
	TableModel hlModel = hlTable.getModel();
	hit_lists_table.setModel(hlModel);
	
    }

    public void refreshHitListsTable(){
	int selected_project_id = ((ComboItem)projectList.getSelectedItem()).getKey();
	CustomTable hlTable = dmf.getDatabaseManager().getDatabaseRetriever().getHitLists(selected_project_id);
	TableModel hlModel = hlTable.getModel();
	hit_lists_table.setModel(hlModel);


    }
	

 
}
