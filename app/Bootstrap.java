import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.autobizlogic.abl.logic.analysis.ClassLoaderManager;

import models.Customer;
import play.Play;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.BytecodeCache;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        // Check if the database is empty
		System.out.println("Bootstrap executing...");
		
		System.out.println("ClassLoader for Customer is a: " + Customer.class.getClassLoader().getClass());
		play.classloading.ApplicationClassloader cl = (play.classloading.ApplicationClassloader)Customer.class.getClassLoader();
		System.out.println("ClassLoader for Customer is : " + cl);
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("models/Customer.class");
		if (is == null)
			System.out.println("ContextClassLoad returned null stream for Customer");
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream("java/lang/Object.class");
		if (is == null)
			System.out.println("ContextClassLoad returned null stream for Object too");
		else
			System.out.println("We can get the bytes for Object, at least");

		is = cl.getResourceAsStream("java/lang/Object.class");
		if (is == null)
			System.out.println("ContextClassLoad returned null stream for Object too");
		else
			System.out.println("We can get the bytes for Object2, at least");
		is = cl.getResourceAsStream("models/Customer.class");
		if (is == null)
			System.out.println("ContextClassLoad returned null stream for Customer2");
		else
			System.out.println("We can get the bytes for Customer2");
		Class<?> custCls = cl.loadApplicationClass("models.Customer");
		System.out.println("loadApplicationClass returns: " + custCls);
		if (Play.classes.getApplicationClass("models.Customer").enhancedByteCode == null)
			System.out.println("Play.classes.getApplicationClass(\"models.Customer\").enhancedByteCode is null");

//		System.out.println("Attempting to load all classes...");
		List<Class> allClasses = Play.classloader.getAllClasses();
		System.out.println("Got all classes: " + allClasses);
		
		System.out.println("****************************************");
		ApplicationClass applicationClass = Play.classes.getApplicationClass("models.Customer");
		System.out.println("Got ApplicationClass: " + applicationClass);
		byte[] bc = BytecodeCache.getBytecode("models.Customer", applicationClass.javaSource);
		System.out.println("Got bytecode: " + bc.length);

//		if (Play.classes == null)
//			System.out.println("Play.classes is null");
//		if (Play.classes.getApplicationClass("models.Customer") == null)
//			System.out.println("Play.classes.getApplicationClass(\"models.Customer\") is null");
//		List<ApplicationClass> appClasses = Play.classes.all();
//		for (ApplicationClass cls : appClasses) {
//			System.out.print("App class: " + cls);
//			if (cls.compile() == null)
//				System.out.println(" with null compile()");
//			else
//				System.out.println(" with NOT NULL compile()");
//		}
		
//		if (Play.classes.getApplicationClass("models.CustomerLogic").enhancedByteCode == null)
//			System.out.println("Play.classes.getApplicationClass(\"models.CustomerLogic\").enhancedByteCode is null");
//		
//		if (Play.classes.getApplicationClass("models.Customer").enhancedByteCode == null)
//			System.out.println("enhancedByteCode is STILL null");
//		if (Play.classes.getApplicationClass("models.Customer").enhance() == null)
//			System.out.println("enhance() returns null");
//		
//		InputStream inStr = Play.classloader.getResourceAsStream("/models/Customer.class");
//		InputStream inStr = getClass().getResourceAsStream("Customer.class");
//		if (inStr == null)
//			System.out.println("getResourceAsStream returned null");
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
//		ClassLoaderManager.getInstance().defineClass("businesslogic.CustomerLogic", 
//				Play.classes.getApplicationClass("businesslogic.CustomerLogic").enhancedByteCode);
//		ClassLoaderManager.getInstance().defineClass("businesslogic.PurchaseOrderLogic", 
//				Play.classes.getApplicationClass("businesslogic.PurchaseOrderLogic").enhancedByteCode);
//		ClassLoaderManager.getInstance().defineClass("businesslogic.LineItemLogic", 
//				Play.classes.getApplicationClass("businesslogic.LineItemLogic").enhancedByteCode);
//		ClassLoaderManager.getInstance().defineClass("businesslogic.LogicObject", 
//				Play.classes.getApplicationClass("businesslogic.LogicObject").enhancedByteCode);

        if(Customer.count() == 0) {
        	try {
        		Fixtures.loadModels("initial-data.yml");
        	}
        	catch(Exception ex) {
        		ex.printStackTrace();
        	}
    		System.out.println("Loading bootstrap data...");
        }
    }
}
