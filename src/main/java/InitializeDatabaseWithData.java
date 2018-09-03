package pm;

import java.util.ArrayList;
import java.util.HashMap;

import java.time.Instant;
import javax.persistence.*;

/**
 *
 */

public class InitializeDatabaseWithData {

  private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:myDbFile.tmp;drop");
  private static EntityManager em = emf.createEntityManager();

  public InitializeDatabaseWithData( ) {

 try {
	em.getTransaction().begin();
	
	for(int i = 1; i <101; i++){
	  Project prj = new Project("my desc" + i, "mbcladwell" + i, "aname" + i);
	  em.persist( prj );
	  prj.onPrePersist();
	  
	}
	em.getTransaction().commit();
      }
      finally {
	if (em.getTransaction().isActive())
	  em.getTransaction().rollback();
	//em.close();
      }

try {
	em.getTransaction().begin();
	
	for(int i = 1; i <4; i++){
	  PlateSet ps = new PlateSet("PRJ3", 3, 96, "desc"+ i, "assay", em);
	  em.persist( ps );
	  ps.onPrePersist();
	  
	}
	em.getTransaction().commit();
      }
      finally {
	if (em.getTransaction().isActive())
	  em.getTransaction().rollback();
	//em.close();
      }

try {
	em.getTransaction().begin();
	
	for(int i = 1; i <10; i++){
	  HitList hl = new HitList("description" + i, "owner" + i );
	  em.persist( hl );
	  hl.onPrePersist();
	  
	}
	em.getTransaction().commit();
      }
      finally {
	if (em.getTransaction().isActive())
	  em.getTransaction().rollback();
	em.close();
      }




 
      emf.close();    
    
    }

}
