import org.hibernate.Session;
import org.hibernate.Transaction;

import com.autobizlogic.abl.logic.analysis.ClassLoaderManager;

import models.Customer;
import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        // Check if the database is empty
		System.out.println("Bootstrap executing...");
		
		ClassLoaderManager.getInstance().defineClass("models.Customer", 
				Play.classes.getApplicationClass("models.Customer").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("models.PurchaseOrder", 
				Play.classes.getApplicationClass("models.PurchaseOrder").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("models.LineItem", 
				Play.classes.getApplicationClass("models.LineItem").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("businesslogic.CustomerLogic", 
				Play.classes.getApplicationClass("businesslogic.CustomerLogic").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("businesslogic.PurchaseOrderLogic", 
				Play.classes.getApplicationClass("businesslogic.PurchaseOrderLogic").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("businesslogic.LineItemLogic", 
				Play.classes.getApplicationClass("businesslogic.LineItemLogic").enhancedByteCode);
		ClassLoaderManager.getInstance().defineClass("businesslogic.LogicObject", 
				Play.classes.getApplicationClass("businesslogic.LogicObject").enhancedByteCode);

        if(Customer.count() == 0) {
        	try {
        		//Session session = (Session) JPA.em().getDelegate();
        		//Session session2 = session.getSessionFactory().getCurrentSession();
        		//Transaction tx = session.beginTransaction();
        		
        		Fixtures.loadModels("initial-data.yml");
        		
        		//tx.commit();
        	}
        	catch(Exception ex) {
        		ex.printStackTrace();
        	}
    		System.out.println("Loading bootstrap data...");
        }
    }
}
