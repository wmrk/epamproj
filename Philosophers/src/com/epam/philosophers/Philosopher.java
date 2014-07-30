package com.epam.philosophers;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class Philosopher implements Runnable {
	
	static enum State {IDLE, DISCUSS, EATING, END};
	
	private Fork leftFork;
	private Fork rightFork;
	private AtomicInteger riceBowl;
	private State state = State.IDLE;
	private String name;
	
	@Override
	public void run() {
		while(getState() != State.END)
		{
			nextCicle();
		}
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		if(this.state != state) {
			this.state = state;
			System.out.println(name + " : " + state.toString());
		}
	}

	public Philosopher(String name, AtomicInteger riceBowl, Fork leftFork, Fork rightFork) {
		this.name = name;
		this.riceBowl = riceBowl;
		this.leftFork = leftFork;
		this.rightFork = rightFork;
	}
	
	public void nextCicle() {
		
		if(state==State.END)
			return;
		
		Random random = new Random();
		int eatingTime = random.nextInt(1000);
		int discussTime = random.nextInt(1000);


		//stage 1: try take forks
		ReentrantLock leftForkLock = leftFork.getLock();
		ReentrantLock rightForkLock = rightFork.getLock();
		
		boolean leftLocked = leftForkLock.tryLock();
		boolean rightLocked = rightForkLock.tryLock();

		try {	
			if(leftLocked && rightLocked)
			{
				//stage 2: looking at bowl
				int riceBalance = riceBowl.decrementAndGet();
				
				if(riceBalance <= 0) {
					if(riceBalance == 0) {
						System.out.println(name + " ate last rice grain!");
					}
					setState(State.END);
					return;
				}
				
				//stage 3: eating
				setState(State.EATING);
				try {
					Thread.sleep(eatingTime);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					return;
				}
				
				if(riceBalance == 0) {
					System.out.println(name + " ate last rice grain!");
					setState(State.END);
					return;
				}
			}
		}
		finally {
			if(state != State.END)
				setState(State.DISCUSS);
			if(leftLocked) 
				leftForkLock.unlock();
			if(rightLocked)
				rightForkLock.unlock();
		}
				
		//stage 4: discuss
		try {
			Thread.sleep(discussTime);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}	

	@Override
	public String toString() {
		return name;
	}
}
