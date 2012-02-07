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

public class DemoPlugin extends PlayPlugin {
	
	private static final String TMP_BASE = "/app/abltmp";

	@Override
	public void onLoad() {
		System.out.println("ABL DemoPlugin is loaded 1 - current dir: " + System.getProperty("user.dir"));
		
		File clsDir = new File(TMP_BASE);
		if (clsDir.exists()) {
			System.out.println("Class cache found - loading bytecode");
			readClassDir(clsDir);
		}
		else
			System.out.println("DemoPlugin: No class cache found");
	}
	
	private void readClassDir(File dir) {
		
		File[] files = dir.listFiles();
		for (File f : files) {
			if ( ! f.isDirectory() && f.getName().endsWith(".class")) {
				if (System.currentTimeMillis() - f.lastModified() > 30000) {
					System.out.println("Class file " + f.getAbsolutePath() + " is too old -- skipping");
					continue;
				}
				readClassFile(f);
			}
			else if (f.isDirectory()) {
				readClassDir(f);
			}
		}

	}
	
	private void readClassFile(File clsFile) {
		System.out.println("Reading bytecode for cached class file: " + clsFile.getAbsolutePath());
		FileInputStream inStr;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		try {
			inStr = new FileInputStream(clsFile);
			while ((b = inStr.read()) != -1)
				baos.write(b);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error while reading class bytes for " + clsFile.getAbsolutePath());
		}

		String fname = clsFile.getAbsolutePath().substring(TMP_BASE.length() + 1);
		fname = fname.substring(0, fname.length() - ".class".length());
		fname = fname.replace('/', '.');

		System.out.println("Defining class from cache: " + fname);
		ClassLoaderManager.getInstance().defineClass(fname, baos.toByteArray());
	}
	
	/**
	 * This gets called at precompile time. We save the bytecode for the classes in the tmp
	 * directory, to be read again when the app actually starts.
	 */
	@Override
	public void enhance(ApplicationClasses.ApplicationClass cls) {
		//System.out.println("Chance to enhance class: " + cls.name + ", bytecodes: " + (cls.enhancedByteCode == null ? "null" : "NOT NULL"));
		if (cls.name.startsWith("businesslogic.") || cls.name.startsWith("models.")) {
			ClassLoaderManager.getInstance().defineClass(cls.name, cls.enhancedByteCode);
			
			File dir = new File(TMP_BASE);
			if ( ! dir.exists()) {
				if ( ! dir.mkdir())
					throw new RuntimeException("Unable to create temporary directory: " + TMP_BASE);
			}
			
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
			File outFile = new File(TMP_BASE + "/" + packDir + "/" + clsName + ".class");
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
