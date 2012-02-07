import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

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
		
		System.out.println("Attempting to load all classes...");
		List<Class> allClasses = Play.classloader.getAllClasses();
		System.out.println("Got all classes: " + allClasses);

//		if (Play.classes == null)
//			System.out.println("Play.classes is null");
//		if (Play.classes.getApplicationClass("models.Customer") == null)
//			System.out.println("Play.classes.getApplicationClass(\"models.Customer\") is null");
		if (Play.classes.getApplicationClass("models.CustomerLogic").enhancedByteCode == null)
			System.out.println("Play.classes.getApplicationClass(\"models.CustomerLogic\").enhancedByteCode is null");
//		
//		if (Play.classes.getApplicationClass("models.Customer").enhancedByteCode == null)
//			System.out.println("enhancedByteCode is STILL null");
//		if (Play.classes.getApplicationClass("models.Customer").enhance() == null)
//			System.out.println("enhance() returns null");
//		
//		InputStream inStr = Play.classloader.getResourceAsStream("/models/Customer.class");
//		if (inStr == null)
//			System.out.println("Play.classloader.getResourceAsStream returned null");
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int b;
//		try {
//			while ((b = inStr.read()) != -1)
//				baos.write(b);
//		}
//		catch(Exception ex) {
//			ex.printStackTrace();
//			throw new RuntimeException("Error while reading class bytes");
//		}
//		
//		ClassLoaderManager.getInstance().defineClass("models.Customer", baos.toByteArray());

		
//		ClassLoaderManager.getInstance().defineClass("models.Customer", 
//				Play.classes.getApplicationClass("models.Customer").enhancedByteCode);
//		ClassLoaderManager.getInstance().defineClass("models.PurchaseOrder", 
//				Play.classes.getApplicationClass("models.PurchaseOrder").enhancedByteCode);
//		ClassLoaderManager.getInstance().defineClass("models.LineItem", 
//				Play.classes.getApplicationClass("models.LineItem").enhancedByteCode);
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
