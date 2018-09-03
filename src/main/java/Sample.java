package pm;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@SequenceGenerator(name = "sampleSeqGenerator", initialValue = 1, allocationSize = 96)
public class Sample implements Serializable {
  
  //private static final long serialVersionUID = 1L;

  private String sampleName;
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sampleSeqGenerator")
  @Id long id;
  
 
  public Sample() {
   }
  
  @PrePersist void onPrePersist() {
    this.sampleName = "SPL" +  String.valueOf(this.id);
  }

  public String getSampleName() {
    return this.sampleName;
  }

  public long getSampleId( ) {
    return this.id;
  }
}
