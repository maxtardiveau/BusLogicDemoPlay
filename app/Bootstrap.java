import com.autobizlogic.abl.logic.analysis.ClassLoaderManager;

import models.Customer;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job<Void> {

	public void doJob() {
		
		// For some reason, our plugin does not get called in standalone mode, so we need to
		// pre-load the classes into the ABL engine here
		if (Play.standalonePlayServer) {
			System.out.println("Standalone mode: pre-loading classes into ABL");
			preloadClass("models.Customer");
			preloadClass("models.PurchaseOrder");
			preloadClass("models.LineItem");
			preloadClass("models.Product");
			preloadClass("businesslogic.CustomerLogic");
			preloadClass("businesslogic.PurchaseOrderLogic");
			preloadClass("businesslogic.LineItemLogic");
			preloadClass("businesslogic.LogicObject");
		}
		
        if(Customer.count() == 0) {
        	try {
        		Fixtures.loadModels("initial-data.yml");
        	}
        	catch(Exception ex) {
        		ex.printStackTrace();
        	}
    		System.out.println("Loaded bootstrap data...");
        }
    }
	
	/**
	 * Because of the peculiar way Play handles class loading, we have to pre-load all
	 * logic classes and persistent beans to the ABL engine before firing any business logic.
	 * This is normally done in the plugin, but the plugin does not get invoked in standalone mode.
	 */
	private static void preloadClass(String clsName) {
		ClassLoaderManager.getInstance().defineClass(clsName, 
				Play.classes.getApplicationClass(clsName).enhancedByteCode);
	}
}
