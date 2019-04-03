package pm;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import com.google.common.math.Stats;

public class ResponseWrangler {

   private double max_response;
    private double min_response;
    private double mean_bkgrnd;
    private double stdev_bkgrnd;
    private double threshold;
    private double mean_3_sd;
    private double mean_2_sd;
    private int format;
    private int num_plates;
    private int num_hits=0;
    private CustomTable table;
    private double[][] sorted_response;
  private DecimalFormat df = new DecimalFormat("#####.####");
        private Set<Integer> plate_set = new HashSet<Integer>();
    private Set<Integer> well_set = new HashSet<Integer>();
    private List<Double> desired_response_list = new LinkedList<Double>();
    private List<Double> blank_list = new LinkedList<Double>();
    private List<Double> neg_list = new LinkedList<Double>();
    private int num_data_points;

    public static final int RAW = 0;
    public static final int NORM = 1;
    public static final int NORM_POS = 2;
      private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    
    /**
     * 
     *	 Array looks like
     * plate, well, response, bkgrnd_sub,   norm,   norm_pos ,  well_type_id,  replicates, target 
     * 1,1,0.293825507,0.293825507,0.434007883,0.511445642,1,1,a
     * class org.postgresql.jdbc.PgArray
     * set up some variable with data limits
     *
     * double[][]  sorted_response [response] [well] [type_id] [sample_id]
     */
    public ResponseWrangler(CustomTable _table, int _desired_response){
	table = _table;
	DefaultTableModel dtm = (DefaultTableModel)table.getModel();
	
	sorted_response = new double[table.getRowCount()][4];
	for(int row = 0;row < table.getRowCount();row++) {
	    
	    int plate = (int)dtm.getValueAt(row, 0);
	    
	    plate_set.add((int)dtm.getValueAt(row, 0));
	    int well = (int)dtm.getValueAt(row, 1);
	    well_set.add((int)dtm.getValueAt(row, 1));
	    sorted_response[row][1] = well;
	    
	    double response = Double.valueOf((float)dtm.getValueAt(row, 2));	
	    Double bkgrnd = Double.valueOf((float)dtm.getValueAt(row, 3));
	    Double norm = Double.valueOf((float)dtm.getValueAt(row, 4));
	    Double normpos = Double.valueOf((float)dtm.getValueAt(row, 5));
	    int well_type_id = (int)dtm.getValueAt(row, 6);
	    sorted_response[row][2] = (int)dtm.getValueAt(row, 6);
	    String replicates = (String)dtm.getValueAt(row, 7);
	    String target = (String)dtm.getValueAt(row, 8);
	    int sample_id = 0;
	    if(dtm.getValueAt(row, 9) != null){
		sample_id = (int)dtm.getValueAt(row, 9);
	    }
		sorted_response[row][3] = sample_id;
	  
	switch(_desired_response){
	case 0: //raw
	    desired_response_list.add(response);
	    sorted_response[row][0] = response;
	    if(well_type_id==4){  //if it is a blank
		blank_list.add(Double.valueOf((float)dtm.getValueAt(row, 2)));
	    }
	    if(well_type_id==3){  //if it is a negative control
		neg_list.add(Double.valueOf((float)dtm.getValueAt(row, 2)));
	    }
	    break;
	case 1: //norm
	    desired_response_list.add(norm);
	sorted_response[row][0] = norm;
	    if(well_type_id==4){  //if it is a blank
		blank_list.add(Double.valueOf((float)dtm.getValueAt(row, 4)));
	    }
	    if(well_type_id==3){  //if it is a negative control
		neg_list.add(Double.valueOf((float)dtm.getValueAt(row, 4)));
	    }
	    break;
	case 2: //normpos
	    desired_response_list.add(normpos);
	sorted_response[row][0] = normpos;
	    if(well_type_id==4){  //if it is a blank
		blank_list.add(Double.valueOf((float)dtm.getValueAt(row, 5)));
	    }
	    if(well_type_id==3){  //if it is a negative control
		neg_list.add(Double.valueOf((float)dtm.getValueAt(row, 5)));
	    }
	    break;
	}

       
	
    	}

    format = well_set.size();
    num_plates = plate_set.size();
    max_response = Double.valueOf(Collections.max(desired_response_list));
    min_response = Double.valueOf(Collections.min(desired_response_list));
    num_data_points = desired_response_list.size();
    
    mean_bkgrnd = Stats.meanOf(blank_list);
    stdev_bkgrnd = Stats.of(blank_list).populationStandardDeviation();
    mean_3_sd = mean_bkgrnd + 3*(stdev_bkgrnd);
    mean_2_sd = mean_bkgrnd + 2*(stdev_bkgrnd);
    threshold = mean_3_sd;

    Arrays.sort(sorted_response, new Comparator<double[]>() {
        @Override
        public int compare(double[] o1, double[] o2) {
            return Double.compare(o2[0], o1[0]);
        }
    });
    /*
    if(_desired_response == 0){
	System.out.println("max: " + max_response);
	System.out.println("min: " + min_response);
	System.out.println("bkrnd: " + mean_bkgrnd);
	System.out.println("stdev: " + stdev_bkgrnd);
	System.out.println("mean_3_sd: " + mean_3_sd);
	System.out.println("mean_2_sd: " + mean_2_sd);
	System.out.println("threshold: " + threshold);
	System.out.println("" );

    for(int i=0; i < sorted_response.length; i++){
	System.out.println("[" + i + "][0] " + sorted_response[i][0] );
	System.out.println("[" + i + "][1] " + sorted_response[i][1]);
	System.out.println("[" + i + "][2] " + sorted_response[i][2]);
	System.out.println("[" + i + "][3] " + sorted_response[i][3]);
	
    }
    */
	
    
    }

    /**
     * Sorted in descending order
     * double[][]  sorted_response [response] [well] [type_id] [sample_id]
     */
    public int getHitsAboveThreshold(double _threshold){
	double threshold = _threshold;
	int results = 0;
	for(int i = 0; i < sorted_response.length; i++){
	    if(sorted_response[i][0] > threshold){
		if(sorted_response[i][2] == 1){ //an "unknown" (i.e. not a control)
		    results++;   
		}
	    }
	}
	return results;
    }
    
    /**
	 * @return the max_response
	 */
	public double getMax_response() {
		return max_response;
	}


	/**
	 * @param max_response the max_response to set
	 */
	public void setMax_response(double max_response) {
		this.max_response = max_response;
	}


	/**
	 * @return the min_response
	 */
	public double getMin_response() {
		return min_response;
	}


	/**
	 * @param min_response the min_response to set
	 */
	public void setMin_response(double min_response) {
		this.min_response = min_response;
	}


	/**
	 * @return the mean_bkgrnd
	 */
	public double getMean_bkgrnd() {
		return mean_bkgrnd;
	}


	/**
	 * @param mean_bkgrnd the mean_bkgrnd to set
	 */
	public void setMean_bkgrnd(double mean_bkgrnd) {
		this.mean_bkgrnd = mean_bkgrnd;
	}


	/**
	 * @return the stdev_bkgrnd
	 */
	public double getStdev_bkgrnd() {
		return stdev_bkgrnd;
	}


	/**
	 * @param stdev_bkgrnd the stdev_bkgrnd to set
	 */
	public void setStdev_bkgrnd(double stdev_bkgrnd) {
		this.stdev_bkgrnd = stdev_bkgrnd;
	}


	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}


	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}


	/**
	 * @return the mean_3_sd
	 */
	public double getMean_3_sd() {
		return mean_3_sd;
	}


	/**
	 * @param mean_3_sd the mean_3_sd to set
	 */
	public void setMean_3_sd(double mean_3_sd) {
		this.mean_3_sd = mean_3_sd;
	}


	/**
	 * @return the mean_2_sd
	 */
	public double getMean_2_sd() {
		return mean_2_sd;
	}


	/**
	 * @param mean_2_sd the mean_2_sd to set
	 */
	public void setMean_2_sd(double mean_2_sd) {
		this.mean_2_sd = mean_2_sd;
	}


	/**
	 * @return the num_hits
	 */
	public int getNum_hits() {
		return num_hits;
	}

	/**
	 * @return the num_plates
	 */
	public int getNum_plates() {
		return num_plates;
	}

	/**
	 * @return the format (number of wells per plate)
	 */
	public int getFormat() {
		return format;
	}

    
	/**
	 * @param num_hits the num_hits to set
	 */
	public void setNum_hits(int num_hits) {
		this.num_hits = num_hits;
	}


	/**
	 * @return the table
	 */
	public CustomTable getTable() {
		return table;
	}


	/**
	 * @param table the table to set
	 */
	public void setTable(CustomTable table) {
		this.table = table;
	}


	/**
	 * @return the sorted_response
	 */
	public double[][] getSortedResponse() {
		return sorted_response;
	}


	/**
	 * @param sorted_response the sorted_response to set
	 */
	public void setSortedResponse(double[][] sorted_response) {
		this.sorted_response = sorted_response;
	}


	/**
	 * @return the df
	 */
	public DecimalFormat getDf() {
		return df;
	}


	/**
	 * @param df the df to set
	 */
	public void setDf(DecimalFormat df) {
		this.df = df;
	}


	/**
	 * @return the plate_set
	 */
	public Set<Integer> getPlate_set() {
		return plate_set;
	}


	/**
	 * @param plate_set the plate_set to set
	 */
	public void setPlate_set(Set<Integer> plate_set) {
		this.plate_set = plate_set;
	}


	/**
	 * @return the well_set
	 */
	public Set<Integer> getWell_set() {
		return well_set;
	}




	/**
	 * @return the bkgrnd_list
	 */
	public List<Double> getBlank_list() {
		return blank_list;
	}


	/**
	 * @return the max_response
	 */

	/**
	 * @return the desired_response_list
	 */
	public List<Double> getDesired_response_list() {
		return desired_response_list;
	}


	/**
	 * @param desired_response_list the desired_response_list to set
	 */
	public void setDesired_response_list(List<Double> desired_response_list) {
		this.desired_response_list = desired_response_list;
	}



	/**
	 * @return the rAW
	 */
	public int getRAW() {
		return RAW;
	}


	/**
	 * @return the nORM
	 */
	public int getNORM() {
		return NORM;
	}


	/**
	 * @return the nORM_POS
	 */
	public int getNORM_POS() {
		return NORM_POS;
	}
}



