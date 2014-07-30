package com.epam.philosophers;
import java.util.concurrent.locks.ReentrantLock;


public class Fork {
	
	private String name;	
	private ReentrantLock lock = new ReentrantLock();
	
	public Fork(String name) {
		this.name = name;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	@Override
	public String toString() {
		return name;
	}
}
