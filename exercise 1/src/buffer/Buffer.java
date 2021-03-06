package buffer;

import java.util.LinkedList;

import se.lth.cs.realtime.semaphore.CountingSem;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * The buffer.
 */
class Buffer {
	Semaphore mutex; // For mutual exclusion blocking.
	Semaphore free; // For buffer full blocking.
	Semaphore avail; // For blocking when no data is available.
	String buffData = ""; // The actual buffer.
	LinkedList<String> buffer = new LinkedList<String>();
	String[] ring = new String[8];
	int first = 0;
	int next = 0;
	

	Buffer() {
		mutex = new MutexSem();
		free = new CountingSem(8);
		avail = new CountingSem();
	}

	void putLine(String input) {
		free.take(); // Wait for buffer empty.
		mutex.take(); // Wait for exclusive access.

//		buffData = new String(input); // Store copy of object.

//		buffer.add(input);
		
		ring[next] = input;
		next = (next+1) % 8;
		
		mutex.give(); // Allow others to access.
		avail.give(); // Allow others to get line.
	}

	String getLine() {
		// Exercise 2 ...
		// Here you should add code so that if the buffer is empty, the
		// calling process is delayed until a line becomes available.
		// A caller of putLine hanging on buffer full should be released.
		// ...
		avail.take();
		mutex.take();
//		String buffCpy = new String(buffData);
		
//		String buffCpy = buffer.poll();
		
		String buffCpy = ring[first];
		ring[first] = null;
		first = (first+1) % 8;
		
		mutex.give();
		free.give();
		return buffCpy;
	}
}
