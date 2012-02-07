package util;

import play.PlayPlugin;
import play.classloading.ApplicationClasses;

public class DemoPlugin extends PlayPlugin {

	@Override
	public void onLoad() {
		System.out.println("ABL DemoPlugin is loaded");
	}
	
	@Override
	public void compileAll(java.util.List<ApplicationClasses.ApplicationClass> classes) {
		System.out.println("ABL plugin - onClassChanges");
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
		System.out.println("Chance to enhance class: " + cls.name + ", bytecodes: " + (cls.enhancedByteCode == null ? "null" : "NOT NULL"));
	}
}