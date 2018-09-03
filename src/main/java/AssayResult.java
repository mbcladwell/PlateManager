package pm;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@SequenceGenerator(name = "assayResultSeqGenerator", initialValue = 1, allocationSize = 1)
public class AssayResult implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private String assayResultName;
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assayResultSeqGenerator")
  @Id long id;
  
 
  public AssayResult() {
   }
  
  @PrePersist void onPrePersist() {
    this.assayResultName = "HL" +  String.valueOf(this.id);
  }

  public String getAssayResultName() {
    return this.assayResultName;
  }

  public long getAssayResultId( ) {
    return this.id;
  }
}
