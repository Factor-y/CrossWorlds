package org.openntf.xworlds.demo.testclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestConcurrentDocCreation {

	class Creator implements Runnable {
		
		private int _count = 10;

		public Creator(int count) {
			_count  = count;
		}

		@Override
		public void run() {
			
			try { 
				URL maker = new URL("http://localhost:9080/CrossWorldsDemo/domrest/test/makedocs?count=" + _count);

				long start = System.currentTimeMillis();
				System.out.println("Thread " + Thread.currentThread().getId() + " for " + _count + " documents starting");

				HttpURLConnection uc = (HttpURLConnection) maker.openConnection();
				uc.setRequestMethod("POST");
				uc.connect();

				System.out.println("Thread " + Thread.currentThread().getId() + " for " + _count + " documents = results" + uc.getResponseCode() + " in " + (System.currentTimeMillis() - start) + " millisec");

				
			} catch (IOException e) {
				
			}
		}
		
	}
	
	public static void main(String[] args) throws MalformedURLException {

		TestConcurrentDocCreation tester = new TestConcurrentDocCreation();
		tester.perform();
		
	}

	private void perform() {
		List<Thread> Ts = new ArrayList<Thread>();
		
		int threads = 5;
		int docsPerThread = 1000;
		
		for (int i = 0; i < threads; i++) {
			Ts.add(new Thread(new Creator(docsPerThread)));
		}
		
		for (Thread thread : Ts) {
			thread.start();
		}
	}

}
