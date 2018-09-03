package pm;

import javax.persistence.*;

/** */
public class Main {

  //private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("myDbFile.odb");
  //private static EntityManager em = emf.createEntityManager();
  private static PlateSet ps;
  private static DatabaseManager dm;
  // private static DialogMainFrame dmf;

  public static void main(String[] args) {
    /*   try {
    em.getTransaction().begin();

    ps = new PlateSet(10, 384, "Desc1", "assay", em);
    em.persist(ps);
    ps.onPrePersist();
    em.getTransaction().commit();
         }
         finally {
    if (em.getTransaction().isActive())
      em.getTransaction().rollback();
    em.close();
         }
         emf.close();
         */
    //    javax.swing.SwingUtilities.invokeLater(
					   // new Runnable() {
					   // public void run() {
    //new InitializeDatabaseWithData();
    //   dm = new DatabaseManager();
	    //dm.getMainFrameTableData();
     new   DialogMainFrame();
     //  new InitializeDatabaseWithData();
	    // }
	    //  });
  }
}
