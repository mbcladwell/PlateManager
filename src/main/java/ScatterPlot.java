/*
 * ScatterPlot.java
 * http://forums.devshed.com/java-help-9/java-scatterplot-121767.html

 */
package pm;

    import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ScatterPlot extends JFrame {
  private List coords1 = new ArrayList();
  private List coords2 = new ArrayList();
 private JTextField labelsX = new JTextField("10", 6);
  private JTextField labelsY = new JTextField("10", 6);
  private JButton updateBtn = new JButton("Update");
    private DatabaseRetriever dbr;
    private DialogMainFrame dmf;
    private ArrayList table;
      private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public ScatterPlot(DialogMainFrame _dmf) {
    super("ScatterPlot");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.dmf = _dmf;
    table = dmf.getDatabaseManager().getDatabaseRetriever().getDataForScatterPlot(9);

    // Array look like
    //  plate, well, response, bkgrnd_sub,   norm,   norm_pos ,  well_type_id,  replicates, target 
    // -,1,0.293825507,0.293825507,0.434007883,0.511445642,1,1,a
    // class org.postgresql.jdbc.PgArray


    for (int i = 0; i < table.size(); i++) {
	LOGGER.info("row: " +  (Object)table.get(i));
	
	/*
	try{
	    table.get(i).getArray(2,2));
	LOGGER.info("row: " + row[3]);
	
	}catch(SQLException sqle){
	    LOGGER.info("sqle: " + sqle);
	    
	}
    	*/
	//LOGGER.info("class: " + table.get(i).getClass());
	//	LOGGER.info("1: " + row[1]);
	//LOGGER.info("2: " + row[2]);
	
	// coords1.add(new Point2D.Float(j, k));
	//coords2.add(new Point2D.Float(l, m));
	//Collections.shuffle(coords1);
	//Collections.shuffle(coords2);
    }

    // Above will be replaced with the imported coordinates.

    JPanel panel = new JPanel() {
	    private float scaleX;
	    private float scaleY;
	    private float originX;
	    private float originY;

	    public String getToolTipText(MouseEvent event) {
          int xPos = event.getX();   // the x, y coords pointed to
          int yPos = event.getY();

          return "("+(int)((xPos-originX)/scaleX)+","
		+(int)((yPos-originY)/-scaleY)+")"; // return tooltip text
      }
	    
      public void paintComponent(Graphics g) {
        // Plot points below.
        int wth = getWidth();
        int hgt = getHeight();
        for (Iterator i=coords1.iterator(); i.hasNext(); ) {
          Point2D.Float pt = (Point2D.Float)i.next();
          g.setColor(Color.blue);
          g.fillOval((int)(pt.x + wth/2) - 2, (int)(-pt.y + hgt/2) - 2, 4, 4);
        }
        for (Iterator i = coords2.iterator(); i.hasNext(); ) {
          Point2D.Float pt = (Point2D.Float)i.next();
          g.setColor(Color.red);
          //g.fillOval((int)pt.x + wth/2 - 2, hgt/2 - (int)pt.y - 2, 4, 4);
	  //g.drawString("+", (int)pt.x + wth/2, hgt/2 - (int)pt.y);
	  g.drawString("+", 100, 100);
	  g.drawString("+", 200, 200);
	  g.drawString("+", 300, 300);
	  g.drawString("+", 400, 400);
	  
	  
        }
        // Plot points above.

        // Draw the axes below.
        int width = getWidth();
        int height = getHeight();
        setVisible(true);
        g.setColor(Color.black);
        g.drawLine(40, 0,  40, height-40); // y-axis
        g.drawLine(40, height-40, width-40, height-40); // x-axis
        // Draw the axes above.
        int numLabelsX = 10;
	int numLabelsY = 10;

        try {
            numLabelsX = Integer.parseInt(labelsX.getText());
        } catch(NumberFormatException nfe) { 
            // invalid number entered - will remain as 10
        } 
        try {
            numLabelsY = Integer.parseInt(labelsY.getText());
        } catch(NumberFormatException nfe) { 
            // invalid number entered - will remain as 10
        } 
        // can't have negative numbers of labels
        if(numLabelsX <= 0)
            numLabelsX = 1;
        if(numLabelsY <= 0)
            numLabelsY = 1;
        // Label the axes below.
        g.setColor(Color.black);
        g.drawString("--20", width/2, height/2 +20);
        g.drawString("-20", width/2, height/2 - 20);
        g.drawString("-40", width/2, height/2 - 40);
        // Label the axes above.

        // Test geom below.
        //g.drawOval(width/2 - 3, height/2 - 3, 6, 6);
        // Test geom above.
      }
    };
    panel.setToolTipText("");
     getContentPane().add(panel, BorderLayout.CENTER);
     // setContentPane(panel);

    JPanel lblEditPanel = new JPanel();
    lblEditPanel.add(new JLabel("Y-axis labels:"));
    lblEditPanel.add(labelsY);
    lblEditPanel.add(new JLabel("X-axis labels:"));
    lblEditPanel.add(labelsX);
    updateBtn.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
            repaint();
        }
    });
    lblEditPanel.add(updateBtn);
    getContentPane().add(lblEditPanel, BorderLayout.SOUTH);
    
    int width = getWidth();
    int height = getHeight();
    //setBounds(20, 20, width, height);
   

    setVisible(true);
   
 
  }

 
}
