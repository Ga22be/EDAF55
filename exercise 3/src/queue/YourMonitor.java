package queue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import se.lth.cs.realtime.RTError;

class YourMonitor {
	// Put your attributes here...
	private static int MAX_TICKETS = 100;
	
	private int nCounters;
	private int lastCustomerArrived = 99;
	private int lastCustomerServed = 99;
	private int nrOfFreeClerks = 0;
	private int lastClerkAssigned = 0;
	private boolean[] counterFree;

	YourMonitor(int n) {
		// Initialize your attributes here...
		nCounters = n;
		counterFree = new boolean[n];
	}

	/**
	 * Return the next queue number in the intervall 0...99. There is never more
	 * than 100 customers waiting.
	 */
	synchronized int customerArrived() {
		// Implement this method...
		lastCustomerArrived = (lastCustomerArrived+1) % MAX_TICKETS;
		notifyAll();
		return lastCustomerArrived;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any.
	 */
	synchronized void clerkFree(int id) {
		// Implement this method...
		if(!counterFree[id]) {
			nrOfFreeClerks++;
			counterFree[id] = true;
			notifyAll();
		}
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then return the
	 * cueue number of next customer to serve and the counter number of the engaged
	 * clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException {
		// Implement this method...
		while (lastCustomerArrived == lastCustomerServed || nrOfFreeClerks == 0) wait();
		
		while (!counterFree[lastClerkAssigned]) lastClerkAssigned = (lastClerkAssigned+1) % nCounters;
		DispData dispData = new DispData();
		dispData.counter = lastClerkAssigned;
		counterFree[lastClerkAssigned] = false;
		nrOfFreeClerks--;
		dispData.ticket = lastCustomerServed = (lastCustomerServed+1) % MAX_TICKETS;		
		return dispData;
	}
}
