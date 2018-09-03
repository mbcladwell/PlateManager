package pm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


import java.time.Instant;
import javax.persistence.*;

/**
 *
 */

public class DatabaseManager {

  private EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:myDbFile.tmp");
  private EntityManager em = emf.createEntityManager();
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");


  public DatabaseManager( ) {
    
    }

  public void persistObject( pm.Project prj){
    EntityManager em = emf.createEntityManager();
     try {
	em.getTransaction().begin();
	
     
	em.persist( prj );
         prj.onPrePersist();
	em.getTransaction().commit();
      }
      finally {
	if (em.getTransaction().isActive())
	  em.getTransaction().rollback();
	em.close();
      }
     // emf.close();
  }

  public Object[][] getProjectTableData(){
     Object[][] data;
        EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();	
      TypedQuery<Object[]> query = em.createQuery( "SELECT p.timeStampMilli, p.projectName, p.owner, p.description FROM Project AS p", Object[].class);
       List<Object[]> results = query.getResultList();
       data = new Object[results.size()][4];
       for (int i = 0; i < results.size(); i++) {
	 //System.out.println( results.get(i)[0] + "   " + results.get(i)[1] + "   " + results.get(i)[2] + "   " + results.get(i)[3]);
       
	 data[i][0] = this.simpleDateFormat.format(results.get(i)[0]);
       data[i][1] = results.get(i)[1];
        data[i][2] = results.get(i)[2];
        data[i][3] = results.get(i)[3];
	 	
      }	 
      em.getTransaction().commit();
    }
      
      finally {
	if (em.getTransaction().isActive())
	  em.getTransaction().rollback();
	em.close();
      }
    // emf.close();
      return  data;

  }

  public Object[][] getPlateSetTableData(){
    Object[][] data;
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();	
      TypedQuery<Object[]> query = em.createQuery( "SELECT ps.timeStampMilli, ps.plateSetName, ps.plateType, ps.description FROM PlateSet AS ps", Object[].class);
      List<Object[]> results = query.getResultList();
      data = new Object[results.size()][4];
      for (int i = 0; i < results.size(); i++) {
	
	data[i][0] = this.simpleDateFormat.format(results.get(i)[0]);
	data[i][1] = results.get(i)[1];
        data[i][2] = results.get(i)[2];
        data[i][3] = results.get(i)[3];
	
      }	 
      em.getTransaction().commit();
    }
    
    finally {
      if (em.getTransaction().isActive())
	em.getTransaction().rollback();
      em.close();
    }
    
    return  data;
    
  }

  //entity: plateset, project etc
  //colnum: number of columns
  
  public Object[][] getTableData( String entity ){

    String jpaquery = new String();
    int numcols =0;
    
    switch ( entity ){
    case "project":
      jpaquery = "SELECT p.timeStampMilli, p.projectName, p.owner, p.description FROM Project AS p";
      numcols = 4;
      break;
    case "plateset":
      jpaquery = "SELECT ps.timeStampMilli, ps.plateSetName, ps.plateType, ps.description FROM PlateSet AS ps";
      numcols = 4;
      
    }

    
    Object[][] data;
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();	
      TypedQuery<Object[]> query = em.createQuery( jpaquery, Object[].class);
      List<Object[]> results = query.getResultList();
      data = new Object[results.size()][numcols];
      for (int i = 0; i < results.size(); i++) {
	for (int j = 0; j < numcols; j++)
	  if(j==0){
	    data[i][0] = this.simpleDateFormat.format(results.get(i)[0]);
	  }else{
	    data[i][j] = results.get(i)[j];
	  }
      }	 
      em.getTransaction().commit();
    }
    
    finally {
      if (em.getTransaction().isActive())
	em.getTransaction().rollback();
      em.close();
    }
    
    return  data;
    
  }

   public Object[][] getPlateSetTableData(  String pid ){

  
    Object[][] data;
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();	
      TypedQuery<PlateSet> query = em.createQuery( "SELECT ps.timeStampMilli, ps.plateSetName, ps.plateType, ps.description FROM PlateSet AS ps WHERE ps.projectId = :projid", PlateSet.class);
      List<PlateSet> results = query.setParameter("projid", pid).getResultList();
      data = new Object[results.size()][4];
      
      for (int i = 0; i < results.size(); i++) {
       
	//	data[i][0] = this.simpleDateFormat.format(results.get(i).getTimestamp());
	//data[i][1] = results.get(i).getPlateSetName();
	//data[i][2] = results.get(i).getPlateSetType();
	//data[i][3] = results.get(i).getPlateSetDescription();
	  
      }
      em.getTransaction().commit();
    }
    
    finally {
      if (em.getTransaction().isActive())
	em.getTransaction().rollback();
      em.close();
    }
    
    return  data;
    
  }

  public Object[][] getPlateTableData( String plateId){
    return null;
  }
  
  
}   
   
