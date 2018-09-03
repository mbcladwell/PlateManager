package pm;

import java.time.Instant;
import java.util.HashMap;
import javax.persistence.*;

/** */
@Entity
@SequenceGenerator(name = "projectSeqGenerator", initialValue = 1, allocationSize = 1)
public class Project {

  private String projectName;
  private String description;
  private HashMap<String, PlateSet> plateSetMap;
  private long timeStampMilli;
  private String owner;

  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSeqGenerator")
  @Id
  long id;

  public Project(String description, String owner, String name) {

    this.description = description;
    this.owner = owner;
    this.projectName = name;

    Instant instant = Instant.now();
    this.timeStampMilli = instant.toEpochMilli();
    this.plateSetMap = new HashMap<String, PlateSet>();
  }

  @PrePersist
  void onPrePersist() {
    this.projectName = "PRJ" + String.valueOf(this.id);
  }

  public String getProjectName() {
    return projectName;
  }

  public long getTimestamp() {
    return timeStampMilli;
  }
}
