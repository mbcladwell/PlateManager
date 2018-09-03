package pm;

import javax.persistence.*;

public class SampleFactory  {
  
  EntityManagerFactory emf = Persistence.createEntityManagerFactory("myDbFile.odb");
  EntityManager em = emf.createEntityManager();
  Sample s;
  
  public SampleFactory( int n ) {
      
    try {
      em.getTransaction().begin();
      
      for(int i = 0; i < n; i++ ){
	s = new Sample();
	em.persist( s);
	s.onPrePersist();	     
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
