package com.epam.philosophers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	private static final int numberOfPhilosophers = 5;
	private static final AtomicInteger riceBowl = new AtomicInteger(100);

	public static void main(String[] args) {

		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
		Fork[] forks = new Fork[numberOfPhilosophers];
		ExecutorService execService = Executors.newCachedThreadPool();

		for (int i = 0; i < numberOfPhilosophers; i++) {
			forks[i] = new Fork("Fork " + (i + 1));
		}

		for (int i = 0; i < numberOfPhilosophers; i++) {
			int numberOfLeftFork = i;
			int numberOfRightFork = (i != (numberOfPhilosophers - 1)) ? i + 1 : 0;

			philosophers[i] = new Philosopher("Phil " + (i + 1), riceBowl,
					forks[numberOfLeftFork], forks[numberOfRightFork]);
		}

		for (int i = 0; i < numberOfPhilosophers; i++) {
			execService.execute(philosophers[i]);
		}
	}

}
