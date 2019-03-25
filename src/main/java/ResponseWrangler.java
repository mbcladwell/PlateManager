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
    private double[][] sortedResponse;
  private DecimalFormat df = new DecimalFormat("#####.####");
        private Set<Integer> plate_set = new HashSet<Integer>();
    private Set<Integer> well_set = new HashSet<Integer>();
    private List<Float> desired_response_list = new LinkedList<Float>();
    private List<Float> bkgrnd_list = new LinkedList<Float>();
    private int num_data_points;

    public static final int RAW = 0;
    public static final int NORM = 1;
    public static final int NORM_POS = 2;
      private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    
    /**
     * 
     *	 Array look like
     * plate, well, response, bkgrnd_sub,   norm,   norm_pos ,  well_type_id,  replicates, target 
     * -,1,1,0.293825507,0.293825507,0.434007883,0.511445642,1,1,a
     * class org.postgresql.jdbc.PgArray
     * cannot retrieve first element so first element is duplicated, start extracting at [1]
     * set up some variable with data limits
     *
     * double[][]  sortedResponse [response] [well] [type_id] [sample_id]
     */
    public ResponseWrangler(CustomTable _table, int _desired_response){
	table = _table;
	DefaultTableModel dtm = (DefaultTableModel)table.getModel();
	
	sortedResponse = new double[table.getRowCount()][4];
	    
 for(int row = 0;row < table.getRowCount();row++) {

	
      	//String[] holder = table.get(i).toString().split(",");
     LOGGER.info("table: " + dtm.getValueAt(row,1));
  
     int plate = (int)dtm.getValueAt(row, 0);
       
     plate_set.add((int)dtm.getValueAt(row, 0));
int well = (int)dtm.getValueAt(row, 1);
	well_set.add((int)dtm.getValueAt(row, 1));
	
	//sortedResponse[i][1] = Double.parseDouble(holder[2]);

	Float response = (float)dtm.getValueAt(row, 2);
	
	Float bkgrnd = (float)dtm.getValueAt(row, 3);
	bkgrnd_list.add((float)dtm.getValueAt(row, 3));
	
	Float norm = (float)dtm.getValueAt(row, 4);
	Float normpos = (float)dtm.getValueAt(row, 5);
	int well_type_id = (int)dtm.getValueAt(row, 6);
	sortedResponse[row][2] = (int)dtm.getValueAt(row, 6);
	String replicates = (String)dtm.getValueAt(row, 7);
	String target = (String)dtm.getValueAt(row, 8);
	int sample_id = (int)dtm.getValueAt(row, 9);
	sortedResponse[row][3] = (int)dtm.getValueAt(row, 9);
	

	
	switch(_desired_response){
	case 0: //raw
	    desired_response_list.add(response);
	    sortedResponse[row][0] = response;
	    break;
	case 1: //norm
	    desired_response_list.add(norm);
	sortedResponse[row][0] = norm;
	    break;
	case 2: //normpos
	    desired_response_list.add(normpos);
	sortedResponse[row][0] = normpos;
	    break;
	}

       
	
    	}

    format = well_set.size();
    num_plates = plate_set.size();
    max_response = Double.valueOf(Collections.max(desired_response_list));
    min_response = Double.valueOf(Collections.min(desired_response_list));
    num_data_points = desired_response_list.size();
    
    mean_bkgrnd = Stats.meanOf(bkgrnd_list);
    stdev_bkgrnd = Stats.of(bkgrnd_list).populationStandardDeviation();
    mean_3_sd = mean_bkgrnd + 3*(stdev_bkgrnd);
    mean_2_sd = mean_bkgrnd + 2*(stdev_bkgrnd);
    threshold = mean_3_sd;

    Arrays.sort(sortedResponse, new Comparator<double[]>() {
        @Override
        public int compare(double[] o1, double[] o2) {
            return Double.compare(o2[0], o1[0]);
        }
    });

LOGGER.info("sortedResponse: " + sortedResponse.toString());


    }

    public int getHitsAboveThreshold(double _threshold){
	int firstIndexGreaterThanThreshold=0;
	for(int i = 0; i < sortedResponse.length; i++){
	    if(sortedResponse[i][0] > threshold){
		firstIndexGreaterThanThreshold = i;
		break;
	    }
	}
	return ( sortedResponse.length - firstIndexGreaterThanThreshold);
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
	 * @return the sortedResponse
	 */
	public double[][] getSortedResponse() {
		return sortedResponse;
	}


	/**
	 * @param sortedResponse the sortedResponse to set
	 */
	public void setSortedResponse(double[][] sortedResponse) {
		this.sortedResponse = sortedResponse;
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
	 * @param well_set the well_set to set
	 */
	public void setWell_set(Set<Integer> well_set) {
		this.well_set = well_set;
	}




	/**
	 * @return the bkgrnd_list
	 */
	public List<Float> getBkgrnd_list() {
		return bkgrnd_list;
	}


	/**
	 * @param bkgrnd_list the bkgrnd_list to set
	 */
	public void setBkgrnd_list(List<Float> bkgrnd_list) {
		this.bkgrnd_list = bkgrnd_list;
	}  

	
  


	/**
	 * @return the max_response
	 */

	/**
	 * @return the desired_response_list
	 */
	public List<Float> getDesired_response_list() {
		return desired_response_list;
	}


	/**
	 * @param desired_response_list the desired_response_list to set
	 */
	public void setDesired_response_list(List<Float> desired_response_list) {
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



