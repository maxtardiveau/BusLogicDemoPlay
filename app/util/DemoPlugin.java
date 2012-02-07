package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

import com.autobizlogic.abl.logic.analysis.ClassLoaderManager;

import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses;

// For this to work properly on Heroku, you must be sure to disable production mode:
// The issue is being caused due to the fact that Heroku precompile in a separate step on the platform. 
// You can disable this by editing your PLAY_OPTS environment variable: 
// $ heroku config:remove PLAY_OPTS 
// $ heroku config:add PLAY_OPTS=--%prod 
// Alternatively, you can edit your Procfile to read: 
// web: play run --http.port=$PORT --%prod 

public class DemoPlugin extends PlayPlugin {
	
	/**
	 * This gets called at precompile time. We pass the bytecode for the bean and logic classes
	 * to the ABL engine for analysis. Play handles classes in a very peculiar way: it instruments
	 * classes without (conceptually at least) writing the bytecode to disk. Therefore getting the bytecode
	 * for analysis can be a challenge. This is the best way we have found so far.
	 */
	@Override
	public void enhance(ApplicationClasses.ApplicationClass cls) {
		if (cls.name.startsWith("businesslogic.") || cls.name.startsWith("models.")) {
			ClassLoaderManager.getInstance().defineClass(cls.name, cls.enhancedByteCode);
			System.out.println("Passed enhanced bytecode to ABL for class : " + cls.name);
		}
	}
}
