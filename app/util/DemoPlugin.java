package util;

import java.io.File;
import java.io.FileOutputStream;

import com.autobizlogic.abl.logic.analysis.ClassLoaderManager;

import play.PlayPlugin;
import play.classloading.ApplicationClasses;

public class DemoPlugin extends PlayPlugin {
	
	private static final String TMP_BASE = "/abltmp";

	@Override
	public void onLoad() {
		System.out.println("ABL DemoPlugin is loaded - current dir: " + System.getProperty("user.dir"));
	}
	
	@Override
	public void compileAll(java.util.List<ApplicationClasses.ApplicationClass> classes) {
		System.out.println("ABL plugin - compileAll");
		System.out.print("Classes : " + classes);
	}
	
	@Override
	public java.util.List<ApplicationClasses.ApplicationClass> onClassesChange(java.util.List<ApplicationClasses.ApplicationClass> classes) {
		System.out.println("ABL plugin - onClassChanges");
		for (ApplicationClasses.ApplicationClass appCls : classes) {
			System.out.println("Class changed: " + appCls.name + ", bytecodes: " + (appCls.enhancedByteCode == null ? "null" : "NOT NULL"));
		}
			
		return classes;
	}
	
	@Override
	public void enhance(ApplicationClasses.ApplicationClass cls) {
		//System.out.println("Chance to enhance class: " + cls.name + ", bytecodes: " + (cls.enhancedByteCode == null ? "null" : "NOT NULL"));
		if (cls.name.startsWith("businesslogic.") || cls.name.startsWith("models.")) {
			ClassLoaderManager.getInstance().defineClass(cls.name, cls.enhancedByteCode);
			
			File dir = new File(TMP_BASE);
			if ( ! dir.exists())
				dir.mkdir();
			
			String packDir = "";
			String clsName = cls.name;
			int lastDot = cls.name.lastIndexOf('.');
			if (lastDot != -1) {
				packDir = cls.name.substring(0, lastDot);
				packDir = packDir.replaceAll("\\.", "/");
				clsName = clsName.substring(lastDot + 1);
			}
			File packFile = new File(TMP_BASE + "/" + packDir);
			packFile.mkdirs();
			File outFile = new File(TMP_BASE + "/" + packDir + "/" + clsName);
			try {
				FileOutputStream outStr = new FileOutputStream(outFile);
				outStr.write(cls.enhancedByteCode);
				outStr.close();
				System.out.println("Cached class for ABL: " + outFile.getAbsolutePath());
			}
			catch(Exception ex) {
				throw new RuntimeException("Exception while caching class file", ex);
			}
		}
	}
}
