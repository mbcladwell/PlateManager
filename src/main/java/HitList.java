package pm;

import java.io.Serializable;
import javax.persistence.*;
import java.time.Instant;
import java.util.HashMap;

@Entity
@SequenceGenerator(name = "hitListSeqGenerator", initialValue = 1, allocationSize = 1)
public class HitList implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private String hitListName;
  private String description;
  private String owner;
  private long timeStampMilli;
  private HashMap<String, Sample> sampleMap;

  
  
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hitListSeqGenerator")
  @Id long id;
  
 
  public HitList(String description, String owner) {
        this.description = description;
    this.owner = owner;

    Instant instant = Instant.now();
    this.timeStampMilli = instant.toEpochMilli();
    this.sampleMap = new HashMap<String, Sample>();

   }
  
  @PrePersist void onPrePersist() {
    this.hitListName = "HL" +  String.valueOf(this.id);
  }

  public String getHitListName() {
    return this.hitListName;
  }

  public long getHitListId( ) {
    return this.id;
  }

  public long getTimestamp() {
    return timeStampMilli;
  }

}
