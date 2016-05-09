package org.openntf.xworlds.demo.testclient;

import java.io.InputStream;
import java.io.InputStreamReader;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.Factory;

import groovy.lang.GroovyShell;

public class TestGroovyScript {

	public static void main(String[] args) throws InterruptedException {
		
		Factory.startup();
		
		Thread.sleep(3000);
		
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		NotesThread.sinitThread();
		
		GroovyShell gs = new GroovyShell();
		
//		gs.evaluate(in)
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/openntf/xworlds/demo/testclient/testscript.groovy");
		
		System.out.println(is);
		gs.evaluate(new InputStreamReader(is));
		
		NotesThread.stermThread();
		Factory.termThread();
		Factory.shutdown();
		
	}
	
	
}
