package pm;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.Instant;
import javax.persistence.*;

/**
 *
 */
@Entity
@SequenceGenerator(name = "plateSetSeqGenerator", initialValue = 1, allocationSize = 1)

public class PlateSet {

  private int numberOfPlates;
  private String plateSetName;
  private String description;
  private HashMap<String, Plate> plateMap;
  private long timeStampMilli;
  private String plateType; //assay, glycerol
  private int wellsPerPlate;
  private String projectId;
  
  private Plate p;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plateSetSeqGenerator")
 @Id long id;


  public PlateSet(String projId, int numberOfPlates, int wellsPerPlate, String description, String plateType, EntityManager em) {

    this.projectId = projId;
    this.numberOfPlates = numberOfPlates;
    this.wellsPerPlate = wellsPerPlate;
    this.description = description;
    this.plateType = plateType;
  
    Instant instant = Instant.now();
    this.timeStampMilli = instant.toEpochMilli();
    this.plateMap = new HashMap<String, Plate>();

    
    for (int i=0;  i < this.numberOfPlates; i++){
    
      p = new Plate(this.wellsPerPlate, this.plateType, em);
	em.persist( p );
	p.onPrePersist();     
	plateMap.put(p.getPlateName(), p);
    }
    
    }

    @PrePersist void onPrePersist() {
    this.plateSetName = "PS" +  String.valueOf(this.id);
  }
  
    public String getPlateSetName() {
        return plateSetName;
    }

    public long getTimestamp() {
        return timeStampMilli;
    }
   public String getPlateSetType() {
     return this.plateType;
    }
    public String getPlateSetDescription() {
     return this.description;
    }

}
